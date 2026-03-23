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

    public BigDecimal getAmount() {
        return amount;
    }

    public String getDescription() {
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
