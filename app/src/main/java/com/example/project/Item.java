package com.example.project;
import java.io.ByteArrayOutputStream;
import java.io.Serializable;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;
//this class we use it to make an arrayObject of the items that the client clicked on
public class Item implements Serializable, Parcelable {
    public ImageView image;
    public String nameItem;
    public int amount;

    public Item(ImageView image,String name) {
        this.image = image;
        this.nameItem=name;
        this.amount=1;
    }

    protected Item(Parcel in) {

        nameItem = in.readString();
        amount = in.readInt();
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    public void addAmount(){
        this.amount++;
    }
    public void miniAmount(){
        this.amount--;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nameItem);
        dest.writeInt(amount);
    }
}
