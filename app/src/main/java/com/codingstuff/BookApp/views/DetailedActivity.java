package com.codingstuff.BookApp.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.codingstuff.BookApp.R;
import com.codingstuff.BookApp.utils.model.ItemCart;
import com.codingstuff.BookApp.utils.model.Item;
import com.codingstuff.BookApp.viewmodel.CartViewModel;

import java.util.ArrayList;
import java.util.List;

public class DetailedActivity extends AppCompatActivity {

    private ImageView ImageView;
    private TextView ProductNameTV, CategoryTV, PriceTV, DescriptionTV,AuthorTV;
    private AppCompatButton addToCartBtn;
    private Item item;
    private Spinner sizeSpinner;
    private CartViewModel viewModel;
    private List<ItemCart> itemCartList;
    private String[] sizeOptions = {"Select Size", "37", "38", "39", "40", "41", "42", "43", "44", "45"};

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
                // Check if a size has been selected
//                if (sizeSpinner.getSelectedItemPosition() == 0) {
//                    // Show an alert if no size has been selected
//                    AlertDialog.Builder builder = new AlertDialog.Builder(DetailedActivity.this);
//                    builder.setMessage("Please select a size.")
//                            .setTitle("Size selection required")
//                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//                                    // User clicked OK button
//                                }
//                            });
//                    AlertDialog dialog = builder.create();
//                    dialog.show();
//                } else {
                    // Insert to room if a size has been selected
                    insertToRoom();
               // }
            }
        });
    }

    private void insertToRoom(){
        ItemCart itemCart = new ItemCart();
        itemCart.setShoeName(item.getProductName());
        itemCart.setShoeBrandName(item.getCategory());
        itemCart.setShoePrice(item.getPrice());
        itemCart.setShoeImage(item.getImage());
        itemCart.setAuthor(item.getAuthor());

//        shoeCart.setShoeSize(sizeSpinner.getSelectedItem().toString());

        final int[] quantity = {1};
        final int[] id = new int[1];

        if (!itemCartList.isEmpty()){
            for(int i = 0; i< itemCartList.size(); i++){
                if (itemCart.getShoeName().equals(itemCartList.get(i).getShoeName()) ){
                    quantity[0] = itemCartList.get(i).getQuantity();
                    quantity[0]++;
                    id[0] = itemCartList.get(i).getId();
                }
            }
        }

        if (quantity[0]==1){
            itemCart.setQuantity(quantity[0]);
            itemCart.setTotalItemPrice(quantity[0]* itemCart.getShoePrice());
            viewModel.insertCartItem(itemCart);
        }else{

            viewModel.updateQuantity(id[0] ,quantity[0]);
            viewModel.updatePrice(id[0] , quantity[0]* itemCart.getShoePrice());
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