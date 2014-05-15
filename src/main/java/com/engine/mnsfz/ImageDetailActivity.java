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

package com.engine.mnsfz;

import android.annotation.TargetApi;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.*;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import cn.waps.AppConnect;
import com.engine.mnsfz.fragment.ImageDetailFragment;
import com.engine.mnsfz.greendao.ModelBean;
import com.engine.mnsfz.greendao.ModelBeanDao;
import com.engine.mnsfz.jsoup.FechModle;
import com.engine.mnsfz.jsoup.ImageType;
import com.engine.mnsfz.jsoup.IndexBean;
import com.engine.mnsfz.util.ImageCache;
import com.engine.mnsfz.util.ImageFetcher;
import de.greenrobot.dao.query.WhereCondition;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImageDetailActivity extends FragmentActivity implements OnClickListener {
    private static final String IMAGE_CACHE_DIR = "images";
    public static final String EXTRA_IMAGE = "extra_image";
    public static final int GET_MODEL = 1;
    public static final int IO_EXCEPTION = 2;
    RelativeLayout.LayoutParams params = null;

    private List<ModelBean> list = null;
    private ImagePagerAdapter mAdapter;
    private ImageFetcher mImageFetcher;
    private ViewPager mPager;
    private ImageView backButton;
    private TextView textView;
    private Map<Integer, ImageType> map = new HashMap<Integer, ImageType>();
    private String url;
    private int type;
    private String title;
    private boolean hasFinish = false;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GET_MODEL:
                    if (!hasFinish) {
                        initAdapter();
                    }
                    break;
                case IO_EXCEPTION:
                    Toast.makeText(ImageDetailActivity.this, getString(R.string.net_error), Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);
        backButton = (ImageView) findViewById(R.id.back);
        backButton.setOnClickListener(this);
        textView = (TextView) findViewById(R.id.titleText);
        registMap();
        ImageCache.ImageCacheParams cacheParams =
                new ImageCache.ImageCacheParams(this, IMAGE_CACHE_DIR);
        cacheParams.setMemCacheSizePercent(0.25f); // Set memory cache to 25% of app memory

        // The ImageFetcher takes care of loading images into our ImageView children asynchronously
        int longest = getResources().getDimensionPixelSize(R.dimen.image_size);
        mImageFetcher = new ImageFetcher(this, longest);
        mImageFetcher.addImageCache(getSupportFragmentManager(), cacheParams);
        mImageFetcher.setImageFadeIn(false);
        url = getIntent().getStringExtra("srcUrl");
        type = getIntent().getIntExtra("type", 1);
        title = getIntent().getStringExtra("title");
        initAds();
        asyImage(title);
    }

    private void initAds() {
        LinearLayout conten = (LinearLayout) findViewById(R.id.AdLinearLayout);
        AppConnect.getInstance(this).showBannerAd(this, conten);
    }

    private void initAdapter() {
        if(list==null) {
            WhereCondition condition = ModelBeanDao.Properties.Title.eq(title);
            list = DaoManager.getDaoSession().getModelBeanDao().queryBuilder().where(condition).list();
        }
        if (list != null && list.size() > 0) {
            mAdapter = new ImagePagerAdapter(getSupportFragmentManager(), list);
            mPager = (ViewPager) findViewById(R.id.pager);
            mPager.setAdapter(mAdapter);
            textView.setText(1+"/"+list.size());
            mPager.setPageMargin((int) getResources().getDimension(R.dimen.image_detail_pager_margin));
            mPager.setOffscreenPageLimit(2);
            mPager.setOnPageChangeListener(onPageChangeListener);
            final int extraCurrentItem = getIntent().getIntExtra(EXTRA_IMAGE, -1);
            if (extraCurrentItem != -1) {
                mPager.setCurrentItem(extraCurrentItem);
            }
        }

    }

    private void asyImage(final String title) {
        new Thread() {
            @Override
            public void run() {
                try {
                    WhereCondition condition = ModelBeanDao.Properties.Title.eq(title);
                    list = DaoManager.getDaoSession().getModelBeanDao().queryBuilder().where(condition).list();
                    if (list != null && list.size() > 0) {
                        sendMessage(GET_MODEL, null);
                    } else {
                        FechModle modle = new FechModle(map.get(type));
                         modle.getModeList(url, title);
                        sendMessage(GET_MODEL, null);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    sendMessage(IO_EXCEPTION, null);
                }

            }
        }.start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.clear_cache:
                setWallPaper();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    private void setWallPaper() {
        int index = mPager.getCurrentItem();
        ModelBean bean = list.get(index);
        ImageView view = new ImageView(this);
        mImageFetcher.loadImage(bean.getSrc(), view);
        BitmapDrawable drawable = (BitmapDrawable) view.getDrawable();
        try {
            setWallpaper(drawable.getBitmap());
        } catch (IOException e) {
            e.printStackTrace();
        }
        // wallpaperManager.setBitmap(drawable.getBitmap());
        Toast.makeText(this, "设置成功", Toast.LENGTH_SHORT).show();
    }

    private void sendMessage(int what, Object o) {
        Message message = new Message();
        message.what = what;
        message.obj = o;
        handler.sendMessage(message);
    }

    @Override
    public void onResume() {
        super.onResume();
        mImageFetcher.setExitTasksEarly(false);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mImageFetcher.setExitTasksEarly(true);
        mImageFetcher.flushCache();
    }

    @Override
    protected void onDestroy() {
        mImageFetcher.closeCache();
        hasFinish = true;
        super.onDestroy();
    }

    private void registMap() {
        for (ImageType t : ImageType.values()) {
            map.put(Integer.parseInt(t.toString()), t);
        }
    }


    /**
     * Called by the ViewPager child fragments to load images via the one ImageFetcher
     */
    public ImageFetcher getImageFetcher() {
        return mImageFetcher;
    }


    /**
     * The main adapter that backs the ViewPager. A subclass of FragmentStatePagerAdapter as there
     * could be a large number of items in the ViewPager and we don't want to retain them all in
     * memory at once but create/destroy them on the fly.
     */
    private class ImagePagerAdapter extends FragmentStatePagerAdapter {
        List<ModelBean> list;

        public ImagePagerAdapter(FragmentManager fm, List<ModelBean> list) {
            super(fm);
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Fragment getItem(int position) {
            return ImageDetailFragment.newInstance(list.get(position).getSrc());
        }
    }

    /**
     * Set on the ImageView in the ViewPager children fragments, to enable/disable low profile mode
     * when the ImageView is touched.
     */
    @TargetApi(11)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
        }
    }

    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i2) {

        }

        @Override
        public void onPageSelected(int i) {
            textView.setText(++i + "/" + list.size());
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };
}
