package com.example.admin.socialnetwork;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.admin.socialnetwork.Chat.MainActivityChat;
import com.example.admin.socialnetwork.ImageCrop.CropOption;
import com.example.admin.socialnetwork.ImageCrop.CropOptionAdapter;
import com.example.admin.socialnetwork.RecyclerView.GS;
import com.example.admin.socialnetwork.RecyclerView.MyAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HomeActivity extends AppCompatActivity
{
    private EditText etxStatus;
    private ImageView imageViewUploaded;
    private SharedPreferences sharedPreferences, sharedPreferencesPPS;
    private Uri selectedImage, downloadUri;
    private StorageReference storageReference;
    private ProgressBar pb;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference();
    private String status, USER, TimeComplte, downloadUrl;
    private ArrayList<GS> movies = new ArrayList<>();
    private MyAdapter adapter;
    private RecyclerView recyclerView;
    private String Time, TimeYear, Likes, profileurl = " ";

    private static final int PICK_FROM_CAMERA = 1;
    private static final int CROP_FROM_CAMERA = 2;
    private static final int PICK_FROM_FILE = 3;

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = new MenuInflater(this);
        menuInflater.inflate(R.menu.menu_items, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        super.onOptionsItemSelected(item);

        switch (item.getItemId())
        {
            case R.id.about:
                Toast.makeText(this, "This app is developed by Vijay Shinde and Shobhun Shah. ", Toast.LENGTH_SHORT).show();
                break;

            case R.id.logout:
                pb.setVisibility(View.VISIBLE);

                sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
                USER = sharedPreferences.getString("USER", "NOUSER");
                sharedPreferences.edit().putString("USER", "NOUSER").apply();

                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                pb.setVisibility(View.INVISIBLE);
                finish();
                break;

            case R.id.profile:
                Intent i = new Intent(HomeActivity.this, ProfileSetActivity.class);
                sharedPreferencesPPS = PreferenceManager.getDefaultSharedPreferences(this);
                sharedPreferencesPPS.edit().putString(USER, "NOTSET").apply();
                startActivity(i);
                finish();
                break;

            case R.id.changePassword:
                Intent change = new Intent(HomeActivity.this, ChangePassword.class);
                startActivity(change);
                break;

            case R.id.chats:
                Intent chats = new Intent(HomeActivity.this, MainActivityChat.class);
                startActivity(chats);
                break;

        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_home);

        //Runtime Permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            String[] permission =
                    {
                            android.Manifest.permission.READ_EXTERNAL_STORAGE,

                    };

            int flag = 0;
            flag = this.checkSelfPermission(android.Manifest.permission_group.STORAGE);
            if (flag == PackageManager.PERMISSION_DENIED)
            {
                this.requestPermissions(permission, 1001);
            }
        }

        //User name
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        USER = sharedPreferences.getString("USER", "NOUSER");
        Toast.makeText(this, "Welcome : " + USER, Toast.LENGTH_SHORT).show();

        // Initialization
        pb = (ProgressBar) findViewById(R.id.pb);
        etxStatus = (EditText) findViewById(R.id.etxStatus);
        imageViewUploaded = (ImageView) findViewById(R.id.uploadedImage);

        SimpleDateFormat dt1 = new SimpleDateFormat("dd:MM:yy");
        TimeYear = dt1.format(Calendar.getInstance().getTime());
        SimpleDateFormat dt2 = new SimpleDateFormat("hh:mm:ss");
        Time = dt2.format(Calendar.getInstance().getTime());


        //UserDATA to Firebase
        root = root.child(USER);
        root.child("time").setValue(Time + TimeYear);


        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        firebaseDatabase = FirebaseDatabase.getInstance();
        root = firebaseDatabase.getReference().child(USER);
        root.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                profileurl = dataSnapshot.getValue(GS.class).getProfileurl();
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });

        //Pointing to root
        firebaseDatabase = FirebaseDatabase.getInstance();
        root = firebaseDatabase.getReference();
        root = root.child("AllPosts");
//        root = root.child("Post_ImageUri");
        //adding childEvent Listener
        root.addChildEventListener(new ChildEventListener()
        {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s)
            {
                retrieveData(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s)
            {
                retrieveData(dataSnapshot);
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

        findViewById(R.id.btnUpload).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent image = new Intent(Intent.ACTION_GET_CONTENT);
                image.setType("image/*");
                startActivityForResult(image, 1001);
            }
        });



        findViewById(R.id.btnPost).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                pb.setVisibility(View.VISIBLE);

                status = etxStatus.getText().toString();
                SimpleDateFormat dt = new SimpleDateFormat("dd:MM:yy=hh:mm:ss");
                TimeComplte = dt.format(Calendar.getInstance().getTime());

                firebaseDatabase = FirebaseDatabase.getInstance();
                root = firebaseDatabase.getReference();
                root = root.child("AllPosts");
                root = root.child("Post_ImageUri");

                root = root.child(TimeComplte);

                try
                {
                    storageReference = FirebaseStorage.getInstance().getReference().child("image[" + Time + "]");
                    storageReference.putFile(selectedImage)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>()
                            {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                                {
                                    downloadUri = taskSnapshot.getDownloadUrl();
                                    downloadUrl = downloadUri.toString();

                                    root.child("timecomplete").setValue(TimeComplte);
                                    root.child("status").setValue(status);
                                    root.child("url").setValue(downloadUrl);
                                    root.child("time").setValue(Time);
                                    root.child("year").setValue(TimeYear);
                                    root.child("username").setValue(USER);
                                    root.child("like").setValue("0");
                                    root.child("profileurl").setValue(profileurl);

                                    Toast.makeText(HomeActivity.this, "Succcess", Toast.LENGTH_SHORT).show();
                                    pb.setVisibility(View.INVISIBLE);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener()
                            {
                                @Override
                                public void onFailure(@NonNull Exception e)
                                {
                                    e.printStackTrace();
                                    pb.setVisibility(View.INVISIBLE);
                                    Toast.makeText(HomeActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                                }
                            });


                } catch (NullPointerException NP)
                {
                    root.child("timecomplete").setValue(TimeComplte);
                    root.child("status").setValue("  ");
                    root.child("url").setValue(downloadUrl);
                    root.child("time").setValue(Time);
                    root.child("year").setValue(TimeYear);
                    root.child("username").setValue(USER);
                    root.child("like").setValue("0");
                    root.child("profileurl").setValue(profileurl);
                    NP.printStackTrace();
                } catch (IllegalArgumentException IA)
                {
                    root.child("timecomplete").setValue(TimeComplte);
                    root.child("status").setValue(status);
                    root.child("url").setValue("");
                    root.child("time").setValue(Time);
                    root.child("year").setValue(TimeYear);
                    root.child("username").setValue(USER);
                    root.child("like").setValue("0");
                    root.child("profileurl").setValue(profileurl);
                    IA.printStackTrace();
                }

                etxStatus.setText(null);
                imageViewUploaded.setImageBitmap(null);
                selectedImage = null;
            }
        });

    }

    private void retrieveData(DataSnapshot dataSnapshot)
    {
        movies.clear();
        for (DataSnapshot ds : dataSnapshot.getChildren())
        {
            GS m = new GS();
            m.setStatus(ds.getValue(GS.class).getStatus());
            m.setTime(ds.getValue(GS.class).getTime());
            m.setUrl(ds.getValue(GS.class).getUrl());
            m.setUsername(ds.getValue(GS.class).getUsername());
            m.setYear(ds.getValue(GS.class).getYear());
            m.setTimecomplete(ds.getValue(GS.class).getTimecomplete());
            m.setProfileurl(ds.getValue(GS.class).getProfileurl());
            movies.add(m);
        }
        if (movies.size() > 0)
        {
            adapter = new MyAdapter(this, movies);
            recyclerView.setAdapter(adapter);
        } else
        {
            Toast.makeText(this, "No Data", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed()
    {
        Intent i = new Intent(Intent.ACTION_MAIN);
        i.addCategory(Intent.CATEGORY_HOME);
//        i.setFlags()
        startActivity(i);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == 1001)
        {
            if (resultCode == RESULT_OK)
            {
                selectedImage = data.getData();
                try
                {
                    Bitmap bm = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                    imageViewUploaded.setImageBitmap(Bitmap.createScaledBitmap(bm, 300, 300, false));
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
                doCrop();
            }
            switch (requestCode)
            {
                case CROP_FROM_CAMERA:
                    Bundle extras = data.getExtras();

                    if (extras != null) {
                        Bitmap photo = extras.getParcelable("data");

                        imageViewUploaded.setImageBitmap(photo);
                    }

                    File f = new File(selectedImage.getPath());

                    if (f.exists()) f.delete();

                    break;
            }
        }
    }

    private void doCrop()
    {
        final ArrayList<CropOption> cropOptions = new ArrayList<CropOption>();

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setType("image/*");

        List<ResolveInfo> list = getPackageManager().queryIntentActivities( intent, 0 );

        int size = list.size();

        if (size == 0) {
            Toast.makeText(this, "Can not find image crop app", Toast.LENGTH_SHORT).show();

            return;
        } else {
            intent.setData(selectedImage);

            intent.putExtra("outputX", 200);
            intent.putExtra("outputY", 200);
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("scale", true);
            intent.putExtra("return-data", true);

            if (size == 1) {
                Intent i 		= new Intent(intent);
                ResolveInfo res	= list.get(0);

                i.setComponent( new ComponentName(res.activityInfo.packageName, res.activityInfo.name));

                startActivityForResult(i, CROP_FROM_CAMERA);
            } else {
                for (ResolveInfo res : list) {
                    final CropOption co = new CropOption();

                    co.title 	= getPackageManager().getApplicationLabel(res.activityInfo.applicationInfo);
                    co.icon		= getPackageManager().getApplicationIcon(res.activityInfo.applicationInfo);
                    co.appIntent= new Intent(intent);

                    co.appIntent.setComponent( new ComponentName(res.activityInfo.packageName, res.activityInfo.name));

                    cropOptions.add(co);
                }

                CropOptionAdapter adapter = new CropOptionAdapter(getApplicationContext(), cropOptions);

                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
                builder.setTitle("Choose Crop App");
                builder.setAdapter( adapter, new DialogInterface.OnClickListener() {
                    public void onClick( DialogInterface dialog, int item ) {
                        startActivityForResult( cropOptions.get(item).appIntent, CROP_FROM_CAMERA);
                    }
                });

                builder.setOnCancelListener( new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel( DialogInterface dialog ) {

                        if (selectedImage != null ) {
                            getContentResolver().delete(selectedImage, null, null );
                            selectedImage = null;
                        }
                    }
                } );

                android.support.v7.app.AlertDialog alert = builder.create();

                alert.show();
            }
        }
    }

}