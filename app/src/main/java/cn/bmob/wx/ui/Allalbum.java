package cn.bmob.wx.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import cn.bmob.imdemo.R;
import cn.bmob.wx.adapter.ImageAdaper;
import cn.bmob.wx.base.BaseActivity;

/**
 * Created by wx on 2019/6/6.
 */

public class Allalbum extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.allalbum);

        GridView gridView=(GridView)findViewById(R.id.gridview);
        gridView.setAdapter(new ImageAdaper(this));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i=new Intent(getApplicationContext(),SingleViewActivity.class);

                i.putExtra("id",position);
                startActivity(i);
            }
        });

    }
}
