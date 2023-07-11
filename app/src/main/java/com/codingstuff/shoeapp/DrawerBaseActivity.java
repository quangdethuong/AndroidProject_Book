package com.codingstuff.shoeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.codingstuff.shoeapp.views.AboutActivity;
import com.codingstuff.shoeapp.views.EditAccountActivity;
import com.codingstuff.shoeapp.views.LoginActivity;
import com.codingstuff.shoeapp.views.MainActivity;
import com.codingstuff.shoeapp.views.MapActivity;
import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class DrawerBaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;

    @Override
    public void setContentView(View view){
        drawerLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_drawer_base, null);
        FrameLayout container = drawerLayout.findViewById(R.id.activityContainer);
        container.addView(view);
        super.setContentView(drawerLayout);

        Toolbar toolbar = drawerLayout.findViewById(R.id.toolBar);

        setSupportActionBar(toolbar);

        NavigationView navigationView = drawerLayout.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
        ActionBarDrawerToggle toogle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.menu_drawer_open, R.string.menu_drawer_close);
        drawerLayout.addDrawerListener(toogle);
        toogle.syncState();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawer(GravityCompat.START);
        switch (item.getItemId()){
            case R.id.nav_home:
                startActivity(new Intent(this, MainActivity.class));
                overridePendingTransition(0, 0);
                break;
            case R.id.nav_map:
                startActivity(new Intent(this, MapActivity.class));
                break;
            case R.id.nav_logout:
                MainActivity.sessionUser = "default_session_value";
                startActivity(new Intent(this, LoginActivity.class));
                try {
                    Log.d("datasession", "default_session_value");
                    // Get the file path to the app's internal storage directory with the file name "session.txt"
                    File file = new File(getApplicationContext().getFilesDir(), "session.txt");

                    // Create a new FileWriter object with the file path
                    FileWriter writer = new FileWriter(file);

                    // Write the session string to the file
                    writer.write("default_session_value");
                    writer.flush();
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.nav_aboutUs:
                startActivity(new Intent(this, AboutActivity.class));
                break;
            case R.id.nav_editInfo:
                startActivity(new Intent(this, EditAccountActivity.class));
        }
        return false;
    }
    protected void allocateActivityTitle(String titleString){
        if (getSupportActionBar() != null){
            getSupportActionBar().setTitle((titleString));
        }
    }
}