package cn.bmob.wx.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.baidu.location.LocationClient;


import cn.bmob.imdemo.R;
import cn.bmob.wx.base.BaseActivity;
import cn.bmob.wx.bean.User;
import cn.bmob.wx.model.UserModel;

public class SplashActivity extends BaseActivity {

 


    private static final int GO_HOME = 100;
    private static final int GO_LOGIN = 200;

    // 定位获取当前用户的地理位置
    private LocationClient mLocationClient;

 


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

      
        
        Handler handler =new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                User user = UserModel.getInstance().getCurrentUser();
                if (user == null) {
                    startActivity(LoginActivity.class,null,true);
                }else{
                    updateUserInfos();
                    startActivity(MainActivity.class,null,true);
                }
            }
        },1000);

    }
}
