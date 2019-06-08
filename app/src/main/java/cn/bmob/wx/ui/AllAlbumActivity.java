package cn.bmob.wx.ui;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import cn.bmob.imdemo.R;
import cn.bmob.wx.adapter.ImageAdaper;
import cn.bmob.wx.base.BaseActivity;

/**
 * Created by wx on 2019/6/6.
 */

public class AllAlbumActivity extends BaseActivity {

    private static int RESULT_LOAD_IMAGE = 1;
    public ImageAdaper adaper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.allalbum);

        GridView gridView=(GridView)findViewById(R.id.gridview);
        adaper=new ImageAdaper(this);

        TextView textView=(TextView)findViewById(R.id.tv_right) ;
        textView.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View arg0) {


                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, RESULT_LOAD_IMAGE);

            }

        });




        gridView.setAdapter(adaper);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i=new Intent(getApplicationContext(),SingleViewActivity.class);

                i.putExtra("id",position);
                startActivity(i);
            }
        });

    }


    public void  onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {

//获取返回的数据，这里是android自定义的Uri地址
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            //获取选择照片的数据视图
            Cursor cursor = this.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            //从数据视图中获取已选择图片的路径
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            //将图片显示到界面上

         ImageView imageView = (ImageView) this.findViewById(R.id.imgView);
            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));

//
//            Integer temp[]=new Integer[adaper.mThumbIds.length];
//            for(int i=0;i<adaper.mThumbIds.length;i++){
//                temp[i]=adaper.mThumbIds[i];
//
//            }
//          adaper.mThumbIds=new Integer[adaper.mThumbIds.length+1];
//            for(int i=0;i<adaper.mThumbIds.length;i++){
//                adaper.mThumbIds[i]=temp[i];
//
//            }
//            adaper.mThumbIds[adaper.mThumbIds.length]=Integer.parseInt(picturePath);
//


        }

    }




}
