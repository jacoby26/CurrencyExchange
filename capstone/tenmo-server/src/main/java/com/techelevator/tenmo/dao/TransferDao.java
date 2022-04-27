package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDao
{
    public List<Transfer> getAllTransfers(Long userId);
    public Transfer getTransfersById(Long transferId);
    public List<Transfer> getAllTransfersByStatus(Long userId, Long transferStatusId);
    public List<Transfer> getAllTransfersByType(Long userId, Long transferTypeId);
    public Boolean canTransfer(Long accountFrom, Long accountTo, BigDecimal amount);
    // can request transfer


}