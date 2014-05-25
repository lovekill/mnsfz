package com.engine.mnsfz.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import cn.waps.AppConnect;
import com.engine.mnsfz.IndexActivity;
import com.engine.mnsfz.R;

/**
 * Created by USER on 2014/5/10.
 */
public class LeftMenuFragment extends BaseFragment {
    private View getScoreBtn ;
    private View allBtn;
    private View cllectBtn ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.left_menu,container,false) ;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getScoreBtn =  view.findViewById(R.id.getscore);
        allBtn = view.findViewById(R.id.all) ;
        view.findViewById(R.id.love).setOnClickListener(onClickListener);
        allBtn.setOnClickListener(onClickListener);
        getScoreBtn.setOnClickListener(onClickListener);
    }
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.getscore:
                    AppConnect.getInstance(getActivity()).showOffers(getActivity());
                    break;
                case R.id.all:
                    ((IndexActivity)getActivity()).showAll();
                    break ;
                case R.id.love:
                    ((IndexActivity)getActivity()).showCollect();
                    break;
            }

        }
    };
}
