package com.buccodev.banking_service.controllers;

import com.buccodev.banking_service.dtos.sharedDtos.PageResponseDto;
import com.buccodev.banking_service.entities.Roles;
import com.buccodev.banking_service.services.CustomerService;
import com.buccodev.banking_service.dtos.customer.CustomerRequestDto;
import com.buccodev.banking_service.dtos.customer.CustomerResponseDto;
import com.buccodev.banking_service.dtos.customer.CustomerUpdateDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    public ResponseEntity<CustomerResponseDto> createCustomer(@Valid @RequestBody CustomerRequestDto customerRequestDto) {
        var customerDto = customerService.createCustomer(customerRequestDto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(customerDto.id()).toUri();
        return ResponseEntity.created(location).body(customerDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponseDto> getCustomerById(@PathVariable Long id) {
        var customerDto = customerService.getCustomerById(id);
        return ResponseEntity.ok(customerDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<CustomerResponseDto> getCustomerByEmail(@PathVariable String email) {
        var customerDto = customerService.getCustomerByEmail(email);
        return ResponseEntity.ok(customerDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/role/{role}")
    public ResponseEntity<Void> updateRole(@PathVariable Long id, @PathVariable Roles role) {
        customerService.updateRole(id, role);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateCustomer(@PathVariable Long id,  @Valid @RequestBody CustomerUpdateDto customerUpdateDtoDto) {
        customerService.updateCustomer(id, customerUpdateDtoDto);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<PageResponseDto<CustomerResponseDto>> getAllCustomers(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        PageResponseDto<CustomerResponseDto> response = customerService.getAllCustomers(page, size);
        return ResponseEntity.ok(response);
    }

}
