package com.codingstuff.BookApp.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.codingstuff.BookApp.dao.CartDAO;
import com.codingstuff.BookApp.database.CartDatabase;
import com.codingstuff.BookApp.utils.model.ItemCart;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CartRepo {

    private CartDAO cartDAO;
    private LiveData<List<ItemCart>> allCartItemsLiveData;
    private Executor executor = Executors.newSingleThreadExecutor();

    public LiveData<List<ItemCart>> getAllCartItemsLiveData() {
        return allCartItemsLiveData;
    }

    public CartRepo(Application application){
        cartDAO = CartDatabase.getInstance(application).cartDAO();
        allCartItemsLiveData = cartDAO.getAllCartItems();
    }

    public void insertCartItem(ItemCart itemCart){
        executor.execute(new Runnable() {
            @Override
            public void run() {
                cartDAO.insertCartItem(itemCart);
            }
        });
    }

    public void deleteCartItem(ItemCart itemCart){
        executor.execute(new Runnable() {
            @Override
            public void run() {
                cartDAO.deleteCartItem(itemCart);
            }
        });
    }

    public void updateQuantity(int id , int quantity) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                cartDAO.updateQuantity(id, quantity);
            }
        });
    }

    public void updatePrice(int id , double price){
        executor.execute(new Runnable() {
            @Override
            public void run() {
                cartDAO.updatePrice(id , price);
            }
        });
    }

    public void deleteAllCartItems(){
        executor.execute(new Runnable() {
            @Override
            public void run() {
                cartDAO.deleteAllItems();
            }
        });
    }
}
