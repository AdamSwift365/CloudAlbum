package cn.bmob.wx.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.imdemo.R;
import cn.bmob.newim.BmobIM;
import cn.bmob.v3.BmobUser;
import cn.bmob.wx.base.ParentWithNaviFragment;
import cn.bmob.wx.bean.User;
import cn.bmob.wx.model.UserModel;
import cn.bmob.wx.ui.BlackListActivity;
import cn.bmob.wx.ui.LoginActivity;
import cn.bmob.wx.ui.UserInfoActivity;
import cn.bmob.wx.util.SharePreferenceUtil;

import static cn.bmob.imdemo.R.id.rl_switch_notification;

public class SetFragment extends ParentWithNaviFragment {

    @Bind(R.id.tv_set_name)
    TextView tv_set_name;

    @Bind(R.id.layout_info)
    RelativeLayout layout_info;

    //消息提醒
    @Bind(rl_switch_notification)
    RelativeLayout layoutNotification;
    @Bind(R.id.iv_close_notification)
    ImageView ivCloseNotify;

    //震动开关
    @Bind(R.id.rl_switch_vibrate)
    RelativeLayout layoutVibrate;
    @Bind(R.id.iv_close_vibrate)
    ImageView ivCloseVibrate;

    //声音开关
    @Bind(R.id.rl_switch_voice)
    RelativeLayout layoutVoice;

    @Bind(R.id.iv_close_voice)
    ImageView ivCloseVoice;

    SharePreferenceUtil sharePreferenceUtil;

    @Override
    protected String title() {
        return "设置";
    }

    public static SetFragment newInstance() {
        SetFragment fragment = new SetFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public SetFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_set, container, false);
        initNaviView();
        ButterKnife.bind(this, rootView);
        String username = UserModel.getInstance().getCurrentUser().getUsername();
        tv_set_name.setText(TextUtils.isEmpty(username) ? "" : username);

        sharePreferenceUtil = new SharePreferenceUtil(getActivity(), SharePreferenceUtil.SETTING);

        updateSwitch(-1);




        return rootView;
    }



    @OnClick(R.id.layout_info)
    public void onInfoClick(View view) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("u", BmobUser.getCurrentUser(User.class));
        startActivity(UserInfoActivity.class, bundle);
    }

 public void onClick(View view){
     Intent i=new Intent(this.getActivity(), BlackListActivity.class);
     startActivity(i);
 }



    /**
    @OnClick(R.id.layout_blacklist)
    public void onClick(){
  //      Bundle bundle=new Bundle();
    //    bundle.putSerializable("u",BmobUser.getCurrentUser(User.class));
        Intent i=new Intent(this.getActivity(),BlackListActivity.class);
        startActivity(BlackListActivity.class, null);

    }
     **/

    @OnClick({R.id.btn_logout, R.id.rl_switch_notification, R.id.rl_switch_vibrate, R.id.rl_switch_voice})
    public void onLogoutClick(View view) {
        switch (view.getId()) {
            case R.id.btn_logout:

                UserModel.getInstance().logout();
                //可断开连接
                BmobIM.getInstance().disConnect();
                getActivity().finish();
                startActivity(LoginActivity.class, null);
                break;
            case R.id.rl_switch_notification:
                //开关消息提醒
                sharePreferenceUtil.setPushNotifyEnable(!sharePreferenceUtil.isAllowPushNotify());
                updateSwitch(0);
                break;
            
            case R.id.rl_switch_voice:
                //开关声音提醒
                sharePreferenceUtil.setAllowVoiceEnable(!sharePreferenceUtil.isAllowVoice());
                updateSwitch(1);

                break;
            case R.id.rl_switch_vibrate:
                //开关震动提醒
                sharePreferenceUtil.setAllowVibrateEnable(!sharePreferenceUtil.isAllowVibrate());
                updateSwitch(2);

                break;
        }

    }

    public void updateSwitch(int posi) {
        switch (posi) {
            case 0:
                if (sharePreferenceUtil.isAllowPushNotify()) {
                    ivCloseNotify.setVisibility(View.GONE);
                }else {
                    ivCloseNotify.setVisibility(View.VISIBLE);
                }
                break;
            case 1:
                if (sharePreferenceUtil.isAllowVoice()) {
                    ivCloseVoice.setVisibility(View.GONE);
                }else {
                    ivCloseVoice.setVisibility(View.VISIBLE);
                }
                break;
            case 2:
                if (sharePreferenceUtil.isAllowVibrate()) {
                    ivCloseVibrate.setVisibility(View.GONE);
                }else {
                    ivCloseVibrate.setVisibility(View.VISIBLE);
                }
            default:
                if (sharePreferenceUtil.isAllowVibrate()) {
                    ivCloseVibrate.setVisibility(View.GONE);
                }else {
                    ivCloseVibrate.setVisibility(View.VISIBLE);
                }
                if (sharePreferenceUtil.isAllowVoice()) {
                    ivCloseVoice.setVisibility(View.GONE);
                }else {
                    ivCloseVoice.setVisibility(View.VISIBLE);
                }
                if (sharePreferenceUtil.isAllowPushNotify()) {
                    ivCloseNotify.setVisibility(View.GONE);
                }else {
                    ivCloseNotify.setVisibility(View.VISIBLE);
                }
        }


    }


}
