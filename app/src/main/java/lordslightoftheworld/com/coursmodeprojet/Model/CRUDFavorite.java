package lordslightoftheworld.com.coursmodeprojet.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import lordslightoftheworld.com.coursmodeprojet.Presenter.CommonPresenter;

public class CRUDFavorite extends CreateTables{

    // Table name
    public static final String TABLE_NAME = "table_favorites";
    // Fields names
    public static final String COL_1 = "id";
    public static final String COL_2 = "productId";
    public static final String COL_3 = "title";
    public static final String COL_4 = "filename";

    // Constructor
    public CRUDFavorite(Context context) {
        super(context);
    }

    // List all favorites products
    public ArrayList<Favorite> list(){
        ArrayList<Favorite> favorites = new ArrayList<>();
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_NAME, null);
            if(cursor != null && cursor.getCount() > 0){
                while (cursor.moveToNext()){
                    int id = cursor.getInt(0);
                    int productId = cursor.getInt(1);
                    String title = cursor.getString(2);
                    String filename = cursor.getString(3);
                    favorites.add(new Favorite(id, new Product(productId, title, filename)));
                }
            }
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "CRUDFavorite-->list() : "+ex.getMessage());
        }
        finally {
            return favorites;
        }
    }

    /**
     * Verify if product exists
     * @param productId
     * @return
     */
    public boolean isProductExists(int productId){
        ArrayList<Favorite> favorites = new ArrayList<>();
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE "+COL_2+" = ?", new String[]{""+productId});
            if(cursor != null && cursor.getCount() > 0){
                while (cursor.moveToNext()){
                    int id = cursor.getInt(0);
                    String product = cursor.getString(1);
                    favorites.add(new Favorite(id, CommonPresenter.getProductFromJSON(product)));
                }
            }
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "CRUDFavorite-->isProductExists() : "+ex.getMessage());
        }
        finally {
            return (favorites != null && favorites.size() > 0);
        }
    }

    // Add new product in favorites
    public boolean add(Favorite favorite){
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(COL_2, favorite.getProduct().getProductId());
            contentValues.put(COL_3, favorite.getProduct().getTitle());
            contentValues.put(COL_4, favorite.getProduct().getFilename());
            long result = db.insert(TABLE_NAME, null, contentValues);
            db.close();
            return !(result == -1);
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "CRUDFavorite-->add() : "+ex.getMessage());
        }
        finally {
            return false;
        }
    }

    // Delete favorite
    public boolean deleteByProductId(int productId){
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            int result = db.delete(TABLE_NAME, COL_2+" = ?", new String[]{""+productId});
            return (result > 0);
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "CRUDFavorite-->deleteByProductId() : "+ex.getMessage());
        }
        finally {
            return false;
        }
    }
}
