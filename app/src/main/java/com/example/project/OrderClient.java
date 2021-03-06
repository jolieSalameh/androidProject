package com.example.project;

import java.util.ArrayList;
import java.util.Date;
// this class we use it when we get tha order from fireStore
public class OrderClient {
    public String firstName;
    public String lastName;
    public String phoneNumber;
    public String city;
    public String id;
    public ArrayList<String>itemsName = new ArrayList<>();
    public ArrayList<String>amount = new ArrayList<>();
    public boolean isReady;
    public Date date;

    public OrderClient(String firstName, String lastName, String phoneNumber, String city, String id, ArrayList<String> itemsName, ArrayList<String> amount, Date date) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.city = city;
        this.id = id;
        for(int i=0;i<itemsName.size();i++){
            this.itemsName.add(i,itemsName.get(i));
            this.amount.add(i,amount.get(i));
        }
        this.isReady=false;
        this.date = date;
    }
}
