package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@PreAuthorize("IsAuthenticated()")
public class TransferController
{
    private TransferDao transferDao;
    private UserDao userDao;

    @Autowired
    public TransferController(TransferDao transferDao, UserDao userDao) {
        this.transferDao = transferDao;
        this.userDao = userDao;
    }

    @GetMapping("transfer/user/{userId}")
    public List<Transfer> getAllTransfers(@PathVariable Long userId)
    {
        List<Transfer> tranfers = transferDao.getAllTransfers(userId);
        return tranfers;
    }

    @GetMapping("transfer/{transferId}")
    public Transfer getTransferById(@PathVariable Long transferId)
    {
        Transfer tranfer = transferDao.getTransferById(transferId);
        return tranfer;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("transfer")
    public void transfer(@RequestBody Transfer transfer)
    {
        boolean canTransfer = transferDao.canTransfer(transfer.getAccountFrom()
                , transfer.getAccountTo()
                , transfer.getAmount());
        Long transferStatus = 2L;

        if(transfer.getTransferTypeId() == 1)
        {
            transferStatus = 1L;
            canTransfer = false;
        }

        if(canTransfer)
        {
            transferDao.create(transfer.getTransferId(),
                    transfer.getTransferTypeId(),
                    transferStatus,
                    transfer.getAccountFrom(),
                    transfer.getAccountTo(),
                    transfer.getAmount());
        }
    }
}
