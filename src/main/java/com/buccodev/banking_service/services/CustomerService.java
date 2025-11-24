package com.buccodev.banking_service.services;

import com.buccodev.banking_service.dtos.sharedDtos.PageResponseDto;
import com.buccodev.banking_service.entities.Account;
import com.buccodev.banking_service.entities.Card;
import com.buccodev.banking_service.entities.Customer;
import com.buccodev.banking_service.entities.Roles;
import com.buccodev.banking_service.exceptions.CredentialInvalidException;
import com.buccodev.banking_service.exceptions.account.AccountAlreadyException;
import com.buccodev.banking_service.exceptions.card.CardAlreadyException;
import com.buccodev.banking_service.exceptions.customer.CustomerAlreadyRegisteredException;
import com.buccodev.banking_service.exceptions.customer.ResourceNotFoundException;
import com.buccodev.banking_service.repositories.AccountRepository;
import com.buccodev.banking_service.repositories.CardRepository;
import com.buccodev.banking_service.repositories.CustomerRepository;
import com.buccodev.banking_service.dtos.customer.CustomerRequestDto;
import com.buccodev.banking_service.dtos.customer.CustomerResponseDto;
import com.buccodev.banking_service.dtos.customer.CustomerUpdateDto;
import com.buccodev.banking_service.utils.auth.ResourceOwnerChecker;
import com.buccodev.banking_service.utils.mapper.CustomerMapper;
import com.buccodev.banking_service.utils.num_generate.AccountNumGenerator;
import com.buccodev.banking_service.utils.num_generate.CardNumGenerate;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;
    private final CardRepository cardRepository;
    private final PasswordEncoder passwordEncoder;

    public CustomerService(CustomerRepository customerRepository, AccountRepository accountRepository, CardRepository cardRepository, PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.accountRepository = accountRepository;
        this.cardRepository = cardRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public CustomerResponseDto createCustomer(CustomerRequestDto customerRequestDto) {

        if (customerRepository.existsByEmail(customerRequestDto.email())) {
            throw new CustomerAlreadyRegisteredException("Customer already exists");
        }

        var numCard = CardNumGenerate.generateNumCard();

        LocalDate date = LocalDate.now().plusYears(5);
        LocalDate expirationCard = YearMonth.from(date).atEndOfMonth();


        var cvv = CardNumGenerate.generateCvv();

        var numAccount = AccountNumGenerator.generateBBAccountNumber();

        if(cardRepository.existsByCardNumber(numCard)) {
          throw new CardAlreadyException("Card already exists");
        }

        if(accountRepository.existsByAccountNumber(numAccount)) {
           throw new AccountAlreadyException("Account already exists");
        }

        var password = CardNumGenerate.generatePassword();

        var card = new Card(null, null, numCard, cvv, expirationCard, password);

        var account  = new Account(null, null, numAccount, card);

        var passwordEncoded = passwordEncoder.encode(customerRequestDto.password());

        var customer = new Customer(null, customerRequestDto.name(), customerRequestDto.email(), customerRequestDto.cpf(),
                passwordEncoded, customerRequestDto.phone(), account);

        account.setCustomer(customer);
        card.setAccount(account);

        cardRepository.save(card);
        accountRepository.save(account);
        customerRepository.save(customer);

        return CustomerMapper.toCustomerResponseDto(customer);
    }

    public CustomerResponseDto getCustomerById(Long id) {
        var customer = customerRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Resource not found"));
        if(ResourceOwnerChecker.verificationById(customer.getId(), SecurityContextHolder.getContext().getAuthentication())){
            throw new CredentialInvalidException("Invalid credentials");
        }
        return CustomerMapper.toCustomerResponseDto(customer);
    }

    public void updateCustomer(Long id, CustomerUpdateDto customerUpdateDto) {
        var customer = customerRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Resource not found"));
        if(ResourceOwnerChecker.verificationById(customer.getId(), SecurityContextHolder.getContext().getAuthentication())){
            throw new CredentialInvalidException("Invalid credentials");
        }
        customer.setName(customerUpdateDto.name());
        customer.setPhone(customerUpdateDto.phone());
        customerRepository.save(customer);
    }

    public void deleteCustomer(Long id) {
        var customer = customerRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Resource not found"));
        if(ResourceOwnerChecker.verificationById(customer.getId(), SecurityContextHolder.getContext().getAuthentication())){
            throw new CredentialInvalidException("Invalid credentials");
        }
        customerRepository.deleteById(customer.getId());
    }

    public CustomerResponseDto getCustomerByEmail(String email) {
        var customer = customerRepository.findByEmail(email).orElseThrow(()-> new ResourceNotFoundException("Resource not found"));
        if(ResourceOwnerChecker.verificationById(customer.getId(), SecurityContextHolder.getContext().getAuthentication())){
            throw new CredentialInvalidException("Invalid credentials");
        }
        return CustomerMapper.toCustomerResponseDto(customer);
    }

    public void updateRole(Long id, Roles role){
        var customer = customerRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Resource not found"));
        customer.setRole(role);
        customerRepository.save(customer);
    }

    public PageResponseDto<CustomerResponseDto> getAllCustomers(Integer page, Integer size) {
        if (page < 0) page = 0;
        if (size <= 0 || size > 100) size = 10;

        Page<Customer> customersPage = customerRepository.findAll(PageRequest.of(page, size));

        List<CustomerResponseDto> content = customersPage.getContent().stream()
                .map(CustomerMapper::toCustomerResponseDto)
                .toList();

        return new PageResponseDto<>(
                content,
                customersPage.getNumber(),
                customersPage.getSize(),
                customersPage.getTotalElements(),
                customersPage.getTotalPages(),
                customersPage.isFirst(),
                customersPage.isLast()
        );
    }

}
