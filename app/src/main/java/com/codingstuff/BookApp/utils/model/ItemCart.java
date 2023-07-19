package com.codingstuff.BookApp.utils.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "cart_table")
public class ItemCart {

    @PrimaryKey(autoGenerate = true)
    private int id;


    private String productName, categoryName;
//    private String shoeSize;
    private String image;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
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

    private String author;
    private double price;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    private int quantity;
    private double totalItemPrice;

//    public String getShoeSize() {
//        return shoeSize;
//    }
//
//    public void setShoeSize(String shoeSize) {
//        this.shoeSize = shoeSize;
//    }
//
//    public String getShoeName() {
//        return shoeName;
//    }
//
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


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalItemPrice() {
        return totalItemPrice;
    }

    public void setTotalItemPrice(double totalItemPrice) {
        this.totalItemPrice = totalItemPrice;
    }
}
