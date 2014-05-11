package com.engine.mnsfz;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.engine.mnsfz.fragment.ui.RecyclingImageView;
import com.engine.mnsfz.jsoup.IndexBean;
import com.engine.mnsfz.util.ImageFetcher;
import com.engine.mnsfz.util.LogUtil;

import java.util.List;

/**
 * Created by USER on 13-7-9.
 */
public class GridAdapter extends EngineAdapter<IndexBean> {
    private ImageFetcher mImageFetcher ;
    protected GridAdapter(Context mContext, List<IndexBean> list) {
        super(mContext, list);
    }

    public GridAdapter(Context mContext, List<IndexBean> list, ImageFetcher mImageFetcher) {
        super(mContext, list);
        this.mImageFetcher = mImageFetcher;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view==null){
           view= LayoutInflater.from(mContext).inflate(R.layout.grid_image_item,null) ;
        }
        ImageView imageView = (ImageView) view.findViewById(R.id.image);
        TextView text = (TextView) view.findViewById(R.id.name);
    //    text.setText(getItem(i).getTitle());
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        mImageFetcher.loadImage(getItem(i).getSrc(),imageView);
        return view;
    }
}
