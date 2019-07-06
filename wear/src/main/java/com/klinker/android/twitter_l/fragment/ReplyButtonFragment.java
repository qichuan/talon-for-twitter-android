package com.klinker.android.twitter_l.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.klinker.android.twitter_l.R;
import com.klinker.android.twitter_l.activity.WearActivity;
import com.klinker.android.twitter_l.view.MyFrameLayout;

public class ReplyButtonFragment extends Fragment {

    private static final String ARG_TWEET_ID = "tweet_id";
    private static final String ARG_USER_SCREENNAME = "screenname";

    public static ReplyButtonFragment create(long tweetId, String screenname) {
        Bundle args = new Bundle();
        args.putLong(ARG_TWEET_ID, tweetId);
        args.putString(ARG_USER_SCREENNAME, screenname);

        ReplyButtonFragment frag = new ReplyButtonFragment();
        frag.setArguments(args);

        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_button_and_text, parent, false);
        MyFrameLayout button = view.findViewById(R.id.button);
        ImageView imageView = view.findViewById(R.id.image);
        TextView textView = view.findViewById(R.id.text);

        button.updateBackgroundDrawableColor();
        imageView.setImageResource(R.drawable.ic_wear_reply);
        textView.setText(R.string.reply);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((WearActivity) getActivity()).startReplyRequest(
                        getArguments().getString(ARG_USER_SCREENNAME),
                        getArguments().getLong(ARG_TWEET_ID)
                );
            }
        });

        return view;
    }

}
