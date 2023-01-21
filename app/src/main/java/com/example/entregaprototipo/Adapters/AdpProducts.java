package com.example.entregaprototipo.Adapters;

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
import com.example.entregaprototipo.ProductModel.ProductData;
import com.example.entregaprototipo.R;
import com.example.entregaprototipo.Shop.ActivityProductInfo;

import java.util.ArrayList;

public class AdpProducts extends RecyclerView.Adapter<AdpProducts.ViewHolder> {

    private Context context;
    private ArrayList<ProductData> list_product;
    private LayoutInflater mInflater;
    private Boolean useCategoryCard, useSmallCard;

    public AdpProducts(Context context, ArrayList<ProductData> list_product, Boolean useCategoryCard, Boolean useSmallCard) {
        this.context = context;
        this.list_product = list_product;
        this.useCategoryCard = useCategoryCard;
        this.mInflater = LayoutInflater.from(context);
        this.useSmallCard = useSmallCard;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(this.useCategoryCard){
            View view = mInflater.inflate(R.layout.resourse_card_category_product, null);
            return new AdpProducts.ViewHolder(view);
        } else if (this.useSmallCard) {
            View view = mInflater.inflate(R.layout.resource_card_product_small, null);
            return new AdpProducts.ViewHolder(view);
        }
        else{
            View view = mInflater.inflate(R.layout.resource_card_product_large, null);
            return new AdpProducts.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(list_product.get(position));
        ProductData product_selected = list_product.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), ActivityProductInfo.class);
                i.putExtra("id_product", product_selected.getProduct_id());
                v.getContext().startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list_product.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvNameProduct, tvPriceProduct, tvDescription;
        ImageView imageProduct;

        //Recogera componentes del layout
        public ViewHolder(@NonNull View itemView){
            super(itemView);
            tvNameProduct = itemView.findViewById(R.id.product_name);
            tvPriceProduct = itemView.findViewById(R.id.product_price);
            imageProduct = itemView.findViewById(R.id.image_product);
            tvDescription = itemView.findViewById(R.id.product_description);
        }

        //Pondra la informacion al objeto
        public void bindData(@NonNull ProductData productData){
            tvNameProduct.setText(productData.getProduct_name());
            tvPriceProduct.setText(String.format( "%,.2f" ,productData.getProduct_price()) + "€");
            tvDescription.setText(productData.getProduct_description());
            ArrayList<String> url_images = productData.getUrl_set_image_data();
            Uri product_image = Uri.parse(url_images.get(0));
            Glide.with(itemView).load(String.valueOf(product_image)).into(imageProduct);
        }
    }
}
