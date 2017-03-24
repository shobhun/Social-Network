package com.example.admin.socialnetwork;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Admin on 10-12-2016.
 */

public class ForgetPasswordFragment extends Fragment
{
    private FragmentManager fragmentManager;
    private FirebaseAuth firebaseAuth;
    private EditText etxReserPassword;
    private String EmailReset;
    private Snackbar snackbar;
    private ProgressBar pbForget;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_forget_password,container,false);

        pbForget = (ProgressBar) view.findViewById(R.id.pbForgetPass);

        firebaseAuth = FirebaseAuth.getInstance();
        etxReserPassword = (EditText) view.findViewById(R.id.etxEmailReset);
        view.findViewById(R.id.btnResetPassword).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                pbForget.setVisibility(View.VISIBLE);

                EmailReset = etxReserPassword.getText().toString();
                if (TextUtils.isEmpty(EmailReset))
                {
                    snackbar= Snackbar.make(getView(),"Please Enter Email",Snackbar.LENGTH_INDEFINITE);
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
                    pbForget.setVisibility(View.INVISIBLE);
                }
                else
                {
                    firebaseAuth.sendPasswordResetEmail(EmailReset).addOnCompleteListener(new OnCompleteListener<Void>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {
                            if (task.isSuccessful())
                            {
                                Toast.makeText(getContext(), "Reset link has been sent.", Toast.LENGTH_SHORT).show();
                                fragmentManager = getActivity().getSupportFragmentManager();
                                fragmentManager.beginTransaction().replace(R.id.container,new LoginFragment()).commit();
                                pbForget.setVisibility(View.INVISIBLE);
                            }
                            else
                            {
                                snackbar= Snackbar.make(getView(),"Incorrect Email",Snackbar.LENGTH_INDEFINITE);
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
                                pbForget.setVisibility(View.INVISIBLE);
                            }
                        }
                    });
                }
            }
        });
        return view;
    }
}
