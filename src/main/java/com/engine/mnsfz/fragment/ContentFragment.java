package com.engine.mnsfz.fragment;

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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.engine.mnsfz.IndexActivity;
import com.engine.mnsfz.R;
import com.engine.mnsfz.jsoup.ImageType;
import com.engine.mnsfz.jsoup.NetFech;
import com.engine.mnsfz.jsoup.Page;
import com.engine.mnsfz.util.LogUtil;

import java.io.IOException;
import java.util.List;
import java.util.Random;

/**
 * Created by USER on 2014/5/10.
 */
public class ContentFragment extends BaseFragment implements View.OnClickListener{
    private static final String TAG = "ImageGridActivity";
    private static final int GET_PAGE = 1;
    private static final int IO_EXCEPTION = 2;
    private ImagePagerAdapter mAdapter;
    private ViewPager mPager;
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
                    initAdaper();
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
        return inflater.inflate(R.layout.image_detail_pager,container,false) ;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        backBtn = (ImageView) view.findViewById(R.id.back);
        backBtn.setOnClickListener(this);
        mPager = (ViewPager) view.findViewById(R.id.pager);
        titleText = (TextView) view.findViewById(R.id.titleText);
        ((IndexActivity)getActivity()).setFragment(this);
       fechData(3);

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
    private void initAdaper() {
//        LogUtil.d(getClass(),"initAdapter");
        // Set up ViewPager and backing adapter
        mAdapter = new ImagePagerAdapter(getFragmentManager(), list);

        mPager.setAdapter(mAdapter);
        mPager.setPageMargin((int) getResources().getDimension(R.dimen.image_detail_pager_margin));
        mPager.setOffscreenPageLimit(2);
        mPager.setOnPageChangeListener(onPageChangeListener);
        // Set up activity to go full screen
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        titleText.setText("1/" + list.size());
        // Enable some additional newer visibility and ActionBar features to create a more
        // immersive photo viewing experience


        // Set the current item based on the extra passed in to this activity
        final int extraCurrentItem = getActivity().getIntent().getIntExtra(EXTRA_IMAGE, -1);
        if (extraCurrentItem != -1) {
            mPager.setCurrentItem(extraCurrentItem);
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

    private int getRondomInt(int max) {
        Random random = new Random();
        return random.nextInt(3) + 1;
    }

    private void sendMessage(int what, Object o) {
        Message message = new Message();
        message.what = what;
        message.obj = o;
        handler.sendMessage(message);
    }

    private class ImagePagerAdapter extends FragmentStatePagerAdapter {
        List<Page> list;

        public ImagePagerAdapter(FragmentManager fm, List<Page> list) {
            super(fm);
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Fragment getItem(int position) {
            return new ImageGridFragment(list.get(position).getHref(), fech);
            //  return ImageGridFragment.newInstance(list.get(position).getHref());
        }
    }

    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i2) {

        }

        @Override
        public void onPageSelected(int i) {
            titleText.setText((i + 1) + "/" + list.size());
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };
}
