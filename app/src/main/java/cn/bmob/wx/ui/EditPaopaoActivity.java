package cn.bmob.wx.ui;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.Calendar;

import cn.bmob.imdemo.R;
import cn.bmob.wx.base.BaseActivity;
import cn.bmob.wx.base.YmApplication;
import cn.bmob.wx.bean.Paopao;
import cn.bmob.wx.bean.User;
import cn.bmob.wx.event.RefreshPaoList;
import cn.bmob.wx.ui.view.DialogView;
import cn.bmob.wx.ui.view.ToastView;
import cn.bmob.wx.util.FileUtils;
import cn.bmob.wx.util.PhotoUtils;
import cn.bmob.wx.util.SharePreferenceUtil;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;


public class EditPaopaoActivity extends BaseActivity {
    private static final String TAG = "EditPaopaoActivity";
    private ImageView ivPhoto;
    private ImageView ivClose;
    private EditText editContent;
    AlertDialog chooseDialog;
    private Context context;
    String dateTime;
    private static final int REQUEST_CODE_CAMERA = 1;
    private static final int REQUEST_CODE_IMAGE = 2;
    private static final int REQUEST_CODE_RESULT = 3;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private Dialog loadDialog;
    private ImageView mBackIv;
    private Button mOkIv;


    private final String PAOPAO_PHOTO = "paopao_photo";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_paopao_layout);
        
        YmApplication.getInstance().addActivity(this);
        context = this;
        loadDialog = DialogView.loadDialog(context, R.string.pushing);
//        setActionBar() ;
        initView();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //申请 WRITE_CONTACTS 权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    ChatActivity.REQUEST_CODE_WRITE_STORAGE);
        }
        
        String path = SharePreferenceUtil.getStringSF(this,PAOPAO_PHOTO);
        if(!TextUtils.isEmpty(path)){
            imageLoader.displayImage("file://"+path,ivPhoto);   
        }

    }

    public void initView() {
        ivPhoto = (ImageView) findViewById(R.id.iv_photo);
        ivClose = (ImageView) findViewById(R.id.iv_photo_close);
        editContent = (EditText) findViewById(R.id.add_content);
//        image_pros = (TextView) findViewById(R.id.image_progress);
//        image_pros.setTypeface(YmApplication.chineseTypeface);
        editContent.setTypeface(YmApplication.chineseTypeface);
//        imageView.setOnClickListener(this);
        mBackIv = (ImageView) findViewById(R.id.backIv);
        mOkIv = (Button) findViewById(R.id.okIv);
        mBackIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "show_finish", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        mOkIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "show_add", Toast.LENGTH_SHORT).show();
                actionAdd();
            }
        });
        ivPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //选择图片
                //图片点击。打开图库选择图片
                if (ContextCompat.checkSelfPermission(EditPaopaoActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    //申请 WRITE_CONTACTS 权限
                    ActivityCompat.requestPermissions(EditPaopaoActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            ChatActivity.REQUEST_CODE_WRITE_STORAGE);
                } else {
                    PhotoUtils.selectPhoto(EditPaopaoActivity.this, PhotoUtils.INTENT_REQUEST_CODE_ALBUM);

                }
            }
        });

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //关闭图片
                SharePreferenceUtil.SetStringSF(EditPaopaoActivity.this,PAOPAO_PHOTO, "");
                ivPhoto.setImageDrawable(null);
                ivClose.setVisibility(View.GONE);

            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == ChatActivity.REQUEST_CODE_WRITE_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted 获得权限后执行xxx
                //PhotoUtils.selectPhoto(this, PhotoUtils.INTENT_REQUEST_CODE_ALBUM);
            } else {
                // Permission Denied 拒绝后xx的操作。
                ToastView.showToast(this, "获取权限失败");
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PhotoUtils.INTENT_REQUEST_CODE_ALBUM: //选择图片返回
                selectPicFromMediaStore(requestCode, resultCode, data);
                break;
        }
    }

    /**
     * 从相册中获取图像设置缩列图
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    private void selectPicFromMediaStore(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {

            if (data == null)
                return;

            if (data.getData() == null)
                return;

            if (!FileUtils.isSdcardExist()) {
                ToastView.showToast(this, "SD卡不可用,请检查...");
                return;
            }
            String path = PhotoUtils.getPath(this, data.getData());

            if (path == null || path.isEmpty()) {
                ToastView.showToast(this, "获取图片失败!");

                SharePreferenceUtil.SetStringSF(EditPaopaoActivity.this,PAOPAO_PHOTO, "");
                ivPhoto.setImageDrawable(null);

                ivClose.setVisibility(View.GONE);
            } else {
                //处理图片
                Bitmap bitmap = PhotoUtils.createBitmap(path, mScreenWidth, mScreenHeight);
                //保存图片
                PhotoUtils.saveBitmap(bitmap, path);

                //显示图片
                imageLoader.displayImage("file://"+path, ivPhoto);

                SharePreferenceUtil.SetStringSF(this,PAOPAO_PHOTO, path);

                ivClose.setVisibility(View.VISIBLE);

            }

        }
    }


    /**
     * 发布泡泡
     */
    public void actionAdd() {
        String commitContent = editContent.getText().toString().trim();
        if (TextUtils.isEmpty(commitContent)) {
            ToastView.showToast(context, "内容不能为空！", Toast.LENGTH_SHORT);
            return;
        }
        //    ToastView.showToast(context,"正在发布！", Toast.LENGTH_SHORT);
        loadDialog.show();


        String picPath = SharePreferenceUtil.getStringSF(this,PAOPAO_PHOTO);

        Log.e("@@","路径:"+picPath);

        Calendar now = Calendar.getInstance();
        String timeMillis = String.valueOf(now.getTimeInMillis());
        User user = BmobUser.getCurrentUser(User.class);
        final Paopao paopao = new Paopao();
        paopao.setUser(user);
        paopao.setCreateTimeMillis(timeMillis);
        paopao.setPhoneModel(Build.MODEL);
        paopao.setContent(commitContent);

        if (!TextUtils.isEmpty(picPath)) {
            
            final BmobFile photo = new BmobFile(new File(picPath));
            
            //上传图片
            photo.uploadblock(new UploadFileListener() {
                @Override
                public void done(BmobException e) {
                    if(e == null){
                        paopao.setPhoto(photo);
                        pushPaoPao(paopao);
                    }else{

                        if (loadDialog.isShowing()) {
                            loadDialog.dismiss();
                        }
                        ToastView.showToast(context, "图片上传失败！");
                        e.printStackTrace();
                    }
                }
            });
            
        }else{

            pushPaoPao(paopao);
        }

    }


    private void pushPaoPao(final Paopao paopao) {
        if (TextUtils.isEmpty(paopao.getObjectId())) {
            //新建
            paopao.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    handlePushResult(paopao, e,false);
                }
            });
        } else {
            //修改更新,,还没添加该功能
            paopao.update(new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    handlePushResult(paopao, e,true);
                }
            });
        }


    }


    private void handlePushResult(final Paopao paopao, BmobException e, boolean isUpdate) {

        final String tip = isUpdate ? "更新" : "发表";

        if (e == null) {

            Log.d(TAG, "-------paopao onSuccess--------");
            BmobRelation relation = new BmobRelation();
            relation.add(paopao);
            BmobUser.getCurrentUser(User.class).setPaopao(relation);

            BmobUser.getCurrentUser(User.class).update(new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        Log.d(TAG, "-------paopao relation  onSuccess--------");
                        if (loadDialog.isShowing()) {
                            loadDialog.dismiss();
                        }
                        SharePreferenceUtil.SetStringSF(EditPaopaoActivity.this,PAOPAO_PHOTO,"");
                        ToastView.showToast(context, tip+"成功！", Toast.LENGTH_SHORT);
                        
                        //发送消息更新paolist
                        EventBus.getDefault().post(new RefreshPaoList());
                        finish();
                    } else {
                        if (loadDialog.isShowing()) {
                            loadDialog.dismiss();
                        }
                        Log.d(TAG, "-------paopao relation  onSuccess--------");
                        ToastView.showToast(context, tip+"失败！", Toast.LENGTH_SHORT);
                    }
                }
            });

        } else {
            e.printStackTrace();

            if (loadDialog.isShowing()) {
                loadDialog.dismiss();
            }
            ToastView.showToast(context, tip+"失败！", Toast.LENGTH_SHORT);
            Log.d(TAG, "-------paopao save onFailure--------"+e.getMessage());
        }
    }


}
