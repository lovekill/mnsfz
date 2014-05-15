package com.engine.mnsfz.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.engine.mnsfz.*;
import com.engine.mnsfz.greendao.ImageBean;
import com.engine.mnsfz.jsoup.ImageType;
import com.engine.mnsfz.jsoup.NetFech;
import com.engine.mnsfz.jsoup.Page;
import com.engine.mnsfz.util.ImageCache;
import com.engine.mnsfz.util.ImageFetcher;
import com.engine.mnsfz.util.LogUtil;
import com.etsy.android.grid.StaggeredGridView;

import java.io.IOException;
import java.util.List;
import java.util.Random;

/**
 * Created by USER on 2014/5/10.
 */
public class ContentFragment extends BaseFragment implements View.OnClickListener,AdapterView.OnItemClickListener{
    private static final String TAG = "ImageGridActivity";
    private static final int GET_PAGE = 1;
    private static final int IO_EXCEPTION = 2;
    private static final String IMAGE_CACHE_DIR = "thumbs";
    private static final String IMAGE_DATA_EXTRA = "extra_image_data";
    private int mImageThumbSize;
    private int mImageThumbSpacing;
    private ImageFetcher mImageFetcher;
    private StaggeredGridView gridView ;
    private GridAdapter mAdapter;
//    private ViewPager mPager;
    public static final String EXTRA_IMAGE = "extra_image";

    List<Page> list = null;
    private ImageView backBtn;
    private TextView titleText;
    private NetFech fech;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GET_PAGE:
//                    initAdapter1();
                    break;
                case IO_EXCEPTION:
                    Toast.makeText(getActivity(), getString(R.string.net_error), Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    public NetFech getFech() {
        return fech;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_index,container,false) ;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        backBtn = (ImageView) view.findViewById(R.id.back);
        backBtn.setOnClickListener(this);
        titleText = (TextView) view.findViewById(R.id.titleText);
        gridView = (StaggeredGridView) view.findViewById(R.id.grid_view);
        gridView.setOnItemClickListener(this);
        ((IndexActivity)getActivity()).setFragment(this);
        initImageFech();
        initAdapter1();
       fechData(3);
        fechData(1);
        fechData(2);

    }
    public void fechData(int i){
        switch (i) {
            case 1:
                fech = new NetFech(ImageType.QINCUN);
                break;
            case 2:
                fech = new NetFech(ImageType.SUNMM);
                break;
            case 3:
                fech = new NetFech(ImageType.QIAOPI);
                break;
            default:
                fech = new NetFech(ImageType.QIAOPI);
                break;
        }
        asyPageList();
    }
    private void  initImageFech(){
         mImageThumbSize = getResources().getDimensionPixelSize(R.dimen.image_thumbnail_size);
        mImageThumbSpacing = getResources().getDimensionPixelSize(R.dimen.image_thumbnail_spacing);

        ImageCache.ImageCacheParams cacheParams = new ImageCache.ImageCacheParams(getActivity(), IMAGE_CACHE_DIR);

        cacheParams.setMemCacheSizePercent(0.25f); // Set memory cache to 25% of app memory

        // The ImageFetcher takes care of loading images into our ImageView children asynchronously
        mImageFetcher = new ImageFetcher(getActivity(), mImageThumbSize);
        mImageFetcher.setLoadingImage(R.drawable.empty_photo);
        mImageFetcher.addImageCache(getActivity().getSupportFragmentManager(), cacheParams);
    }
    private  void initAdapter1(){
        List<ImageBean> imageBeans = DaoManager.getDaoSession().getImageBeanDao().queryBuilder().list();
        if(imageBeans!=null&&imageBeans.size()>0) {
            mAdapter = new GridAdapter(getActivity(), imageBeans, mImageFetcher);
            gridView.setAdapter(mAdapter);
        }
    }
    private void asyPageList() {
        new Thread() {
            @Override
            public void run() {
                try {
                    list = fech.getPage();
                    sendMessage(GET_PAGE,null);
                } catch (IOException e) {
                    e.printStackTrace();
                    sendMessage(IO_EXCEPTION,null);
                }
            }
        }.start();
    }
    @Override
    public void onClick(View view) {
        ((IndexActivity)getActivity()).showMenu();
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        String url = mAdapter.getItem(i).getHref();
        Intent intent = new Intent(getActivity(), ImageDetailActivity.class);
        intent.putExtra("srcUrl", url);
        intent.putExtra("type",Integer.parseInt(fech.getmType().toString())) ;
        intent.putExtra("title",mAdapter.getItem(i).getTitle()) ;
        getActivity().startActivity(intent);
    }
    private void sendMessage(int what, Object o) {
        Message message = new Message();
        message.what = what;
        message.obj = o;
        handler.sendMessage(message);
    }


}
