package com.techelevator.tenmo.controller;


import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;

@RestController
@PreAuthorize("IsAuthenticated()")
@RequestMapping("account")
public class AccountController {

    UserDao userDao;
    AccountDao accountDao;

    @Autowired
    public AccountController(UserDao userDao, AccountDao accountDao) {
        this.userDao = userDao;
        this.accountDao = accountDao;
    }

    @GetMapping()
    public BigDecimal getBalance(@PathVariable Long id)
    {
        BigDecimal balance = accountDao.findBalance(id);
        return balance;
    }

    @GetMapping()
    public Account getAccount(Principal principal)
    {
        String user = principal.getName();
        User accountId = userDao.findByUsername(user);
        Account id = accountDao.getAccountByUser(accountId.getId());

        return id;
    }
}
