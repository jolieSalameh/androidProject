package com.example.project;

import java.util.ArrayList;
//this class : to save the order in the fireStore , just for the user ->
//that will be saved in the UserOrder collection
public class UserOrder {
    public String id;
    public ArrayList<String> itemsName = new ArrayList<>();
    public ArrayList<String>amount = new ArrayList<>();
    public String price;
    public boolean isReady;

    public UserOrder(ArrayList<String> itemsName, ArrayList<String> amount ,String price,boolean isReady) {
        for(int i=0;i<itemsName.size();i++){
            this.itemsName.add(i,itemsName.get(i));
            this.amount.add(i,amount.get(i));
        }
        this.price=price;
        this.isReady = isReady;
    }

    public UserOrder(String id, ArrayList<String> itemsName, ArrayList<String> amount,String price) {
        this.id = id;
        for(int i=0;i<itemsName.size();i++){
            this.itemsName.add(i,itemsName.get(i));
            this.amount.add(i,amount.get(i));
        }
        this.price=price;
        isReady=false;
    }
}
