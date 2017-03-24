package com.example.admin.socialnetwork;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity
{
    private FragmentManager fragmentManager;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //is user logged in??
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String USER = sharedPreferences.getString("USER","NOUSER");

        if (USER.equals("NOUSER"))
        {
            fragmentManager = this.getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.container,new LoginFragment()).commit();
        }
        else
        {
            Intent intent = new Intent(MainActivity.this,ProfileSetActivity.class);
            startActivity(intent);
        }
    }


}

