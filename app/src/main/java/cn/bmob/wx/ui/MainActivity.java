package cn.bmob.wx.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.Bind;
import cn.bmob.imdemo.R;
import cn.bmob.wx.base.BaseActivity;
import cn.bmob.wx.bean.User;
import cn.bmob.wx.db.NewFriendManager;
import cn.bmob.wx.event.RefreshEvent;
import cn.bmob.wx.ui.fragment.AlbumFragment;
import cn.bmob.wx.ui.fragment.ContactFragment;
import cn.bmob.wx.ui.fragment.ConversationFragment;
import cn.bmob.wx.ui.fragment.PaoPaoFragment;
import cn.bmob.wx.ui.fragment.SetFragment;
import cn.bmob.wx.ui.view.ToastView;
import cn.bmob.wx.util.IMMLeaks;
import cn.bmob.newim.BmobIM;
import cn.bmob.newim.core.ConnectionStatus;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.event.OfflineMessageEvent;
import cn.bmob.newim.listener.ConnectListener;
import cn.bmob.newim.listener.ConnectStatusChangeListener;
import cn.bmob.newim.listener.ObseverListener;
import cn.bmob.newim.notification.BmobNotificationManager;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;

public class MainActivity extends BaseActivity implements ObseverListener {

    @Bind(R.id.btn_conversation)
    Button btn_conversation;
    @Bind(R.id.btn_set)
    Button btn_set;

    @Bind(R.id.btn_contact)
    Button btn_contact;


    @Bind(R.id.btn_paopao)
    Button btnPaoPao;


    @Bind(R.id.btn_album)
    Button btn_album;

    @Bind(R.id.iv_conversation_tips)
    ImageView iv_conversation_tips;

    @Bind(R.id.iv_contact_tips)
    ImageView iv_contact_tips;


    @Bind(R.id.iv_paopao_tips)
    ImageView ivPaoPaoTip;

    @Bind({R.id.iv_album_tips})
    ImageView iv_album_tips;

    private Button[] mTabs;


    private ConversationFragment conversationFragment;
    private SetFragment setFragment;
    private AlbumFragment albumFragment;
    ContactFragment contactFragment;
    PaoPaoFragment paopaoFragment;


    private Fragment[] fragments;
    private int index;
    private int currentTabIndex;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //connect server
        User user = BmobUser.getCurrentUser(User.class);
        BmobIM.connect(user.getObjectId(), new ConnectListener() {
            @Override
            public void done(String uid, BmobException e) {
                if (e == null) {
                    Logger.i("connect success");
                    //服务器连接成功就发送一个更新事件，同步更新会话及主页的小红点
                    EventBus.getDefault().post(new RefreshEvent());
                } else {
                    Logger.e(e.getErrorCode() + "/" + e.getMessage());
                }
            }
        });
        //监听连接状态，也可通过BmobIM.getInstance().getCurrentStatus()来获取当前的长连接状态
        BmobIM.getInstance().setOnConnectStatusChangeListener(new ConnectStatusChangeListener() {
            @Override
            public void onChange(ConnectionStatus status) {
                toast("" + status.getMsg());
            }
        });
        //解决leancanary提示InputMethodManager内存泄露的问题
        IMMLeaks.fixFocusedViewLeak(getApplication());

        if (Build.VERSION.SDK_INT >= 21) {

            //获取权限
            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    ) {

                // 申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义)
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_PHONE_STATE}, BAIDU_READ_PHONE_STATE);

            }
        }

    }

    public final static int BAIDU_READ_PHONE_STATE = 0x44;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {

            // requestCode即所声明的权限获取码，在checkSelfPermission时传入
            case BAIDU_READ_PHONE_STATE:

                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // 获取到权限，作相应处理（调用定位SDK应当确保相关权限均被授权，否则可能引起定位失败）

                } else {
                    ToastView.showToast(this, "获取权限失败，获取位置将会失败！");
                    // 没有获取到权限，做特殊处理
                }
                break;

            default:
                break;

        }
    }

    @Override
    protected void initView() {
        super.initView();
        mTabs = new Button[5];
        mTabs[0] = btn_conversation;
        mTabs[1] = btn_contact;
        mTabs[2]=btn_album;
        mTabs[3] = btnPaoPao;
        mTabs[4] = btn_set;


        mTabs[0].setSelected(true);
        initTab();
    }

    private void initTab() {
        conversationFragment = new ConversationFragment();
        setFragment = new SetFragment();
        contactFragment = new ContactFragment();
        paopaoFragment = new PaoPaoFragment();
        albumFragment=new AlbumFragment();
        fragments = new Fragment[]{conversationFragment, contactFragment,albumFragment, paopaoFragment, setFragment};
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, conversationFragment)
                .add(R.id.fragment_container, contactFragment)
                .add(R.id.fragment_container,albumFragment)
                .add(R.id.fragment_container, paopaoFragment)
                .add(R.id.fragment_container, setFragment)
                .hide(setFragment)
                .hide(paopaoFragment)
                .hide(contactFragment)
                .hide(albumFragment)
                .show(conversationFragment)
                .commit();
    }

    //在布局文件设置点击事件
    public void onTabSelect(View view) {
        switch (view.getId()) {
            case R.id.btn_conversation:
                index = 0;
                break;
            case R.id.btn_contact:
                index = 1;
                break;
            case R.id.btn_album:
                index = 2;
                break;
            case R.id.btn_paopao:
                index = 3;
                break;
            case R.id.btn_set:
                index = 4;
                break;
        }
        onTabIndex(index);
    }

    private void onTabIndex(int index) {
        if (currentTabIndex != index) {
            FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
            trx.hide(fragments[currentTabIndex]);
            if (!fragments[index].isAdded()) {
                trx.add(R.id.fragment_container, fragments[index]);
            }
            trx.show(fragments[index]).commit();
        }
        mTabs[currentTabIndex].setSelected(false);
        mTabs[index].setSelected(true);
        currentTabIndex = index;
    }

    @Override
    protected void onResume() {
        super.onResume();
        //显示小红点
        checkRedPoint();
        //进入应用后，通知栏应取消
        BmobNotificationManager.getInstance(this).cancelNotification();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //清理导致内存泄露的资源
        BmobIM.getInstance().clear();
    }

    /**
     * 注册消息接收事件
     *
     * @param event
     */
    @Subscribe
    public void onEventMainThread(MessageEvent event) {
        checkRedPoint();
    }

    /**
     * 注册离线消息接收事件
     *
     * @param event
     */
    @Subscribe
    public void onEventMainThread(OfflineMessageEvent event) {
        checkRedPoint();
    }

    /**
     * 注册自定义消息接收事件
     *
     * @param event
     */
    @Subscribe
    public void onEventMainThread(RefreshEvent event) {
        log("---主页接收到自定义消息---");
        checkRedPoint();
    }

    private void checkRedPoint() {
        int count = (int) BmobIM.getInstance().getAllUnReadCount();
        if (count > 0) {
            iv_conversation_tips.setVisibility(View.VISIBLE);
        } else {
            iv_conversation_tips.setVisibility(View.GONE);
        }
        //是否有好友添加的请求
        if (NewFriendManager.getInstance(this).hasNewFriendInvitation()) {
            iv_contact_tips.setVisibility(View.VISIBLE);
        } else {
            iv_contact_tips.setVisibility(View.GONE);
        }
    }

}
