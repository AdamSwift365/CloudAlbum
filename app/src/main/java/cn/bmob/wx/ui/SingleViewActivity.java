package cn.bmob.wx.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import cn.bmob.imdemo.R;
import cn.bmob.wx.adapter.ImageAdaper;

/**
 * Created by wx on 17/3/1.
 */

public class SingleViewActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_view);
        Intent i=getIntent();
        int position=i.getExtras().getInt("id");
        ImageAdaper imageAdaper=new ImageAdaper(this);

        ImageView imageView=(ImageView)findViewById(R.id.SingView);
        imageView.setImageResource(imageAdaper.mThumbIds[position]);

    }
}
