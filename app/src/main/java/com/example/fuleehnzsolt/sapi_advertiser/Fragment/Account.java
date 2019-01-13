package com.example.fuleehnzsolt.sapi_advertiser.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.fuleehnzsolt.sapi_advertiser.Activity.MainActivity;
import com.example.fuleehnzsolt.sapi_advertiser.R;



public class Account extends Fragment {

    private ImageView logout, save, avatar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view;
        view = inflater.inflate(R.layout.fragment_account, container, false);
        logout = view.findViewById(R.id.accLogout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent(getActivity(), MainActivity.class));

            }
        });

        return view;
    }
}
