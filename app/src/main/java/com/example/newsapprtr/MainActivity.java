package com.example.newsapprtr;

import android.util.Log;
import android.view.*;
import android.widget.*;
import java.util.*;
import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.widget.ViewPager2;


public class MainActivity extends AppCompatActivity {
    HashMap<String, String> color;
    private static HashMap<String, String> hashMap = new HashMap<String, String>();
    private static final String TAG = "MainActivity";
    private Menu menu;
    News news;
    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private static List<NewsArticle> newsArticles = new ArrayList<>();
    NewsAdapter newsAdapter;
    private ArrayAdapter<Sourcenews> arrayAdapter;
    TextView menuItem;
    private ActionBarDrawerToggle drawerToggle;
    List<Sourcenews> sourceList;
    List<Sourcenews> list;
    private ViewPager2 viewPager2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String app_tile = "News Gateway";
        setTitle(app_tile);
        viewPager2 = findViewById(R.id.viewPager2_id);
        drawerLayout = findViewById(R.id.drawer);
        drawerList = findViewById(R.id.drawer_layout);
        Log.d(TAG, "onCreate: Getting the id of the elements...");
        if(hasNetworkConnection()){
            Log.d(TAG, "onCreate: Getting the News Data...");
            Log.d(TAG, "getNewsData: Getting News from various sources...");
            news = JSONParse.getDataFromAPI(this);
            Log.d(TAG, "onCreate: News Data Fetched...");
        }
        else {
            Log.d(TAG, "onCreate: NO INTERNET CONNECTION!");
        }
        drawerList.setOnItemClickListener(
                (parent, view, position, id) -> selectItem(position)
        );

        drawerToggle = new ActionBarDrawerToggle(this,
                drawerLayout, R.string.drawerOpen,R.string.drawerClose);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
        else{
            Log.d(TAG, "onCreate: Cannot be null...");
        }
    }

    public static Boolean isNetworkConnected(Activity activity){
        ConnectivityManager connectivityManager = activity.getSystemService(ConnectivityManager.class);
        NetworkInfo network = connectivityManager.getActiveNetworkInfo();
        Log.d(TAG, "isNetworkConnected: Network data fetched...");
        if(network != null && network.isConnectedOrConnecting()) {
            Log.d(TAG, "isNetworkConnected: Internet Connected...");
            return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        Log.d(TAG, "onCreateOptionsMenu: Getting the Menu Options...");
        return true;
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
        Log.d(TAG, "onConfigurationChanged: Configuration changed...");
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause: Pausing the app state...");
        super.onPause();
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        if(item.getTitle().equals("all")){
            Log.d(TAG, "onOptionsItemSelected: Getting the items in 'all' category updated...");
            updateItemsDrawer(sourceList, item.getTitle().toString());
        }
        else if(item.getTitle() != null){
            List<Sourcenews> updatedList =  JSONParse.updateDataDrawer(item.getTitle().toString());
            String str = item.getTitle().toString();
            updateItemsDrawer(updatedList, str);
        }
        else {
            Log.d(TAG, "onOptionsItemSelected: List has to be updated...");
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume: Resuming the app state...");
        super.onResume();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        Log.d(TAG, "onPostCreate: Syncing the state...");
        drawerToggle.syncState();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        Log.d(TAG, "onSaveInstanceState: Saving the instance state...");
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d(TAG, "onRestoreInstanceState: Restoring the instance state...");
    }

    private void updateItemsDrawer(List<Sourcenews> updateItemsDrawer, String title) {
        list = updateItemsDrawer;
        arrayAdapter = new ArrayAdapter<Sourcenews>(this, android.R.layout.simple_list_item_1, updateItemsDrawer){
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                Sourcenews newsSource = updateItemsDrawer.get(position);
                View view = super.getView(position, convertView, parent);
                menuItem = view.findViewById(android.R.id.text1);
                menuItem.setText(newsSource.getName());
                if(!title.equals("all")){
                    int len = updateItemsDrawer.size();
                    setTitle(updateItemsDrawer.get(0).getCategory() + " (" + len + ")");
                }
                else {
                    String title = "News Gateway";
                    setTitle(title + " " + "(" + updateItemsDrawer.size() + ")" );
                    menuItem.setTextColor(Color.parseColor(color.get(newsSource.getCategory())));
                    if(title == null){
                        Log.d(TAG, "getView: Set Title!");
                    }
                }
                menuItem.setTextColor(Color.parseColor(color.get(newsSource.getCategory())));
                return view;
            }
        };
        drawerList.setAdapter(arrayAdapter);
    }
    private void selectItem(int position) {
        setTitle(list.get(position).getName());
        Log.d(TAG, "selectItem: Title Set!");
        newsArticles= JSONParse.getNewsArticles(list.get(position).getId(), this);
        viewPager2.setCurrentItem(position);
        Log.d(TAG, "selectItem: View Pager set with position...");
        drawerLayout.setBackgroundResource(0);
        Log.d(TAG, "selectItem: Background Resource set...");
        viewPager2.setBackground(null);
        drawerLayout.closeDrawer(drawerList);
        Log.d(TAG, "selectItem: Drawer is being closed...");
        Log.d(TAG, "selectItem: Items selected...");
    }

    private boolean hasNetworkConnection() {
        Log.d(TAG, "hasNetworkConnection: Returning whether there is internet connection or not...");
        return isNetworkConnected(this);
    }

    @Override
    protected void onRestart() {
        Log.d(TAG, "onRestart: Restarting the application...");
        super.onRestart();
    }

    public void updateData(News news) {
        this.news = news;
        sourceList = news.getSourcenews();
        list = sourceList;
        arrayAdapter = new ArrayAdapter<Sourcenews>(this, android.R.layout.simple_list_item_1, this.sourceList){
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                Sourcenews sourcenews = sourceList.get(position);
                View view = super.getView(position, convertView, parent);
                menuItem = view.findViewById(android.R.id.text1);
                menuItem.setText(sourcenews.getName());
                Log.d(TAG, "getView: Getting name...");
                setTitle("News Gateway " + "(" + sourceList.size() + ")");
                Log.d(TAG, "getView: Title Set...");
                menuItem.setTextColor(Color.parseColor(color.get(sourcenews.getCategory())));
                Log.d(TAG, "getView: Setting the text color...");
                if(view == null){
                    Log.d(TAG, "getView: Error in fetching view...");
                }
                return view;
            }
        };
        drawerList.setAdapter(arrayAdapter);
    }

    public void updateMenuItem(HashSet<String> dynamicMenu) {
        color = colorSource(dynamicMenu);
        for (String menu: dynamicMenu) {
            if(!menu.equals("all")){
                SpannableString spannableString = new SpannableString(menu);
                Log.d(TAG, "updateMenuItem: Getting the length and setting the spannable string...");
                int length = spannableString.length();
                spannableString.setSpan(new ForegroundColorSpan(Color.parseColor(color.get(menu))), 0, length, 0);
                Log.d(TAG, "updateMenuItem: Updating the items in the menu and parsing it with color...");
                this.menu.add(spannableString);
            }
            else {
                this.menu.add(menu);
            }
        }
    }

    public void updatePageView(List<NewsArticle> newsArticles) {
        newsAdapter = new NewsAdapter(newsArticles, this);
        Log.d(TAG, "updatePageView: Updating the View Pager...");
        if(newsAdapter == null){
            Log.d(TAG, "updatePageView: newsAdapter shouldn't be null");
        }
        viewPager2.setAdapter(newsAdapter);
    }

    public static HashMap<String, String> colorSource(HashSet<String> dynamicMenu){
        List<String> list = new ArrayList<String>();
        Log.d(TAG, "colorSource: Initializing the colors...");
        list.add("#B50000FF");
        list.add("#F2008B8B");
        list.add("#E97FFF00");
        list.add("#EBDEB887");
        list.add("#F5D2691E");
        list.add("#DC6495ED");
        list.add("#EBDC143C");
        list.add("#DDA52A2A");
        list.add("#DC8A2BE2");
        list.add("#D8FF7F50");
        list.add("#EB5F9EA0");
        int index = 0;
        for(String str : dynamicMenu){
            hashMap.put(str, list.get(index));
            Log.d(TAG, "colorSource: Colors set!");
            index++;
            if(index == 0){
                index++;
            }
        }
        return hashMap;
    }
}