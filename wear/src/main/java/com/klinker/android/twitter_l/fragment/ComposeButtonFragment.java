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

public class ComposeButtonFragment extends Fragment {

    public static ComposeButtonFragment create() {
        return new ComposeButtonFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_button_and_text, parent, false);
        MyFrameLayout button = view.findViewById(R.id.button);
        ImageView imageView = view.findViewById(R.id.image);
        TextView textView = view.findViewById(R.id.text);

        button.updateBackgroundDrawableColor();
        imageView.setImageResource(R.drawable.ic_wear_compose);
        textView.setText(R.string.settings);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((WearActivity) getActivity()).startComposeRequest();
            }
        });
        return view;
    }

}
