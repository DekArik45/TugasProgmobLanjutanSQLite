package com.example.sqlitetugas.ShowProductList;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.sqlitetugas.Database.DatabaseQueryClass;
import com.example.sqlitetugas.pojo.Product;
import com.example.sqlitetugas.UpdateProductInfo.ProductUpdateDialogFragment;
import com.example.sqlitetugas.UpdateProductInfo.ProductUpdateListener;
import com.example.sqlitetugas.R;
import com.example.sqlitetugas.Util.Config;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import java.util.List;

public class ProductListRecyclerViewAdapter extends RecyclerView.Adapter<CustomViewHolder> {

    private Context context;
    private List<Product> productList;
    private DatabaseQueryClass databaseQueryClass;

    public ProductListRecyclerViewAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
        databaseQueryClass = new DatabaseQueryClass(context);
        Logger.addLogAdapter(new AndroidLogAdapter());
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_item, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        final int itemPosition = position;
        final Product product = productList.get(position);

        holder.txtName.setText(product.getProductName());
        holder.txtMerk.setText(": "+product.getProductMerk());
        holder.txtJenis.setText(": "+product.getProductJenis());
        holder.txtDesc.setText(": "+product.getProductDesc());
        holder.txtHarga.setText(": Rp. "+String.valueOf(product.getProductHarga()));
        holder.txtQty.setText(": "+String.valueOf(product.getProductQty()));

        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setMessage("yakin ingin menghapus product ini?");
                        alertDialogBuilder.setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        deleteStudent(itemPosition);
                                    }
                                });

                alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        holder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProductUpdateDialogFragment productUpdateDialogFragment = ProductUpdateDialogFragment.newInstance(product.getId(), itemPosition, new ProductUpdateListener() {
                    @Override
                    public void onProductInfoUpdated(Product product, int position) {
                        productList.set(position, product);
                        notifyDataSetChanged();
                    }
                });
                productUpdateDialogFragment.show(((ProductListActivity) context).getSupportFragmentManager(), Config.UPDATE_PRODUCT);
            }
        });
    }

    private void deleteStudent(int position) {
        Product product = productList.get(position);
        long count = databaseQueryClass.deleteProductByID(product.getId());

        if(count>0){
            productList.remove(position);
            notifyDataSetChanged();
            ((ProductListActivity) context).viewVisibility();
            Toast.makeText(context, "Product berhasil di delete", Toast.LENGTH_LONG).show();
        } else
            Toast.makeText(context, "gagal delete, ada yang salah!", Toast.LENGTH_LONG).show();

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}
