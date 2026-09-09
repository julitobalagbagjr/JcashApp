package jcash.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Transaction {

    private int id;
    private String referenceNo;
    private int senderAccountId;
    private int receiverAccountId;
    private BigDecimal amount;
    private LocalDateTime createdAt;

    public Transaction(
            int id,
            String referenceNo,
            int senderAccountId,
            int receiverAccountId,
            BigDecimal amount,
            LocalDateTime createdAt
    ) {
        this.id = id;
        this.referenceNo = referenceNo;
        this.senderAccountId = senderAccountId;
        this.receiverAccountId = receiverAccountId;
        this.amount = amount;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public String getReferenceNo() {
        return referenceNo;
    }

    public int getSenderAccountId() {
        return senderAccountId;
    }

    public int getReceiverAccountId() {
        return receiverAccountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}