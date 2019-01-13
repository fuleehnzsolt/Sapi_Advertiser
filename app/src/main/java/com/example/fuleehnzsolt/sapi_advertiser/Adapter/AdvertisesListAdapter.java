package com.example.fuleehnzsolt.sapi_advertiser.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.fuleehnzsolt.sapi_advertiser.Data.Advertise;
import com.example.fuleehnzsolt.sapi_advertiser.R;



import java.util.List;

public class AdvertisesListAdapter extends RecyclerView.Adapter<AdvertisesListAdapter.ViewHolder> {

    public List<Advertise> advertisesList;

    public AdvertisesListAdapter(List<Advertise> advertisesList) {
        this.advertisesList = advertisesList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.l_items, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.title.setText(advertisesList.get(i).getTitle());
        viewHolder.shortDesccription.setText(advertisesList.get(i).getShortDescription());
        viewHolder.longDescription.setText(advertisesList.get(i).getLongDescription());
        viewHolder.phoneNumber.setText(advertisesList.get(i).getPhoneNumber());
        viewHolder.location.setText(advertisesList.get(i).getLocation());
        viewHolder.image = advertisesList.get(i).getImage();
        Glide.with(viewHolder.mView).load(viewHolder.image).into(viewHolder.imageAd);
    }

    @Override
    public int getItemCount() {
        return advertisesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public TextView title;
        public TextView shortDesccription;
        public TextView longDescription;
        public TextView phoneNumber;
        public TextView location;
        public String image;
        public ImageView imageAd;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;

            title = mView.findViewById(R.id.detail_title);
            shortDesccription = mView.findViewById(R.id.detail_shortDescription);
            longDescription = mView.findViewById(R.id.ad_LongDescription);
            phoneNumber = mView.findViewById(R.id.detail_phoneNum);
            location = mView.findViewById(R.id.detail_location);
            imageAd = mView.findViewById(R.id.detail_image);
        }
    }
}
