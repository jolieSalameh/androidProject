package com.example.project;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Order implements Serializable {
    public ArrayList<ItemsOrder> order = new ArrayList<>();
    public Client client;
    public Date date;
    public boolean isReady;

    public Order(){}
    public Order(ArrayList<ItemsOrder> ord, Client client,Date date) {
        for(int i=0;i<ord.size();i++)
            this.order.add(i,ord.get(i));
        this.client = client;
        this.date = date;
        this.isReady = false;
    }
}
