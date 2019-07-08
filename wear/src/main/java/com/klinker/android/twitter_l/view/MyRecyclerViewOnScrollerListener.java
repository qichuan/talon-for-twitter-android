package com.klinker.android.twitter_l.view;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

/**
 * @author sean.zhang Created on 2019-07-08.
 */
public class MyRecyclerViewOnScrollerListener extends RecyclerView.OnScrollListener {

    private SnapHelper snapHelper;
    private OnSnapPositionChangeListener onSnapPositionChangeListener;
    private int snapPosition = RecyclerView.NO_POSITION;

    public MyRecyclerViewOnScrollerListener(SnapHelper snapHelper,
                                            OnSnapPositionChangeListener onSnapPositionChangeListener) {
        this.snapHelper = snapHelper;
        this.onSnapPositionChangeListener = onSnapPositionChangeListener;
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
    }

    @Override
    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
        int snapPosition = getSnapPosition(recyclerView);
        boolean snapPositionChanged = this.snapPosition != snapPosition;
        if (snapPositionChanged) {
            onSnapPositionChangeListener
                .onSnapPositionChange(snapPosition);
            this.snapPosition = snapPosition;
        }
    }

    private int getSnapPosition(RecyclerView recyclerView) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();

        if (layoutManager == null) {
            return RecyclerView.NO_POSITION;
        }

        View snapView = snapHelper.findSnapView(layoutManager);
        if (snapView == null) {
            return RecyclerView.NO_POSITION;
        }
        return layoutManager.getPosition(snapView);
    }
}
