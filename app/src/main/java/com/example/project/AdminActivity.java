package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class AdminActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
    }
    public void startMenu(View v){
        Intent intent = new Intent(this,AddToMenu.class);
        startActivity(intent);
    }
    public void toTheOrders(View v){
        startActivity(new Intent(this,AllTheOrders.class));
    }
    public void logOut(View v){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(this,LogIn.class));
        finish();
    }
}