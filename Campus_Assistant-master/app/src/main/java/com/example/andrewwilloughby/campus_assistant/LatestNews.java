package com.example.andrewwilloughby.campus_assistant;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.support.v4.widget.SwipeRefreshLayout;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.TimelineResult;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.twitter.sdk.android.tweetui.UserTimeline;

public class LatestNews extends AppCompatActivity {

    private Button uorButton;
    private Button rusuButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_latest_news);

        final Context ctx = this;

        setTitle("Latest News");

        if (!isInternetConnected(getApplicationContext())) {
            Toast.makeText(getApplicationContext(), "No network connection deteced", Toast.LENGTH_LONG).show();
        }

        uorButton = (Button) findViewById(R.id.uniTweetsBtn);
        uorButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                final UserTimeline userTimeline = new UserTimeline.Builder()
                        .screenName("UniofReading")
                        .build();
                final TweetTimelineListAdapter adapter = new TweetTimelineListAdapter.Builder(ctx)
                        .setTimeline(userTimeline)
                        .build();

                ListView listView = (ListView) findViewById(R.id.listView);
                listView.setAdapter(adapter);
            }
        });

        rusuButton = (Button) findViewById(R.id.rusuTweetsBtn);
        rusuButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                final UserTimeline userTimeline = new UserTimeline.Builder()
                        .screenName("RUSUtweets")
                        .build();
                final TweetTimelineListAdapter adapter = new TweetTimelineListAdapter.Builder(ctx)
                        .setTimeline(userTimeline)
                        .build();

                ListView listView = (ListView) findViewById(R.id.listView);
                listView.setAdapter(adapter);
            }
        });

        final SwipeRefreshLayout swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_layout);

        final UserTimeline userTimeline = new UserTimeline.Builder()
                .screenName("UniofReading")
                .build();
        final TweetTimelineListAdapter adapter = new TweetTimelineListAdapter.Builder(this)
                .setTimeline(userTimeline)
                .build();

        ListView listView = (ListView) findViewById(R.id.listView);
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

    private boolean isInternetConnected(Context applicationContext){
        ConnectivityManager connection = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connection.getActiveNetworkInfo();
        if (info == null) {
            Toast.makeText(getApplicationContext(), "No network connection detected", Toast.LENGTH_LONG).show();
            return false;
        } else
            return true;

    }
}
