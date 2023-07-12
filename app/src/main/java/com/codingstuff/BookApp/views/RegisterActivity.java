package com.codingstuff.BookApp.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.codingstuff.BookApp.EditTextValidation;
import com.codingstuff.BookApp.R;
import com.codingstuff.BookApp.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class RegisterActivity extends AppCompatActivity{

    //region GLOBALS

    private static final String TAG = "RegisterActivity";

    private static String PASSWORD_CONSTRAINT_MSG;
    private static String PHONE_NO_CONSTRAINT_MSG;

    private Button registerButton;
    private Button backButton;
    private TextView termsAndConditions;
    private CheckBox checkBox;
    private EditText username,
            email,
            phone,
            address,
            password,
            confirmPassword;
    private TextView registerTitle;

    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
    }

    //Method to initialise EditViews, TextViews, Buttons and CheckBox
    public void init() {
        registerTitle = (TextView) findViewById(R.id.simple_toolbar_title);
        backButton = (Button) findViewById(R.id.simple_toolbar_back_button);

        username = (EditText) findViewById(R.id.register_username);
        email = (EditText) findViewById(R.id.register_email);
        phone = (EditText) findViewById(R.id.register_phone_number);
        address = (EditText) findViewById(R.id.register_address);
        password = (EditText) findViewById(R.id.register_password);
        confirmPassword = (EditText) findViewById(R.id.register_confirm_password);

        registerButton = (Button) findViewById(R.id.register_button);

        PHONE_NO_CONSTRAINT_MSG = getResources().getString(R.string.phone_no_length_constraint_error_msg);
        PASSWORD_CONSTRAINT_MSG = getResources().getString(R.string.password_constraint_msg);
    }

    public void toLogin(View view){
        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
    }

    public void registerBtnClicked(View view) throws ExecutionException, InterruptedException {
        username.setText(username.getText().toString().trim().toLowerCase(Locale.ROOT));
        if (canRegister()) {
            try {
                boolean result = new CheckUserExistedTask().execute(username.getText().toString().trim()).get();
                if (result) {
                    Toast.makeText(RegisterActivity.this, "User exists", Toast.LENGTH_SHORT).show();
                } else {
                    AddUserTask task = new AddUserTask(this, username.getText().toString() , password.getText().toString(), email.getText().toString(), address.getText().toString(), phone.getText().toString());
                    task.execute("http://10.0.2.2:80/prmapi/registerUser.php");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

// execute the task and get the result



            /*} else {
                Toast.makeText(this, "User Already Existed", Toast.LENGTH_SHORT).show();
            }*/
        }
    }

    /**
     * canRegister method is checking if the fields are valid.
     * If not then alert messages will be displayed and the invalid fields will be underlined with red.
     */
    private boolean canRegister() {
        if (username.getText().toString().isEmpty() &&
                email.getText().toString().isEmpty() && phone.getText().toString().isEmpty() &&
                address.getText().toString().isEmpty() &&
                password.getText().toString().isEmpty() && confirmPassword.getText().toString().isEmpty()) {
            Utils.showSingleButtonAlertWithoutTitle(this, getResources().getString(R.string.alert_all_fields_are_required));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                username.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.red)));
                email.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.red)));
                phone.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.red)));
                address.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.red)));
                password.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.red)));
                confirmPassword.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.red)));
            }
            checkBox.setButtonDrawable(ContextCompat.getDrawable(this, R.drawable.checkbox_not_checked_selector));
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



        if (!EditTextValidation.isPasswordValid(password.getText().toString().trim()).equals("Success")) {
            Utils.showSingleButtonAlert(this, getResources().getString(R.string.invalid_input), EditTextValidation.isPasswordValid(password.getText().toString().trim()));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                password.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.red)));
            return false;
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                password.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.navigation_buttons_violet)));
        }


        if (!EditTextValidation.isConfirmPasswordValid(confirmPassword.getText().toString().trim()).equals("Success")) {
            Utils.showSingleButtonAlert(this, getResources().getString(R.string.invalid_input), EditTextValidation.isConfirmPasswordValid(confirmPassword.getText().toString().trim()));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                confirmPassword.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.red)));
            return false;
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                confirmPassword.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.navigation_buttons_violet)));
        }

        if (!confirmPassword.getText().toString().equals(password.getText().toString())) {
            Utils.showSingleButtonAlertWithoutTitle(this, getResources().getString(R.string.alert_passwords_don_t_match));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                password.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.red)));
                confirmPassword.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.red)));
            }
            return false;
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                password.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.black)));
                confirmPassword.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.black)));
            }
        }

        return true;
    }


    private class CheckUserExistedTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            String username = params[0];

            try {
                String url = "http://10.0.2.2:80/prmapi/checkUser.php?username=" + URLEncoder.encode(username, "UTF-8");
                HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
                conn.setRequestMethod("GET");
                conn.connect();

                // read the response as a string
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                // parse the JSON response to check if the user exists
                return response.toString().trim().equals("exit");

            } catch (IOException  e) {
                e.printStackTrace();
                Log.d("renene2", e.toString());
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            // handle the result
            if (result) {
                // user exists
            } else {
                // user doesn't exist
            }
        }
    }



    public class AddUserTask extends AsyncTask<String, Void, Boolean> {
        private Context mContext;
        private String mUsername;
        private String mPassword;
        private String mEmail;
        private String mAddress;
        private String mPhone;

        public AddUserTask(Context context, String username, String password, String email, String address, String phone) {
            mContext = context;
            mUsername = username;
            mPassword = password;
            mEmail = email;
            mAddress = address;
            mPhone = phone;
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            String urlString = strings[0];
            try {
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);

                // Construct data string to send to PHP script
                String data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(mUsername, "UTF-8");
                data += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(mPassword, "UTF-8");
                data += "&" + URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(mEmail, "UTF-8");
                data += "&" + URLEncoder.encode("address", "UTF-8") + "=" + URLEncoder.encode(mAddress, "UTF-8");
                data += "&" + URLEncoder.encode("phone", "UTF-8") + "=" + URLEncoder.encode(mPhone, "UTF-8");

                OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
                writer.write(data);
                writer.flush();

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    return true;
                } else {
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                Toast.makeText(mContext, "User Registed Successfully!!", Toast.LENGTH_SHORT).show();
                MainActivity.sessionUser = username.getText().toString().trim();
                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                finish();
            } else {
                Toast.makeText(mContext, "Error Register user", Toast.LENGTH_SHORT).show();
            }
        }
    }

}