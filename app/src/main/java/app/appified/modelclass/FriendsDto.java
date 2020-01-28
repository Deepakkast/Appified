package app.appified.modelclass;

import java.io.Serializable;
import java.util.List;

public class FriendsDto implements Serializable {

    public String id;
    public String userName;
    public String profile;
    public List<UserApp> apps;
    public List<UserApp>commonApps;


    public FriendsDto() {
        this.id=id;
        this.userName = userName;
        this.profile = profile;
        this.apps = apps;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;

    }

    public List<UserApp> getCommonApps() {
        return commonApps;
    }

    public void setCommonApps(List<UserApp> commonApps) {
        this.commonApps = commonApps;
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

    public List<UserApp> getApps() {
        return apps;
    }

    public void setApps(List<UserApp> apps) {
        this.apps = apps;
    }


    @Override
    public String toString() {
        return "FriendsDto{" +
                "userName='" + userName + '\'' +
                ", profile='" + profile + '\'' +
                ", apps=" + apps +
                '}';
    }
}
