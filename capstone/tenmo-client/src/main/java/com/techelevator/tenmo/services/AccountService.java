package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.User;
import com.techelevator.util.BasicLogger;
import io.cucumber.java.bs.A;
import io.cucumber.java.en_old.Ac;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.security.Principal;
import java.text.Bidi;

public class AccountService {

    private static final String BASE_URL = "http://localhost:8080/";
    private static final RestTemplate restTemplate = new RestTemplate();

    private String authToken  = null;

    public void setAuthToken(String authToken)
    {
        this.authToken = authToken;
    }

    public BigDecimal getBalance(AuthenticatedUser user)
    {
        BigDecimal balance = new BigDecimal("0.00");

        try
        {
            String url = BASE_URL + "account/balance/" + user.getUser().getId();
            ResponseEntity<BigDecimal> response = restTemplate.exchange(url, HttpMethod.GET, makeEntity(), BigDecimal.class);
            balance = response.getBody();
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
            String url = BASE_URL + "account/user/";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(authToken);
            HttpEntity<User> entity = new HttpEntity<> (user, headers);

            ResponseEntity<Account> response = restTemplate.exchange(url, HttpMethod.GET, entity, Account.class);
            account = response.getBody();
        }
        catch (RestClientException e)
        {
            /*e.printStackTrace();*/
            BasicLogger.log(e.getMessage());
        }
        return account;
    }

    public HttpEntity makeEntity()
    {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authToken);
        HttpEntity<Void> entity = new HttpEntity<> (headers);
        return entity;
    }

}
