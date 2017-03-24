package com.example.admin.socialnetwork;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Admin on 10-12-2016.
 */

public class LoginFragment extends Fragment
{
    private EditText etxEmail,etxPassword;
    private String Email,Password;
    private Snackbar snackbar;
    private FirebaseAuth fbAuth;
    private FragmentManager fragmentManager;
    private SharedPreferences sharedPreferences;
    private ProgressBar progressbar;
    private String[] email;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragmnt_login,null,false);

        progressbar = (ProgressBar) view.findViewById(R.id.progressbar);
        etxEmail = (EditText) view.findViewById(R.id.etxEmail);
        etxPassword = (EditText) view.findViewById(R.id.etxPassword);

        fbAuth = FirebaseAuth.getInstance();
        view.findViewById(R.id.btnSigUp).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(final View view)
            {
                fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.container,new SignUpFragment()).commit();
            }
        });

        //Log In
        view.findViewById(R.id.btnLogIn).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(final View view)
            {
                progressbar.setVisibility(View.VISIBLE);
                Email = etxEmail.getText().toString();
                Password = etxPassword.getText().toString();


                if (TextUtils.isEmpty(Email))
                {
                    snackbar= Snackbar.make(view,"Please Enter Email ID",Snackbar.LENGTH_LONG);
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
                    progressbar.setVisibility(View.INVISIBLE);
                }
                else if (TextUtils.isEmpty(Password))
                {
                    snackbar= Snackbar.make(view,"Please Enter Password",Snackbar.LENGTH_LONG);
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
                    progressbar.setVisibility(View.INVISIBLE);
                }
                else if (Password.length()<6)
                {
                    snackbar= Snackbar.make(view,"Minimum length of password is 6",Snackbar.LENGTH_LONG);
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
                    progressbar.setVisibility(View.INVISIBLE);
                }
                else
                {
                    fbAuth.signInWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {
                            if (task.isSuccessful())
                            {
                                Intent intent = new Intent(getContext(), ProfileSetActivity.class);
                                email = Email.split("@");
                                sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                                sharedPreferences.edit().putString("USER",email[0]).apply();
                                startActivity(intent);
                                progressbar.setVisibility(View.INVISIBLE);
                            }
                            else
                            {
                                snackbar= Snackbar.make(view,"Incorrect Password",Snackbar.LENGTH_LONG);
                                snackbar.setAction("Forgot Password ?", new View.OnClickListener()
                                {
                                    @Override
                                    public void onClick(View view)
                                    {
                                        fragmentManager = getActivity().getSupportFragmentManager();
                                        fragmentManager.beginTransaction().replace(R.id.container,new ForgetPasswordFragment()).commit();
                                    }
                                });

                                View sview = snackbar.getView();
                                sview.setBackgroundColor(Color.BLUE);
                                snackbar.show();
                                progressbar.setVisibility(View.INVISIBLE);
                            }
                        }
                    });
                }
            }
        });

        return view;
    }
}
