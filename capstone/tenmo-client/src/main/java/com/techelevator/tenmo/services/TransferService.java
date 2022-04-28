package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TransferService
{
    private static final String BASE_URL = "http://localhost:8080/";
    private static final RestTemplate restTemplate = new RestTemplate();

    private String authToken  = null;

    public void setAuthToken(String authToken)
    {
        this.authToken = authToken;
    }

    public List<Transfer> getHistory(AuthenticatedUser user)
    {
        List<Transfer> history = new ArrayList<>();

        try
        {
            String url = BASE_URL + "transfer/user/" + user.getUser().getId();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(authToken);
            HttpEntity<AuthenticatedUser> entity = new HttpEntity<> (user, headers);

            ResponseEntity<Transfer[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, Transfer[].class);
            if (response.getBody() != null) history = Arrays.asList(response.getBody());
        }
        catch (RestClientException e)
        {
            BasicLogger.log(e.getMessage());
        }
        return history;
    }

    public Transfer getTransfer(Long transferId)
    {
        Transfer transfer = new Transfer();

        try
        {
            String url = BASE_URL + "transfer/" + transferId;

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(authToken);
            HttpEntity<Transfer> entity = new HttpEntity<> (headers);

            ResponseEntity<Transfer> response = restTemplate.exchange(url, HttpMethod.GET, entity, Transfer.class);
            transfer = response.getBody();
        }
        catch (RestClientException e)
        {
            BasicLogger.log(e.getMessage());
        }
        return transfer;
    }

    public void makeTransfer(
             Long transferId,
             Long transferTypeId,
             Long transferStatusId,
             Long accountFrom,
             Long accountTo,
             BigDecimal amount
    )
    {
        Transfer transfer = new Transfer();

        try
        {
            String url = BASE_URL + "transfer/" + transferId;

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(authToken);
            HttpEntity<Transfer> entity = new HttpEntity<> (headers);

            ResponseEntity<Void> response = restTemplate.postForEntity(url, entity, Void.class);
        }
        catch (RestClientException e)
        {
            BasicLogger.log(e.getMessage());
        }
    }

}
