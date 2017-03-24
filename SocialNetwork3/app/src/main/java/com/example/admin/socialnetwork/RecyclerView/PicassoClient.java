package com.example.admin.socialnetwork.RecyclerView;

import android.content.Context;
import android.widget.ImageView;

import com.example.admin.socialnetwork.R;
import com.squareup.picasso.Picasso;

/**
 * Created by Admin on 15-12-2016.
 */

public class PicassoClient
{
    public static void downloadImage(Context c, String url, ImageView imageView)
    {
        if (url !=null && url.length()>0)
        {
            Picasso.with(c).load(url).placeholder(R.drawable.noimage).resize(640,380).into(imageView);
        }
    }
}
