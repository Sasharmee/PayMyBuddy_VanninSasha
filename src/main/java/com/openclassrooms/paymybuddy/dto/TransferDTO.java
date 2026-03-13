package com.openclassrooms.paymybuddy.dto;

import java.math.BigDecimal;

public class TransferDTO {

    private String receiverEmail;

    private BigDecimal amount;

    private String description;

    public TransferDTO() {
    }

    public String getReceiverEmail() {
        return receiverEmail;
    }

    public String getAmount() {
        return amount;
    }

    public BigDecimal getDescription() {
        return description;
    }

    public void setReceiverEmail(String receiverEmail) {
        this.receiverEmail = receiverEmail;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
