package com.codingstuff.BookApp.views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.codingstuff.BookApp.R;
import com.codingstuff.BookApp.utils.model.Account;
import com.codingstuff.BookApp.utils.model.ItemCart;
import com.codingstuff.BookApp.viewmodel.CartViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {
    //private final Pattern emailPattern = Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");
    private final Pattern phonePattern = Pattern.compile("(0|\\+84)[0-9]{9,10}");
    private CartViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText emailBox= findViewById(R.id.txtEmail);
        EditText passwordBox= findViewById(R.id.txtPassword);
        Button button = findViewById(R.id.btnLogin);

        button.setOnClickListener(view -> {

            String username = emailBox.getText().toString().toLowerCase().trim();
//            if(!emailPattern.matcher(username).matches() && !phonePattern.matcher(username).matches()){
//                Toast.makeText(LoginActivity.this, "Invalid Email or Password", Toast.LENGTH_SHORT).show();
//                return;
//            }


            String password = passwordBox.getText().toString();
            if (password.length() <5 || password.length() >20){
                Toast.makeText(LoginActivity.this, "Password must be between 15 and 30", Toast.LENGTH_SHORT).show();
                return;
            }
            getAccounts(new LoginActivity.OnDataReceivedListener() {
                @Override
                public void onDataReceived(List<Account> accounts) {
                    boolean logined = false;
                    for (Account a: accounts ) {
                        if (a.getUsername().equals(username)  && a.getPassword().equals(password)) logined = true;
                        Log.d("response33", a.getUsername() + "-"+a.getPassword()+("password".equals(password)  && "password".equals(password)));
                    }
                    if (logined){
                        MainActivity.sessionUser = username;
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("key", username);
                        startActivity(intent);
                        finish();
                        Toast.makeText(LoginActivity.this, "Login Success!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(LoginActivity.this, "Login Failed. Please Try Again!", Toast.LENGTH_SHORT).show();
                    }
                }
            });


           /* findViewById(R.id.textView2).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("xin chao", "loginregi");
                    startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                }
            });*/

        });
    }
    public void getAccounts(final LoginActivity.OnDataReceivedListener listener) {
        List<Account> accounts = new ArrayList<>();

        String url = "http://10.0.2.2:80/prmapi/getUser.php";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("response11", response.toString());
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                String username = jsonObject.getString("username").toString().trim();
                                String password = jsonObject.getString("password").toString().trim();
                                String email = jsonObject.getString("email").toString().trim();
                                String address = jsonObject.getString("address").toString().trim();
                                String phone = jsonObject.getString("phone").toString().trim();
                                Account account = new Account(username, password, email, address, phone);
                                accounts.add(account);
                                Log.d("response22", accounts.size()+"");
                            }
                            listener.onDataReceived(accounts);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("error11", error.toString());

                        error.printStackTrace();
                    }
                });

        Volley.newRequestQueue(getApplicationContext()).add(jsonArrayRequest);
    }

    public void toRegister(View view) {
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        finish();
    }

    public interface OnDataReceivedListener {
        void onDataReceived(List<Account> accounts);
    }
}
