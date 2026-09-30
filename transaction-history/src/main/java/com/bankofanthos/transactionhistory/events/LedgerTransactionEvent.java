package com.bankofanthos.transactionhistory.events;
import com.bankofanthos.transactionhistory.domain.Transaction;
public record LedgerTransactionEvent(Transaction transaction) {}
