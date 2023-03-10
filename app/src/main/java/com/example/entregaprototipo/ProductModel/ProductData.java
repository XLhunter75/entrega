package com.example.entregaprototipo.ProductModel;

import java.io.Serializable;
import java.util.ArrayList;

public class ProductData implements Serializable {

    private String product_id;
    private String user_name_product;
    private String uid_user;
    private String user_profile_url;
    private String product_name;
    private String product_description;
    private String product_category;
    private double product_price;
    private ArrayList<String> url_set_image_data;
    private boolean product_available;
    private ArrayList<String> user_liked;
    private int ammount_aviable;

    public ProductData(String product_id, String user_name_product, String uid_user, String product_name, String product_description, String product_category,
                       double product_price, ArrayList<String> url_main_image_data, boolean product_available, ArrayList<String> user_liked, String user_profile_url,
                       int ammount_aviable) {
        this.user_name_product = user_name_product;
        this.uid_user = uid_user;
        this.product_name = product_name;
        this.product_description = product_description;
        this.product_category = product_category;
        this.product_price = product_price;
        this.url_set_image_data = url_main_image_data;
        this.product_available = product_available;
        this.user_liked = user_liked;
        this.product_id = product_id;
        this.user_profile_url = user_profile_url;
        this.ammount_aviable = ammount_aviable;
    }

    public int getAmmount_aviable() {
        return ammount_aviable;
    }

    public void setAmmount_aviable(int ammount_aviable) {
        this.ammount_aviable = ammount_aviable;
    }

    public String getUser_profile_url() {
        return user_profile_url;
    }

    public void setUser_profile_url(String user_profile_url) {
        this.user_profile_url = user_profile_url;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public ArrayList<String> getUser_liked() {
        return user_liked;
    }

    public void setUser_liked(ArrayList<String> user_liked) {
        this.user_liked = user_liked;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_description() {
        return product_description;
    }

    public void setProduct_description(String product_description) {
        this.product_description = product_description;
    }

    public double getProduct_price() {
        return product_price;
    }

    public void setProduct_price(double product_price) {
        this.product_price = product_price;
    }

    public String getImage_data() {
        return user_name_product;
    }

    public void setImage_data(String image_data) {
        this.user_name_product = image_data;
    }

    public String getProduct_category() {
        return product_category;
    }

    public void setProduct_category(String product_category) {
        this.product_category = product_category;
    }

    public String getUser_name_product() {
        return user_name_product;
    }

    public void setUser_name_product(String user_name_product) {
        this.user_name_product = user_name_product;
    }

    public String getUid_user() {
        return uid_user;
    }

    public void setUid_user(String uid_user) {
        this.uid_user = uid_user;
    }

    public ArrayList<String> getUrl_set_image_data() {
        return url_set_image_data;
    }

    public void setUrl_set_image_data(ArrayList<String> url_set_image_data) {
        this.url_set_image_data = url_set_image_data;
    }

    public boolean isProduct_available() {
        return product_available;
    }

    public void setProduct_available(boolean product_available) {
        this.product_available = product_available;
    }

    @Override
    public String toString() {
        return "ProductData{" +
                "user_name_product='" + user_name_product + '\'' +
                ", uid_user='" + uid_user + '\'' +
                ", product_name='" + product_name + '\'' +
                ", product_description='" + product_description + '\'' +
                ", product_category='" + product_category + '\'' +
                ", product_price=" + product_price +
                ", url_main_image_data='" + url_set_image_data + '\'' +
                ", product_available=" + product_available +
                '}';
    }
}
