package com.example.admin.socialnetwork;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Vijay Shinde on 18-12-2016.
 */

public class ChangePassword extends AppCompatActivity
{
    private FragmentManager fragmentManager;
    private FirebaseAuth firebaseAuth;
    private EditText etxReserPassword;
    private String EmailReset;
    private Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_forget_password);
        firebaseAuth = FirebaseAuth.getInstance();
        etxReserPassword = (EditText) findViewById(R.id.etxEmailReset);
        findViewById(R.id.btnResetPassword).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(final View view)
            {
                EmailReset = etxReserPassword.getText().toString();
                if (TextUtils.isEmpty(EmailReset))
                {
                    snackbar= Snackbar.make(view,"Please Enter Email",Snackbar.LENGTH_INDEFINITE);
                    snackbar.setAction("OK", new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View view)
                        {
                            snackbar.dismiss();
                        }
                    });
                    View sview = snackbar.getView();
                    sview.setBackgroundColor(Color.BLUE);
                    snackbar.show();
                }
                else
                {
                    firebaseAuth.sendPasswordResetEmail(EmailReset).addOnCompleteListener(new OnCompleteListener<Void>()
                    {
                        @Override
                        public void onComplete(Task<Void> task)
                        {
                            if (task.isSuccessful())
                            {
                                Toast.makeText(ChangePassword.this, "Reset link has been sent.", Toast.LENGTH_SHORT).show();
                                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ChangePassword.this);
                                sharedPreferences.edit().putString("USER","NOUSER").apply();
                                Intent intent = new Intent(ChangePassword.this,MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            else
                            {
                                snackbar= Snackbar.make(view,"Incorrect Email",Snackbar.LENGTH_INDEFINITE);
                                snackbar.setAction("OK", new View.OnClickListener()
                                {
                                    @Override
                                    public void onClick(View view)
                                    {
                                        snackbar.dismiss();
                                    }
                                });
                                View sview = snackbar.getView();
                                sview.setBackgroundColor(Color.BLUE);
                                snackbar.show();
                            }
                        }
                    });
                }
            }
        });
    }
}
