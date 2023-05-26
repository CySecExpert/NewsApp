package com.example.newsapprtr;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.*;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.*;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

public class NewsAdapter extends RecyclerView.Adapter<NewsViewHolder>{
    private final List<NewsArticle> newsList;
    private final MainActivity mainActivity;
    //Initializing the Constructor with the variables above...
    public NewsAdapter(List<NewsArticle> newsList, MainActivity mainActivity) {
        this.newsList = newsList;
        this.mainActivity = mainActivity;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NewsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.article_activity, parent, false));
    }

    @Override
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        NewsArticle newsArticle = newsList.get(position);
        String headLineCheck =  newsArticle.getHeadline();
        if(headLineCheck == null){
            ;
        }
        else{
            String title = newsArticle.getHeadline();
            holder.headline.setText(title);
            holder.headline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //This method is used to establish the runnable intent with the URL...
                    Intent newsIntent = new Intent(Intent.ACTION_VIEW);
                    String newsURL = newsArticle.getURL();
                    newsIntent.setData(Uri.parse(newsURL));
                    mainActivity.startActivity(newsIntent);
                }
            });
        }
        if(newsArticle.getAuthor()!=null||!newsArticle.getAuthor().isEmpty()){
            String authors = newsArticle.getAuthor();
            holder.author.setText(authors);
        }
        else{
            holder.author.setVisibility(View.GONE);
        }
        //Setting Image for this task...
        String url = newsArticle.getURL();
        if(url != null){
            Picasso pics = Picasso.with(mainActivity);
            pics.setLoggingEnabled(true);
            pics.load(newsArticle.getImageURL()).placeholder(R.drawable.loading).error(R.drawable.brokenimage).into(holder.newsImage);
            holder.newsImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent newsIntent = new Intent(Intent.ACTION_VIEW);
                    newsIntent.setData(Uri.parse(newsArticle.getURL()));
                    mainActivity.startActivity(newsIntent);
                }
            });
        }
        else{
            holder.newsImage.setImageResource(R.drawable.noimage);
        }
        //Setting the date for the news article...
        String articleTest = newsArticle.getDateTime();
        if(articleTest==null){
            ;
        }
        else{
            String dateTime = dateOfArticle(newsArticle.getDateTime());
            holder.date_time.setText(dateTime);
        }
        String descArticle = newsArticle.getArticleDesc();
        if(descArticle != null || !newsArticle.getAuthor().isEmpty()){
            holder.articleDesc.setText(descArticle);
            holder.articleDesc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent newsIntent = new Intent(Intent.ACTION_VIEW);
                    newsIntent.setData(Uri.parse(newsArticle.getURL()));
                    mainActivity.startActivity(newsIntent);
                }
            });
        }
        else{
            holder.articleDesc.setVisibility(View.GONE);
        }
        String pageNumber = String.format("%d / %d", position+1, newsList.size());
        holder.pageNumber.setText(pageNumber);
    }
    @RequiresApi(api=Build.VERSION_CODES.O)
    private String dateOfArticle(String dateTime) {
        String date = "";
        try{
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_INSTANT;
            TemporalAccessor temporalAccessor = dateTimeFormatter.parse(dateTime);
            DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern("LLL dd, yyyy kk:mm");
            LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.from(temporalAccessor), ZoneId.systemDefault());
            date = localDateTime.format(dtFormatter);
        }
        catch (Exception exception){
            exception.printStackTrace();
        }
        if(date.isEmpty()){
            try{
                DateTimeFormatter dateTimeFormat = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
                TemporalAccessor temporalAccess = dateTimeFormat.parse(dateTime);
                DateTimeFormatter dtFormat = DateTimeFormatter.ofPattern("LLL dd, yyyy kk:mm");
                LocalDateTime localDT = LocalDateTime.ofInstant(Instant.from(temporalAccess), ZoneId.systemDefault());
                date = localDT.format(dtFormat);
            }
            catch(Exception exception){
                exception.printStackTrace();
            }
        }
        return date;
    }


    @Override
    public int getItemCount() {
        return newsList.size();
    }
}
