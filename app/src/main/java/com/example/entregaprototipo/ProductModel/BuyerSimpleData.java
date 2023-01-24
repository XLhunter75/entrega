package com.example.entregaprototipo.ProductModel;

public class BuyerSimpleData {

    private String buyer_name;
    private String buyer_uid;
    private String product_id;

    public BuyerSimpleData(String buyer_name, String buyer_uid, String product_id) {
        this.buyer_name = buyer_name;
        this.buyer_uid = buyer_uid;
        this.product_id = product_id;
    }

    public String getBuyer_name() {
        return buyer_name;
    }

    public void setBuyer_name(String buyer_name) {
        this.buyer_name = buyer_name;
    }

    public String getBuyer_uid() {
        return buyer_uid;
    }

    public void setBuyer_uid(String buyer_uid) {
        this.buyer_uid = buyer_uid;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }
}
