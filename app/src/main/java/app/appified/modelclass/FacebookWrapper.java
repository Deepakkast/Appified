package app.appified.modelclass;

import java.io.Serializable;

public class FacebookWrapper implements Serializable {
    public String id;
    public String name;
    public String email;
    public String mobile;
   // public  String profile;
    public FriendsFacebook friends;
    //public FriendsFacebook friends;
    public ProfilePicture picture;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProfilePicture getPicture() {
        return picture;
    }

    public void setPicture(ProfilePicture picture) {
        this.picture = picture;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public FriendsFacebook getFriends() {
        return friends;
    }

    public void setFriends(FriendsFacebook friends) {
        this.friends = friends;
    }

    @Override
    public String toString() {
        return "FacebookWrapper{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", mobile='" + mobile + '\'' +
                ", friends=" + friends +
                '}';
    }
}

