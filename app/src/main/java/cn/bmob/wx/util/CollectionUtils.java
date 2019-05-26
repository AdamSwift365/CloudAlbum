package cn.bmob.wx.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import cn.bmob.v3.BmobUser;


public class CollectionUtils {

    public static boolean isNotNull(Collection<?> collection) {
        if (collection != null && collection.size() > 0) {
            return true;
        }
        return false;
    }

    /**
     * list转map
     * 以用户名为key
     *
     * @return Map<String,BmobUser>
     * @throws
     */
    public static Map<String, BmobUser> list2map(List<BmobUser> users) {
        Map<String, BmobUser> friends = new HashMap<String, BmobUser>();
        for (BmobUser user : users) {
            friends.put(user.getUsername(), user);
        }
        return friends;
    }


    /**
     * map转list
     *
     * @return List<BmobUser>
     * @throws
     * @Title: map2list
     */
    public static List<BmobUser> map2list(Map<String, BmobUser> maps) {
        List<BmobUser> users = new ArrayList<BmobUser>();
        Iterator<Entry<String, BmobUser>> iterator = maps.entrySet().iterator();
        while (iterator.hasNext()) {
            Entry<String, BmobUser> entry = iterator.next();
            users.add(entry.getValue());
        }
        return users;
    }
}
