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

package com.klinker.android.twitter_l.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.klinker.android.twitter_l.R;
import com.klinker.android.twitter_l.activity.SettingsActivity;
import com.klinker.android.twitter_l.activity.WearActivity;
import com.klinker.android.twitter_l.activity.WearTransactionActivity;
import com.klinker.android.twitter_l.transaction.KeyProperties;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private WearTransactionActivity context;

    private static final int VIEWTYPE_TWEET = 1;
    private static final int VIEWTYPE_COMPOSE = 2;
    private static final int VIEWTYPE_SETTINGS = 3;

    public RecyclerViewAdapter(WearTransactionActivity context) {
        this.context = context;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEWTYPE_SETTINGS || viewType == VIEWTYPE_COMPOSE) {
            View view = LayoutInflater
                    .from(context).inflate(R.layout.view_button_and_text,
                            parent, false);
            return new ButtonTextViewHolder(view);
        } else if (viewType == VIEWTYPE_TWEET) {
            View view = LayoutInflater
                    .from(context).inflate(R.layout.view_tweet,
                            parent, false);
            return new TweetViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ButtonTextViewHolder) {
            // Settings button
            if (position == 0) {
                ButtonTextViewHolder settingsButtonViewHolder = (ButtonTextViewHolder) holder;
                settingsButtonViewHolder.onBind(
                        R.drawable.ic_settings,
                        R.string.settings,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(context, SettingsActivity.class);
                                context.startActivity(intent);
                            }
                        });
                // Compose
            } else if (position == 1) {
                ButtonTextViewHolder composeButtonViewHolder = (ButtonTextViewHolder) holder;
                composeButtonViewHolder.onBind(
                        R.drawable.ic_wear_compose,
                        R.string.compose,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ((WearActivity) context).startComposeRequest();
                            }
                        });
            }
        }
        if (holder instanceof TweetViewHolder) {
            TweetViewHolder tweetViewHolder = (TweetViewHolder) holder;
            String text = context.getBodies().get(position).replace(KeyProperties.LINE_BREAK, "\n\n");
            if (text.endsWith("\n\n")) {
                text = text.substring(0, text.length() - 2);
            }
            String bodyText = "";
            String[] info = text.split(KeyProperties.DIVIDER);
            if (info.length > 1) {
                bodyText = info[1];
            }
            String profilePicUrl = info[0];
            String userName = context.getNames().get(position);
            String screenName = context.getScreennames().get(position);

            tweetViewHolder.onBind(userName, screenName, bodyText, profilePicUrl);
        }
    }

    @Override
    public int getItemCount() {
        if (context.getNames() != null && context.getNames().size() != 0) {
            return context.getNames().size() + 4;
        } else {
            return 3;
        }
    }

    @Override
    public int getItemViewType(int position) {
        int itemCount = getItemCount();
        if (position != itemCount - 1 &&
                position != itemCount - 2 &&
                position != 0 &&
                position != 1) {
            return VIEWTYPE_TWEET;
        } else if (position == 0) {
            return VIEWTYPE_SETTINGS;
        } else if (position == 1) {
            return VIEWTYPE_COMPOSE;
        }
        return VIEWTYPE_TWEET;

    }
}
