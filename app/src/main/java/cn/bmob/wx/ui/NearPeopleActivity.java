package cn.bmob.wx.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.wx.BmobIMApplication;
import cn.bmob.imdemo.R;
import cn.bmob.wx.adapter.NearPeopleAdapter;
import cn.bmob.wx.base.ParentWithNaviActivity;
import cn.bmob.wx.bean.User;
import cn.bmob.wx.ui.view.ToastView;
import cn.bmob.wx.ui.view.xlist.XListView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;


public class NearPeopleActivity extends ParentWithNaviActivity implements XListView.IXListViewListener, OnItemClickListener {

    XListView mListView;
    NearPeopleAdapter adapter;
    String from = "";
    
    List<User> nearFriendlist = new ArrayList<User>();

    private double QUERY_KILOMETERS = 5;//默认查询5公里范围内的人
    private int PAGE_SIZE = 10;//默认一页10条数据
    
    
    int curPage;
    
    ProgressDialog progress;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_people);
        initNaviView();
        initXListView();
    }
    
    @Override
    protected String title() {
        return "附近的人";
    }
    

    private void initXListView() {
        mListView = (XListView) findViewById(R.id.list_near);
        mListView.setOnItemClickListener(this);
        // 首先不允许加载更多
        mListView.setPullLoadEnable(false);
        // 允许下拉
        mListView.setPullRefreshEnable(true);
        // 设置监听器
        mListView.setXListViewListener(this);
        //
        mListView.pullRefreshing();

        adapter = new NearPeopleAdapter(this, nearFriendlist);
        mListView.setAdapter(adapter);
        initNearByList(false);
    }


   

    private void initNearByList(final boolean isUpdate) {
        if (!isUpdate) {
            progress = new ProgressDialog(NearPeopleActivity.this);
            progress.setMessage("正在查询附近的人...");
            progress.setCanceledOnTouchOutside(true);
            progress.show();
        }

        if (!BmobIMApplication.getInstance().isPointeEmpty()) {
        
            curPage = 1;
            findFriend(BmobIMApplication.getInstance().lastPoint,QUERY_KILOMETERS,null,curPage);
            
        } else {
            ToastView.showToast(this,"获取位置失败!");
            progress.dismiss();
            refreshPull();
        }

    }

  
    
    private void findFriend(BmobGeoPoint point, double far, Boolean sex, final int page){
        
       
        BmobQuery<User> query = new BmobQuery<>();
        //query.addWhereEqualTo("user", user);
        //query.include("friendUser");
        query.order("-updatedAt");
        if(sex != null){
            query.addWhereEqualTo("sex",sex);  //性别
        }
        query.addWhereWithinKilometers("location",point,far);   //查询最大公里
        
        query.setLimit(PAGE_SIZE); //一次查询10条数据
        
        //不包含自己
        query.addWhereNotEqualTo("objectId", BmobUser.getCurrentUser().getObjectId());
        
        if(page > 1){
            query.setSkip((page-1)*10);  //分页查询跳过数据
        }
        
        
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if(e == null){

                    if(page == 1){
                        nearFriendlist.clear();
                        refreshPull();
                    }else{
                        refreshLoad();
                    }
                    
                    if (list != null && list.size() > 0) {
                        //有附近的人
                        
                        if(list.size() < PAGE_SIZE){
                            //搜索完毕
                            //没有了
                            mListView.setPullLoadEnable(false);
                        }else{
                            mListView.setPullLoadEnable(true);
                        }
                        //ToastView.showToast(NearPeopleActivity.this,"");
                        nearFriendlist.addAll(list);
                        adapter.notifyDataSetChanged();
                        
                    } else {
                        //没有了
                        ToastView.showToast(NearPeopleActivity.this,"没有数据哦");
                        mListView.setPullLoadEnable(false);
                        curPage--;


                    }

                    
                }else{
                    e.printStackTrace();
                   //查询错误
                    ToastView.showToast(NearPeopleActivity.this,"查询失败"+e.getMessage());
                    if(page == 1){
                        refreshPull();
                    }else{
                        refreshLoad();
                        curPage--;
                    }
                }

                progress.dismiss();
            }

       
        });
    }
    
    

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
        // TODO Auto-generated method stub
        User user = (User) adapter.getItem(position - 1);
      
        Bundle bundle = new Bundle();
        bundle.putSerializable("u", user);
    
        startActivity(UserInfoActivity.class,bundle);
    }

    @Override
    public void onRefresh() {
        // TODO Auto-generated method stub
        initNearByList(true);
    }

    private void refreshLoad() {
        if (mListView.getPullLoading()) {
            mListView.stopLoadMore();
        }
    }

    private void refreshPull() {
        if (mListView.getPullRefreshing()) {
            mListView.stopRefresh();
        }
    }

    @Override
    public void onLoadMore() {
        // TODO Auto-generated method stub
        
        curPage++;
        findFriend(BmobIMApplication.getInstance().lastPoint,QUERY_KILOMETERS,null,curPage);

    }


}
