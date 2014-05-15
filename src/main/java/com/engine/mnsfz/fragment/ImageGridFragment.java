/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.engine.mnsfz.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.engine.mnsfz.*;
import com.engine.mnsfz.greendao.ImageBean;
import com.engine.mnsfz.jsoup.ImageType;
import com.engine.mnsfz.jsoup.IndexBean;
import com.engine.mnsfz.jsoup.NetFech;
import com.engine.mnsfz.jsoup.QiaoPi;
import com.engine.mnsfz.util.ImageCache;
import com.engine.mnsfz.util.ImageFetcher;

import java.io.IOException;
import java.util.List;

/**
 * The main fragment that powers the ImageGridActivity screen. Fairly straight forward GridView
 * implementation with the key addition being the ImageWorker class w/ImageCache to load children
 * asynchronously, keeping the UI nice and smooth and caching thumbnails for quick retrieval. The
 * cache is retained over configuration changes like orientation change so the images are populated
 * quickly if, for example, the user rotates the device.
 */
public class ImageGridFragment extends Fragment implements AdapterView.OnItemClickListener {
    private static final String TAG = "ImageGridFragment";
    private static final String IMAGE_CACHE_DIR = "thumbs";
    private static final String IMAGE_DATA_EXTRA = "extra_image_data";
    private int mImageThumbSize;
    private int mImageThumbSpacing;
    private GridAdapter mAdapter;
    private ImageFetcher mImageFetcher;
    private List<IndexBean> list;
   private IndexActivity activity ;
    private NetFech fech ;
    private GridView mGridView;
    private  String pageUrl ;
    private static final int GET_PAGE = 1;
    private static final int IO_EXCEPTION = 2;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GET_PAGE:
                    initAdapter();
                    break;
                case IO_EXCEPTION:
                    Toast.makeText(getActivity(), getString(R.string.net_error), Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    public ImageGridFragment() {
    }

    public ImageGridFragment(String pageUrl,NetFech fech) {
        final Bundle args = new Bundle();
        args.putString(IMAGE_DATA_EXTRA, pageUrl);
        this.pageUrl=pageUrl ;
        setArguments(args);
//        QiaoPi qiaopi = new QiaoPi();
        this.fech=fech;
//        try {
//            list=fech.printMainImage(pageUrl) ;
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
       // asyPageList(pageUrl);
    }

//    public static ImageGridFragment newInstance(String imageUrl) {
//        final ImageGridFragment f = new ImageGridFragment(imageUrl);
//
//
//        return f;
//    }

    /**
     * Empty constructor as per the Fragment documentation
     */
    private void asyPageList(final String pageUrl) {
        new Thread() {
            @Override
            public void run() {
                try {
                    list=fech.printMainImage(pageUrl) ;
                    sendMessage(GET_PAGE,null);
                } catch (IOException e) {
                    e.printStackTrace();
                    sendMessage(IO_EXCEPTION,null);
                }
            }
        }.start();
    }
    private void sendMessage(int what, Object o) {
        Message message = new Message();
        message.what = what;
        message.obj = o;
        handler.sendMessage(message);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mImageThumbSize = getResources().getDimensionPixelSize(R.dimen.image_thumbnail_size);
        mImageThumbSpacing = getResources().getDimensionPixelSize(R.dimen.image_thumbnail_spacing);

        ImageCache.ImageCacheParams cacheParams = new ImageCache.ImageCacheParams(getActivity(), IMAGE_CACHE_DIR);

        cacheParams.setMemCacheSizePercent(0.25f); // Set memory cache to 25% of app memory

        // The ImageFetcher takes care of loading images into our ImageView children asynchronously
        mImageFetcher = new ImageFetcher(getActivity(), mImageThumbSize);
        mImageFetcher.setLoadingImage(R.drawable.empty_photo);
        mImageFetcher.addImageCache(getActivity().getSupportFragmentManager(), cacheParams);
    }

    private  void initAdapter(){
        List<ImageBean> imageBeans = DaoManager.getDaoSession().getImageBeanDao().queryBuilder().limit(10).list();
        if(imageBeans!=null&&imageBeans.size()>0) {
            mAdapter = new GridAdapter(getActivity(), imageBeans, mImageFetcher);
            mGridView.setAdapter(mAdapter);
        }
    }
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.grid_layout, container, false);
        mGridView = (GridView) v.findViewById(R.id.gridView);

        mGridView.setOnItemClickListener(this);
        initAdapter();
        asyPageList(pageUrl);
        mGridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                // Pause fetcher to ensure smoother scrolling when flinging
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
                    mImageFetcher.setPauseWork(true);
                } else {
                    mImageFetcher.setPauseWork(false);
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
            }
        });

        // This listener is used to get the final width of the GridView and then calculate the
        // number of columns and the width of each column. The width of each column is variable
        // as the GridView has stretchMode=columnWidth. The column width is used to set the height
        // of each view so we get nice square thumbnails.

        return v;
    }


    @Override
    public void onResume() {
        super.onResume();
        mImageFetcher.setExitTasksEarly(false);
       // mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPause() {
        super.onPause();
        mImageFetcher.setPauseWork(false);
        mImageFetcher.setExitTasksEarly(true);
        mImageFetcher.flushCache();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mImageFetcher.closeCache();
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


}
