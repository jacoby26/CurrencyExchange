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
import java.util.List;

@RestController
@RequestMapping("account")
@PreAuthorize("isAuthenticated()")
//@RequestMapping("account")
public class AccountController {

    UserDao userDao;
    AccountDao accountDao;

    @Autowired
    public AccountController(UserDao userDao, AccountDao accountDao) {
        this.userDao = userDao;
        this.accountDao = accountDao;
    }

   /* @GetMapping("account/balance/{id}")
    public BigDecimal getBalance(@PathVariable Long id)
    {
        BigDecimal balance = accountDao.findBalance(id);
        return balance;
    }*/

    @GetMapping("user")
    public Account getAccount(Principal principal)
    {
        String username = principal.getName();
        User user = userDao.findByUsername(username);
        Account userAccount = accountDao.getAccountByUser(user.getId());

        return userAccount;
    }

    @GetMapping("user/balance")
    public BigDecimal getBalance(Principal principal)
    {
        String username = principal.getName();
        User user = userDao.findByUsername(username);
        Account userAccount = accountDao.getAccountByUser(user.getId());

        return userAccount.getBalance();
    }

    @GetMapping("user/account_id")
    public Long getAccountId(Principal principal)
    {
        String username = principal.getName();
        User user = userDao.findByUsername(username);
        Account userAccount = accountDao.getAccountByUser(user.getId());

        return userAccount.getId();
    }


    @GetMapping("{account_id}")
    public User getUser(@PathVariable Long accountId)
    {
        return userDao.findById(accountDao.getAccount(accountId).getUserId());
    }

    @GetMapping()
    public List<Account> listAccounts()
    {
        return accountDao.accountList();
    }
}
