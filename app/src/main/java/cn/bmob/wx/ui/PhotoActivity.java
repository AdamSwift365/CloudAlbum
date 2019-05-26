package cn.bmob.wx.ui;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.ArrayMap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.github.chrisbanes.photoview.OnPhotoTapListener;
import com.github.chrisbanes.photoview.PhotoView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.imdemo.R;
import cn.bmob.wx.util.LogUtils;


public class
PhotoActivity extends AppCompatActivity {


    public static final String INTENT_PICPATH = "intent_picpath";
    public static final String INTENT_TRANSITIONNAME = "intent_picpath";
    @Bind(R.id.pv_photo)
    PhotoView pvPhoto;
    
    @Bind(R.id.progress)
    ProgressBar progressBar;

    String link;
    //String transitionName;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        
        ButterKnife.bind(this);

        link = getIntent().getStringExtra(INTENT_PICPATH);
        //transitionName = getIntent().getStringExtra(INTENT_PICPATH);

        LogUtils.e("图片控件为空:"+(pvPhoto == null) +" 图片地址"+link);
        
        //pvPhoto.setTransitionName(link);

        initData();
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initData() {


        // 延迟共享动画的执行，防止闪啊闪
        //postponeEnterTransition();
        ImageLoader.getInstance().displayImage(link, pvPhoto, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {
                
            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {

            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        });

        /*ImageLoader.getInstance().displayImage(link, pvPhoto, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {
                
            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                //图片加载完成的回调中，启动过渡动画
                //supportStartPostponedEnterTransition();
                ((PhotoView)view).setImageBitmap(bitmap);
            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        });*/
 


        pvPhoto.setOnPhotoTapListener(new OnPhotoTapListener() {
            @Override
            public void onPhotoTap(ImageView view, float x, float y) {
                onBackPressed();

            }
        });

    }
    

    @Override
    public void onBackPressed() {
        overridePendingTransition(0, 0);
        supportFinishAfterTransition();
        super.onBackPressed();
        
    }

    private static void removeActivityFromTransitionManager(Activity activity) {
        if (Build.VERSION.SDK_INT < 21) {
            return;
        }
        Class transitionManagerClass = TransitionManager.class;
        try {
            Field runningTransitionsField = transitionManagerClass.getDeclaredField("sRunningTransitions");
            runningTransitionsField.setAccessible(true);
            //noinspection unchecked
            ThreadLocal<WeakReference<ArrayMap<ViewGroup, ArrayList<Transition>>>> runningTransitions
                    = (ThreadLocal<WeakReference<ArrayMap<ViewGroup, ArrayList<Transition>>>>)
                    runningTransitionsField.get(transitionManagerClass);
            if (runningTransitions.get() == null || runningTransitions.get().get() == null) {
                return;
            }
            ArrayMap map = runningTransitions.get().get();
            View decorView = activity.getWindow().getDecorView();
            if (map.containsKey(decorView)) {
                map.remove(decorView);
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
