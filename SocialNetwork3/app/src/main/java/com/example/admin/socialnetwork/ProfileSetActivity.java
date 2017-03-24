package com.example.admin.socialnetwork;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ProfileSetActivity extends AppCompatActivity
{
    private TextView txtSelectPP;
    private ImageView imgSelectPP;
    private Uri selectedImage;
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference();
    private SharedPreferences spUser,spProfileSetup;
    private String UserName,age,TimeComplte,profileurl,flag;
    private EditText etxAge;
    private ProgressBar pbPPset;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_set);

        spUser = PreferenceManager.getDefaultSharedPreferences(this);
        UserName = spUser.getString("USER","NOUSER");

        //Checking If Profile is Set
        spProfileSetup = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        flag = spProfileSetup.getString(UserName,"NOTSET");

        if(!flag.equals("NOTSET"))
        {
            Intent i = new Intent(this,HomeActivity.class);
            startActivity(i);
            finish();
        }

        txtSelectPP = (TextView) findViewById(R.id.txtSelectPP);
        imgSelectPP = (ImageView) findViewById(R.id.imgProfilePic);
        etxAge = (EditText) findViewById(R.id.etxAge);
        pbPPset = (ProgressBar) findViewById(R.id.pbPPSet);


        spUser = PreferenceManager.getDefaultSharedPreferences(this);
        UserName = spUser.getString("USER","NOUSER");

        txtSelectPP.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("image/*");
                startActivityForResult(i,1010);
            }
        });
        imgSelectPP.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("image/*");
                startActivityForResult(i,1010);
            }
        });

        SimpleDateFormat dt = new SimpleDateFormat("dd:MM:yy=hh:mm:ss");
        TimeComplte = dt.format(Calendar.getInstance().getTime());

        root = root.child(UserName);

        findViewById(R.id.btnSave).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                pbPPset.setVisibility(View.VISIBLE);
                age = etxAge.getText().toString();
                 try
                 {
                     if (!age.isEmpty() && !selectedImage.equals(null))
                     {
                         StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("imageProfile[" + UserName + TimeComplte + "]");
                         storageReference.putFile(selectedImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>()
                         {
                             @Override
                             public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                             {
                                 profileurl = taskSnapshot.getDownloadUrl().toString();
                                 if (!profileurl.isEmpty())
                                 {
                                     root.child("user").setValue(UserName);
                                     root.child("age").setValue(age);
                                     root.child("timecomplete").setValue(TimeComplte);
                                     root.child("profileurl").setValue(profileurl);
                                 } else
                                 {
                                     root.child("user").setValue(UserName);
                                     root.child("age").setValue(age);
                                     root.child("timecomplete").setValue(TimeComplte);
                                     root.child("profileurl").setValue(" ");
                                 }
                                 spProfileSetup = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                 spProfileSetup.edit().putString(UserName, "SETUP").apply();
                                 Intent i = new Intent(ProfileSetActivity.this, HomeActivity.class);
                                 startActivity(i);
                                 pbPPset.setVisibility(View.INVISIBLE);
                                 finish();
                             }
                         });
                     }
                 }catch (IllegalArgumentException | NullPointerException e)
                 {
                     e.printStackTrace();
                 }

            }
        });

        findViewById(R.id.txtskip).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                pbPPset.setVisibility(View.VISIBLE);
                root.child("user").setValue(UserName);
                root.child("age").setValue(" ");
                root.child("timecomplete").setValue(TimeComplte);
                root.child("profileurl").setValue(" ");

                spProfileSetup = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                spProfileSetup.edit().putString(UserName, "NOTSET").apply();
                Intent i = new Intent(ProfileSetActivity.this, HomeActivity.class);
                startActivity(i);
                pbPPset.setVisibility(View.INVISIBLE);
                finish();

            }
        });

    }

    @Override
    public void onBackPressed()
    {
        Intent i = new Intent(Intent.ACTION_MAIN);
        i.addCategory(Intent.CATEGORY_HOME);
        startActivity(i);
        finish();
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == 1010)
        {
            if (resultCode == RESULT_OK)
            {
                selectedImage = data.getData();
                try
                {
                    Bitmap bm = MediaStore.Images.Media.getBitmap(this.getContentResolver(),selectedImage);
                    imgSelectPP.setImageBitmap(Bitmap.createScaledBitmap(bm,150,100,false));
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
}
