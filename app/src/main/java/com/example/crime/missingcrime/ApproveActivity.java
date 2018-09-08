package com.example.crime.missingcrime;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.crime.missingcrime.Model.ReportModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class ApproveActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference mRef;
    private FirebaseRecyclerAdapter<ReportModel,ApproveActivity.ReportHolder> mFirebaseAdapter;
    private RecyclerView JobList;
    String post_key;
    private static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context = getApplicationContext();
        JobList = (RecyclerView) findViewById(R.id.approveView);
        JobList.setHasFixedSize(true);
        JobList.setLayoutManager(new LinearLayoutManager(this));
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        mRef = database.getReference().child("reports");

//        click();
    }
    protected void onStart() {
        super.onStart();
        mFirebaseAdapter=new FirebaseRecyclerAdapter<ReportModel,ApproveActivity.ReportHolder>(
                ReportModel.class,
                R.layout.approve_row,
                ApproveActivity.ReportHolder.class,
                mRef

        ){

            @Override
            protected void populateViewHolder(ReportHolder viewHolder, ReportModel model, int position) {
//                post_key=getRef(position).getKey();
                final String post_key=model.getId();
                viewHolder.setTitle(model.getTitle(),model.getStatus());
                viewHolder.setLocation(model.getLocation());
                viewHolder.setType(model.getType());
                viewHolder.setImage(model.getImage());
                viewHolder.mView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
//                        mRef.child(post_key).removeValue();
                        new AlertDialog.Builder(ApproveActivity.this)
                                .setTitle("Delete Report")
                                .setMessage("Do you really want to delete this job?")
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        mRef.child(post_key).removeValue();
                                    }})
                                .setNegativeButton(android.R.string.no, null).show();
                        return false;
                    }
                });
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle b=new Bundle();
                        b.putString("id", post_key);
                        Toast.makeText(ApproveActivity.this, post_key, Toast.LENGTH_SHORT).show();
                        Intent intentUserList = new Intent(ApproveActivity.this, ApproveViewActivity.class);
                        intentUserList.putExtras(b);
                        startActivity(intentUserList);
                    }
                });

            }


        };
        JobList.setAdapter(mFirebaseAdapter);

    }
    public static class ReportHolder extends RecyclerView.ViewHolder{
        View mView;

        public ReportHolder(View itemView) {
            super(itemView);
            mView=itemView;
        }
        public void setTitle(String text,String color)
        {
            TextView jobTitle=(TextView)mView.findViewById(R.id.textViewJobTitle);
            TextView status=(TextView)mView.findViewById(R.id.textViewStatus);
            jobTitle.setText(text);
            if(color.isEmpty())
            {
                status.setText("Not Approved");
                status.setTextColor(Color.RED);
                jobTitle.setTextColor(Color.RED);

            }
            else
            {
                status.setText("Approved");
                status.setTextColor(Color.GREEN);
                jobTitle.setTextColor(Color.GREEN);

            }
        }

        public void setType(String edu)
        {
            TextView jobEdu=(TextView)mView.findViewById(R.id.textViewType);
            jobEdu.setText(edu);

        }
        public void setLocation(String exp)
        {
            TextView jobExp=(TextView)mView.findViewById(R.id.textViewLocation);
            jobExp.setText(exp);

        }
        public void setImage(String text)
        {
            ImageView image=(ImageView)mView.findViewById(R.id.imageViewRow);
            Picasso.with(context)
                    .load(text)
                    .resize(120,120)
                    .centerCrop()
                    .into(image);
        }



    }
//    void click()
//    {
//        final DatabaseReference color = database.getReference().child("reports").child(post_key).child("status");
//        color.setValue("RED");
//
//    }
}
