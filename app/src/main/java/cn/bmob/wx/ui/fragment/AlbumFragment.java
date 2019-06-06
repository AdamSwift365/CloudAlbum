package cn.bmob.wx.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import cn.bmob.imdemo.R;
import cn.bmob.wx.base.ParentWithNaviActivity;
import cn.bmob.wx.base.ParentWithNaviFragment;
import cn.bmob.wx.ui.Allalbum;

public class AlbumFragment extends ParentWithNaviFragment {




    LinearLayoutManager layoutManager;

    @Override
    protected String title() {
        return "相册";
    }

    @Override
    public Object left() {
        return R.mipmap.all2;
    }

    @Override
    public Object right() {
        return R.drawable.base_action_bar_add_bg_selector;
    }




    @Override
    public ParentWithNaviActivity.ToolBarListener setToolBarListener() {
        return new ParentWithNaviActivity.ToolBarListener() {
            @Override
            public void clickLeft() {
                startActivity(Allalbum.class, null);

            }

            @Override
            public void clickRight() {
               // startActivity(Allalbum.class, null);
              //  startActivity(PhotoActivity.class, null);

            }
        };
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_album, container, false);
        initNaviView();
        ButterKnife.bind(this, rootView);
        layoutManager = new LinearLayoutManager(getActivity());



        return rootView;
    }




}
