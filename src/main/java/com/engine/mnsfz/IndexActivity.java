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
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.index, menu);
        return true;
    }

    public void getQiaoPi() {
        menu.showContent();
        fragment.fechData(3);
    }
    public void getSunline(){
        menu.showContent();
        fragment.fechData(1);
    }

    public void getQingchun(){
        menu.showContent();
        fragment.fechData(2);
    }

    public void showMenu(){
        menu.showMenu();
    }

}
