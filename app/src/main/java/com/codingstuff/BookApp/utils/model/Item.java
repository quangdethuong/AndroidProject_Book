package com.codingstuff.BookApp.utils.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Item implements Parcelable {




    private String productName, category, description,author;
    private String image;
    private double price;


    public Item(String productName, String category, String description, String author, String image, double price) {
        this.productName = productName;
        this.category = category;
        this.description = description;
        this.author = author;
        this.image = image;
        this.price = price;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

//    public Item(String shoeName, String shoeBrandName, String shoeDescription, String shoeImage, double shoePrice) {
//        this.shoeName = shoeName;
//        this.shoeBrandName = shoeBrandName;
//        this.shoeDescription = shoeDescription;
//        this.shoeImage = shoeImage;
//        this.shoePrice = shoePrice;
//    }

    protected Item(Parcel in) {
        productName = in.readString();
        category = in.readString();
        description = in.readString();
        image = in.readString();
        author = in.readString();
        price = in.readDouble();
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







//    public void setShoeName(String shoeName) {
//        this.shoeName = shoeName;
//    }
//
//    public String getShoeBrandName() {
//        return shoeBrandName;
//    }
//
//    public void setShoeBrandName(String shoeBrandName) {
//        this.shoeBrandName = shoeBrandName;
//    }
//
//    public String getShoeImage() {
//        return shoeImage;
//    }
//
//    public void setShoeImage(String shoeImage) {
//        this.shoeImage = shoeImage;
//    }
//
//    public double getShoePrice() {
//        return shoePrice;
//    }
//
//    public void setShoePrice(double shoePrice) {
//        this.shoePrice = shoePrice;
//    }

    @Override
    public int describeContents() {
        return 0;
    }

//    @Override
//    public void writeToParcel(Parcel parcel, int i) {
//        parcel.writeString(shoeName);
//        parcel.writeString(shoeBrandName);
//        parcel.writeString(shoeDescription);
//        parcel.writeString(shoeImage);
//        parcel.writeDouble(shoePrice);
//    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(productName);
        parcel.writeString(category);
        parcel.writeString(description);
        parcel.writeString(image);
        parcel.writeString(author);
        parcel.writeDouble(price);
    }
}
