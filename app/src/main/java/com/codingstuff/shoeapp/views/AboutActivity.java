package com.codingstuff.shoeapp.views;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.codingstuff.shoeapp.DrawerBaseActivity;
import com.codingstuff.shoeapp.R;
import com.codingstuff.shoeapp.databinding.ActivityAboutBinding;
import com.codingstuff.shoeapp.databinding.ActivityMainBinding;

public class AboutActivity extends DrawerBaseActivity {
    private ActivityAboutBinding activityAboutBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAboutBinding = ActivityAboutBinding.inflate(getLayoutInflater());
        setContentView(activityAboutBinding.getRoot());
        findViewById(R.id.search_edit_text).setVisibility(View.GONE);
        findViewById(R.id.cartIv).setVisibility(View.GONE);
        findViewById(R.id.navtext_editinfo).setVisibility(View.GONE);
        findViewById(R.id.navtext_about).setVisibility(View.VISIBLE);
    }
}