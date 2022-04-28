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

public class AccountService extends ServiceBase<AuthenticatedUser> {

    public AccountService(String BASE_URL) {
        super(BASE_URL);
    }


    public BigDecimal getBalance(AuthenticatedUser user)
    {
        BigDecimal balance = new BigDecimal(0);

        try
        {
            String url = BASE_URL + "user";
            ResponseEntity<Account> response = restTemplate.exchange(url, HttpMethod.GET, getAuthEntity(user), Account.class);
            balance = response.getBody().getBalance();
        } catch (RestClientException e) {
            /*e.printStackTrace();*/
            BasicLogger.log(e.getMessage());
        }
        return balance;
    }

    /*public Account getAccountByUserId(User user)
    {
        Account account = new Account();
        try
        {
            String url = BASE_URL + "account/user/";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(getAuthToken());
            HttpEntity<User> entity = new HttpEntity<> (user, headers);

            ResponseEntity<Account> response = restTemplate.exchange(url, HttpMethod.GET, entity, Account.class);
            account = response.getBody();
        }
        catch (RestClientException e)
        {
            *//*e.printStackTrace();*//*
            BasicLogger.log(e.getMessage());
        }
        return account;
    }*/

    /*public HttpEntity makeEntity()
    {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(getAuthToken());
        HttpEntity<AuthenticatedUser> entity = new HttpEntity<> (headers, user);
        return entity;
    }*/

}
