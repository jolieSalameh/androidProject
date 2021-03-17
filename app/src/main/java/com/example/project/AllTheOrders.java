package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.collect.Table;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class AllTheOrders extends AppCompatActivity {
    ArrayList<Order> orders = new ArrayList<>();
    int index=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_the_orders);
        getData();
    }
    public void getData(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference collectionRef = db.collection("Orders");
        collectionRef.get()    // get the orders data from fireStore
                // this will get all the documents
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            //to go through all the document one by one and get the data fields
                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                initData(document,index);
                                index++;
                                Log.d("successGet", document.getId() + " => " + document.getData());
                            }
                            putTheOrders();
                        } else {
                            Log.d("FailingGet", "Error getting documents: ", task.getException());
                        }
                    }
                });

    }
    //this function saved the document fields in the array
    public void initData(QueryDocumentSnapshot document,int index){
        String firstName = document.getString("firstName");
        String lastName = document.getString("lastName");
        String phoneNumber = document.getString("phoneNumber");
        String city = document.getString("city");
        String id = document.getString("id");
        Client client = new Client(firstName,lastName,phoneNumber,city,id);
        ArrayList<String>itemsName = (ArrayList<String>) document.get("itemsName");
        ArrayList<String>itemsAmount = (ArrayList<String>) document.get("amount");
        ArrayList<ItemsOrder> itemsOrders = new ArrayList<>();
        for(int i=0;i<itemsName.size();i++){
            String name = itemsName.get(i);
            String amount = itemsAmount.get(i);
            ItemsOrder item = new ItemsOrder(name,amount);
            itemsOrders.add(i,item);
            Log.i("itemsOrder","add= "+itemsOrders.get(i).nameRoll);
        }
        Date date = document.getDate("date");
        Order order = new Order(itemsOrders, (Client) client,date);
        orders.add(index,order);
    }
    public void putTheOrders(){
        TableLayout table = findViewById(R.id.tableOrder);
        int r=0, ord=1;
        for(int i=0 ; i<orders.size();i++){
            TableRow rowCInfo = (TableRow) table.getChildAt(r);
            TableRow rowOInfo = (TableRow) table.getChildAt(ord);
            rowOInfo.setVisibility(View.VISIBLE);
            rowCInfo.setVisibility(View.VISIBLE);
            TextView txtFName = (TextView) rowCInfo.getChildAt(0);
            TextView txtLName = (TextView) rowCInfo.getChildAt(1);
            TextView txtPhoneNumber = (TextView) rowCInfo.getChildAt(2);
            TextView txtCity = (TextView) rowCInfo.getChildAt(3);
            TextView txtOrder = (TextView) rowOInfo.getChildAt(1);
            txtFName.setText(orders.get(i).client.firstName);
            txtLName.setText(orders.get(i).client.lastName);
            txtPhoneNumber.setText(orders.get(i).client.phoneNumber);
            txtCity.setText(orders.get(i).client.city);
            String order="";
            for(int j=0;j<orders.get(i).order.size();j++){
                order += orders.get(i).order.get(j).nameRoll;
                order += ".   "+orders.get(i).order.get(j).amount+"\n";
                txtOrder.setText(order);
            }
            r+=2;
            ord+=2;

        }

    }
    public void beforeUpdate(View v){
        TableRow row = (TableRow) v.getParent();
        TextView txtStatus = (TextView) v;
        txtStatus.setBackgroundColor(Color.parseColor("#84ED8C"));
        String isReady="Ready";
        txtStatus.setText(isReady);
        TableLayout table = findViewById(R.id.tableOrder);
        int indexR= table.indexOfChild(row);
        Log.i("lineAlignedChildIndex ","indexR= "+indexR);
        TextView text = (TextView) row.getChildAt(1);
        TableRow targetRow = (TableRow) table.getChildAt(indexR-1);
        TextView txtPhoneNumber = (TextView) targetRow.getVirtualChildAt(2);
        String phoneNumber = txtPhoneNumber.getText().toString();
        findId(phoneNumber,1,indexR-1);
    }
    public void deleteClicked(View v){
        TableRow row = (TableRow) v.getParent();
        TableLayout table = findViewById(R.id.tableOrder);
        int indexR= table.indexOfChild(row);
        Log.i("lineAlignedChildIndex ","indexR= "+indexR);
        TableRow targetRow = (TableRow) table.getChildAt(indexR-1);
        TextView txtPhoneNumber = (TextView) targetRow.getVirtualChildAt(2);
        String phoneNumber = txtPhoneNumber.getText().toString();
        findId(phoneNumber,2,indexR-1);
    }

    public void findId(String phoneN,int flag,int rowIndex) {
        int ind = 0,indexD=0;
        String userId="";
        while (ind < orders.size()) {
            if(phoneN.equals(orders.get(ind).client.phoneNumber)){
                userId = orders.get(ind).client.id;
                indexD=ind;
                break;
            }
            ind++;
        }
        if(flag==1)
            toUpdate(userId);
        else delete(userId,rowIndex,indexD);
    }
    public void toUpdate(String userId){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Orders").document(userId)
                .update(
                        "isReady",true
                );
        db.collection("UsersOrder").document(userId)
                .update(
                        "isReady",true
                );
    }
    public void delete(String userId,int rowIndex,int indexD){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Orders").document(userId)
                .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("TAG", "DocumentSnapshot successfully deleted!");
                resetTable(rowIndex,indexD);
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Error deleting document", e);
                    }
                });
    }
    public void resetTable(int rowIndex,int indexD){
        TableLayout table = findViewById(R.id.tableOrder);
        int r=0, ord=1;
        for(int i=0 ; i<orders.size();i++){
            TableRow rowCInfo = (TableRow) table.getChildAt(r);
            TableRow rowOInfo = (TableRow) table.getChildAt(ord);
            TextView txtFName = (TextView) rowCInfo.getChildAt(0);
            TextView txtLName = (TextView) rowCInfo.getChildAt(1);
            TextView txtPhoneNumber = (TextView) rowCInfo.getChildAt(2);
            TextView txtCity = (TextView) rowCInfo.getChildAt(3);
            TextView txtOrder = (TextView) rowOInfo.getChildAt(1);
            txtFName.setText("");
            txtLName.setText("");
            txtPhoneNumber.setText("");
            txtCity.setText("");
            txtOrder.setText("");
            rowOInfo.setVisibility(View.GONE);
            rowCInfo.setVisibility(View.GONE);
            r+=2;
            ord+=2;
        }
        orders.remove(indexD);
        putTheOrders();

    }
    public void goBackA(View v){
        Intent intent = new Intent(this,AdminActivity.class);
        startActivity(intent);
    }
}