package com.example.sqlitetugas.UpdateProductInfo;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.sqlitetugas.Database.DatabaseQueryClass;
import com.example.sqlitetugas.pojo.Product;
import com.example.sqlitetugas.R;
import com.example.sqlitetugas.Util.Config;


public class ProductUpdateDialogFragment extends DialogFragment {

    private static long idProduct;
    private static int productItemPosition;
    private static ProductUpdateListener productUpdateListener;

    private Product mProduct;

    private EditText editProductName;
    private EditText editProductJenis;
    private EditText editProductMerek;
    private EditText editProductDescription;
    private EditText editProductHarga;
    private EditText editProductQty;
    private Button updateButton;
    private Button cancelButton;

    private String nameString = "", jenisString = "",merekString = "",descString = "";
    private int hargaInt = 0, qtyInt = 0;

    private DatabaseQueryClass databaseQueryClass;

    public ProductUpdateDialogFragment() {
        // Required empty public constructor
    }

    public static ProductUpdateDialogFragment newInstance(long id, int position, ProductUpdateListener listener){
        idProduct = id;
        productItemPosition = position;
        productUpdateListener = listener;
        ProductUpdateDialogFragment productUpdateDialogFragment = new ProductUpdateDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", "Update Product");
        productUpdateDialogFragment.setArguments(args);

        productUpdateDialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);

        return productUpdateDialogFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_product_update_dialog, container, false);

        databaseQueryClass = new DatabaseQueryClass(getContext());

        editProductName = view.findViewById(R.id.update_product_name);
        editProductMerek = view.findViewById(R.id.update_product_merk);
        editProductJenis = view.findViewById(R.id.update_product_jenis);
        editProductDescription = view.findViewById(R.id.update_product_desc);
        editProductHarga = view.findViewById(R.id.update_harga);
        editProductQty = view.findViewById(R.id.update_qty);

        updateButton = view.findViewById(R.id.updateButton);
        cancelButton = view.findViewById(R.id.cancelButton);

        String title = getArguments().getString(Config.TITLE);
        getDialog().setTitle(title);

        mProduct = databaseQueryClass.getProductByID(idProduct);

        if(mProduct !=null){
            editProductName.setText(mProduct.getProductName());
            editProductMerek.setText(mProduct.getProductMerk());
            editProductJenis.setText(mProduct.getProductJenis());
            editProductDescription.setText(mProduct.getProductDesc());
            editProductHarga.setText(String.valueOf(mProduct.getProductHarga()));
            editProductQty.setText(String.valueOf(mProduct.getProductQty()));

            updateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    nameString = editProductName.getText().toString();
                    jenisString = editProductJenis.getText().toString();
                    merekString = editProductMerek.getText().toString();
                    descString = editProductDescription.getText().toString();
                    hargaInt = Integer.parseInt(editProductHarga.getText().toString());
                    qtyInt = Integer.parseInt(editProductQty.getText().toString());

                    mProduct.setProductName(nameString);
                    mProduct.setProductMerk(merekString);
                    mProduct.setProductJenis(jenisString);
                    mProduct.setProductDesc(descString);
                    mProduct.setProductHarga(hargaInt);
                    mProduct.setProductQty(qtyInt);

                    long id = databaseQueryClass.updateProduct(mProduct);

                    if(id>0){
                        productUpdateListener.onProductInfoUpdated(mProduct, productItemPosition);
                        getDialog().dismiss();
                    }
                }
            });

            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getDialog().dismiss();
                }
            });

        }

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            //noinspection ConstantConditions
            dialog.getWindow().setLayout(width, height);
        }
    }

}
