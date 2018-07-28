package com.example.ktran.wannabetinder.models;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.ktran.wannabetinder.R;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

/**
 * Created by ktran on 12/7/17.
 */

public class RequestAdapter extends ArrayAdapter<Friend> {

    public RequestAdapter(Context context, ArrayList<Friend> friends){
        super(context,0, friends);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final User user = getItem(position).getFriend();
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.friend_row, parent, false);
        }
        TextView tvName = convertView.findViewById(R.id.bigName);
        TextView tvPhone = convertView.findViewById(R.id.phone_number);


        tvName.setText(Constants.getSplitName(user.getName()));
        tvPhone.setText(" - " + (user.getDepartment()));

        Uri uri = Uri.parse("http://weknowyourdreams.com/images/picture/picture-12.jpg");
        SimpleDraweeView simpleDraweeView = convertView.findViewById(R.id.drew);
        simpleDraweeView.setImageURI(uri);

        return convertView;
    }
}