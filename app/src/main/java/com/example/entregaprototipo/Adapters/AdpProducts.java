package com.example.entregaprototipo.Adapters;

import static com.example.entregaprototipo.Shop.ActivityMainShop.LOGGED_USER;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.entregaprototipo.ProductModel.BuyerSimpleData;
import com.example.entregaprototipo.ProductModel.ProductData;
import com.example.entregaprototipo.R;
import com.example.entregaprototipo.Shop.ActivityProductInfo;

import java.util.ArrayList;

public class AdpProducts extends RecyclerView.Adapter<AdpProducts.ViewHolder> {

    private Context context;
    private ArrayList<ProductData> list_product;
    private LayoutInflater mInflater;
    private Boolean useCategoryCard, useSmallCard;
    private Boolean boughtProductData, soldProductData, myProductData;
    private Boolean clickableCard;
    private ArrayList<BuyerSimpleData> buyerSimpleData;

    public AdpProducts(Context context, ArrayList<ProductData> list_product, Boolean useCategoryCard, Boolean useSmallCard, Boolean boughtProductData,
                       Boolean soldProductData, Boolean myProductData, Boolean clickableCard, ArrayList<BuyerSimpleData> buyerSimpleData) {
        this.context = context;
        this.list_product = list_product;
        this.useCategoryCard = useCategoryCard;
        this.mInflater = LayoutInflater.from(context);
        this.useSmallCard = useSmallCard;
        this.boughtProductData = boughtProductData;
        this.soldProductData = soldProductData;
        this.clickableCard = clickableCard;
        this.myProductData = myProductData;
        this.buyerSimpleData = buyerSimpleData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(this.useCategoryCard){
            View view = mInflater.inflate(R.layout.resourse_card_category_product, null);
            return new AdpProducts.ViewHolder(view, boughtProductData, soldProductData, myProductData, buyerSimpleData);
        } else if (this.useSmallCard) {
            View view = mInflater.inflate(R.layout.resource_card_product_small, null);
            return new AdpProducts.ViewHolder(view, boughtProductData, soldProductData, myProductData, buyerSimpleData);
        }
        else{
            View view = mInflater.inflate(R.layout.resource_card_product_large, null);
            return new AdpProducts.ViewHolder(view, boughtProductData, soldProductData, myProductData, buyerSimpleData);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(list_product.get(position));
        ProductData product_selected = list_product.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clickableCard){
                    Intent i = new Intent(v.getContext(), ActivityProductInfo.class);
                    i.putExtra("id_product", product_selected.getProduct_id());
                    v.getContext().startActivity(i);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return list_product.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tvNameProduct, tvPriceProduct, tvDescription;
        private ImageView imageProduct;
        private Boolean boughtProductData, soldProductData, myProductData;
        private ArrayList<BuyerSimpleData> buyerSimpleData;

        //Recogera componentes del layout
        public ViewHolder(@NonNull View itemView, Boolean boughtProductData, Boolean soldProductData, Boolean myProductData,
                          ArrayList<BuyerSimpleData> buyerSimpleData){
            super(itemView);
            tvNameProduct = itemView.findViewById(R.id.product_name);
            tvPriceProduct = itemView.findViewById(R.id.product_price);
            imageProduct = itemView.findViewById(R.id.image_product);
            tvDescription = itemView.findViewById(R.id.product_description);
            this.boughtProductData = boughtProductData;
            this.soldProductData = soldProductData;
            this.myProductData = myProductData;
            this.buyerSimpleData = buyerSimpleData;
        }

        //Pondra la informacion al objeto
        public void bindData(@NonNull ProductData productData){
            tvNameProduct.setText(productData.getProduct_name());
            tvPriceProduct.setText(String.format( "%,.2f" ,productData.getProduct_price()) + "€");
            tvDescription.setText(productData.getProduct_description());
            ArrayList<String> url_images = productData.getUrl_set_image_data();
            Uri product_image = Uri.parse(url_images.get(0));
            Glide.with(itemView).load(String.valueOf(product_image)).into(imageProduct);

            if(boughtProductData){
                tvNameProduct.setText("Vendedor:");
                tvDescription.setText(productData.getUser_name_product());
            }

            if(myProductData){
                tvPriceProduct.setText("Quedan: " +productData.getAmmount_aviable());
                if(productData.getAmmount_aviable() == 0){
                    tvPriceProduct.setText("Vendido todo");
                }
            }

            if(soldProductData){
                for(BuyerSimpleData buyer: buyerSimpleData){
                    if(buyer.getProduct_id().equals(productData.getProduct_id())){
                        tvNameProduct.setText("Comprador:");
                        tvDescription.setText(buyer.getBuyer_name());
                    }
                }
            }

        }
    }
}
