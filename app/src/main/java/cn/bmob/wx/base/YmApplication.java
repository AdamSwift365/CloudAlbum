package cn.bmob.wx.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;

import com.baidu.location.LocationClient;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;

import cn.bmob.wx.bean.User;
import cn.bmob.wx.util.ActivityManagerUtils;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobGeoPoint;



/*import com.nostra13.universalimageloader.cache.disc.DiskCache;
import com.nostra13.universalimageloader.cache.disc.impl.LimitedAgeDiskCache;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.impl.ext.LruDiskCache;*/
//import com.nostra13.universalimageloader.cache.memory.MemoryCache;

public class YmApplication extends Application {

    public final String TAG = "YmApplication" ;
    public static YmApplication mInstance;
    public static User user;
    public static float sScale;
    public static int sWidthDp;
    public static int sWidthPix;
    public static BmobGeoPoint lastPoint = null; // 上一次定位到的经纬度
    public static String locationAddrStr = "" ;   //获取反地理编码
    public static String locationCity = "" ;    //城市
    public static String locationDistrict = "" ; // 区县
    //字体样式
    public static Typeface chineseTypeface,englishTypeface ;
    public LocationClient mLocationClient;
    //public MyLocationListener mMyLocationListener;

    private String latitude = "";
    private String longtitude = "";



    public static synchronized YmApplication getInstance() {
        if (mInstance == null) {
            mInstance = new YmApplication();
        }
        return mInstance;
    }

    /**
     * 获取当前登录用户
     * @return  User
     */
    public static synchronized  User getCurrentUser(){
        return BmobUser.getCurrentUser(User.class) ;
    }

    /**
     * 初始化ImageLoader
     */
    public static void initImageLoader(Context context) {
        File cacheDir = StorageUtils.getOwnCacheDirectory(context,
                Constant.OwnCacheDirectory);// 获取到缓存的目录地址

        /*DiskCache discCache = new LimitedAgeDiskCache(cacheDir,15 * 60) ;
        DiskCache diskCache1 = new UnlimitedDiskCache(cacheDir) ;
       LruMemoryCache lruMemoryCache = new LruMemoryCache(5 * 1024 * 1024) ;
       MemoryCache memoryCache = new LimitedAgeMemoryCache(lruMemoryCache,15 * 60) ;*/

        // 创建配置ImageLoader
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context)
                // 线程池内加载的数量
                .threadPoolSize(3).threadPriority(Thread.NORM_PRIORITY - 2)
                .memoryCache(new WeakMemoryCache())
               // .memoryCache(memoryCache)
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                        // 将保存的时候的URI名称用MD5 加密
                .tasksProcessingOrder(QueueProcessingType.LIFO)

               // .discCache(new UnlimitedDiskCache(cacheDir))// 自定义缓存路径
                 //       .diskCache(discCache)
                        // .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                .writeDebugLogs() // Remove for release app
                .discCacheSize(50 * 1024 * 1024)  // 50 Mb
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);// 全局初始化此配置
    }

    @Override
    public void onCreate() {
        super.onCreate();

        sScale = getResources().getDisplayMetrics().density;
        sWidthPix = getResources().getDisplayMetrics().widthPixels;
        sWidthDp = (int) (sWidthPix / sScale);

        mInstance = this ;

   
        initTypeFace(getApplicationContext());
    }

    /**

    /**
     * @param context
     * 初始化字体风格
     */
    private void initTypeFace(Context context) {
        // TODO Auto-generated method stub
        englishTypeface = Typeface.createFromAsset(context.getAssets(),
                "font/Roboto-Light.ttf");
        chineseTypeface = Typeface.createFromAsset(context.getAssets(),
                "font/xiyuan.ttf");
    }


    /**
     * 退出登录,清空缓存数据
     */


    /**
     * 删除缓存文件
     * @param cacheFile
     */
    public void clearCache(File cacheFile) {
        if (cacheFile == null) {
            try {
                File cacheDir = this.getCacheDir() ;
                File filesDir = this.getFilesDir();
                if (cacheDir.exists()) {
                    clearCache(cacheDir);
                    clearCache(filesDir);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (cacheFile.isFile()) {
            cacheFile.delete();
        } else if (cacheFile.isDirectory()) {
            File[] childFiles = cacheFile.listFiles();
            for (int i = 0; i < childFiles.length; i++) {
                clearCache(childFiles[i]);
            }
        }
    }


    public void addActivity(Activity ac){
        ActivityManagerUtils.getInstance().addActivity(ac);
    }

    public void exit(){
        ActivityManagerUtils.getInstance().removeAllActivity();
    }

    public Activity getTopActivity(){
        return ActivityManagerUtils.getInstance().getTopActivity();
    }
}
