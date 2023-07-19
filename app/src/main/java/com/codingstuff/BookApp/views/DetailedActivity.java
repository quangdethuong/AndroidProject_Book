package com.codingstuff.BookApp.views;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.codingstuff.BookApp.MyNotification;
import com.codingstuff.BookApp.R;
import com.codingstuff.BookApp.utils.model.Account;
import com.codingstuff.BookApp.utils.model.ItemCart;
import com.codingstuff.BookApp.utils.model.Item;
import com.codingstuff.BookApp.viewmodel.CartViewModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DetailedActivity extends AppCompatActivity {

    private static final int NOTIFICATION_ID = 1;
    private ImageView ImageView;
    private TextView ProductNameTV, CategoryTV, PriceTV, DescriptionTV,AuthorTV;
    private AppCompatButton addToCartBtn;
    private Item item;
    private Account account;
    private Spinner sizeSpinner;
    private CartViewModel viewModel;
    private List<ItemCart> itemCartList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, sizeOptions);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        item = getIntent().getParcelableExtra("Item");
        initializeVariables();
//        sizeSpinner.setAdapter(adapter);


        viewModel.getAllCartItems().observe(this, new Observer<List<ItemCart>>() {
            @Override
            public void onChanged(List<ItemCart> itemCarts) {
                itemCartList.addAll(itemCarts);
            }
        });

        if (item != null) {
            setDataToWidgets();
        }

        addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    insertToRoom();
                    sendNotification();
            }
        });
    }

    private void sendNotification() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
        Notification notification = new NotificationCompat.Builder(this, MyNotification.CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_add_24)
                .setLargeIcon(bitmap)
                .setContentTitle("Your item have been added")
                .setContentText("Please check")
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null){
            notificationManager.notify(getNotificationId(), notification);
        }
    }

    private int getNotificationId(){
        return (int) new Date().getTime();
    }

    private void insertToRoom(){
        ItemCart itemCart = new ItemCart();
        itemCart.setProductName(item.getProductName());
        itemCart.setCategoryName(item.getCategory());
        itemCart.setPrice(item.getPrice());
        itemCart.setImage(item.getImage());
        itemCart.setAuthor(item.getAuthor());


//        shoeCart.setShoeSize(sizeSpinner.getSelectedItem().toString());

        final int[] quantity = {1};
        final int[] id = new int[1];

        if (!itemCartList.isEmpty()){
            for(int i = 0; i< itemCartList.size(); i++){
                if (itemCart.getProductName().equals(itemCartList.get(i).getProductName()) ){
                    quantity[0] = itemCartList.get(i).getQuantity();
                    quantity[0]++;
                    id[0] = itemCartList.get(i).getId();
                }
            }
        }

        if (quantity[0]==1){
            itemCart.setQuantity(quantity[0]);
            itemCart.setTotalItemPrice(quantity[0]* itemCart.getPrice());
            viewModel.insertCartItem(itemCart);
        }else{

            viewModel.updateQuantity(id[0] ,quantity[0]);
            viewModel.updatePrice(id[0] , quantity[0]* itemCart.getPrice());
        }
        MainActivity.cartCount = itemCartList.size();
        startActivity(new Intent(DetailedActivity.this , CartActivity.class));
    }

    private void setDataToWidgets() {
        ProductNameTV.setText(item.getProductName());
        CategoryTV.setText(item.getCategory());
        DescriptionTV.setText(item.getDescription());
        AuthorTV.setText(item.getAuthor());
        PriceTV.setText(String.valueOf(item.getPrice())+"$");
        Glide.with(getApplicationContext())
                .load(item.getImage())
                .into(ImageView);
    }

    private void initializeVariables() {

        itemCartList = new ArrayList<>();
//        sizeSpinner = findViewById(R.id.size_spinner);
        ImageView = findViewById(R.id.detailActivityIV);
        ProductNameTV = findViewById(R.id.detailActivityProductNameTv);
        DescriptionTV = findViewById(R.id.detailActivityDescription);
        AuthorTV = findViewById(R.id.authorTv);
        CategoryTV = findViewById(R.id.detailActivityCategoryTv);
        PriceTV = findViewById(R.id.detailActivityPriceTv);
        addToCartBtn = findViewById(R.id.detailActivityAddToCartBtn);

        viewModel = new ViewModelProvider(this).get(CartViewModel.class);
    }
    @Override
    public void onBackPressed() {
        // Perform any necessary actions here

        // Create an intent to pass data back to MainActivity
        Intent intent = new Intent();
        intent.putExtra("data", "Some data to pass back to MainActivity");

        // Set the result to indicate success and pass the intent
        setResult(Activity.RESULT_OK, intent);
        Log.d("chekchek", "chekchek");

        // Call finish() to close DetailActivity and return to MainActivity
        super.onBackPressed();
    }
}