package com.aaaa.falas.bean;

/*
 * @Project_Name :SweetBox
 * @package_Name :com.xzwzz.sweetbox.bean
 * @AUTHOR      :xzwzz@vip.QqBean.com
 * @DATE        :2018/4/20
 */
public class PromotionBean {

    private String id;
    private String user_nicename;
    private String avatar;
    private String votes;

    @Override
    public String toString() {
        return "PromotionBean{" +
                "id='" + id + '\'' +
                ", user_nicename='" + user_nicename + '\'' +
                ", avatar='" + avatar + '\'' +
                ", votes='" + votes + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_nicename() {
        return user_nicename;
    }

    public void setUser_nicename(String user_nicename) {
        this.user_nicename = user_nicename;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getVotes() {
        return votes;
    }

    public void setVotes(String votes) {
        this.votes = votes;
    }
}
