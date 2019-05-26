package cn.bmob.wx.adapter;

import android.view.View;
public interface OnRecyclerViewListener {
    void onItemClick(View v, int position);
    boolean onItemLongClick(View v,int position);
}
