package cn.bmob.wx.ui.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import butterknife.ButterKnife;
import cn.bmob.imdemo.R;
import cn.bmob.wx.base.ParentWithNaviActivity;
import cn.bmob.wx.base.ParentWithNaviFragment;
import cn.bmob.wx.ui.AllAlbumActivity;
import cn.bmob.wx.ui.view.CircleImageView;

import org.json.JSONArray;
import org.json.JSONObject;


import com.megvii.cloud.http.CommonOperate;
import com.megvii.cloud.http.Response;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static java.lang.Double.*;


public class AlbumFragment extends ParentWithNaviFragment {

    private static final String TAG = "AlbumFragment";

    public int size = 0;

    private static int RESULT_LOAD_IMAGE = 1;

    LinearLayoutManager layoutManager;

    byte[][] fileBytes = new byte[10][];


    // 人脸识别

    static String apiKey = "F8f5JClgy6gGSa0CsHu2AVFORveGJwlb";//api_key
    static String apiSecret = "WbnPImebUcgZZIcKBCgG4_MB-TfyPjHR";//api_secret
    //private final static int i = 100;



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
                startActivity(AllAlbumActivity.class, null);

            }

            @Override
            public void clickRight() {
               // startActivity(AllAlbumActivity.class, null);
              //  startActivity(PhotoActivity.class, null);
                //选择图片
                //图片点击。打开图库选择图片
//                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                        != PackageManager.PERMISSION_GRANTED) {
//                    //申请 WRITE_CONTACTS 权限
//                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                            ChatActivity.REQUEST_CODE_WRITE_STORAGE);
//                } else {
//                    PhotoUtils.selectPhoto(getActivity(), PhotoUtils.INTENT_REQUEST_CODE_ALBUM);
//
//                }

                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, RESULT_LOAD_IMAGE);

            }
        };
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {

            //获取返回的数据，这里是android自定义的Uri地址
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            //获取选择照片的数据视图
            Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            //从数据视图中获取已选择图片的路径
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            final String picturePath = cursor.getString(columnIndex);
            cursor.close();


            // face++
            Log.i(TAG, "picPath: " + picturePath);


//            byte[] fileByte1 = getBytesFromFile(new File("/storage/emulated/0/DCIM/QQ20190609-1.jpg"));
//            byte[] fileByte2 = getBytesFromFile(new File("/storage/emulated/0/Download/QQ20190609-0.jpg"));


//            FaceThread ft = new FaceThread(fileByte1, fileByte1);
//            ft.start();
//            Log.i(TAG, "compareRes: " + ft.getRes());

//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    Log.i(TAG, "run() called");
//                    CommonOperate commonOperate = new CommonOperate(apiKey, apiSecret, false);
//                    try{
//                        Response compare = commonOperate.compare(null, null, fileByte1, null,
//                                null, null, fileByte2, null);
//                        String result = new String(compare.getContent());
//                        Log.i(TAG, "res: " + result);
//                        JSONObject jsonObject = new JSONObject(result);
//                        double confidence = valueOf(jsonObject.getString("confidence"));
//                        Log.i(TAG, "confidence: " + confidence);
//
//
//                    }catch (Exception e){
//                        Log.i(TAG, "startCompare: " +e.toString());
//                    }
//                }
//            }).start();

            int count = getCount(picturePath);
            Log.i(TAG, "size: " + size + "   count: " + count);

            //将图片显示到界面上
            if (count == 0) {
                CircleImageView imageView = (CircleImageView) getActivity().findViewById(R.id.imgView);
                imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                count++;
            } else if (count == 1) {
                CircleImageView imageView = (CircleImageView) getActivity().findViewById(R.id.imgView1);
                imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                count++;
            } else if (count == 2) {
                CircleImageView imageView = (CircleImageView) getActivity().findViewById(R.id.imgView2);
                imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                count++;
            } else if (count == 3) {
                CircleImageView imageView = (CircleImageView) getActivity().findViewById(R.id.imgView3);
                imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                count++;
            } else if (count == 4) {
                CircleImageView imageView = (CircleImageView) getActivity().findViewById(R.id.imgView4);
                imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                count++;
            } else if (count == 5) {
                CircleImageView imageView = (CircleImageView) getActivity().findViewById(R.id.imgView5);
                imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                count++;
            } else if (count == 6) {
                CircleImageView imageView = (CircleImageView) getActivity().findViewById(R.id.imgView6);
                imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                count++;
            } else if (count == 7) {
                CircleImageView imageView = (CircleImageView) getActivity().findViewById(R.id.imgView7);
                imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                count++;
            } else if (count == 8) {
                CircleImageView imageView = (CircleImageView) getActivity().findViewById(R.id.imgView8);
                imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                count++;
            } else if (count == 9) {
                CircleImageView imageView = (CircleImageView) getActivity().findViewById(R.id.imgView9);
                imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                count++;
            }


        }

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

    /**
     * Function:
     *      获取图片的二进制文件
     *
     */
    public static byte[] getBytesFromFile(File f) {
        if (f == null) {
            return null;
        }
        try {
            FileInputStream stream = new FileInputStream(f);
            ByteArrayOutputStream out = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = stream.read(b)) != -1)
                out.write(b, 0, n);
            stream.close();
            out.close();
            return out.toByteArray();
        } catch (IOException e) {

        }
        return null;
    }


    public int getCount(String filePath) {
        int count = 0;
        byte[] fileByte = getBytesFromFile(new File(filePath));

        if (size == 0) {
            fileBytes[size] = fileByte;
            size++;
            return count;
        }

        for (int i = 0; i < size; i++) {
            FaceThread ft = new FaceThread(fileBytes[i], fileByte);
            ft.start();
            if (ft.getRes() == true) {
                count = -1;
                return count;
            }
        }

        size++;
        count = size - 1;
        fileBytes[size - 1] = fileByte;
        return count;

    }


}



