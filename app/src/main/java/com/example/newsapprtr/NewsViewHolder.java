package com.example.newsapprtr;
import androidx.annotation.NonNull;

import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

public class NewsViewHolder extends RecyclerView.ViewHolder {
    //Declaring the variables for the view holder...
    ImageView newsImage;
    TextView date_time;
    TextView author;
    TextView headline;
    TextView pageNumber;
    TextView articleDesc;

    public NewsViewHolder(@NonNull View itemView) {
        super(itemView);
        //Getting the elements for NewsAdapter...
        newsImage = itemView.findViewById(R.id.newsImage);
        date_time = itemView.findViewById(R.id.date_time);
        author = itemView.findViewById(R.id.author);
        headline = itemView.findViewById(R.id.headline);
        pageNumber = itemView.findViewById(R.id.pageNumber);
        articleDesc = itemView.findViewById(R.id.articleDesc);
        //We are going to use ScrollingMovementMethod;
        //To provide necessary scrolling for the description of the article...
        this.articleDesc.setMovementMethod(new ScrollingMovementMethod());
    }


}
