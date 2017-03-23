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

/**
 * Activity for latest Tweets from UoR and RUSU Twitter accounts.
 *
 * @author Andrew Willoughby
 */
public class LatestNewsActivity extends AppCompatActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener{

    private static final String TWITTER_KEY = "icXPlnJKZL9eTOpDgtmSOklhi";
    private static final String TWITTER_SECRET = "ObkVdHNrCOFUvarHzS0OWvHkwCsCSefDpInYlJdGk2jVvnBima";
    Context context;
    SwipeRefreshLayout swipeLayout;
    TweetTimelineListAdapter adapter;

    /**
     * Method to set up the Activity upon creation.
     *
     * @param savedInstanceState parameter which indicates the previous state of the activity.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.latest_news);

        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));

        context = this;

        setTitle("Latest News");

        final UserTimeline userTimeline = new UserTimeline.Builder().screenName("UniofReading").build();
        adapter = new TweetTimelineListAdapter.Builder(this).setTimeline(userTimeline).build();
        ListView listView = (ListView) findViewById(R.id.tweetsListView);
        listView.setAdapter(adapter);

        Button uorButton = (Button) findViewById(R.id.uniTweetsBtn);
        uorButton.setOnClickListener(this);

        Button rusuButton = (Button) findViewById(R.id.rusuTweetsBtn);
        rusuButton.setOnClickListener(this);

        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_layout);
        swipeLayout.setOnRefreshListener(this);
    }

    /**
     * Method to handle onClick events from buttons displayed in the activity.
     *
     * @param viewInput the view initiating the onClick method.
     */
    public void onClick(View viewInput){
        if (isNetworkAvailable()) {
            switch (viewInput.getId()) {
                case R.id.uniTweetsBtn: {

                    final UserTimeline userTimeline = new UserTimeline.Builder()
                            .screenName("UniofReading")
                            .build();
                    final TweetTimelineListAdapter adapter = new TweetTimelineListAdapter.Builder(context)
                            .setTimeline(userTimeline)
                            .build();

                    ListView listView = (ListView) findViewById(R.id.tweetsListView);
                    listView.setAdapter(adapter);
                }
                break;

                case R.id.rusuTweetsBtn: {

                    final UserTimeline userTimeline = new UserTimeline.Builder()
                            .screenName("RUSUtweets")
                            .build();
                    final TweetTimelineListAdapter adapter = new TweetTimelineListAdapter.Builder(context)
                            .setTimeline(userTimeline)
                            .build();

                    ListView listView = (ListView) findViewById(R.id.tweetsListView);
                    listView.setAdapter(adapter);
                    break;
                }
            }
        } else{
            Toast.makeText(getApplicationContext(), "Internet connection has been lost.", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Method to handle the onRefresh events from the swipe layout.
     */
    public void onRefresh() {
        swipeLayout.setRefreshing(true);
        adapter.refresh(new Callback<TimelineResult<Tweet>>() {
            @Override
            public void success(Result<TimelineResult<Tweet>> result) {
                swipeLayout.setRefreshing(false);
            }

            @Override
            public void failure(TwitterException exception) {
                Toast.makeText(getApplicationContext(), "Failed to refresh Twitter feed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Method that checks the availability of an active network connection.
     *
     * @return boolean value to indicate availability of network.
     */
    protected boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        if (null != activeNetworkInfo){
            return true;
        }

        return false;
    }
}
