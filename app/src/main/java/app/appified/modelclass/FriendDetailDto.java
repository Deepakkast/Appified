package app.appified.modelclass;

import java.io.Serializable;
import java.util.List;

public class FriendDetailDto implements Serializable {

    public String id;
    public String userName;
    public String profile;
    public String fbId;
    public List<UserApp> apps;


    public FriendDetailDto() {
        this.userName = userName;
        this.profile = profile;
    }

    public List<UserApp> getApps() {
        return apps;
    }

    public void setApps(List<UserApp> apps) {
        this.apps = apps;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFbId() {
        return fbId;
    }

    public void setFbId(String fbId) {
        this.fbId = fbId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }


    @Override
    public String toString() {
        return "FriendDetailDto{" +
                "userName='" + userName + '\'' +
                ", profile='" + profile + '\'' +
                '}';
    }
}
