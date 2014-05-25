package com.engine.mnsfz.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.engine.mnsfz.*;
import com.engine.mnsfz.fragment.ui.PullToRefreshStaggeredView;
import com.engine.mnsfz.greendao.ImageBean;
import com.engine.mnsfz.greendao.ImageBeanDao;
import com.engine.mnsfz.jsoup.ImageType;
import com.engine.mnsfz.jsoup.NetFech;
import com.engine.mnsfz.jsoup.Page;
import com.engine.mnsfz.util.ImageCache;
import com.engine.mnsfz.util.ImageFetcher;
import com.engine.mnsfz.util.LogUtil;
import com.etsy.android.grid.StaggeredGridView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import de.greenrobot.dao.query.WhereCondition;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
    private PullToRefreshStaggeredView gridView ;
    private GridAdapter mAdapter;
    public static final String EXTRA_IMAGE = "extra_image";
    private  List<ImageBean> imageBeans = new ArrayList<ImageBean>() ;
    private  Status currentStatus = Status.All ;
    List<Page> list = null;
    private ImageView backBtn;
    private TextView titleText;
    private NetFech fech;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GET_PAGE:
                    initAdapter1();
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

    enum  Status{
        All,CLLOECT
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
        gridView = (PullToRefreshStaggeredView) view.findViewById(R.id.grid_view);
        gridView.getRefreshableView().setOnItemClickListener(this);
        gridView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<StaggeredGridView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<StaggeredGridView> refreshView) {
                if(currentStatus==Status.All) {
                    fechData(3);
                }else {
                    showCollecttor();
                }
                gridView.onRefreshComplete();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<StaggeredGridView> refreshView) {
                if (currentStatus==Status.All) {
                    nextPage();
                }else {
                    nextCllocte();
                    gridView.onRefreshComplete();
                }
            }
        });
        ((IndexActivity)getActivity()).setFragment(this);
        initImageFech();
        initAdapter1();
       fechData(3);
//        fechData(1);
//        fechData(2);

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
        if(currentStatus==Status.CLLOECT) return;
        imageBeans = DaoManager.getDaoSession().getImageBeanDao().queryBuilder().orderDesc(ImageBeanDao.Properties.Time).limit(10).list();
        if(imageBeans!=null&&imageBeans.size()>0) {
            mAdapter = new GridAdapter(getActivity(), imageBeans, mImageFetcher);
            gridView.getRefreshableView().setAdapter(mAdapter);
        }
    }

    public void showAll(){
        currentStatus=Status.All ;
        initAdapter1();
    }

   public  void showCollecttor(){
       currentStatus = Status.CLLOECT ;
        WhereCondition condition = ImageBeanDao.Properties.Love.eq(true) ;
       imageBeans = DaoManager.getDaoSession().getImageBeanDao().queryBuilder().where(condition).orderDesc(ImageBeanDao.Properties.Time).limit(10).list();
           mAdapter = new GridAdapter(getActivity(), imageBeans, mImageFetcher);
           gridView.getRefreshableView().setAdapter(mAdapter);
   }

    private void asyPageList() {
        new Thread() {
            @Override
            public void run() {
                try {
                    list = fech.getPage();
                    for (int i = 0; i < list.size(); i++) {
                        fech.getModelIndexImage(list.get(i).getHref()) ;
                    }
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

    private void nextPage(){
        List<ImageBean> l= DaoManager.getDaoSession().getImageBeanDao().queryBuilder().orderDesc(ImageBeanDao.Properties.Time).offset(imageBeans.size()).limit(10).list();
        if(l!=null&&l.size()>0) {
            imageBeans.addAll(l);
            gridView.onRefreshComplete();
            mAdapter.notifyDataSetChanged();
        }
    }

    private void nextCllocte(){
        WhereCondition condition = ImageBeanDao.Properties.Love.eq(true) ;
        List<ImageBean> l= DaoManager.getDaoSession().getImageBeanDao().queryBuilder().where(condition).orderDesc(ImageBeanDao.Properties.Time).offset(imageBeans.size()).limit(10).list();
        if(l!=null&&l.size()>0) {
            imageBeans.addAll(l);
            gridView.onRefreshComplete();
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        String url = mAdapter.getItem(i).getHref();
        Intent intent = new Intent(getActivity(), ImageDetailActivity.class);
        intent.putExtra("srcUrl", url);
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
