package com.example.admin.socialnetwork.RecyclerView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.admin.socialnetwork.R;

import java.util.ArrayList;

/**
 * Created by Admin on 15-12-2016.
 */

public class MyAdapter extends RecyclerView.Adapter<MyHolder>
{
    Context c;
    ArrayList<GS> arrayListData;
    String timecomplete,likes;


    public MyAdapter(Context c,ArrayList<GS> GS)
    {
        this.c = c;
        this.arrayListData = GS;
    }
    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.model,parent,false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position)
    {
        holder.txtTime.setText(arrayListData.get(position).getTime());
        holder.txtUserName.setText(arrayListData.get(position).getUsername());
        holder.txtStatus.setText(arrayListData.get(position).getStatus());
        holder.txtYear.setText(arrayListData.get(position).getYear());
        PicassoClient.downloadImage(c, arrayListData.get(position).getUrl(),holder.imageView);
        PicassoClient.downloadImage(c, arrayListData.get(position).getProfileurl(),holder.imgUserPP);

        timecomplete = arrayListData.get(position).getTimecomplete();


        holder.imgLikes.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Toast.makeText(c, "Liked", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public int getItemCount()
    {
        return arrayListData.size();
    }
}
