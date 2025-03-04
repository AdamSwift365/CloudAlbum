package cn.bmob.wx.model.i;

import cn.bmob.wx.bean.User;
import cn.bmob.newim.listener.BmobListener1;
import cn.bmob.v3.exception.BmobException;

public abstract class QueryUserListener extends BmobListener1<User> {

    public abstract void done(User s, BmobException e);

    @Override
    protected void postDone(User o, BmobException e) {
        done(o, e);
    }
}
