package com.example.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import java.util.ArrayList;

public class menu extends AppCompatActivity {
    ArrayList<Item> itemsOrder = new ArrayList<Item>(); // this array to save the items roll that has been selected
    int indexI=0;
    String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        userId = getIntent().getStringExtra("userId");  // take the userId
    }
    //when the client clicked on any items roll,will activate this function
    //this function change the background of the card,and save the items that been selected in the array
    public void onClickV(View v){
        CardView card = (CardView) v;
        TextView text = (TextView) card.getChildAt(1);
       // card.setBackgroundColor(Color.parseColor("#5E0C0C"));
        text.setBackgroundColor(Color.parseColor("#5E0C0C"));
        ImageView image = (ImageView) card.getChildAt(0);
        String itemName = text.getText().toString();
        Drawable draw = image.getBackground();
        Item item = new Item(image,itemName);
        itemsOrder.add(indexI,item);
        indexI++;

    }

    // when we clicked on the save button
    //This function puts the selected items on the table
    public void saveClick(View v){
        visibilityFunction(1); // make the menu invisible , and show the selected items on the table
        int i=0;
        TableLayout table = findViewById(R.id.orderTable);
        while(i<itemsOrder.size()){
            TableRow row = (TableRow) table.getChildAt(i);
            row.setVisibility(View.VISIBLE);
            ImageView image = (ImageView) row.getChildAt(0);
            TextView text = (TextView) row.getChildAt(1);
            image.setBackground(itemsOrder.get(i).image.getBackground());
            text.setText(itemsOrder.get(i).nameItem);
            i++;
        }
    }
    public void addAmount(View v){
        ImageView img = (ImageView) v;
        CardView card = (CardView) img.getParent();
        EditText amountText = (EditText) card.getChildAt(1);
        int amount = Integer.parseInt(amountText.getText().toString());
        amount++;
        amountText.setText(String.valueOf(amount));
        TableRow row = (TableRow) card.getParent();
        TextView text = (TextView) row.getChildAt(1);
        for(Item item : itemsOrder){
            if(item.nameItem.equals(text.getText().toString())){
                int i=itemsOrder.indexOf(item);
                itemsOrder.get(i).amount=amount;
                break;
            }
        }
    }
    public void miniAmount(View v){
        ImageView img = (ImageView) v;
        CardView card = (CardView) img.getParent();
        EditText amountText = (EditText) card.getChildAt(1);
        int amount = Integer.parseInt(amountText.getText().toString());
        amount--;
        amountText.setText(String.valueOf(amount));
        TableRow row = (TableRow) card.getParent();
        TextView text = (TextView) row.getChildAt(1);
        for(Item item : itemsOrder){
            if(item.nameItem.equals(text.getText().toString())){
                int i=itemsOrder.indexOf(item);
                itemsOrder.get(i).amount=amount;
                break;
            }
        }
    }
    public void visibilityFunction(int f){
        GridLayout grid = findViewById(R.id.gridOrder);
        CardView cardView = findViewById(R.id.cardOrder);
        ImageView backArrow = findViewById(R.id.goBack);
        Button btnSave = findViewById(R.id.saveBtn);
        Button btnFinish = findViewById(R.id.finishBtn);

        if(f==1){
            grid.setVisibility(View.GONE);
            cardView.setVisibility(View.VISIBLE);
            backArrow.setVisibility(View.VISIBLE);
            btnSave.setVisibility(View.GONE);
            btnFinish.setVisibility(View.VISIBLE);
        }
        if(f==2){
            grid.setVisibility(View.VISIBLE);
            cardView.setVisibility(View.GONE);
            backArrow.setVisibility(View.GONE);
            btnSave.setVisibility(View.VISIBLE);
            btnFinish.setVisibility(View.GONE);
        }
    }
    public void goBack(View v){
        visibilityFunction(2);
    }
    //send the items to the next activity
    public void toTheNextActivity(View v){
        Intent intent = new Intent(menu.this,FinishOrder.class);
        intent.putExtra("item",itemsOrder);
        intent.putExtra("userId",userId);
        startActivity(intent);
    }
}