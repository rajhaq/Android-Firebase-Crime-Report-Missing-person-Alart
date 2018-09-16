package com.example.crime.missingcrime;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {
    Context context;
    ArrayList<String> title;
    ArrayList<String> lcoation;
    ArrayList<String> range;
    ArrayList<String> key;
    ArrayList<String> logo;
    String key_post;

    class SearchViewHolder extends RecyclerView.ViewHolder {
        TextView jobTitle, jobEdu,jobExp;
        ImageView ImageHotel;

        public SearchViewHolder(View itemView) {
            super(itemView);
            jobTitle=(TextView)itemView.findViewById(R.id.textViewJobTitle);
            jobEdu=(TextView)itemView.findViewById(R.id.textViewType);
            jobExp=(TextView)itemView.findViewById(R.id.textViewLocation);
            ImageHotel=(ImageView)itemView.findViewById(R.id.imageViewRow);

        }
    }

    public SearchAdapter(Context context, ArrayList<String> title, ArrayList<String> location, ArrayList<String> range, ArrayList<String> key, ArrayList<String> logo) {
        this.context = context;
        this.title = title;
        this.lcoation = location;
        this.range = range;
        this.key = key;
        this.logo = logo;
    }

    @Override
    public SearchAdapter.SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.report_items, parent, false);
        return new SearchAdapter.SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchViewHolder holder, final int position) {
        final String key_post;
        holder.jobTitle.setText(title.get(position));
        holder.jobEdu.setText(range.get(position));
        holder.jobExp.setText(lcoation.get(position));
        Picasso.with(context)
                .load(logo.get(position))
                .resize(120,120)
                .centerCrop()
                .into(holder.ImageHotel);
        key_post=key.get(position);
        holder.jobTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b=new Bundle();
                b.putString("id",key_post );
                Toast.makeText(context, "From Search , ID:"+key_post, Toast.LENGTH_SHORT).show();
                Intent intentUserList = new Intent(context, ReportViewActivity.class);
                intentUserList.putExtras(b);
                context.startActivity(intentUserList);
            }
        });

    }

    @Override
    public int getItemCount() {
        return title.size();
    }
}
