package com.example.project;

public class ItemsOrder {
   public String nameRoll;
   public String amount;

    public ItemsOrder(String nameRoll, String amount) {
        this.nameRoll = nameRoll;
        this.amount = amount;
    }


    public String getAmount() {
        return amount;
    }


    public void setAmount(String amount) {
        this.amount = amount;
    }
}
