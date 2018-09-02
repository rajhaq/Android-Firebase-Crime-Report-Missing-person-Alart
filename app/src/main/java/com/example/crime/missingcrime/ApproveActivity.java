package com.example.crime.missingcrime;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.crime.missingcrime.Model.ReportModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ApproveActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference mRef;
    private FirebaseRecyclerAdapter<ReportModel,ApproveActivity.ReportHolder> mFirebaseAdapter;
    private RecyclerView JobList;
    String post_key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve);
        JobList = (RecyclerView) findViewById(R.id.bookedview);
        JobList.setHasFixedSize(true);
        JobList.setLayoutManager(new LinearLayoutManager(this));
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        mRef = database.getReference().child("reports");
        click();
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
                post_key=getRef(position).getKey();
                final String hotelid=model.getId();
                viewHolder.setTitle(model.getTitle(),model.getStatus());
                viewHolder.setEmail(model.getLocation());
                viewHolder.setNumber(model.getType());
                viewHolder.mView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        mRef.child(post_key).removeValue();
                        return false;
                    }
                });
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle b=new Bundle();
                        b.putString("id", post_key);
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

            jobTitle.setText(text);
            if(color.isEmpty())
            {
                jobTitle.setTextColor(Color.GREEN);

            }
            else
            {
                jobTitle.setTextColor(Color.RED);

            }
        }

        public void setNumber(String edu)
        {
            TextView jobEdu=(TextView)mView.findViewById(R.id.textViewEdu);
            jobEdu.setText(edu);

        }
        public void setEmail(String exp)
        {
            TextView jobExp=(TextView)mView.findViewById(R.id.textViewExp);
            jobExp.setText(exp);

        }


    }
    void click()
    {
        final DatabaseReference color = database.getReference().child("bookinglist").child(post_key).child("color");
        color.setValue("RED");

    }
}
