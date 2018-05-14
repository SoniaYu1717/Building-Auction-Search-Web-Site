package edu.ucla.cs.cs144;

public class ItemResult {

    private String itemID;
    private String name;
    private String currently;
    private String first_bid;
    private String buy_price;
    private String num_of_bids;
    private String location;
    private String country;
    private String started;
    private String ends;
    private String longitude;
    private String latitude;
    private String sellerID;
    private String rating;
    private String description;
    private String[] categories;
    private Bid[] bids;

    public ItemResult() {}

    public ItemResult(String itemID, String name, String currently, String first_bid, String num_of_bids,
                      String started, String ends, String sellerID, String rating, String description) {
        this.itemID = itemID;
        this.name = name;
        this.currently = currently;
        this.first_bid = first_bid;
        this.num_of_bids = num_of_bids;
        this.started = started;
        this.ends = ends;
        this.sellerID = sellerID;
        this.rating = rating;
        this.description = description;
    }

    public void setBuyPrice (String buy_price) {
        if (buy_price != null)
            this.buy_price = buy_price;
    }

    public void setLocationInfo (String location, String country, String longitude, String latitude) {
        this.location = location;
        this.country = country;
        if (longitude != null) {
            this.longitude = longitude;
            this.latitude = latitude;
        } else {
            this.longitude = "";
            this.latitude = "";
        }
    }

    public void setCategories (String[] categories) {
        this.categories = categories;
    }

    public void setBids(Bid[] bids) {
        this.bids = bids;
    }

    public String[] getCategories() {
        return categories;
    }

    public Bid[] getBids() {
        return bids;
    }

    public String getItemID() {
        return itemID;
    }

    public String getName() {
        return name;
    }

    public String getCurrently() {
        return currently;
    }

    public String getFirstBid() {
        return first_bid;
    }

    public String getBuyPrice() {
        return buy_price;
    }

    public String getNumBids() {
        return num_of_bids;
    }

    public String getLocation() {
        return location;
    }

    public String getCountry() {
        return country;
    }

    public String getStarted() {
        return started;
    }

    public String getEnds() {
        return ends;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getSellerID() {
        return sellerID;
    }

    public String getRating() {
        return rating;
    }

    public String getDescription() {
        return description;
    }

}
