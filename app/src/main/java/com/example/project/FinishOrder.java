package com.example.project;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;

public class FinishOrder extends AppCompatActivity {

    ArrayList<Item> itemsArray =new ArrayList<Item>();
    ArrayList<ItemsOrder> finalOrder = new ArrayList<ItemsOrder>();
    ArrayList<String>itemsName = new ArrayList<>();
    ArrayList<String>itemsAmount = new ArrayList<>();
    EditText fName,lName,phoneNum,city;
    TextView priceTxt;
    String userId,price;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_order);
        //get the array that been saved and sent from the menu activity
        itemsArray = (ArrayList<Item>) getIntent().getSerializableExtra("item");
        userId = getIntent().getStringExtra("userId");
        price = getIntent().getStringExtra("price");
        Log.i("finishOrder","price: "+price);
        TableLayout table = findViewById(R.id.orderTable);
        int i=0;
        //puts the order from the array on the table
        while(i < itemsArray.size()){
            TableRow tableRow = (TableRow) table.getChildAt(i+1);
            tableRow.setVisibility(View.VISIBLE);
            TextView text = (TextView) tableRow.getChildAt(0);
            TextView amount = (TextView) tableRow.getChildAt(1);
            text.setText(itemsArray.get(i).nameItem);
            amount.setText(String.valueOf(itemsArray.get(i).amount));
            i++;
        }
        priceTxt = findViewById(R.id.priceTxt);
        String pr = price+"$";
        priceTxt.setText(pr);
    }
    public void saveToFireBase(View v){
        fName = findViewById(R.id.firstName);
        lName = findViewById(R.id.lastName);
        phoneNum = findViewById(R.id.phoneNumber);
        city = findViewById(R.id.city);
        String firstName,lastName,phoneNumber,cityS;
        firstName = fName.getText().toString();
        lastName = lName.getText().toString();
        phoneNumber = phoneNum.getText().toString();
        cityS = city.getText().toString();
        int check=0;
        check = checkInput(firstName, lastName, phoneNumber, cityS);
        if(check ==1) {
            FirebaseApp.initializeApp(this);
            FirebaseFirestore db = FirebaseFirestore.getInstance();  // fireStore instance
            finalOrder();
            Date date = Calendar.getInstance().getTime(); // take the current date
            OrderClient orderClient = new OrderClient(firstName,lastName,phoneNumber,cityS,userId,itemsName,itemsAmount,price,date);
            // save to fireStore
            db.collection("Orders").document(userId).set(orderClient)  //this will save to the Orders collection
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            FirebaseFirestore dbu = FirebaseFirestore.getInstance();
                            //will be saved in the UsersOrder collection
                            dbu.collection("UsersOrder").document(userId).set(new UserOrder(userId,itemsName,itemsAmount,price))
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(FinishOrder.this, "Your order has been saved", Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(FinishOrder.this, MainActivity.class);
                                            intent.putExtra("userId", userId);
                                            startActivity(intent);
                                        }
                                    });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(FinishOrder.this, "Something Wrong!!", Toast.LENGTH_LONG).show();

                        }
                    });


        }
    }
    public int checkInput(String firstN,String lasN,String phoneN,String city){
        if(firstN.equals("") || lasN.equals("") || phoneN.equals("")|| city.equals("")) {
            Toast.makeText(this,
                    "You must enter your information", Toast.LENGTH_LONG).show();
            return 0;
        }
        assert phoneN != null;
        if(!phoneN.isEmpty() && (!firstN.isEmpty() && !lasN.isEmpty() && !city.isEmpty()) ){
           if(phoneN.length()==10){
               Toast.makeText(this,
                       "The phone number is't valid", Toast.LENGTH_LONG).show();
               return 0;
           }

        }
        return 1;
    }

    public void finalOrder(){
        for(int i=0;i<itemsArray.size();i++){
            String name = itemsArray.get(i).nameItem;
            String amount = String.valueOf(itemsArray.get(i).amount);
            ItemsOrder order = new ItemsOrder(name,amount);
            finalOrder.add(i,order);
            itemsName.add(i,finalOrder.get(i).nameRoll);
            itemsAmount.add(i,finalOrder.get(i).amount);
        }
    }
}