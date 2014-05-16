package com.engine.mnsfz.fragment.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
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
        if(getRefreshableView().getAdapter()==null) return  false ;
        if(getRefreshableView().getLastVisiblePosition()==getRefreshableView().getAdapter().getCount()-1) {
            return true;
        }else {
            return  false ;
        }
    }

    @Override
    protected boolean isReadyForPullStart() {
        if(getRefreshableView().getFirstVisiblePosition()==0) {
            return true;
        }else{
            return false ;
        }
    }
}
