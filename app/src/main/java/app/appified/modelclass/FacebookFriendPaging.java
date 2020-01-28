package app.appified.modelclass;

class FacebookFriendPaging {


    public Cursors cursors;

    public Cursors getCursors() {
        return cursors;
    }

    public void setCursors(Cursors cursors) {
        this.cursors = cursors;
    }

    @Override
    public String toString() {
        return "FacebookFriendPaging{" +
                "cursors=" + cursors +
                '}';
    }
}



