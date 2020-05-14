package lordslightoftheworld.com.coursmodeprojet.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

public class CRUDProduct extends CreateTables{

    // Table name
    public static final String TABLE_NAME = "table_product";
    // Fields names
    public static final String COL_1 = "id";
    public static final String COL_2 = "productId";
    public static final String COL_3 = "title";
    public static final String COL_4 = "filename";
    // Data base version number
    private static final int VERSION_NUMBER = 1;

    public CRUDProduct(Context context) {
        super(context);
    }

    // List products
    public ArrayList<Product> list(){
        ArrayList<Product> products = new ArrayList<>();
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_NAME, null);
            if(cursor != null && cursor.getCount() > 0){
                while (cursor.moveToNext()){
                    int id = cursor.getInt(0);
                    int productId = cursor.getInt(1);
                    String title = cursor.getString(2);
                    String filename = cursor.getString(3);
                    products.add(new Product(productId, title, filename));
                }
            }
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "CRUDProduct-->list() : "+ex.getMessage());
        }
        finally {
            return products;
        }
    }

    // Add new produt
    public boolean add(Product product){
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(COL_2, product.getProductId());
            contentValues.put(COL_3, product.getTitle());
            contentValues.put(COL_4, product.getFilename());
            long result = db.insert(TABLE_NAME, null, contentValues);
            db.close();
            return !(result == -1);
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "CRUDProduct-->add() : "+ex.getMessage());
        }
        finally {
            return false;
        }
    }

    // Delete product
    public boolean deleteAll(){
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            int result = db.delete(TABLE_NAME, COL_1+" > ?", new String[]{"0"});
            return (result > 0);
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "CRUDProduct-->deleteAll() : "+ex.getMessage());
        }
        finally {
            return false;
        }
    }

    // Delete product
    public boolean delete(int id){
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            int result = db.delete(TABLE_NAME, COL_1+" = ?", new String[]{""+id});
            return (result > 0);
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "CRUDProduct-->delete() : "+ex.getMessage());
        }
        finally {
            return false;
        }
    }
}
