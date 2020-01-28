package app.appified.modelclass;

public  class SummaryDTO {
    public int total_count;

    public int getTotal_count() {
        return total_count;
    }

    public void setTotal_count(int total_count) {
        this.total_count = total_count;
    }

    @Override
    public String toString() {
        return "SummaryDTO{" +
                "total_count=" + total_count +
                '}';
    }
}
