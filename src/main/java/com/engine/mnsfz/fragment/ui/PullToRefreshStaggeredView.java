package com.engine.mnsfz.fragment.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import com.engine.mnsfz.util.LogUtil;
import com.etsy.android.grid.StaggeredGridView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;

/**
 * Created by Administrator on 14-5-16.
 */
public class PullToRefreshStaggeredView extends PullToRefreshBase<StaggeredGridView>{
    public PullToRefreshStaggeredView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public Orientation getPullToRefreshScrollDirection() {
        return Orientation.VERTICAL;
    }

    @Override
    protected StaggeredGridView createRefreshableView(Context context, AttributeSet attrs) {
        return new StaggeredGridView(context,attrs);
    }

    @Override
    protected boolean isReadyForPullEnd() {
        return isLastItemVisible() ;
    }

    @Override
    protected boolean isReadyForPullStart() {
        if (getRefreshableView().getFirstVisiblePosition() <= 1) {
            final View firstVisibleChild = getRefreshableView().getChildAt(0);
            if (firstVisibleChild != null) {
                return firstVisibleChild.getTop() >= getRefreshableView().getTop();
            }
        }
        return false ;
    }
    private boolean isLastItemVisible() {
        final Adapter adapter = getRefreshableView().getAdapter();

        if (null == adapter || adapter.isEmpty()) {
            return true;
        } else {
            final int lastItemPosition = getRefreshableView().getCount() - 1;
            final int lastVisiblePosition = getRefreshableView().getLastVisiblePosition();

            /**
             * This check should really just be: lastVisiblePosition ==
             * lastItemPosition, but PtRListView internally uses a FooterView
             * which messes the positions up. For me we'll just subtract one to
             * account for it and rely on the inner condition which checks
             * getBottom().
             */
            if (lastVisiblePosition >= lastItemPosition - 1) {
                final int childIndex = lastVisiblePosition - getRefreshableView().getFirstVisiblePosition();
                final View lastVisibleChild = getRefreshableView().getChildAt(childIndex);
                if (lastVisibleChild != null) {
                    return lastVisibleChild.getBottom() <= getRefreshableView().getBottom();
                }
            }
        }

        return false;
    }
}
