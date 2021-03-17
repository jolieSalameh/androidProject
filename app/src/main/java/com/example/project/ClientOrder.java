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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ClientOrder extends AppCompatActivity {
    String userId;
    UserOrder userOrder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_order);
        userId = getIntent().getStringExtra("userId");
        getData();
    }
    public void getData(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentInfo = db.collection("UsersOrder").document(userId);
        //will get the document of the userId
        documentInfo.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        ArrayList<String> itemsName = (ArrayList<String>) document.get("itemsName");
                        ArrayList<String> itemsAmount = (ArrayList<String>) document.get("amount");
                        boolean status = document.getBoolean("isReady");
                        String price = document.getString("price");
                        userOrder = new UserOrder(itemsName,itemsAmount,price,status);
                        Log.d("TAG", "DocumentSnapshot data: " + document.getData());
                        putData();
                    } else {
                        Log.d("TAG", "No such document");
                    }
                } else {
                    Log.d("TAG", "get failed with ", task.getException());
                }
            }
        });

    }
// after we get the data this function will puts the data from the userOrder object to the table
    public void putData(){
        TableLayout table = findViewById(R.id.orderTable);
        for(int i=0;i<userOrder.itemsName.size();i++){
            TableRow row = (TableRow) table.getChildAt(i+1);
            row.setVisibility(View.VISIBLE);
            TextView txtName = (TextView) row.getChildAt(0);
            TextView amount = (TextView) row.getChildAt(1);
            txtName.setText(userOrder.itemsName.get(i));
            amount.setText(userOrder.amount.get(i));
        }
        TextView textP= findViewById(R.id.priceTxt);
        String price = userOrder.price;
        price+="$";
        textP.setText(price);
        putStatus();

    }
    //get the status and update if it's ready or not
    public void putStatus(){
        boolean status = userOrder.isReady;
        TextView statusTxt = findViewById(R.id.statusTxt);
        Log.i("putStatus: ","status= "+status);
        if(status==true){
            String isReady = "Ready";
            statusTxt.setText(isReady);
            statusTxt.setBackgroundColor(Color.parseColor("#43C34C"));
        }
        else {
            String isReady = "Not Ready";
            statusTxt.setText(isReady);
            statusTxt.setBackgroundColor(Color.parseColor("#DC3838"));
        }
    }
    // when we clicked on delete btn
    public void delete(View v){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("UsersOrder").document(userId)   //delete from fireStore
                .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("TAG", "DocumentSnapshot successfully deleted!");
                resetTable();  // after we delete it we must reset the table
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Error deleting document", e);
                    }
                });
    }
    // this function will make the table fields empty
    public void resetTable(){
        TableLayout table = findViewById(R.id.orderTable);
        for(int i=0;i<userOrder.itemsName.size();i++){
            TableRow row = (TableRow) table.getChildAt(i+1);
            TextView txtName = (TextView) row.getChildAt(0);
            TextView amount = (TextView) row.getChildAt(1);
            txtName.setText("");
            amount.setText("");
            row.setVisibility(View.GONE);
        }
        TextView statusTxt = findViewById(R.id.statusTxt);
        statusTxt.setText("");
        statusTxt.setBackgroundColor(Color.parseColor("#ffffff"));
    }

    public void goBackM(View v){
        Intent intent = new Intent(this,MainActivity.class);
        intent.putExtra("userId",userId);
        startActivity(intent);
    }
}