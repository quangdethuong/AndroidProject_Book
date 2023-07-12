package com.codingstuff.BookApp.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.codingstuff.BookApp.R;

public class MapActivity extends AppCompatActivity {

    private AppCompatButton backHomebtn;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_map);
        Fragment fragment = new Map_Fragment();
        getSupportFragmentManager().beginTransaction().replace(R.id .frame_layout, fragment).commit();

        backHomebtn = findViewById(R.id.backHomeBtn);
        backHomebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MapActivity.this, MainActivity.class));
            }
        });
    }
}
