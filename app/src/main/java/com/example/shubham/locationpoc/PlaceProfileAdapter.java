package com.example.shubham.locationpoc;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import io.realm.Realm;
import io.realm.RealmBaseAdapter;
import io.realm.RealmResults;

/**
 * Created by Shubham on 6/7/2016.
 */
public class PlaceProfileAdapter extends RealmBaseAdapter {

    private Context mContext;
    Realm realm;

    private static class PhotoViewHolder {
        private TextView ivNick;
        private TextView ivAbout;
        private TextView ivInterests;
        private TextView ivPhone;
        private TextView ivAddress;
        private TextView ivEmail;
    }


    public PlaceProfileAdapter(Context context, int resId, RealmResults<Place> realmResults, boolean automaticUpdate) {
        super(context, realmResults, automaticUpdate);
        mContext = context;
        realm = Realm.getInstance(mContext);
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        PhotoViewHolder holder;
        if (view != null) {
            holder = (PhotoViewHolder) view.getTag();
        } else {
            holder = new PhotoViewHolder();
            view = inflater.inflate(R.layout.place_item_view, viewGroup, false);
            holder.ivNick = (TextView) view.findViewById(R.id.ivNick);
            //    holder.ivPromoter = (TextView) view.findViewById(R.id.ivPromoter);
            holder.ivInterests = (TextView) view.findViewById(R.id.ivInterests);
            holder.ivAddress = (TextView) view.findViewById(R.id.ivAddress);
            holder.ivPhone = (TextView) view.findViewById(R.id.ivPhone);
            holder.ivEmail = (TextView) view.findViewById(R.id.ivEmail);
            view.setTag(holder);
        }
        Place place = (Place) realmResults.get(i);
        holder.ivNick.setText(place.getName());
        holder.ivInterests.setText("Place distance is " + place.getDistance() + " m");
        if (place.getPhone().length() > 0) {
            holder.ivPhone.setText(place.getPhone());
        }
        if (place.getAddress().length() > 0) {
            holder.ivAddress.setText(place.getAddress());
        }
        if (place.getEmail().length() > 0) {
            holder.ivEmail.setText(place.getEmail());
        }
        return view;
    }
}
