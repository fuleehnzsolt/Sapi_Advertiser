package com.example.fuleehnzsolt.sapi_advertiser.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fuleehnzsolt.sapi_advertiser.Adapter.AdvertisesListAdapter;
import com.example.fuleehnzsolt.sapi_advertiser.Data.Advertise;
import com.example.fuleehnzsolt.sapi_advertiser.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class Home extends Fragment {

    private RecyclerView recyclerView;
    private FirebaseFirestore db;
    private List<Advertise> advertisesList = new ArrayList<>();
    private AdvertisesListAdapter advertisesListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        advertisesListAdapter = new AdvertisesListAdapter(advertisesList);

        recyclerView = view.findViewById(R.id.advertise_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(advertisesListAdapter);

        db = FirebaseFirestore.getInstance();

        db.collection("advertises").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                for(DocumentSnapshot documentSnapshot: queryDocumentSnapshots) {
                    Advertise advertise = documentSnapshot.toObject(Advertise.class);
                    advertisesList.add(advertise);
                    advertisesListAdapter.notifyDataSetChanged();
                }
            }
        });

        return view;
    }
}
