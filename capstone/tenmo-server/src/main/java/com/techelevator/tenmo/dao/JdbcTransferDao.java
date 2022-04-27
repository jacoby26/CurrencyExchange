package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao
{
    private JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate)
    {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Transfer> getAllTransfers(Long userId)
    {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT t.transfer_id " +
                ", t.transfer_type_id " +
                ", t.transfer_status_id " +
                ", t.account_from " +
                ", t.account_to " +
                ", t.amount " +
                " FROM transfer AS t" +
                " JOIN account AS a ON a.account_id = transfer.account_from " +
                " AND a.account_id = transfer.account_to" +
                " JOIN tenmo_user AS tu ON tu.user_id = a.user_id " +
                " WHERE tu.user_id = ?;";
        SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, userId);

        while(rows.next())
        {
            Transfer transfer = mapRowToTransfer(rows);
            transfers.add(transfer);
        }

        return transfers;
    }

    @Override
    public Transfer getTransfersById(Long transferId)
    {
        String sql = "SELECT transfer_id " +
                ", transfer_type_id " +
                ", transfer_status_id " +
                ", account_from " +
                ", account_to " +
                ", amount " +
                " FROM transfer" +
                " WHERE transfer_id = ?;";
        SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, transferId);

        if(rows.next())
        {
            return mapRowToTransfer(rows);
        }
        return null;
    }

    @Override
    public List<Transfer> getAllTransfersByStatus(Long userId, Long transferStatusId)
    {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT t.transfer_id " +
                ", t.transfer_type_id " +
                ", t.transfer_status_id " +
                ", t.account_from " +
                ", t.account_to " +
                ", t.amount " +
                " FROM transfer AS t" +
                " JOIN account AS a ON a.account_id = transfer.account_from " +
                " AND a.account_id = transfer.account_to" +
                " JOIN tenmo_user AS tu ON tu.user_id = a.user_id " +
                " WHERE tu.user_id = ?" +
                " AND t.transfer_status_id = ?;";
        SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, userId, transferStatusId);

        while(rows.next())
        {
            Transfer transfer = mapRowToTransfer(rows);
            transfers.add(transfer);
        }

        return transfers;
    }

    @Override
    public List<Transfer> getAllTransfersByType(Long userId, Long transferTypeId) {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT t.transfer_id " +
                ", t.transfer_type_id " +
                ", t.transfer_status_id " +
                ", t.account_from " +
                ", t.account_to " +
                ", t.amount " +
                " FROM transfer AS t" +
                " JOIN account AS a ON a.account_id = transfer.account_from " +
                " AND a.account_id = transfer.account_to" +
                " JOIN tenmo_user AS tu ON tu.user_id = a.user_id " +
                " WHERE tu.user_id = ?" +
                " AND t.transfer_type_id = ?;";
        SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, userId, transferTypeId);

        while(rows.next())
        {
            Transfer transfer = mapRowToTransfer(rows);
            transfers.add(transfer);
        }

        return transfers;
    }

    @Override
    public Boolean canTransfer(Long accountFrom, Long accountTo, BigDecimal amount)
    {
        if (accountFrom == accountTo)
        {
            ;
        }



        return null;
    }

    private Transfer mapRowToTransfer(SqlRowSet row)
    {
        Transfer transfers;

        Long transferId = row.getLong("transfer_id");
        Long transferTypeId = row.getLong("transfer_type_id");
        Long transferStatusId = row.getLong("transfer_status_id");
        Long accountFrom = row.getLong("account_from");
        Long accountTo = row.getLong("account_to");
        BigDecimal amount = row.getBigDecimal("amount");

        transfers = new Transfer(transferId, transferTypeId, transferStatusId, accountFrom, accountTo, amount);

        return transfers;
    }


}
