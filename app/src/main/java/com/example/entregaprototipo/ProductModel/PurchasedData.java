package com.example.entregaprototipo.ProductModel;

public class PurchasedData {

    private String purchase_id;
    private String buyer_uid;
    private String buyer_profile_url;
    private String buyer_name;

    private String seller_uid;
    private String seller_profile_url;
    private String seller_name;

    private String product_id;
    private Double product_price;
    private String product_name;
    private String product_image_url;


    public PurchasedData(String purchase_id,String buyer_uid, String buyer_profile_url, String seller_uid, String seller_profile_url, String product_id, Double product_price, String product_name, String product_image_url, String buyer_name, String seller_name) {
        this.purchase_id = purchase_id;
        this.buyer_uid = buyer_uid;
        this.buyer_profile_url = buyer_profile_url;
        this.seller_uid = seller_uid;
        this.seller_profile_url = seller_profile_url;
        this.product_id = product_id;
        this.product_price = product_price;
        this.product_name = product_name;
        this.product_image_url = product_image_url;
        this.seller_name = seller_name;
        this.buyer_name = buyer_name;

    }

    public String getBuyer_name() {
        return buyer_name;
    }

    public void setBuyer_name(String buyer_name) {
        this.buyer_name = buyer_name;
    }

    public String getSeller_name() {
        return seller_name;
    }

    public void setSeller_name(String seller_name) {
        this.seller_name = seller_name;
    }

    public String getPurchase_id() {
        return purchase_id;
    }

    public void setPurchase_id(String purchase_id) {
        this.purchase_id = purchase_id;
    }

    public String getBuyer_uid() {
        return buyer_uid;
    }

    public void setBuyer_uid(String buyer_uid) {
        this.buyer_uid = buyer_uid;
    }

    public String getBuyer_profile_url() {
        return buyer_profile_url;
    }

    public void setBuyer_profile_url(String buyer_profile_url) {
        this.buyer_profile_url = buyer_profile_url;
    }

    public String getSeller_uid() {
        return seller_uid;
    }

    public void setSeller_uid(String seller_uid) {
        this.seller_uid = seller_uid;
    }

    public String getSeller_profile_url() {
        return seller_profile_url;
    }

    public void setSeller_profile_url(String seller_profile_url) {
        this.seller_profile_url = seller_profile_url;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public Double getProduct_price() {
        return product_price;
    }

    public void setProduct_price(Double product_price) {
        this.product_price = product_price;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_image_url() {
        return product_image_url;
    }

    public void setProduct_image_url(String product_image_url) {
        this.product_image_url = product_image_url;
    }
}
