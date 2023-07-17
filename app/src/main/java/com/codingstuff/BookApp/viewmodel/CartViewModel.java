package com.codingstuff.BookApp.viewmodel;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.codingstuff.BookApp.repository.CartRepo;
import com.codingstuff.BookApp.utils.model.ItemCart;

import java.util.List;

public class CartViewModel extends AndroidViewModel {

    private CartRepo cartRepo;

    public CartViewModel(@NonNull Application application) {
        super(application);
        cartRepo = new CartRepo(application);
    }

    public LiveData<List<ItemCart>> getAllCartItems() {
        return cartRepo.getAllCartItemsLiveData();
    }
    public List<ItemCart> getAll() {
        return cartRepo.getAllCartItemsLiveData().getValue();
    }

    public void insertCartItem(ItemCart itemCart) {
        cartRepo.insertCartItem(itemCart);
    }

    public void updateQuantity(int id, int quantity) {
        cartRepo.updateQuantity(id, quantity);
    }

    public void updatePrice(int id, double price) {
        cartRepo.updatePrice(id, price);
    }

    public void deleteCartItem(ItemCart itemCart) {
        cartRepo.deleteCartItem(itemCart);
    }

    public void deleteAllCartItems() {
        cartRepo.deleteAllCartItems();
    }
}
