package com.klinker.android.twitter_l.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.klinker.android.twitter_l.R;
import com.klinker.android.twitter_l.transaction.KeyProperties;

public class MyFrameLayout extends FrameLayout {
    public MyFrameLayout(@NonNull Context context) {
        super(context);
    }

    public MyFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MyFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void updateBackgroundDrawableColor() {
        if (getBackground() instanceof GradientDrawable) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            int accentColor = sharedPreferences.getInt(KeyProperties.KEY_ACCENT_COLOR,
                    ContextCompat.getColor(getContext(), R.color.orange_accent_color));
            GradientDrawable drawable = (GradientDrawable) getBackground();
            drawable.setColor(accentColor);
        }
    }
}
