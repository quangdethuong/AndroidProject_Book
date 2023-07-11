package com.codingstuff.shoeapp.views;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codingstuff.shoeapp.DrawerBaseActivity;
import com.codingstuff.shoeapp.R;
import com.codingstuff.shoeapp.databinding.ActivityMainBinding;
import com.codingstuff.shoeapp.utils.adapter.CategoryAdapter;
import com.codingstuff.shoeapp.utils.adapter.ShoeItemAdapter;
import com.codingstuff.shoeapp.utils.model.ShoeCart;
import com.codingstuff.shoeapp.utils.model.Item;
import com.codingstuff.shoeapp.viewmodel.CartViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.Locale;

public class MainActivity extends DrawerBaseActivity implements ShoeItemAdapter.ItemClickedListeners, CategoryAdapter.CategoryClickedListeners {

    public static final int REQUEST_CODE_DETAIL_ACTIVITY = 1;
    private RecyclerView recyclerView;
    private List<Item> ItemList;
    private ShoeItemAdapter adapter;
    private CategoryAdapter categoryAdapter;
    private CartViewModel viewModel;
    private List<ShoeCart> shoeCartList;
    BufferedReader readerSession;
    FileInputStream inputStream;
    private ImageButton sortBtn;
    private String currentCategory = "all";
    private RecyclerView categoryRecyclerView;
    private EditText searchEditText;
    private CoordinatorLayout coordinatorLayout;
    private TextView cartCountTv;
    public static int cartCount = 0;
    private ImageView cartImageView;
    public static String sessionUser = "default_session_value";
    private ActivityMainBinding activityMainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bundle extras = getIntent().getExtras();
        initSession();
        if (extras != null) {
            Log.d("Exxxxx", extras.getString("key"));
            String value = extras.getString("key");
            MainActivity.sessionUser = extras.getString("key");
            setSession(value);
        }

        if (MainActivity.sessionUser.contains("efault_session")){
            MainActivity.sessionUser = "default_session_value";
        }

        if (MainActivity.sessionUser.equals("default_session_value")){
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }
        currentCategory = "all";
        List<String> categoryList = new ArrayList<>();
        categoryList.add("all");
        categoryList.add("psychology");
        categoryList.add("education");
        categoryList.add("healing");
        categoryList.add("numerology");

        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());
        //setContentView(R.layout.activity_main);

        initializeVariables();
        setUpList();
        recyclerView.setAdapter(adapter);
        //searchEditText = findViewById(R.id.search_edit_text);
        categoryRecyclerView.setAdapter(categoryAdapter);
        categoryAdapter.setCategoryList(categoryList);
        cartImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, CartActivity.class));
            }
        });
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Perform search operation using the query
                searchItem();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        setCartCount();
    }
    public void initSession(){
        try {
            // check if the file exists
            Log.d("dddd", (getApplicationContext().getFilesDir().toString()));
            File file = new File(getApplicationContext().getFilesDir(), "session.txt");
            if (!file.exists()) {
                Log.d("filesession", "aa");
                // if the file doesn't exist, create it
                FileWriter writer = new FileWriter(file);

                // write some default data to the file
                writer.write("default_session_value");

                // close the FileWriter
                writer.close();
            }

            // open the file for reading
            inputStream = openFileInput("session.txt");

            // create a new BufferedReader
            readerSession = new BufferedReader(new InputStreamReader(inputStream));

            // read the contents of the file
            String line;
            while ((line = readerSession.readLine()) != null) {
                MainActivity.sessionUser = line;
                Log.d("xinchao", line);
            }

            // close the BufferedReader
            readerSession.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void setSession(String a){
        try {
            Log.d("datasession", a);
            // Get the file path to the app's internal storage directory with the file name "session.txt"
            File file = new File(getApplicationContext().getFilesDir(), "session.txt");

            // Create a new FileWriter object with the file path
            FileWriter writer = new FileWriter(file);

            // Write the session string to the file
            writer.write(a);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        viewModel.getAllCartItems().observe(this, new Observer<List<ShoeCart>>() {
            @Override
            public void onChanged(List<ShoeCart> shoeCarts) {
                shoeCartList.addAll(shoeCarts);
            }
        });
    }

    private void setUpList() {
        new GetShoeShopTask().execute("http://10.0.2.2:80/prmapi/ShoeShop.php");
    }


    public List<Item> getShoeShop(String apiUrl) {
        List<Item> ItemList = new ArrayList<Item>();

        try {
            // Open connection to API URL
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");

            // Read response as input stream
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder responseBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                responseBuilder.append(line);
            }
            reader.close();

            // Convert response to ShoeItem list
            String response = responseBuilder.toString();
            JSONArray shoeItemsJsonArray = new JSONArray(response);
            for (int i = 0; i < shoeItemsJsonArray.length(); ++i) {
                JSONObject shoeItemJsonObject = shoeItemsJsonArray.getJSONObject(i);
                String shoeName = shoeItemJsonObject.getString("ProductName");
                String shoeBrandName = shoeItemJsonObject.getString("Category");
                String shoeDescription = shoeItemJsonObject.getString("Description");
                String shoeImage = shoeItemJsonObject.getString("Image");
                String author = shoeItemJsonObject.getString("Author");
                double shoePrice = shoeItemJsonObject.getDouble("Price");
                Item shoeItem = new Item(shoeName, shoeBrandName, shoeDescription,author, shoeImage, shoePrice);
                ItemList.add(shoeItem);
            }
            /*String response = responseBuilder.toString();
            JSONObject shoeItemsJsonObject = new JSONObject(response);

            for (int i = 0; i < shoeItemsJsonObject.length(); i++) {
                String key = Integer.toString(i);
                JSONObject shoeItemJsonObject = shoeItemsJsonObject.getJSONObject(key);
                String shoeName = shoeItemJsonObject.getString("shoeName");
                String shoeBrandName = shoeItemJsonObject.getString("shoeBrandName");
                String shoeDescription = shoeItemJsonObject.getString("shoeDescription");
                String shoeImage = shoeItemJsonObject.getString("shoeImage");
                double shoePrice = shoeItemJsonObject.getDouble("shoePrice");
                ShoeItem shoeItem = new ShoeItem(shoeName, shoeBrandName, shoeDescription, shoeImage, shoePrice);
                shoeItemList.add(shoeItem);
            }*/

            // Disconnect from API URL
            connection.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ItemList;
    }

    private void initializeVariables() {
        cartCountTv = findViewById(R.id.cart_count);
        searchEditText = findViewById(R.id.search_edit_text);
        sortBtn = findViewById(R.id.sortIv);
        cartImageView = findViewById(R.id.cartIv);
        findViewById(R.id.navtext_about).setVisibility(View.GONE);
        findViewById(R.id.navtext_editinfo).setVisibility(View.GONE);
        cartImageView.setVisibility(View.VISIBLE);
        searchEditText.setVisibility(View.VISIBLE);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        shoeCartList = new ArrayList<>();
        viewModel = new ViewModelProvider(this).get(CartViewModel.class);
        ItemList = new ArrayList<>();
        recyclerView = findViewById(R.id.mainRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        categoryRecyclerView = findViewById(R.id.categoryRecyclerView);
        adapter = new ShoeItemAdapter(this);
        categoryAdapter = new CategoryAdapter(this);
    }

    @Override
    public void onCardClicked(Item shoe) {
        Intent intent = new Intent(MainActivity.this, DetailedActivity.class);
        intent.putExtra("Item", shoe);
        startActivityForResult(intent, REQUEST_CODE_DETAIL_ACTIVITY);
    }
    @Override
    public void onCategoryClicked(String cate) {
        searchEditText.setText("");
        setUpList();
        currentCategory = cate;
        reloadShop();
    }
    public void setCartCount(){
        viewModel.getAllCartItems().observe(this, new Observer<List<ShoeCart>>() {
            @Override
            public void onChanged(List<ShoeCart> shoeCarts) {
                if (shoeCarts != null) {
                    MainActivity.cartCount = shoeCarts.size();
                    cartCountTv.setText(MainActivity.cartCount + "");
                }
            }
        });
    }

    private void searchItem() {
        String query = searchEditText.getText().toString().trim().toLowerCase(Locale.ROOT);
        List<Item> rs = findShoeByCate();
        List<Item> searchRs = new ArrayList<>();
        if (query.equals("") || query == null || query == ""){
            Log.d("testnew", ItemList.size()+"");
            adapter.setItemList(rs);
        } else {
            for (Item i: rs) {
                if(i.getShoeName().toLowerCase(Locale.ROOT).contains(query))
                    searchRs.add(i);
            }
            Log.d("testnew2", currentCategory);
            adapter.setItemList(searchRs);
        }

        // Perform search operation using the query
    }
    /*@Override
    public void onAddToCartBtnClicked(ShoeItem shoeItem) {
        ShoeCart shoeCart = new ShoeCart();
        shoeCart.setShoeName(shoeItem.getShoeName());
        shoeCart.setShoeBrandName(shoeItem.getShoeBrandName());
        shoeCart.setShoePrice(shoeItem.getShoePrice());
        shoeCart.setShoeImage(shoeItem.getShoeImage());

        final int[] quantity = {1};
        final int[] id = new int[1];

        if (!shoeCartList.isEmpty()) {
            for (int i = 0; i < shoeCartList.size(); i++) {
                if (shoeCart.getShoeName().equals(shoeCartList.get(i).getShoeName())) {
                    quantity[0] = shoeCartList.get(i).getQuantity();
                    quantity[0]++;
                    id[0] = shoeCartList.get(i).getId();
                }
            }
        }

        Log.d("TAG", "onAddToCartBtnClicked: " + quantity[0]);

        if (quantity[0] == 1) {
            shoeCart.setQuantity(quantity[0]);
            shoeCart.setTotalItemPrice(quantity[0] * shoeCart.getShoePrice());
            viewModel.insertCartItem(shoeCart);
        } else {
            viewModel.updateQuantity(id[0], quantity[0]);
            viewModel.updatePrice(id[0], quantity[0] * shoeCart.getShoePrice());
        }

        makeSnackBar("Item Added To Cart");
    }*/

    private void makeSnackBar(String msg) {
        Snackbar.make(coordinatorLayout, msg, Snackbar.LENGTH_SHORT)
                .setAction("Go to Cart", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(MainActivity.this, CartActivity.class));
                    }
                }).show();
    }

    public void sortByPrice(View view) {
        Drawable dollar = ContextCompat.getDrawable(this, R.drawable.dollar);
        Drawable dollar_de = ContextCompat.getDrawable(this, R.drawable.dollar_decrease);
        Drawable dollar_in = ContextCompat.getDrawable(this, R.drawable.dollar_increase);

        int currentSort = ((BitmapDrawable) sortBtn.getDrawable()).getBitmap().getGenerationId();
        int dollarInt = ((BitmapDrawable) dollar).getBitmap().getGenerationId();
        int dollar_deInt = ((BitmapDrawable) dollar_de).getBitmap().getGenerationId();
        int dollar_inInt = ((BitmapDrawable) dollar_in).getBitmap().getGenerationId();

        Log.d("sortsort", currentSort + " - " + dollar_deInt + " - " + dollar_inInt);

        List<Item> sortList = new ArrayList<>();
        sortList = findShoeByCate();
        if (currentSort == dollarInt){
            sortBtn.setImageDrawable(dollar_in);
            Collections.sort(sortList, new Comparator<Item>() {
                @Override
                public int compare(Item item1, Item item2) {
                    return Double.compare(item1.getPrice(), item2.getPrice());
                }
            });
            adapter.setItemList(sortList);
        } else if (currentSort == dollar_inInt){
            sortBtn.setImageDrawable(dollar_de);
            Collections.sort(sortList, new Comparator<Item>() {
                @Override
                public int compare(Item item1, Item item2) {
                    return Double.compare(item1.getPrice(), item2.getPrice());
                }
            });
            adapter.setItemList(sortList);
        } else {
            sortBtn.setImageDrawable(dollar);
            setUpList();
        }
    }


    public interface ShoeShopCallback {
        void onShoeShopReceived(List<Item> ItemList);
    }
    private class GetShoeShopTask extends AsyncTask<String, Void, List<Item>> {

        @Override
        protected List<Item> doInBackground(String... urls) {
            return getShoeShop(urls[0]);
        }

        @Override
        protected void onPostExecute(List<Item> ItemList) {
            MainActivity.this.ItemList = ItemList;
            reloadShop();
        }
    }

    private void reloadShop(){
            adapter.setItemList(findShoeByCate());
    }

    protected List<Item> findShoeByCate(){
        List<Item> rs = new ArrayList<>();
        if (currentCategory.equalsIgnoreCase("all")){
            rs = ItemList;
        }
        for (Item s: ItemList) {
            if (s.getCategory().equalsIgnoreCase(currentCategory)){
                rs.add(s);
            }
        }
        return rs;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("checkcheck", resultCode+"");
        if (requestCode == REQUEST_CODE_DETAIL_ACTIVITY && resultCode == RESULT_OK) {
            setUpList();
            reloadShop();
        }
    }
}
