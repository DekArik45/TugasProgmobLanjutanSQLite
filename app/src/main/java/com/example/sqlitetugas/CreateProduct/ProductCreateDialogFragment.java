package com.example.sqlitetugas.CreateProduct;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.sqlitetugas.Database.DatabaseQueryClass;
import com.example.sqlitetugas.R;
import com.example.sqlitetugas.Util.Config;
import com.example.sqlitetugas.pojo.Product;


public class ProductCreateDialogFragment extends DialogFragment {

    private static ProductCreateListener productCreateListener;

    private EditText editProductName;
    private EditText editProductJenis;
    private EditText editProductMerek;
    private EditText editProductDescription;
    private EditText editProductHarga;
    private EditText editProductQty;
    private Button createButton;
    private Button cancelButton;

    private String nameString = "", jenisString = "",merekString = "",descString = "";
    private int hargaInt = 0, qtyInt = 0;

    public ProductCreateDialogFragment() {
        // Required empty public constructor
    }

    public static ProductCreateDialogFragment newInstance(String title, ProductCreateListener listener){
        productCreateListener = listener;
        ProductCreateDialogFragment productCreateDialogFragment = new ProductCreateDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        productCreateDialogFragment.setArguments(args);

        productCreateDialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);

        return productCreateDialogFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_product_create_dialog, container, false);

        editProductName = view.findViewById(R.id.create_product_name);
        editProductMerek = view.findViewById(R.id.create_product_merk);
        editProductJenis = view.findViewById(R.id.create_product_jenis);
        editProductDescription = view.findViewById(R.id.create_product_desc);
        editProductHarga = view.findViewById(R.id.create_harga);
        editProductQty = view.findViewById(R.id.create_qty);

        createButton = view.findViewById(R.id.createButton);
        cancelButton = view.findViewById(R.id.createCancelButton);

        String title = getArguments().getString(Config.TITLE);
        getDialog().setTitle(title);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nameString = editProductName.getText().toString();
                merekString = editProductMerek.getText().toString();
                jenisString = editProductJenis.getText().toString();
                descString = editProductDescription.getText().toString();
                if (editProductHarga.getText().toString().equals("")){
                    hargaInt=0;
                }
                else {
                    hargaInt = Integer.parseInt(editProductHarga.getText().toString());
                }

                if (editProductQty.getText().toString().equals("")){
                    qtyInt=0;
                }
                else {
                    qtyInt = Integer.parseInt(editProductQty.getText().toString());
                }

                Product product = new Product(-1, nameString,merekString,descString,jenisString,hargaInt,qtyInt);

                DatabaseQueryClass databaseQueryClass = new DatabaseQueryClass(getContext());

                long id = databaseQueryClass.insertProduct(product);

                if(id>0){
                    product.setId(id);
                    productCreateListener.onProductCreated(product);
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
