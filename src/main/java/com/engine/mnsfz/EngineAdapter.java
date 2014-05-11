package com.engine.mnsfz;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by USER on 13-7-9.
 */
public abstract class  EngineAdapter<T> extends BaseAdapter {
    protected Context mContext ;
    List<T> list ;

    protected EngineAdapter(Context mContext, List<T> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public T getItem(int i) {
        return list.get(i) ;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public abstract View getView(int i, View view, ViewGroup viewGroup);
}
