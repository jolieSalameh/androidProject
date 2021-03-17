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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Document;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {
    EditText fNameTxt, lNameTxt, emailTxt, passwordTxt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
         fNameTxt = findViewById(R.id.firstName);
         lNameTxt = findViewById(R.id.lastName);
         emailTxt = findViewById(R.id.emailAddress);
         passwordTxt = findViewById(R.id.password);
    }
    public void clickedSign_in(View v){
        String fName = fNameTxt.getText().toString();
        String lName = lNameTxt.getText().toString();
        String email = emailTxt.getText().toString();
        String password = passwordTxt.getText().toString();
        if(checkInput(fName,lName,email,password)==1)
            signIn(fName,lName,email,password);
    }
    public void signIn(String fName ,String lName,String email,String password){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseFirestore fStore = FirebaseFirestore.getInstance();
        auth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                FirebaseUser user = auth.getCurrentUser();
                Toast.makeText(SignUp.this,
                        "You are now signed in", Toast.LENGTH_LONG).show();
                DocumentReference df = fStore.collection("Users").document(user.getUid());
                Map<String,Object> userInfo = new HashMap<>();
                userInfo.put("firstName",fName);
                userInfo.put("lastName",lName);
                userInfo.put("emailAddress",email);
                userInfo.put("isUser","1");
                df.set(userInfo);
                startActivity(new Intent(SignUp.this,LogIn.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignUp.this,
                        "SomeThing wrong!!", Toast.LENGTH_LONG).show();
            }
        });


    }
    public int checkInput(String fName ,String lName,String email,String password){
        if(fName.equals("")|| lName.equals("") || email.equals("") || password.equals("")){
            Toast.makeText(this,
                    "You must enter your information", Toast.LENGTH_LONG).show();
            return 0;
        }
        if(!(email.isEmpty() && fName.isEmpty() && lName.isEmpty() && password.isEmpty())){
            if(email.contains("@")&& (email.contains(".com") || email.contains(".co.il")) ) {
                return 1;
            }
                else{
                    Toast.makeText(this,
                            "Email address is't valid", Toast.LENGTH_LONG).show();
                    return 0;
            }
        }

        return 1;
    }
    public void toLogIn(View v){
        startActivity(new Intent(this,LogIn.class));
    }
}