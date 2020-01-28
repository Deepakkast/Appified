package app.appified.modelclass;

public class ProfileData {
    public String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "ProfileData{" +
                "url='" + url + '\'' +
                '}';
    }
}
