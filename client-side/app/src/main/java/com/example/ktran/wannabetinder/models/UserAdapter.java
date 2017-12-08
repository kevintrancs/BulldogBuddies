package com.example.ktran.wannabetinder.models;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import com.example.ktran.wannabetinder.R;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by ktran on 12/1/17.
 */

public class UserAdapter extends ArrayAdapter<User> {
    private SharedPreferences mSharedPreferences;

    // firebase fields
    FirebaseDatabase mFirebaseDatabase;
    // add a reference to the "messages" portion of our database
    DatabaseReference mMessagesDatabaseReference;
    // add a reference for a messages listener
    ChildEventListener mMessagesChildEventListener;

    public UserAdapter(Context context, ArrayList<User> users){
        super(context,0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final User user = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.add_friend_row, parent, false);
        }
        TextView tvName = convertView.findViewById(R.id.childTextView);
        Button add_btn = convertView.findViewById(R.id.childButton);
        Uri uri = Uri.parse("http://weknowyourdreams.com/images/picture/picture-12.jpg");
        SimpleDraweeView simpleDraweeView = convertView.findViewById(R.id.image);
        simpleDraweeView.setImageURI(uri);
        tvName.setText(user.getName());
        add_btn.setText("Add");
        TextView matchPercent = convertView.findViewById(R.id.m_perc);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        final String mToken = mSharedPreferences.getString("myToken","");
        Gson gson = new Gson();
        String json = mSharedPreferences.getString("myUser", "");
        final User mUser = gson.fromJson(json, User.class);

       Log.d("test", " "+mUser.getSurvey_results());

        matchPercent.setText("");

        add_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {

                OkHttpClient client = new OkHttpClient.Builder()
                        .connectTimeout(10, TimeUnit.SECONDS)
                        .readTimeout(10,TimeUnit.SECONDS).build();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(Constants.BASE_URL).client(client)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                RetroInterfaces friendsInterface = retrofit.create(RetroInterfaces.class);
                Call<ServerResponse> response = friendsInterface.requestAFriend(mToken, user);
                response.enqueue(new Callback<ServerResponse>() {
                    @Override
                    public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                        ServerResponse resp = response.body();
                        if(resp.getSuccess()){
                            Snackbar.make(v,"Request Sent :)", Snackbar.LENGTH_LONG).show();
                            sendNotificationToUser("gina", user.getName() + " send you request.");
                        }
                        else{
                            Log.d("req", "fail");
                        }
                    }
                    @Override
                    public void onFailure(Call<ServerResponse> call, Throwable t) {
                        Log.d("req", "fail more");
                    }
                });
            }
        });
        return convertView;
    }

    public void sendNotificationToUser(String user, final String message) {

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference notifications =  mFirebaseDatabase.getReference().child("notificationRequests");

        Map notification = new HashMap<>();
        notification.put("username", user);
        notification.put("message", message);

        notifications.push().setValue(notification);

    }
}
