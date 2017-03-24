package com.example.admin.socialnetwork.Chat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.admin.socialnetwork.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class MainActivityChat extends AppCompatActivity
{
    private ListView listView;
    private EditText etxChatRoomName;
    private ArrayList<String> arrayList = new ArrayList<>();
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot();
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_chat);
        listView = (ListView) findViewById(R.id.listView);
        etxChatRoomName = (EditText) findViewById(R.id.etxChat);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MainActivityChat.this,android.R.layout.simple_list_item_1,android.R.id.text1,arrayList);
        listView.setAdapter(arrayAdapter);
        requestUserName();
        findViewById(R.id.btnCreateChatRoom).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Map<String,Object> map = new HashMap<String,Object>();
                map.put(etxChatRoomName.getText().toString(),"");
                root.updateChildren(map);
                etxChatRoomName.setText(null);
            }
        });
        root = root.child("Chats");
        root.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                Set<String> set = new HashSet<String>();
                Iterator i = dataSnapshot.getChildren().iterator();
                while (i.hasNext())
                {
                    set.add(((DataSnapshot)i.next()).getKey());
                }
                arrayList.clear();
                arrayList.addAll(set);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                Intent intent = new Intent(getApplicationContext(),ChatRoomActivity.class);
                intent.putExtra("RN",((TextView)view).getText().toString());
                intent.putExtra("User_Name",name);
                startActivity(intent);

            }
        });
    }
    public void requestUserName()
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivityChat.this);
        alertDialog.setTitle("Enter Name");
        final EditText editText = new EditText(this);
        alertDialog.setView(editText);
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                name = editText.getText().toString();
            }
        });
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                dialogInterface.cancel();
                requestUserName();
            }
        });
        alertDialog.show();
    }
}
