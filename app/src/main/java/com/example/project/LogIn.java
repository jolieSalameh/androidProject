package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LogIn extends AppCompatActivity {
    EditText emailTxt,passwordTxt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        emailTxt=findViewById(R.id.Email);
        passwordTxt=findViewById(R.id.password);
    }
    public void clickedLogIn(View v){
        String email = emailTxt.getText().toString();
        String password = passwordTxt.getText().toString();
        if(checkInput(email,password)==1)
            logIn(email,password);
    }

    public void logIn(String email,String password){
        FirebaseFirestore fStore = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(LogIn.this,
                        "Logged in successfully", Toast.LENGTH_LONG).show();
                isAdmin(authResult.getUser().getUid());

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LogIn.this,
                        "this account dose not exist!!", Toast.LENGTH_LONG).show();
            }
        });

    }
    public void isAdmin(String id){
        FirebaseFirestore fStore = FirebaseFirestore.getInstance();
        DocumentReference df = fStore.collection("Users").document(id);
        //extract the data from the document
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.getString("isUser")!=null) {
                    Intent intent = new Intent(LogIn.this, MainActivity.class);
                    intent.putExtra("userId",id);  //to send the id of the user
                    startActivity(intent);
                    finish();
                }
                if(documentSnapshot.getString("isAdmin")!=null) {
                    startActivity(new Intent(LogIn.this, AdminActivity.class));
                    finish();
                }

            }
        });
    }

    public int checkInput(String email,String password){
        if(email == null && password == null){
            Toast.makeText(this,
                    "You input is empty!", Toast.LENGTH_LONG).show();
            return 0;
        }
        if(email == null && password != null){
            Toast.makeText(this,
                    "You did not enter your email address", Toast.LENGTH_LONG).show();
            return 0;
        }
        if(email != null && password == null){
            Toast.makeText(this,
                    "You did not enter your password", Toast.LENGTH_LONG).show();
            return 0;
        }
        return 1;
    }
    public void toSignUp(View v){
        startActivity(new Intent(this,SignUp.class));
    }
}