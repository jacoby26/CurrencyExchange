package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
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

    @GetMapping("/requests")
    public List<Transfer> getAllRequests(Principal principal)
    {
        List<Transfer> requests = new ArrayList<>();
        List<Transfer> transfers = transferDao.getAllTransfers(userDao.findByUsername(principal.getName()).getId());

        for(Transfer transfer: transfers)
        {
            if (transfer.getTransferTypeId() == 1) requests.add(transfer);
        }

        return requests;
    }

    @GetMapping("/requests/pending")
    public List<Transfer> getAllPendingRequests(Principal principal)
    {
        List<Transfer> requests = new ArrayList<>();
        List<Transfer> transfers = transferDao.getAllTransfers(userDao.findByUsername(principal.getName()).getId());

        for(Transfer transfer: transfers)
        {
            if (transfer.getTransferTypeId() == 1 && transfer.getTransferStatusId() == 1) requests.add(transfer);
        }

        return requests;
    }

    @PutMapping("/requests/confirm")
    public void confirmRequest(@RequestBody Transfer transfer)
    {
        transferDao.confirmRequest(transfer);
    }

    @PutMapping("/requests/deny/{transferId}")
    public void denyRequest(@PathVariable Long transferId)
    {
        transferDao.denyRequest(transferId);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping()
    public void transfer(@RequestBody Transfer transfer)
    {
        boolean canTransfer = transferDao.canTransfer(transfer.getAccountFrom()
                , transfer.getAccountTo()
                , transfer.getAmount());
        Long transferStatus = 2L;

        if(canTransfer && transfer.getTransferTypeId() == 1)
        {
            transferStatus = 1L;
            transferDao.createRequest(
                    transfer.getAccountFrom(),
                    transfer.getAccountTo(),
                    transfer.getAmount());
        }

        if(canTransfer && transferStatus == 2L)
        {
            transferDao.createSend(
                    transfer.getAccountFrom(),
                    transfer.getAccountTo(),
                    transfer.getAmount());
        }
    }
}
