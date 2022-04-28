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
@PreAuthorize("isAuthenticated()")
@RequestMapping("transfer")
public class TransferController
{
    private TransferDao transferDao;
    private UserDao userDao;

    @Autowired
    public TransferController(TransferDao transferDao, UserDao userDao) {
        this.transferDao = transferDao;
        this.userDao = userDao;
    }

    @GetMapping("user/{userId}")
    public List<Transfer> getAllTransfers(@PathVariable Long userId)
    {
        List<Transfer> transfers = transferDao.getAllTransfers(userId);
        return transfers;
    }

    @GetMapping("{transferId}")
    public Transfer getTransferById(@PathVariable Long transferId)
    {
        Transfer tranfer = transferDao.getTransferById(transferId);
        return tranfer;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping()
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

        if(canTransfer && transferStatus == 2L)
        {
            transferDao.createSend(
                    transfer.getAccountFrom(),
                    transfer.getAccountTo(),
                    transfer.getAmount());
        }
//        if(canTransfer && transferStatus == 1L)
//        {
//            transferDao.createSend(
//                    transfer.getAccountFrom(),
//                    transfer.getAccountTo(),
//                    transfer.getAmount());
//        }
    }
}
