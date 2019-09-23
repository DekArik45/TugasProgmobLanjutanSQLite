package com.example.sqlitetugas.ShowProductList;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sqlitetugas.R;

public class CustomViewHolder extends RecyclerView.ViewHolder {

    TextView txtName, txtMerk, txtJenis, txtDesc,txtHarga,txtQty;
    ImageView imgDelete;
    ImageView imgEdit;

    public CustomViewHolder(View itemView) {
        super(itemView);

        txtName = itemView.findViewById(R.id.item_product_name);
        txtMerk = itemView.findViewById(R.id.item_product_merek);
        txtJenis = itemView.findViewById(R.id.item_product_jenis);
        txtDesc = itemView.findViewById(R.id.item_product_description);
        txtHarga = itemView.findViewById(R.id.item_product_harga);
        txtQty = itemView.findViewById(R.id.item_product_qty);
        imgDelete = itemView.findViewById(R.id.crossImageView);
        imgEdit = itemView.findViewById(R.id.editImageView);
    }
}
