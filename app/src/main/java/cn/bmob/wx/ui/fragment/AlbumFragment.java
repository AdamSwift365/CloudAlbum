package cn.bmob.wx.ui.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import cn.bmob.imdemo.R;
import cn.bmob.wx.base.ParentWithNaviActivity;
import cn.bmob.wx.base.ParentWithNaviFragment;
import cn.bmob.wx.ui.AllAlbumActivity;
import cn.bmob.wx.ui.view.CircleImageView;

import static android.app.Activity.RESULT_OK;


public class AlbumFragment extends ParentWithNaviFragment {

    public int count=0;

    private static int RESULT_LOAD_IMAGE = 1;

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
    public void  onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
                if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {

//获取返回的数据，这里是android自定义的Uri地址
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            //获取选择照片的数据视图
         Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
       cursor.moveToFirst();
            //从数据视图中获取已选择图片的路径
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
          String picturePath = cursor.getString(columnIndex);
           cursor.close();
            //将图片显示到界面上
                  if(count==0) {
                      CircleImageView imageView = (CircleImageView) getActivity().findViewById(R.id.imgView);
                      imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                      count++;
                  }else if(count==1) {
                        CircleImageView imageView = (CircleImageView) getActivity().findViewById(R.id.imgView1);
                        imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                        count++;
                    } else if(count==2) {
                        CircleImageView imageView = (CircleImageView) getActivity().findViewById(R.id.imgView2);
                        imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                        count++;
                    }else if(count==3) {
                      CircleImageView imageView = (CircleImageView) getActivity().findViewById(R.id.imgView3);
                      imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                      count++;
                  }else if(count==4) {
                      CircleImageView imageView = (CircleImageView) getActivity().findViewById(R.id.imgView4);
                      imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                      count++;
                  }else if(count==5) {
                      CircleImageView imageView = (CircleImageView) getActivity().findViewById(R.id.imgView5);
                      imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                      count++;
                  }else if(count==6) {
                      CircleImageView imageView = (CircleImageView) getActivity().findViewById(R.id.imgView6);
                      imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                      count++;
                  }else if(count==7) {
                      CircleImageView imageView = (CircleImageView) getActivity().findViewById(R.id.imgView7);
                      imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                      count++;
                  }else if(count==8) {
                      CircleImageView imageView = (CircleImageView) getActivity().findViewById(R.id.imgView8);
                      imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                      count++;
                  }else if(count==9) {
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




}
