package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import com.techelevator.util.BasicLogger;
import io.cucumber.java.bs.A;
import io.cucumber.java.en_old.Ac;
import org.springframework.http.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.security.Principal;

public class AccountService {

    private static final String BASE_URL = "http://localhost:8080/";
    private static final RestTemplate restTemplate = new RestTemplate();

    private String authToken  = null;

    public void setAuthToken(String authToken)
    {
        this.authToken = authToken;
    }

    public BigDecimal getBalance(Account account)
    {
        BigDecimal balance = new BigDecimal("0.00");

        try
        {
            String url = BASE_URL + "account";
            ResponseEntity<Account> response = restTemplate.exchange(url, HttpMethod.GET, makeEntity(account), Account.class);
            balance = response.getBody().getBalance();
        } catch (RestClientException e) {
            /*e.printStackTrace();*/
            BasicLogger.log(e.getMessage());
        }
        return balance;

    }

    public Account getAccountByUserId(User user)
    {
        Account account = new Account();
        try
        {
            
        }
    }

    public HttpEntity makeEntity(Account account)
    {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authToken);
        HttpEntity<Account> entity = new HttpEntity<> (account, headers);
        return entity;
    }

}
