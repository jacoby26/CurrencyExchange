package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcAccountDao implements AccountDao
{
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcAccountDao(JdbcTemplate jdbcTemplate)
    {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Account> accountList()
    {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT account_id\n" +
                "FROM account;";
        SqlRowSet rows = jdbcTemplate.queryForRowSet(sql);

        while(rows.next())
        {
            Account account = mapRowToAccount(rows);
            accounts.add(account);
        }

        return accounts;
    }


    @Override
    public BigDecimal findBalance(Long id)
    {
        String sql = "SELECT balance " +
                "FROM account " +
                "WHERE account_id = ?;";

        SqlRowSet row = jdbcTemplate.queryForRowSet(sql, id);

        BigDecimal balance = null;
        if(row.next())
        {
            balance = row.getBigDecimal("balance");
        }
        return balance;
    }

    @Override
    public Account getAccount(Long id)
    {
        String sql = "SELECT account_id " +
                "FROM account " +
                "WHERE account_id = ?;";

        SqlRowSet row = jdbcTemplate.queryForRowSet(sql, id);

        Account account = null;
        if(row.next())
        {
            account = mapRowToAccount(row);
        }
        return account;
    }


    private Account mapRowToAccount(SqlRowSet row)
    {
        Account account;

        Long id = row.getLong("account_id");
        Long userId = row.getLong("user_id");
        BigDecimal balance = row.getBigDecimal("balance");

        account = new Account(id, userId, balance);

        return account;
    }
}
