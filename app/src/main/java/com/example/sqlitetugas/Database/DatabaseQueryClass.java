package com.example.sqlitetugas.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.widget.Toast;

import com.example.sqlitetugas.pojo.Product;
import com.example.sqlitetugas.Util.Config;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DatabaseQueryClass {

    private Context context;

    public DatabaseQueryClass(Context context){
        this.context = context;
        Logger.addLogAdapter(new AndroidLogAdapter());
    }

    public long insertProduct(Product product){

        long id = -1;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Config.COLUMN_PRODUCT_NAME, product.getProductName());
        contentValues.put(Config.COLUMN_PRODUCT_JENIS, product.getProductJenis());
        contentValues.put(Config.COLUMN_PRODUCT_MERK, product.getProductMerk());
        contentValues.put(Config.COLUMN_PRODUCT_QTY, product.getProductQty());
        contentValues.put(Config.COLUMN_PRODUCT_HARGA, product.getProductHarga());
        contentValues.put(Config.COLUMN_PRODUCT_DESC, product.getProductDesc());

        try {
            id = sqLiteDatabase.insertOrThrow(Config.TABLE_PRODUCT, null, contentValues);
        } catch (SQLiteException e){
            Logger.d("Exception: " + e.getMessage());
            Toast.makeText(context, "Operation failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            sqLiteDatabase.close();
        }

        return id;
    }

    public List<Product> getAllProduct(){

        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();

        Cursor cursor = null;
        try {

            cursor = sqLiteDatabase.query(Config.TABLE_PRODUCT, null, null, null, null, null, null, null);

            if(cursor!=null)
                if(cursor.moveToFirst()){
                    List<Product> productList = new ArrayList<>();
                    do {
                        int id = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_PRODUCT_ID));
                        String name = cursor.getString(cursor.getColumnIndex(Config.COLUMN_PRODUCT_NAME));
                        String jenis = cursor.getString(cursor.getColumnIndex(Config.COLUMN_PRODUCT_JENIS));
                        String merk = cursor.getString(cursor.getColumnIndex(Config.COLUMN_PRODUCT_MERK));
                        String desc = cursor.getString(cursor.getColumnIndex(Config.COLUMN_PRODUCT_DESC));
                        int harga = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_PRODUCT_HARGA));
                        int qty = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_PRODUCT_QTY));

                        productList.add(new Product(id, name, merk,desc,jenis,harga,qty));
                    }   while (cursor.moveToNext());

                    return productList;
                }
        } catch (Exception e){
            Logger.d("Exception: " + e.getMessage());
            Toast.makeText(context, "Operation failed", Toast.LENGTH_SHORT).show();
        } finally {
            if(cursor!=null)
                cursor.close();
            sqLiteDatabase.close();
        }

        return Collections.emptyList();
    }

    public Product getProductByID(long id){

        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();

        Cursor cursor = null;
        Product product = null;
        try {

            cursor = sqLiteDatabase.query(Config.TABLE_PRODUCT, null,
                    Config.COLUMN_PRODUCT_ID + " = ? ", new String[]{String.valueOf(id)},
                    null, null, null);

            if(cursor.moveToFirst()){
                int idProduct = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_PRODUCT_ID));
                String name = cursor.getString(cursor.getColumnIndex(Config.COLUMN_PRODUCT_NAME));
                String merk = cursor.getString(cursor.getColumnIndex(Config.COLUMN_PRODUCT_MERK));
                String jenis = cursor.getString(cursor.getColumnIndex(Config.COLUMN_PRODUCT_JENIS));
                String desc = cursor.getString(cursor.getColumnIndex(Config.COLUMN_PRODUCT_DESC));
                int harga = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_PRODUCT_HARGA));
                int qty = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_PRODUCT_QTY));

                product = new Product(idProduct, name, merk,desc,jenis,harga,qty);
            }
        } catch (Exception e){
            Logger.d("Exception: " + e.getMessage());
            Toast.makeText(context, "Operation failed", Toast.LENGTH_SHORT).show();
        } finally {
            if(cursor!=null)
                cursor.close();
            sqLiteDatabase.close();
        }

        return product;
    }

    public long updateProduct(Product product){

        long rowCount = 0;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Config.COLUMN_PRODUCT_NAME, product.getProductName());
        contentValues.put(Config.COLUMN_PRODUCT_MERK, product.getProductMerk());
        contentValues.put(Config.COLUMN_PRODUCT_JENIS, product.getProductJenis());
        contentValues.put(Config.COLUMN_PRODUCT_DESC, product.getProductDesc());
        contentValues.put(Config.COLUMN_PRODUCT_HARGA, product.getProductHarga());
        contentValues.put(Config.COLUMN_PRODUCT_QTY, product.getProductQty());

        try {
            rowCount = sqLiteDatabase.update(Config.TABLE_PRODUCT, contentValues,
                    Config.COLUMN_PRODUCT_ID + " = ? ",
                    new String[] {String.valueOf(product.getId())});
        } catch (SQLiteException e){
            Logger.d("Exception: " + e.getMessage());
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            sqLiteDatabase.close();
        }

        return rowCount;
    }

    public long deleteProductByID(long id) {
        long deletedRowCount = -1;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        try {
            deletedRowCount = sqLiteDatabase.delete(Config.TABLE_PRODUCT,
                                    Config.COLUMN_PRODUCT_ID + " = ? ",
                                    new String[]{ String.valueOf(id)});
        } catch (SQLiteException e){
            Logger.d("Exception: " + e.getMessage());
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            sqLiteDatabase.close();
        }

        return deletedRowCount;
    }

    public boolean deleteAllProducts(){
        boolean deleteStatus = false;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        try {
            //for "1" delete() method returns number of deleted rows
            //if you don't want row count just use delete(TABLE_NAME, null, null)
            sqLiteDatabase.delete(Config.TABLE_PRODUCT, null, null);

            long count = DatabaseUtils.queryNumEntries(sqLiteDatabase, Config.TABLE_PRODUCT);

            if(count==0)
                deleteStatus = true;

        } catch (SQLiteException e){
            Logger.d("Exception: " + e.getMessage());
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            sqLiteDatabase.close();
        }

        return deleteStatus;
    }

}