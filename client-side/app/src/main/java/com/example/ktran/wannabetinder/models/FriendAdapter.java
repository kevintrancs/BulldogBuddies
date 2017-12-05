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
 * Created by ktran on 12/1/17.
 */

public class FriendAdapter extends ArrayAdapter<Friend> {

    public FriendAdapter(Context context, ArrayList<Friend> friends){
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

        Log.d("TEST", " " + user.getName());

        tvName.setText(user.getName());
        tvPhone.setText((user.getPhone()));

        Uri uri = Uri.parse("http://weknowyourdreams.com/images/picture/picture-12.jpg");
        SimpleDraweeView simpleDraweeView = convertView.findViewById(R.id.drew);
        simpleDraweeView.setImageURI(uri);

        return convertView;
    }
}
