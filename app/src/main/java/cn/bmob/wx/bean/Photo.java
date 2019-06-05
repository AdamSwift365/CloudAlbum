package cn.bmob.wx.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;


public class Photo extends BmobObject {

    // 相片文件
    private BmobFile photo;
    // 包含人脸token
    private String face_token;
    // 所属相册token
    private String facealbum_token;
    // 上传的用户
    private User user;


    public BmobFile getPhoto() {
        return photo;
    }

    public void setPhoto(BmobFile photo) {
        this.photo = photo;
    }

    public String getFace_token() {
        return face_token;
    }

    public void setFace_token(String face_token) {
        this.face_token = face_token;
    }

    public String getFacealbum_token() {
        return facealbum_token;
    }

    public void setFacealbum_token(String facealbum_token) {
        this.facealbum_token = facealbum_token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
