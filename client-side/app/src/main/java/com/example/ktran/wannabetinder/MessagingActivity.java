package com.example.ktran.wannabetinder;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ktran.wannabetinder.models.Friend;
import com.example.ktran.wannabetinder.models.User;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


//This activity was a bad attempt at instant messaging
public class MessagingActivity extends AppCompatActivity {

    ListView messagesView;
    ArrayList<Friend> messageFriends;
    // firebase fields
    FirebaseDatabase mFirebaseDatabase;
    // add a reference to the "messages" portion of our database
    DatabaseReference mMessagesDatabaseReference;
    // add a reference for a messages listener
    ChildEventListener mMessagesChildEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Database stuff
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mMessagesDatabaseReference =
                mFirebaseDatabase.getReference().child("messages");


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                sendNotificationToUser("TEST",  "TESTTT");
            }
        });

        setTitle("Messages");

        //Initialize the listview and its adapter, and the arraylist
        messageFriends = new ArrayList<>();
        messagesView = (ListView) findViewById(R.id.messagesListView);
        ArrayAdapter<Friend> arrayAdapter = new ArrayAdapter<Friend>(this,
                android.R.layout.simple_list_item_1, messageFriends);

    }

    public void sendNotificationToUser(String user, final String message) {

        //String username = "puf";
        //FirebaseMessaging.getInstance().subscribeToTopic("user_"+username);

        //DatabaseReference notifications =  mFirebaseDatabase.getReference().child("Notification Requests");

        //Map notification = new HashMap<>();
        //notification.put("username", user);
        //notification.put("message", message);

        //notifications.push().setValue(notification);
    }

}
