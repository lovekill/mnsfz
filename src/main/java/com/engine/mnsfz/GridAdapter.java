package com.engine.mnsfz;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.engine.mnsfz.fragment.ui.RecyclingImageView;
import com.engine.mnsfz.greendao.ImageBean;
import com.engine.mnsfz.jsoup.IndexBean;
import com.engine.mnsfz.util.ImageFetcher;
import com.engine.mnsfz.util.LogUtil;
import com.etsy.android.grid.util.DynamicHeightImageView;

import java.util.List;
import java.util.Random;

/**
 * Created by USER on 13-7-9.
 */
public class GridAdapter extends EngineAdapter<ImageBean> {
    private ImageFetcher mImageFetcher ;
    private Random mRandom ;
    private static final SparseArray<Double> sPositionHeightRatios = new SparseArray<Double>();
    protected GridAdapter(Context mContext, List<ImageBean> list) {
        super(mContext, list);
        mRandom = new Random();
    }

    public GridAdapter(Context mContext, List<ImageBean> list, ImageFetcher mImageFetcher) {
        super(mContext, list);
        this.mImageFetcher = mImageFetcher;
        mRandom = new Random();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LogUtil.e(getClass(),"getView-->"+i+"===>"+getCount());
        if(view==null){
           view= LayoutInflater.from(mContext).inflate(R.layout.grid_image_item,null) ;
        }
        double positionHeight = getPositionRatio(i);
        DynamicHeightImageView imageView = (DynamicHeightImageView) view.findViewById(R.id.image);
        imageView.setHeightRatio(positionHeight);
        TextView text = (TextView) view.findViewById(R.id.name);
        text.setText(getItem(i).getTitle());
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        mImageFetcher.loadImage(getItem(i).getSrc(),imageView);
        return view;
    }
    private double getRandomHeightRatio() {
        return (mRandom.nextDouble() / 2.0) + 1.0; // height will be 1.0 - 1.5 the width
    }

    private double getPositionRatio(final int position) {
        double ratio = sPositionHeightRatios.get(position, 0.0);
        // if not yet done generate and stash the columns height
        // in our real world scenario this will be determined by
        // some match based on the known height and width of the image
        // and maybe a helpful way to get the column height!
        if (ratio == 0) {
            ratio = getRandomHeightRatio();
            sPositionHeightRatios.append(position, ratio);
        }
        return ratio;
    }

}
