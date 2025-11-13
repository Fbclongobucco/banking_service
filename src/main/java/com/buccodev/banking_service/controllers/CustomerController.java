package com.buccodev.banking_service.controllers;

import com.buccodev.banking_service.services.CustomerService;
import com.buccodev.banking_service.utils.dto.customer.CustomerRequestDto;
import com.buccodev.banking_service.utils.dto.customer.CustomerResponseDto;
import com.buccodev.banking_service.utils.dto.customer.CustomerUpdateDto;
import jakarta.websocket.server.PathParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    public ResponseEntity<CustomerResponseDto> createCustomer(@RequestBody CustomerRequestDto customerRequestDto) {
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

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateCustomer(@PathVariable Long id, @RequestBody CustomerUpdateDto customerUpdateDtoDto) {
        customerService.updateCustomer(id, customerUpdateDtoDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<CustomerResponseDto>> getAllCustomers(@PathParam("page") Integer page, @PathParam("size") Integer size) {
        var customers = customerService.getAllCustomers(page, size);
        return ResponseEntity.ok(customers);
    }

}
