package artc.artc.keepup;

public class Shares {

    String sharesTimestamp;
    String sharesTicker;
    String sharesPrice;
    String sharesQty;

    public Shares(){

    }

    public Shares(String sharesTimestamp, String sharesTicker, String sharesPrice, String sharesQty) {
        this.sharesTimestamp = sharesTimestamp;
        this.sharesTicker = sharesTicker;
        this.sharesPrice = sharesPrice;
        this.sharesQty = sharesQty;
    }

    public String getSharesTimestamp() {
        return sharesTimestamp;
    }

    public void setSharesTimestamp(String sharesTimestamp) {
        this.sharesTimestamp = sharesTimestamp;
    }

    public String getSharesTicker() {
        return sharesTicker;
    }

    public void setSharesTicker(String sharesTicker) {
        this.sharesTicker = sharesTicker;
    }

    public String getSharesPrice() {
        return sharesPrice;
    }

    public void setSharesPrice(String sharesPrice) {
        this.sharesPrice = sharesPrice;
    }

    public String getSharesQty() {
        return sharesQty;
    }

    public void setSharesQty(String sharesQty) {
        this.sharesQty = sharesQty;
    }

}