package app.appified.modelclass;

public class ProfilePicture {
    public ProfileData data;

    public ProfileData getData() {
        return data;
    }

    public void setData(ProfileData data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ProfilePicture{" +
                "data=" + data +
                '}';
    }
}
