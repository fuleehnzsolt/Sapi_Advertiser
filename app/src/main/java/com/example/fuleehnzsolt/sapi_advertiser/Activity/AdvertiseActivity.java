package com.example.fuleehnzsolt.sapi_advertiser.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.fuleehnzsolt.sapi_advertiser.Fragment.Account;
import com.example.fuleehnzsolt.sapi_advertiser.Fragment.AddNewAdvertise;
import com.example.fuleehnzsolt.sapi_advertiser.Fragment.Home;
import com.example.fuleehnzsolt.sapi_advertiser.R;


public class AdvertiseActivity extends AppCompatActivity {

    private String uFirstName = null;
    private String uLastName = null;
    private String uPhoneNumber = null;

    /**
     * Lekérjük az előző Activitytől a felhasználó adatait putExtra -> bundle.get
     * **/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advertise);

        loadFragment(new Home());

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            uFirstName = (String) bundle.get("USER_FIRSTNAME");
            uLastName = (String) bundle.get("USER_LASTNAME");
            uPhoneNumber = (String) bundle.get("USER_PHONENUMBER");
        }

        /**
         * Létrehozzuk egy navigációs egységet a képernyő allján, ami Fragmentekből áll,
         * Új poszt létrehozása, Home és a Felhasználó adatai.
         * **/

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                Fragment fragment = null;
                switch (menuItem.getItemId()) {
                    case R.id.bottom_navigation_add:
                        fragment = new AddNewAdvertise();
                        break;
                    case R.id.bottom_navigation_home:
                        fragment = new Home();
                        break;
                    case R.id.bottom_navigation_account:
                        fragment = new Account();
                        break;
                }
                return loadFragment(fragment);
            }
        });

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
