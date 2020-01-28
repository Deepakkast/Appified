package app.appified.modelclass;

public class InstallDetailDto  {
    public String profile;
    public String username;
    public String installdate;

    public InstallDetailDto(String profile, String username, String installdate) {
        this.profile = profile;
        this.username = username;
        this.installdate = installdate;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getInstalldate() {
        return installdate;
    }

    public void setInstalldate(String installdate) {
        this.installdate = installdate;
    }

    @Override
    public String toString() {
        return "InstallDetailDto{" +
                "profile='" + profile + '\'' +
                ", username='" + username + '\'' +
                ", installdate='" + installdate + '\'' +
                '}';
    }
}
