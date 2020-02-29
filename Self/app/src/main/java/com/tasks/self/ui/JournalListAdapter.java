package com.tasks.self.ui;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.tasks.self.Model.Journal;
import com.tasks.self.R;

import java.util.List;

public class JournalListAdapter extends RecyclerView.Adapter<JournalListAdapter.ViewHolder> {
    private List<Journal> journalList;
    private Context context;
    private LayoutInflater layoutInflater;

    public JournalListAdapter(List<Journal> journalList, Context context) {
        this.journalList = journalList;
        this.context = context;
        layoutInflater=LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=layoutInflater.inflate(R.layout.journalrow,parent,false);

        return new ViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (journalList!=null){
            final Journal journal = journalList.get(position);
            String imageurl;
            holder.title.setText(journal.getTitle());
            holder.thoughts.setText(journal.getThoughts());
            holder.name.setText(journal.getUsername());
            imageurl=journal.getImageURL();
//            Use Picasso to get the image from the imageURL
//            Source:https://square.github.io/picasso/
            Picasso.get()
                    .load(imageurl)
                    .fit()
                    .placeholder(R.drawable.image_three)//in case the server sends wrong image
                    .into(holder.journalImage);

//            getting time ago
//            Source:https://medium.com/@shaktisinh/time-a-go-in-android-8bad8b171f87
            String timeago= (String) DateUtils.getRelativeTimeSpanString(journal.getTimeadded().getSeconds()*1000);
            holder.dateAdded.setText(timeago);
            holder.share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_SUBJECT,"Title:"+journal.getTitle());
                    intent.putExtra(Intent.EXTRA_TEXT,"Thoughts:"+journal.getThoughts());
                    context.startActivity(intent);

                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return journalList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView
        title,
        thoughts,
        dateAdded,
        name;
        public ImageView journalImage;
        public ImageButton share;

        public String username;
        public String userId;

        public ViewHolder(@NonNull final View itemView, Context ctx) {
            super(itemView);
            context=ctx;
            title=itemView.findViewById(R.id.course_title);
            thoughts=itemView.findViewById(R.id.course_thoughts);
            dateAdded=itemView.findViewById(R.id.date_timestamp);
            name=itemView.findViewById(R.id.username_list);
            journalImage=itemView.findViewById(R.id.image_course);
            share=itemView.findViewById(R.id.share);


        }
    }
}
