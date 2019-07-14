/*
 * Copyright 2014 Luke Klinker
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.klinker.android.twitter_l.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.TextView;

import com.klinker.android.twitter_l.R;
import com.klinker.android.twitter_l.adapter.RecyclerViewAdapter;
import com.klinker.android.twitter_l.transaction.KeyProperties;
import com.klinker.android.twitter_l.view.CircularProgressBar;
import com.klinker.android.twitter_l.view.MyRecyclerViewLayoutCallback;
import com.klinker.android.twitter_l.view.MyRecyclerViewOnScrollerListener;
import com.klinker.android.twitter_l.view.OnSnapPositionChangeListener;

import java.util.List;

import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.SnapHelper;
import androidx.wear.widget.WearableLinearLayoutManager;
import androidx.wear.widget.WearableRecyclerView;

public class WearActivity extends WearTransactionActivity {

    private static final String TAG = "WearActivity";

    private WearableRecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private CircularProgressBar progressBar;
    private TextView emptyView;

    private int primaryColor;
    private int accentColor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_wear);
        recyclerView = (WearableRecyclerView) findViewById(R.id.recycler_view);
        adapter = new RecyclerViewAdapter(this);
        recyclerView.setAdapter(adapter);

        progressBar = (CircularProgressBar) findViewById(R.id.progress_bar);
        emptyView = (TextView) findViewById(R.id.empty_view);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        accentColor = sharedPreferences.getInt(KeyProperties.KEY_ACCENT_COLOR, getResources().getColor(R.color.orange_accent_color));
        primaryColor = sharedPreferences.getInt(KeyProperties.KEY_PRIMARY_COLOR, getResources().getColor(R.color.orange_primary_color));
        progressBar.setColor(accentColor);
        recyclerView.setBackgroundColor(primaryColor);

        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);

        recyclerView.addOnScrollListener(new MyRecyclerViewOnScrollerListener(snapHelper,
            new OnSnapPositionChangeListener() {
                @Override
                public void onSnapPositionChange(int position) {
                    if (position != -1) {
                        sendReadStatus(getIds().get(position));
                    }
                }
            }));
    }

    @Override
    public void updateDisplay() {
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        if (getNames().isEmpty()) {
            emptyView.setVisibility(View.VISIBLE);
        } else {
            emptyView.setVisibility(View.INVISIBLE);
        }
        adapter = new RecyclerViewAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(false);
        recyclerView.setEdgeItemsCenteringEnabled(true);
        recyclerView.setLayoutManager(new WearableLinearLayoutManager(this,
            new MyRecyclerViewLayoutCallback()));
    }

    private static final int COMPOSE_REQUEST_CODE = 101;
    private static final int REPLY_REQUEST_CODE = 102;

    public void startComposeRequest() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        startActivityForResult(intent, COMPOSE_REQUEST_CODE);
    }

    private String replyingToScreenname = "";
    private long replyingToTweetId = 0l;
    public void startReplyRequest(String screenname, long tweetId) {
        this.replyingToScreenname = screenname;
        this.replyingToTweetId = tweetId;

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        startActivityForResult(intent, REPLY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == COMPOSE_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            String spokenText = results.get(0);

            sendComposeRequest(spokenText);
        } else if (requestCode == REPLY_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            String spokenText = results.get(0);

            sendReplyRequest(replyingToTweetId, replyingToScreenname, spokenText);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

}
