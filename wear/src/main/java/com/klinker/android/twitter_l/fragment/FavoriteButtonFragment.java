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

public class FavoriteButtonFragment extends Fragment {

    private static final String ARG_TWEET_ID = "tweet_id";

    public static FavoriteButtonFragment create(long id) {
        Bundle args = new Bundle();
        args.putLong(ARG_TWEET_ID, id);

        FavoriteButtonFragment frag = new FavoriteButtonFragment();
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
        imageView.setImageResource(R.drawable.ic_wear_favorite);
        textView.setText(R.string.favorite);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((WearActivity)getActivity()).sendFavoriteRequest(getArguments().getLong(ARG_TWEET_ID));
            }
        });

        return view;
    }

}
