package app.appified.modelclass;


import java.io.Serializable;

public class PagfListModel implements Serializable {
    public String appName;
    public String packageName;
    public String icon;
    public String originalPrice;
    public String timer;
    public String onOfferPrice;
    public Boolean isFree;

    public Boolean getIsFree() {
        return isFree;
    }

    public void setIsFree(Boolean isFree) {
        this.isFree = isFree;
    }

    public String getTimer() {
        return timer;
    }

    public void setTimer(String timer) {
        this.timer = timer;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(String originalPrice) {
        this.originalPrice = originalPrice;
    }


    public String getOnOfferPrice() {
        return onOfferPrice;
    }

    public void setOnOfferPrice(String onOfferPrice) {
        this.onOfferPrice = onOfferPrice;
    }
}


