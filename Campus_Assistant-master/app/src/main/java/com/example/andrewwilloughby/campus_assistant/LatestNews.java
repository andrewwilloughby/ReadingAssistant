package com.example.andrewwilloughby.campus_assistant;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.TimelineResult;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.twitter.sdk.android.tweetui.UserTimeline;

import io.fabric.sdk.android.Fabric;

public class LatestNews extends AppCompatActivity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "icXPlnJKZL9eTOpDgtmSOklhi";
    private static final String TWITTER_SECRET = "ObkVdHNrCOFUvarHzS0OWvHkwCsCSefDpInYlJdGk2jVvnBima";

    private Button uorButton;
    private Button rusuButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_latest_news);

        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));

        final Context context = this;

        setTitle("Latest News");

        uorButton = (Button) findViewById(R.id.uniTweetsBtn);
        uorButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (isNetworkAvailable()) {
                    final UserTimeline userTimeline = new UserTimeline.Builder()
                            .screenName("UniofReading")
                            .build();
                    final TweetTimelineListAdapter adapter = new TweetTimelineListAdapter.Builder(context)
                            .setTimeline(userTimeline)
                            .build();

                    ListView listView = (ListView) findViewById(R.id.tweetsListView);
                    listView.setAdapter(adapter);
                } else {
                    Toast.makeText(context, "Internet connection has been lost.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        rusuButton = (Button) findViewById(R.id.rusuTweetsBtn);
        rusuButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (isNetworkAvailable()) {
                    final UserTimeline userTimeline = new UserTimeline.Builder()
                            .screenName("RUSUtweets")
                            .build();
                    final TweetTimelineListAdapter adapter = new TweetTimelineListAdapter.Builder(context)
                            .setTimeline(userTimeline)
                            .build();

                    ListView listView = (ListView) findViewById(R.id.tweetsListView);
                    listView.setAdapter(adapter);
                } else {
                    Toast.makeText(context, "Internet connection has been lost.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        final SwipeRefreshLayout swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_layout);

        final UserTimeline userTimeline = new UserTimeline.Builder()
                .screenName("UniofReading")
                .build();
        final TweetTimelineListAdapter adapter = new TweetTimelineListAdapter.Builder(this)
                .setTimeline(userTimeline)
                .build();

        ListView listView = (ListView) findViewById(R.id.tweetsListView);
        listView.setAdapter(adapter);

        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeLayout.setRefreshing(true);
                adapter.refresh(new Callback<TimelineResult<Tweet>>() {
                    @Override
                    public void success(Result<TimelineResult<Tweet>> result) {
                        swipeLayout.setRefreshing(false);
                    }

                    @Override
                    public void failure(TwitterException exception) {
                        Toast.makeText(getApplicationContext(), "Failed to refresh Twitter feed", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    protected boolean isNetworkAvailable() {

        // Simple, but important, check for an active network connection.
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        // Returns true or false indicating whether network is available.
        if (null != activeNetworkInfo){
            return true;
        } else {
            return false;
        }
    }
}
