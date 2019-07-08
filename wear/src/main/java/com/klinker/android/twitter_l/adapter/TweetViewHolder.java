package com.klinker.android.twitter_l.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.wear.widget.RoundedDrawable;

import com.klinker.android.twitter_l.R;
import com.klinker.android.twitter_l.activity.WearTransactionActivity;

import java.io.File;

public class TweetViewHolder extends RecyclerView.ViewHolder {

    private TextView txtUserName;
    private TextView txtScreenName;
    private FrameLayout profilePic;
    private TextView txtBody;

    private Handler handler;
    private static final int MAX_IMAGE_ATTEMPTS = 10;

    public TweetViewHolder(@NonNull View itemView) {
        super(itemView);
        txtUserName = (TextView) itemView.findViewById(R.id.name);
        txtScreenName = (TextView) itemView.findViewById(R.id.screenname);
        profilePic = (FrameLayout) itemView.findViewById(R.id.profile_picture);
        txtBody = (TextView) itemView.findViewById(R.id.text);

        handler = new Handler();

    }

    /**
     * Append normal size variant modifier to the image URL, more info at
     * https://developer.twitter.com/en/docs/accounts-and-users/user-profile-images-and-banners.html
     * @param originalUrl
     * @return the url to download the normal size image
     */
    private String getNormalSizeImageUrl(String originalUrl) {
        return originalUrl.replaceAll(".png","_normal.png");
    }

    public void onBind(String userName, String screenName, String body, String profilePicUrl) {
        this.txtUserName.setText(userName);
        this.txtScreenName.setText(screenName);
        this.txtBody.setText(body);

        loadImage(getNormalSizeImageUrl(profilePicUrl));
    }

    private void loadImage(String imageUrl) {
        Thread loader = new Thread(new Runnable() {
            @Override
            public void run() {
                if (itemView.getContext() == null) {
                    return;
                }

                File image = new File(itemView.getContext().getCacheDir(), imageUrl);
                checkExisting(image, profilePic, 0, imageUrl);
            }
        });
        loader.setPriority(Thread.MIN_PRIORITY);
        loader.start();
    }

    public void checkExisting(File f, final FrameLayout profilePic, int attempts, String imageUrl) {
        if (f.exists()) {
            try {
                Bitmap image = BitmapFactory.decodeFile(f.getPath());
                final BitmapDrawable drawable = new BitmapDrawable(itemView.getResources(), image);
                final RoundedDrawable roundedDrawable = new RoundedDrawable();
                roundedDrawable.setClipEnabled(true);
                roundedDrawable.setDrawable(drawable);
                float radius = itemView.getResources().getDimension(R.dimen.profile_pic_radius);
                roundedDrawable.setRadius((int) radius);

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        profilePic.setBackground(roundedDrawable);
                    }
                });
            } catch (Exception e) {
                // the card has scrolled down, we do this on the initial opening of the app,
                // so that fragment is gone and we can't create the handler..

                // the problem really lies in the incomplete methods for the gridviewpager.
                // this force close comes from their end because of my silly workaround for their
                // broken methods haha
            }
        } else {
            if (attempts == 0) {
                ((WearTransactionActivity) itemView.getContext()).sendImageRequest(imageUrl);
            }

            try {
                Thread.sleep(500);
            } catch (Exception e) {
            }

            if (attempts < MAX_IMAGE_ATTEMPTS) {
                checkExisting(f, profilePic, attempts + 1, imageUrl);
            }
        }
    }
}
