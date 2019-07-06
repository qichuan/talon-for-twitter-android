package com.klinker.android.twitter_l.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.recyclerview.widget.RecyclerView;

import com.klinker.android.twitter_l.R;
import com.klinker.android.twitter_l.view.MyFrameLayout;

public class ButtonTextViewHolder extends RecyclerView.ViewHolder {

    private MyFrameLayout button;
    private ImageView imageView;
    private TextView textView;

    public ButtonTextViewHolder(@NonNull View view) {
        super(view);
        this.button = view.findViewById(R.id.button);
        this.imageView = view.findViewById(R.id.image);
        this.textView = view.findViewById(R.id.text);
    }

    public void onBind(@DrawableRes int imageRes,
                       @StringRes int textRes,
                       View.OnClickListener onClickListener) {
        button.updateBackgroundDrawableColor();
        imageView.setImageResource(imageRes);
        textView.setText(textRes);
        button.setOnClickListener(onClickListener);
    }
}
