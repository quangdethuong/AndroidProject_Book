package com.codingstuff.BookApp.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.res.ColorStateList;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.codingstuff.BookApp.EditTextValidation;
import com.codingstuff.BookApp.R;
import com.codingstuff.BookApp.Utils;
import com.codingstuff.BookApp.databinding.ActivityCheckOutBinding;
import com.codingstuff.BookApp.utils.adapter.CartAdapter;
import com.codingstuff.BookApp.utils.model.Account;
import com.codingstuff.BookApp.utils.model.ItemCart;
import com.codingstuff.BookApp.viewmodel.CartViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

public class CheckOutActivity extends AppCompatActivity  {


    private Button checkoutBTN,updateCK;
    private CardView cardView;

private CartViewModel cartViewModel;
    private LinearLayout layoutPW,allLayout;
    private TextView totalCartPriceTv, textViewPrice;

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

               cartViewModel.deleteAllCartItems();
               allLayout.setVisibility(View.INVISIBLE);
               textViewPrice.setVisibility(View.INVISIBLE);
               totalCartPriceTv.setVisibility(View.INVISIBLE);
               checkoutBTN.setVisibility(View.INVISIBLE);
               cardView.setVisibility(View.VISIBLE);
               CartActivity.price = 0;
           }
       });

       updateCK.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if (canSubmit()) {

                   // Create a new instance of the UpdateAccountTask AsyncTask
                   UpdateAccountTask task = new UpdateAccountTask();

                   String usernames = username.getText().toString().trim();
                   String emails = email.getText().toString().trim();
                   String passwords = password.getText().toString().trim();
                   String addresses = address.getText().toString().trim();
                   String phones = phone.getText().toString().trim();

                   task.execute(usernames, passwords, emails, addresses, phones);

                   Toast.makeText(CheckOutActivity.this, "Update done!", Toast.LENGTH_SHORT).show();


               } else {
                   Toast.makeText(CheckOutActivity.this, "Invalid Infomation!", Toast.LENGTH_SHORT).show();

               }
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
        updateCK = activityCheckOutBinding.updateInforCK;
        cardView= activityCheckOutBinding.cartActivityCardViewCk;
        totalCartPriceTv = activityCheckOutBinding.cartActivityTotalPriceTvCK;
        layoutPW = activityCheckOutBinding.layoutPW;
        allLayout = activityCheckOutBinding.allLayout;
        textViewPrice = activityCheckOutBinding.textView2;
        cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);
        layoutPW.setVisibility(View.INVISIBLE);
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

    public class UpdateAccountTask extends AsyncTask<String, Void, String> {

        private static final String UPDATE_ACCOUNT_URL = "http://10.0.2.2:80/prmapi/updateAccount.php";

        @Override
        protected String doInBackground(String... params) {
            String username = params[0];
            String password = params[1];
            String email = params[2];
            String address = params[3];
            String phone = params[4];


            try {
                // Create a new URL object with the updateAccount.php URL
                URL url = new URL(UPDATE_ACCOUNT_URL);

                // Create a new HTTP connection and configure it
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.setDoOutput(true);

// Create the request body
                String requestBody = "username=" + URLEncoder.encode(username, "UTF-8") +
                        "&password=" + URLEncoder.encode(password, "UTF-8") +
                        "&email=" + URLEncoder.encode(email, "UTF-8") +
                        "&address=" + URLEncoder.encode(address, "UTF-8") +
                        "&phone=" + URLEncoder.encode(phone, "UTF-8");

// Write the request body to the connection output stream
                OutputStream outputStream = connection.getOutputStream();
                outputStream.write(requestBody.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();

                // Read the response from the connection input stream
                InputStream inputStream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String response = "";
                String line = "";
                while ((line = reader.readLine()) != null) {
                    response += line;
                }
                reader.close();
                inputStream.close();


                // Return the response
                return response;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }



    }

    private boolean canSubmit() {
        username.setText(username.getText().toString().trim());
        email.setText(email.getText().toString().trim());
        address.setText(address.getText().toString().trim());
        phone.setText(phone.getText().toString().trim());
        password.setText(password.getText().toString().trim());

        if (username.getText().toString().isEmpty() &&
                email.getText().toString().isEmpty() && phone.getText().toString().isEmpty() &&
                address.getText().toString().isEmpty() && password.getText().toString().isEmpty()) {
            Utils.showSingleButtonAlertWithoutTitle(this, getResources().getString(R.string.alert_all_fields_are_required));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                username.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.red)));
                email.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.red)));
                password.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.red)));
                phone.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.red)));
                address.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.red)));
            }
            return false;
        }

        if (!EditTextValidation.isFirstNameValid(username.getText().toString().trim()).equals("Success")) {
            Utils.showSingleButtonAlert(this, getResources().getString(R.string.invalid_input), EditTextValidation.isFirstNameValid(username.getText().toString().trim()));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                username.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.red)));
            }
            return false;
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                username.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.navigation_buttons_violet)));
        }


        if (!EditTextValidation.isFirstNameValid(password.getText().toString().trim()).equals("Success")) {
            Utils.showSingleButtonAlert(this, getResources().getString(R.string.invalid_input), EditTextValidation.isFirstNameValid(password.getText().toString().trim()));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                password.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.red)));
            }
            return false;
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                password.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.navigation_buttons_violet)));
        }

        if (!EditTextValidation.isValidEmail(email.getText().toString().trim()).equals("Success")) {
            Utils.showSingleButtonAlert(this, getResources().getString(R.string.invalid_input), EditTextValidation.isValidEmail(email.getText().toString().trim()));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                email.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.red)));
            return false;
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                email.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.navigation_buttons_violet)));
        }

        if (!EditTextValidation.isPhoneNumberValid(phone.getText().toString().trim()).equals("Success")) {
            Utils.showSingleButtonAlert(this, getResources().getString(R.string.invalid_input), EditTextValidation.isPhoneNumberValid(phone.getText().toString().trim()));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                phone.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.red)));
            return false;
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                phone.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.navigation_buttons_violet)));
        }

        if (!EditTextValidation.isStreetValid(address.getText().toString().trim()).equals("Success")) {
            Utils.showSingleButtonAlert(this, getResources().getString(R.string.invalid_input), EditTextValidation.isStreetValid(address.getText().toString().trim()));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                address.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.red)));
            return false;
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                address.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.navigation_buttons_violet)));
        }

        return true;
    }







}