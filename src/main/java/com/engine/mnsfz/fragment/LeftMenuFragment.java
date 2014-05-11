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
    private Button getScoreBtn ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.left_menu,container,false) ;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getScoreBtn = (Button) view.findViewById(R.id.getscore);
        getScoreBtn.setOnClickListener(onClickListener);
        view.findViewById(R.id.qiaopi).setOnClickListener(onClickListener);
        view.findViewById(R.id.sunline).setOnClickListener(onClickListener);
        view.findViewById(R.id.qingchun).setOnClickListener(onClickListener);
    }
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.getscore:
                    AppConnect.getInstance(getActivity()).showOffers(getActivity());
                    break;
                case R.id.qiaopi:
                    ((IndexActivity) getActivity()).getQiaoPi();
                    break;
                case R.id.sunline:
                    ((IndexActivity) getActivity()).getSunline();
                   break;
                case R.id.qingchun:
                    ((IndexActivity) getActivity()).getQingchun();
                    break;
            }

        }
    };
}
