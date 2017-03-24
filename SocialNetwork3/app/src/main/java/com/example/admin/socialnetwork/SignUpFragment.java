package com.example.admin.socialnetwork;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Admin on 18-12-2016.
 */

public class SignUpFragment extends Fragment
{
    private EditText etxEmailS, etxPasswordS,etxPasswordagain;
    private String SEmail, PasswordS;
    private Snackbar snackbar;
    private FirebaseAuth fbAuth;
    private FragmentManager fragmentManager;
    private SharedPreferences sharedPreferences;
    private ProgressBar progressbarS;
    private String[] email;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        final View view = inflater.inflate(R.layout.fragment_signup,container,false);

        progressbarS = (ProgressBar) view.findViewById(R.id.Fprogressbar);
        etxEmailS = (EditText) view.findViewById(R.id.etxFEmail);
        etxPasswordS = (EditText) view.findViewById(R.id.etxFPassword);
        etxPasswordagain = (EditText) view.findViewById(R.id.etxFPasswordAgain);

        fbAuth = FirebaseAuth.getInstance();

        view.findViewById(R.id.btnFSigUp).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(final View view)
            {
                progressbarS.setVisibility(View.VISIBLE);
                SEmail = etxEmailS.getText().toString();
                PasswordS = etxPasswordS.getText().toString();


                if (TextUtils.isEmpty(SEmail))
                {
                    snackbar= Snackbar.make(view,"Please Enter SEmail ID",Snackbar.LENGTH_LONG);
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
                    progressbarS.setVisibility(View.INVISIBLE);
                }
                else if (TextUtils.isEmpty(PasswordS))
                {
                    snackbar= Snackbar.make(view,"Please Enter PasswordS",Snackbar.LENGTH_LONG);
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
                    progressbarS.setVisibility(View.INVISIBLE);
                }
                else if (TextUtils.isEmpty(etxPasswordagain.getText().toString()))
                {
                    snackbar= Snackbar.make(view,"Please Enter PasswordS",Snackbar.LENGTH_LONG);
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
                    progressbarS.setVisibility(View.INVISIBLE);
                }
                else if (PasswordS.length()<6 | etxPasswordagain.getText().toString().length()<6)
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
                    progressbarS.setVisibility(View.INVISIBLE);
                }
                else if (!(PasswordS.equals(etxPasswordagain.getText().toString())))
                {
                    snackbar= Snackbar.make(view,"Password did not match",Snackbar.LENGTH_LONG);
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
                    progressbarS.setVisibility(View.INVISIBLE);
                }
                else
                {
                    fbAuth.createUserWithEmailAndPassword(SEmail, PasswordS).addOnCompleteListener(new OnCompleteListener<AuthResult>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {
                            if (task.isSuccessful())
                            {
                                Toast.makeText(getContext(), "Registered & Logged In Successfully", Toast.LENGTH_SHORT).show();
                                Intent ProfileSetActivity = new Intent(getContext(), com.example.admin.socialnetwork.ProfileSetActivity.class);

                                email = SEmail.split("@");

                                sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                                sharedPreferences.edit().putString("USER",email[0]).apply();

                                startActivity(ProfileSetActivity);
                                progressbarS.setVisibility(View.INVISIBLE);
                            }
                            else if ((task.getException().toString()).equals("com.google.firebase.auth.FirebaseAuthUserCollisionException: The email address is already in use by another account."))
                            {
                                snackbar= Snackbar.make(view,"Email Id Already Used.",Snackbar.LENGTH_INDEFINITE);
                                snackbar.setAction("Forgot PasswordS ?", new View.OnClickListener()
                                {
                                    @Override
                                    public void onClick(View view)
                                    {
                                        fragmentManager = getActivity().getSupportFragmentManager();
                                        fragmentManager.beginTransaction().replace(R.id.container, new ForgetPasswordFragment()).commit();

                                    }
                                });
                                View sview = snackbar.getView();
                                sview.setBackgroundColor(Color.BLUE);
                                snackbar.show();
                                progressbarS.setVisibility(View.INVISIBLE);
                            }
                            else
                            {
                                Toast.makeText(getContext(), ""+task.getException().toString() , Toast.LENGTH_SHORT).show();
                                Log.d("TAG:",task.getException().toString());
                                progressbarS.setVisibility(View.INVISIBLE);
                            }
                        }

                    });

                }
            }
        });




        return view;
    }
}
