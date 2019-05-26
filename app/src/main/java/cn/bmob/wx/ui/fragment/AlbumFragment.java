package cn.bmob.wx.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import cn.bmob.imdemo.R;
import cn.bmob.wx.base.ParentWithNaviFragment;
import cn.bmob.wx.model.UserModel;
import cn.bmob.wx.util.SharePreferenceUtil;

public class AlbumFragment extends ParentWithNaviFragment {



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_album, container, false);




        return rootView;
    }

    @Override
    protected String title() {
        return "相册";
    }


}
