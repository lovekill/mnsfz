package com.engine.mnsfz;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import cn.waps.AppConnect;
import com.engine.mnsfz.fragment.ContentFragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class IndexActivity extends FragmentActivity {

    private SlidingMenu menu;
    private ContentFragment fragment;

    public void setFragment(ContentFragment fragment) {
        this.fragment = fragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppConnect.getInstance(this);
        setContentView(android.R.layout.activity_list_item);

        initSlidMenu();
    }

    private void initSlidMenu() {
        menu = new SlidingMenu(this);
        menu.setMode(SlidingMenu.LEFT);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        menu.setShadowWidthRes(R.dimen.shadow_width);
        menu.setShadowDrawable(R.drawable.shadow);
        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        menu.setFadeDegree(0.35f);
        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        menu.setMenu(R.layout.fragment_left_menu);
        menu.setContent(R.layout.content);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppConnect.getInstance(this).close();
    }
    public void showMenu(){
        menu.showMenu();
    }

    public void showCollect(){
        fragment.showCollecttor();
        menu.showContent();
    }
    public void showAll(){
        fragment.showAll();
        menu.showContent();
    }
}
