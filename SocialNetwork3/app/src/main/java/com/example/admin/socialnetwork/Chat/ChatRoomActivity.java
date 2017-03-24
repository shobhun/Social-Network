package com.example.admin.socialnetwork.Chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.admin.socialnetwork.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ChatRoomActivity extends AppCompatActivity
{
    private EditText etxSendingMessage;
    private TextView txtReceivedMesssage;
    private String RoomName,UserName,key_temp,chat_msg,chat_username;
    private DatabaseReference root;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        etxSendingMessage = (EditText)findViewById(R.id.etxSendindMessage);
        txtReceivedMesssage = (TextView) findViewById(R.id.etxReceivedMessage);

        Intent intent = getIntent();
        RoomName = intent.getStringExtra("RN");
        UserName = intent.getStringExtra("User_Name");
        setTitle(" Room - "+RoomName);
        root = FirebaseDatabase.getInstance().getReference().child("chats").child(RoomName);
        findViewById(R.id.btnSendMessage).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Map<String,Object> map = new HashMap<String, Object>();
                key_temp = root.push().getKey();
                root.updateChildren(map);

                DatabaseReference message_root = root.child(key_temp);
                Map<String,Object> map2 = new HashMap<String, Object>();
                map2.put("name",UserName);
                map2.put("msg",etxSendingMessage.getText().toString());
                message_root.updateChildren(map2);
                etxSendingMessage.setText(null);
            }
        });
        root.addChildEventListener(new ChildEventListener()
        {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s)
            {
                 append_chat_conversaion(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s)
            {
                append_chat_conversaion(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot)
            {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s)
            {

            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });
    }

    private void append_chat_conversaion(DataSnapshot dataSnapshot)
    {
        Iterator i = dataSnapshot.getChildren().iterator();
        while (i.hasNext())
        {
            chat_msg = (String) ((DataSnapshot)i.next()).getValue();
            chat_username = (String) ((DataSnapshot)i.next()).getValue();
            txtReceivedMesssage.append(chat_username+" : "+chat_msg+"\n");
        }
    }
}
