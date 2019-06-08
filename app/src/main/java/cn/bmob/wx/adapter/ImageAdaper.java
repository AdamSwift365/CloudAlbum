package cn.bmob.wx.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import cn.bmob.imdemo.R;

/**
 * Created by wx on 17/3/1.
 */

public class ImageAdaper extends BaseAdapter {
private Context mContext;

    public ImageAdaper(Context c)
    {
        mContext=c;
    }
public int getCount()
{
    return mThumbIds.length;
}

 public Object getItem(int position)
 {
     return null;
 }

    public long getItemId(int position)
    {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
     ImageView imageView;
        if(convertView==null)
        {
            imageView=new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(200,300));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8,8,8,8);
        }
        else
        {
            imageView=(ImageView)convertView;
        }
        imageView.setImageResource(mThumbIds[position]);
        return imageView;

    }

    public Integer[] mThumbIds={R.drawable.p1,R.drawable.p2,R.drawable.p3};



}
