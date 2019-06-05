package cn.bmob.wx.bean;

import cn.bmob.v3.BmobObject;

public class FaceAlbum extends BmobObject {

    // 包含人脸token
    private String face_token;
    // 相册token
    private String facealbum_token;
    // 用户
    private User user;
    // 封面相片
    private Photo cover;

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

    public Photo getCover() {
        return cover;
    }

    public void setCover(Photo cover) {
        this.cover = cover;
    }
}
