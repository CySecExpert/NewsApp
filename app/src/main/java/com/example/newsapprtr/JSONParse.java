package com.example.newsapprtr;
import android.net.Uri;
import android.util.Log;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.*;
import java.util.*;
import com.android.volley.*;
public class JSONParse {
    private static final String TAG = "JSONParse";
    private static final String API_KEY = "a0bf3707d4d8435da28c8d6efea8a383";
    private static final String NEWS_URL ="https://newsapi.org/v2/sources";
    private static final String SOURCE_URL = "https://newsapi.org/v2/top-headlines";
    private static List<NewsArticle> newsArticles;
    private static List<Sourcenews> newssources;
    private static RequestQueue queue;
    private static RequestQueue newsQueue;
    private static MainActivity mainActivity;
    private static HashSet<String> set = new HashSet<String>();
    private static News newsData;


    private static News parseJSON(JSONObject response) {
        String sourceID, nameSource, descSource;
        try{
            List<Sourcenews> sources = new ArrayList<Sourcenews>();
            String status  = response.getString("status");
            JSONArray jsonArray = response.getJSONArray("sources");
            int i = 0;
            while(i < jsonArray.length()) {
                JSONObject item = jsonArray.getJSONObject(i);
                sourceID = item.getString("id");
                nameSource = item.getString("name");
                descSource = item.getString("category");
                sources.add(new Sourcenews(sourceID, nameSource, descSource));
                Log.d(TAG, "parseJSON: Updating the Sources list with JSON Objects...");
                i = i + 1;
            }
            newsData = new News(status, sources);
        }
        catch (Exception exception){
            exception.printStackTrace();
        }
        if(newsData == null){
            Log.d(TAG, "parseJSON: newsData is not a null value...");
        }
        return newsData;
    }

    private static String getURL() {
        Uri.Builder buildURL = Uri.parse(NEWS_URL).buildUpon();
        buildURL.appendQueryParameter("apiKey", API_KEY);
        return buildURL.build().toString();
    }

    private static List<NewsArticle> detailsParse(JSONObject response) {
        List<NewsArticle> parsedNewsList = new ArrayList<NewsArticle>();
        try{
            JSONArray jsonArray = response.getJSONArray("articles");
            int length = jsonArray.length();
            int i = 0;
            while(i < length){
                NewsArticle object = new NewsArticle();
                JSONObject jsonItem = jsonArray.getJSONObject(i);
                String headline = jsonItem.getString("title");
                String author_name = jsonItem.getString("author");
                String URL = jsonItem.getString("url");
                String description = jsonItem.getString("description");
                String urlToImage = jsonItem.getString("urlToImage");
                String date = jsonItem.getString("publishedAt");
                Log.d(TAG, "detailsParse: Setting the JSON objects in the list...");
                object.setHeadline(headline);
                object.setAuthor(author_name);
                object.setURL(URL);
                object.setArticleDesc(description);
                object.setImageURL(urlToImage);
                object.setDateTime(date);
                parsedNewsList.add(object);
                Log.d(TAG, "detailsParse: The JSON Objects are set in the list...");
                i = i + 1;
            }
        }
        catch (Exception exception){
            Log.d(TAG, "detailsParse: Exception!");
            exception.printStackTrace();
        }
        return parsedNewsList;
    }

    public static News getDataFromAPI(MainActivity mainAct) {
        mainActivity = mainAct;
        queue = Volley.newRequestQueue(mainActivity);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, getURL(), null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        newsData = parseJSON(response);
                        mainAct.updateData(newsData);
                        set.add("all");
                        newssources = newsData.getSourcenews();
                        for (Sourcenews element: newssources){
                            set.add(element.getCategory());
                        }
                        mainAct.updateMenuItem(set);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "onErrorResponse: Seems like there is no error response...");
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("User-Agent", "Ryan-Timothy-Rishab");
                if(headers == null){
                    Log.d(TAG, "getHeaders: Headers cannot be null!");
                }
                return headers;
            }
        };
        queue.add(jsonObjectRequest);
        return newsData;
    }

    public static List<NewsArticle> getNewsArticles(String source, MainActivity mainAct){
        mainActivity = mainAct;
        newsQueue = Volley.newRequestQueue(mainActivity);
        Uri.Builder buildURL = Uri.parse(SOURCE_URL).buildUpon();
        buildURL.appendQueryParameter("sources", source);
        buildURL.appendQueryParameter("apiKey", API_KEY);
        JsonObjectRequest JSONRequest = new JsonObjectRequest
                (Request.Method.GET, buildURL.build().toString(), null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        newsArticles = detailsParse(response);
                        mainAct.updatePageView(newsArticles);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("User-Agent", "Ryan-Timothy-Rishab");
                return headers;
            }
        };
        newsQueue.add(JSONRequest);
        return newsArticles;
    }

    public static List<Sourcenews> updateDataDrawer(String title) {
        List<Sourcenews> updateNewsList = new ArrayList<Sourcenews>();
        int list_length = newssources.size();
        int i = 0;
        Log.d(TAG, "updateDataDrawer: Beginning the while loop...");
        while(i < list_length){
            String category = newssources.get(i).getCategory();
            if(category.equalsIgnoreCase(title)){
                updateNewsList.add(newssources.get(i));
            }
            i = i+1;
        }
        return updateNewsList;
    }
}