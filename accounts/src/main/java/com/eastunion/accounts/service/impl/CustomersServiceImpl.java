package com.eastunion.accounts.service.impl;

import com.eastunion.accounts.dto.AccountsDto;
import com.eastunion.accounts.dto.CardsDto;
import com.eastunion.accounts.dto.CustomerDetailsDto;
import com.eastunion.accounts.dto.LoansDto;
import com.eastunion.accounts.entity.Accounts;
import com.eastunion.accounts.entity.Customer;
import com.eastunion.accounts.exception.ResourceNotFoundException;
import com.eastunion.accounts.mapper.AccountsMapper;
import com.eastunion.accounts.mapper.CustomerMapper;
import com.eastunion.accounts.repository.AccountsRepository;
import com.eastunion.accounts.repository.CustomerRepository;
import com.eastunion.accounts.service.ICustomersService;
import com.eastunion.accounts.service.client.CardsFeignClient;
import com.eastunion.accounts.service.client.LoansFeignClient;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomersServiceImpl implements ICustomersService {

    private AccountsRepository accountsRepository;
    private CustomerRepository customerRepository;
    private CardsFeignClient cardsFeignClient;
    private LoansFeignClient loansFeignClient;

    /**
     * @param mobileNumber - Input Mobile Number
     * @return Customer Details based on a given mobileNumber
     */
    @Override
    public CustomerDetailsDto fetchCustomerDetails(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );
        Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(
                () -> new ResourceNotFoundException("Account", "customerId", customer.getCustomerId().toString())
        );

        CustomerDetailsDto customerDetailsDto = CustomerMapper.mapToCustomerDetailsDto(customer, new CustomerDetailsDto());
        customerDetailsDto.setAccountsDto(AccountsMapper.mapToAccountsDto(accounts, new AccountsDto()));

        ResponseEntity<LoansDto> loansDtoResponseEntity = loansFeignClient.fetchLoanDetails(mobileNumber);
        customerDetailsDto.setLoansDto(loansDtoResponseEntity.getBody());

        ResponseEntity<CardsDto> cardsDtoResponseEntity = cardsFeignClient.fetchCardDetails(mobileNumber);
        customerDetailsDto.setCardsDto(cardsDtoResponseEntity.getBody());

        return customerDetailsDto;

    }
}
