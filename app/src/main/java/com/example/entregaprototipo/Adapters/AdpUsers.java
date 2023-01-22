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
import com.example.entregaprototipo.Shop.ActivityUserInfo;
import com.example.entregaprototipo.UserModel.UserData;

import java.util.ArrayList;

public class AdpUsers extends RecyclerView.Adapter<AdpUsers.ViewHolder> {

    private Context context;
    private ArrayList<UserData> list_users;
    private LayoutInflater mInflater;

    public AdpUsers(Context context, ArrayList<UserData> list_users) {
        this.context = context;
        this.list_users = list_users;
        this.mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.resource_card_user_large, null);
        return new AdpUsers.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(list_users.get(position));
        UserData user_selected = list_users.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), ActivityUserInfo.class);
                i.putExtra("seller_uid", user_selected.getUid());
                v.getContext().startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list_users.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvUserName;
        ImageView imageProfile;

        //Recogera componentes del layout
        public ViewHolder(@NonNull View itemView){
            super(itemView);
            tvUserName = itemView.findViewById(R.id.user_name);
            imageProfile = itemView.findViewById(R.id.user_profile);
        }

        //Pondra la informacion al objeto
        public void bindData(@NonNull UserData user){
            tvUserName.setText(user.getName());
            Uri product_image = Uri.parse(user.getProfileURL());
            Glide.with(itemView).load(String.valueOf(product_image)).into(imageProfile);
        }
    }
}
