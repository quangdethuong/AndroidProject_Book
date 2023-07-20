package com.codingstuff.BookApp.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.codingstuff.BookApp.R;
import com.codingstuff.BookApp.databinding.ActivityCheckOutBinding;
import com.codingstuff.BookApp.utils.adapter.CartAdapter;
import com.codingstuff.BookApp.utils.model.Account;
import com.codingstuff.BookApp.utils.model.ItemCart;
import com.codingstuff.BookApp.viewmodel.CartViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

public class CheckOutActivity extends AppCompatActivity  {


    private Button checkoutBTN;
    private CardView cardView;

    private TextView totalCartPriceTv;
    private CartAdapter cartAdapter;
    private CartViewModel cartViewModel;
    private EditText username, password,
            email,
            phone,
            address;
    private ActivityCheckOutBinding activityCheckOutBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityCheckOutBinding = ActivityCheckOutBinding.inflate(getLayoutInflater());
        setContentView(activityCheckOutBinding.getRoot());

        initCheckout();
        totalCartPriceTv.setText(String.valueOf(CartActivity.price));


       checkoutBTN.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
//               cartViewModel.deleteAllCartItems();

               cardView.setVisibility(View.VISIBLE);

           }
       });
    }

    private void initCheckout() {


        username = activityCheckOutBinding.editUsernameCk;
        password = activityCheckOutBinding.editPasswordCk;
        email = activityCheckOutBinding.editEmailCk;
        phone = activityCheckOutBinding.editPhoneNumberCk;
        address = activityCheckOutBinding.editAddressCk;
        checkoutBTN = activityCheckOutBinding.ckBtn;
        cardView= activityCheckOutBinding.cartActivityCardViewCk;
        totalCartPriceTv = activityCheckOutBinding.cartActivityTotalPriceTvCK;

        GetAccountTask task = new GetAccountTask();
        task.execute(MainActivity.sessionUser);

        task.setOnAccountLoadedListener(new EditAccountActivity.OnAccountLoadedListener() {
            @Override
            public void onAccountLoaded(Account account) {

                username.setText(account.getUsername().toString().trim());

                password.setText(account.getPassword().toString().trim());
                email.setText(account.getEmail().toString().trim());
                address.setText(account.getAddress().toString().trim());
                phone.setText(account.getPhone().toString().trim());
            }
        });

    }



    private class GetAccountTask extends AsyncTask<String, Void, Account> {

        private static final String SERVER_URL = "http://10.0.2.2:80/prmapi/getSpecificAccount.php";

        private EditAccountActivity.OnAccountLoadedListener mListener;

        public void setOnAccountLoadedListener(EditAccountActivity.OnAccountLoadedListener listener) {
            mListener = listener;
        }

        @Override
        protected Account doInBackground(String... usernames) {
            if (usernames.length != 1) {
                throw new IllegalArgumentException("Only one username parameter should be provided.");
            }

            String username = usernames[0];
            Account account = null;

            try {
                // Build the request body
                String requestBody = "username=" + URLEncoder.encode(username, "UTF-8");

                // Make the API call
                URL apiUrl = new URL(SERVER_URL);
                HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.setDoOutput(true);

                OutputStream outputStream = connection.getOutputStream();
                outputStream.write(requestBody.getBytes());
                outputStream.flush();
                outputStream.close();

                // Parse the response
                InputStream inputStream = connection.getInputStream();
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();

                int nRead;
                byte[] data = new byte[1024];
                while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
                    buffer.write(data, 0, nRead);
                }

                buffer.flush();
                JSONObject responseObject = new JSONObject(buffer.toString("UTF-8"));

                if (responseObject.getBoolean("success")) {
                    JSONObject accountObject = responseObject.getJSONObject("account");

                    account = new Account(
                            accountObject.getString("username"),
                            accountObject.getString("password"),
                            accountObject.optString("email", ""),
                            accountObject.optString("address", ""),
                            accountObject.optString("phone", "")
                    );
                }

                inputStream.close();
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return account;
        }

        @Override
        protected void onPostExecute(Account account) {
            if (mListener != null) {
                mListener.onAccountLoaded(account);
            }
        }
    }






}