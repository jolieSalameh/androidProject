package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userId = getIntent().getStringExtra("userId");  // take the userId
    }
    public void startMenu(View v){
        Intent intent = new Intent(this,menu.class);
        intent.putExtra("userId",userId);
        startActivity(intent);
    }
    public void toClientOrder(View v){
        Intent intent = new Intent(this,ClientOrder.class);
        intent.putExtra("userId",userId);
        startActivity(intent);
    }
    public void logOut(View v){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(this,LogIn.class));
        finish();
    }


}