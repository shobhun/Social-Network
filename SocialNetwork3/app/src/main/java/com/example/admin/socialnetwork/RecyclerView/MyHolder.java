package com.example.admin.socialnetwork.RecyclerView;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.admin.socialnetwork.R;

/**
 * Created by Admin on 15-12-2016.
 */

public class MyHolder extends RecyclerView.ViewHolder
{
    TextView txtUserName,txtStatus,txtTime,txtYear,txtLikes;
    ImageView imageView,imgLikes,imgUserPP;

    public MyHolder(View itemView)
    {
        super(itemView);

        txtUserName = (TextView) itemView.findViewById(R.id.txtUserName);
        txtStatus = (TextView) itemView.findViewById(R.id.txtStatus);
        txtTime = (TextView) itemView.findViewById(R.id.txtTime);
        txtYear = (TextView) itemView.findViewById(R.id.txtYear);
        imageView = (ImageView) itemView.findViewById(R.id.imageShow);
        imgLikes = (ImageView) itemView.findViewById(R.id.imgLike);
        imgUserPP = (ImageView) itemView.findViewById(R.id.imgUserPP);
    }
}
