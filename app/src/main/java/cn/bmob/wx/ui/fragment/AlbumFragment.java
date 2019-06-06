package cn.bmob.wx.ui.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import cn.bmob.imdemo.R;
import cn.bmob.wx.base.ParentWithNaviActivity;
import cn.bmob.wx.base.ParentWithNaviFragment;
import cn.bmob.wx.ui.Allalbum;
import cn.bmob.wx.ui.ChatActivity;
import cn.bmob.wx.util.PhotoUtils;

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
                //选择图片
                //图片点击。打开图库选择图片
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    //申请 WRITE_CONTACTS 权限
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            ChatActivity.REQUEST_CODE_WRITE_STORAGE);
                } else {
                    PhotoUtils.selectPhoto(getActivity(), PhotoUtils.INTENT_REQUEST_CODE_ALBUM);

                }

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
