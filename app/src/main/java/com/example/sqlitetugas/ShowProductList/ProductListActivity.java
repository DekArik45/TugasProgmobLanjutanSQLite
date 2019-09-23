package com.example.sqlitetugas.ShowProductList;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.sqlitetugas.Database.DatabaseQueryClass;
import com.example.sqlitetugas.pojo.Product;
import com.example.sqlitetugas.CreateProduct.ProductCreateDialogFragment;
import com.example.sqlitetugas.CreateProduct.ProductCreateListener;
import com.example.sqlitetugas.R;
import com.example.sqlitetugas.Util.Config;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

public class ProductListActivity extends AppCompatActivity implements ProductCreateListener {

    private DatabaseQueryClass databaseQueryClass = new DatabaseQueryClass(this);

    private List<Product> productList = new ArrayList<>();

    private TextView productListEmptyTextView;
    private RecyclerView recyclerView;
    private ProductListRecyclerViewAdapter productListRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Logger.addLogAdapter(new AndroidLogAdapter());

        recyclerView = (RecyclerView) findViewById(R.id.productRecyclerView);
        productListEmptyTextView = (TextView) findViewById(R.id.emptyProductListTextView);

        productList.addAll(databaseQueryClass.getAllProduct());

        productListRecyclerViewAdapter = new ProductListRecyclerViewAdapter(this, productList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(productListRecyclerViewAdapter);

        viewVisibility();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openStudentCreateDialog();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==R.id.action_delete){

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("Yakin ingin menghapus semua Products?");
            alertDialogBuilder.setPositiveButton("Yes",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            boolean isAllDeleted = databaseQueryClass.deleteAllProducts();
                            if(isAllDeleted){
                                productList.clear();
                                productListRecyclerViewAdapter.notifyDataSetChanged();
                                viewVisibility();
                            }
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

        return super.onOptionsItemSelected(item);
    }

    public void viewVisibility() {
        if(productList.isEmpty())
            productListEmptyTextView.setVisibility(View.VISIBLE);
        else
            productListEmptyTextView.setVisibility(View.GONE);
    }

    private void openStudentCreateDialog() {
        ProductCreateDialogFragment productCreateDialogFragment = ProductCreateDialogFragment.newInstance("Create Product", this);
        productCreateDialogFragment.show(getSupportFragmentManager(), Config.CREATE_PRODUCT);
    }

    @Override
    public void onProductCreated(Product product) {
        productList.add(product);
        productListRecyclerViewAdapter.notifyDataSetChanged();
        viewVisibility();
        Logger.d(product.getProductName());
    }

}
