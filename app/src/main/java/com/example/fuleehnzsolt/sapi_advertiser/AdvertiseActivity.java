package com.example.fuleehnzsolt.sapi_advertiser;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;


public class AdvertiseActivity extends AppCompatActivity {

    private String userFirstName = null;
    private String userLastName = null;
    private String userPhoneNumber = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advertise);


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null) {
            userFirstName = (String)bundle.get("USER_FIRSTNAME");
            userLastName = (String)bundle.get("USER_LASTNAME");
            userPhoneNumber = (String)bundle.get("USER_PHONENUMBER");
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);


    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

}
