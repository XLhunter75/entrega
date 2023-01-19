package com.example.entregaprototipo.Adapters;

import static com.example.entregaprototipo.Shop.ActivityMainShop.LOGGED_USER;

import android.content.Context;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class AdpShop extends RecyclerView.Adapter<AdpShop.ViewHolder> {

    private Context context;
    private ArrayList<ProductData> list_product;
    private LayoutInflater mInflater;
    private Boolean useLargeCard, useBoxCard;

    public AdpShop(Context context, ArrayList<ProductData> list_product, Boolean useLargeCard, Boolean useBoxCard) {
        this.context = context;
        this.list_product = list_product;
        this.useLargeCard = useLargeCard;
        this.mInflater = LayoutInflater.from(context);
        this.useBoxCard = useBoxCard;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(this.useLargeCard){
            View view = mInflater.inflate(R.layout.resourse_card_popular_product, null);
            return new AdpShop.ViewHolder(view);
        } else if (this.useBoxCard) {
            View view = mInflater.inflate(R.layout.resource_card_product_box, null);
            return new AdpShop.ViewHolder(view);
        }
        else{
            View view = mInflater.inflate(R.layout.resource_card_product_box, null);
            return new AdpShop.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(list_product.get(position));
        ProductData p = list_product.get(position);
        //Reaccionara las cardviews en caso de click
        /*if(!isCart){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, ActivityItemProduct.class);
                    i.putExtra("Product", p);
                    context.startActivity(i);
                }
            });
        }*/
    }

    @Override
    public int getItemCount() {
        return list_product.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvNameProduct, tvPriceProduct;
        ImageView imageProduct;
        StorageReference mStorage = FirebaseStorage.getInstance().getReference();

        //Recogera componentes del layout
        public ViewHolder(@NonNull View itemView){
            super(itemView);
            tvNameProduct = itemView.findViewById(R.id.product_name);
            tvPriceProduct = itemView.findViewById(R.id.product_price);
            imageProduct = itemView.findViewById(R.id.image_product);
        }

        //Pondra la informacion al objeto
        public void bindData(@NonNull ProductData productData){
            tvNameProduct.setText(productData.getProduct_name());
            tvPriceProduct.setText(String.format( "%,.2f" ,productData.getProduct_price()) + "€");
            ArrayList<String> url_images = productData.getUrl_set_image_data();
            Uri product_image = Uri.parse(url_images.get(0));
            Glide.with(itemView).load(String.valueOf(product_image)).into(imageProduct);
        }
    }
}
