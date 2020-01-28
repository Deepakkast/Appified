package app.appified.modelclass;

import java.util.ArrayList;

public class FriendsFacebook {

    public ArrayList<FbFriendDTO> data;
    public SummaryDTO summary;
    public FacebookFriendPaging paging;


    public ArrayList<FbFriendDTO> getData() {
        return data;
    }

    public SummaryDTO getSummary() {
        return summary;
    }

    public void setSummary(SummaryDTO summary) {
        this.summary = summary;
    }

    public FacebookFriendPaging getPaging() {
        return paging;
    }

    public void setPaging(FacebookFriendPaging paging) {
        this.paging = paging;
    }




    public FriendsFacebook(ArrayList<FbFriendDTO> data) {
        this.data = data;
    }


    public void setData(ArrayList<FbFriendDTO> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "FriendsFacebook{" +
                "data=" + data +
                '}';
    }
}
