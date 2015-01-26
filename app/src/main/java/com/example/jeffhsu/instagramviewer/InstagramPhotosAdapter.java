package com.example.jeffhsu.instagramviewer;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jeffhsu on 1/23/15.
 */
public class InstagramPhotosAdapter extends ArrayAdapter<InstagramPhoto> {

    public InstagramPhotosAdapter(Context context, List<InstagramPhoto> photos) {
        super(context, R.layout.item_photo, photos);
    }

    // Takes a data item at a position, converts it to a row in the listview
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //Take the data source


        // 1.  Get the item
        // 2.  Check if we are using a recycle view
        // 3.  Loop up the subview within the template
        // 4.  Populate the subviews (textfield, imageview) with correct data

        InstagramPhoto photo = getItem(position);
        if(convertView==null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photo, parent, false);
        }
            //Lookup subview within the template
            TextView tvCaption = (TextView) convertView.findViewById(R.id.tvCaption);
            ImageView imgPhoto = (ImageView) convertView.findViewById(R.id.imgPhoto);

            //Lookup profile elements
            TextView tvProfile = (TextView) convertView.findViewById(R.id.tvProfile);
            ImageView imgProfile = (ImageView) convertView.findViewById(R.id.imgProfile);

            //Lookup like elements
            TextView tvLike = (TextView) convertView.findViewById(R.id.tvLikes);

            //Lookup comment user
            TextView tvCommentUser = (TextView) convertView.findViewById(R.id.tvCommentUser);
            TextView tvUserComment = (TextView) convertView.findViewById(R.id.tvUserComment);

            //Populate comments
            tvCommentUser.setText(photo.commentUser+": ");
            tvUserComment.setText(photo.userComment);
            //Populate profile elements
            tvProfile.setText(photo.username);
            imgProfile.setImageResource(0);

            //Populate likes count
            tvLike.setText(photo.likesCount + " likes");

            Picasso.with(getContext()).load(photo.imageProfileUrl).transform(new CircleTransform()).into(imgProfile);
//            Picasso.with(getContext()).load(photo.imageProfileUrl).into(imgProfile);

            //Populate the subviews (textfield.imageView) with the correct data
            tvCaption.setText(photo.caption);
            // Set the image height before loading
            imgPhoto.getLayoutParams().height = Integer.parseInt(photo.imageHeight);
            // Reset the image from the recycled view
            imgPhoto.setImageResource(0);
            // Ask for the photo to be added to the imageview based on photo url
            // Background:  Send a network request to the url, download the image bytes, convert into bitmap, insert bitmap to the imageview
            Picasso.with(getContext()).load(photo.imageUrl).placeholder(R.drawable.loading).into(imgPhoto);

        //Return the view for that data item
        return convertView;
    }
}
