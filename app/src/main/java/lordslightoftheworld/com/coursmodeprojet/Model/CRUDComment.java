package lordslightoftheworld.com.coursmodeprojet.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import java.util.ArrayList;


public class CRUDComment extends CreateTables{

    // Table name
    public static final String TABLE_NAME = "table_comment";
    // Fields names
    public static final String COL_1 = "id";
    public static final String COL_2 = "commentId";
    public static final String COL_3 = "productId";
    public static final String COL_4 = "email";
    public static final String COL_5 = "content";

    // Constructor
    public CRUDComment(Context context) {
        super(context);
    }

    // List comment by product id
    public ArrayList<Comment> listByProductId(int mProductId){
        ArrayList<Comment> comments = new ArrayList<>();
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE "+COL_3+" = ? ORDER BY "+COL_3+" DESC", new String[]{""+mProductId});
            if(cursor != null && cursor.getCount() > 0){
                while (cursor.moveToNext()){
                    int id = cursor.getInt(0);
                    int commentId = cursor.getInt(1);
                    int productId = cursor.getInt(2);
                    String email = cursor.getString(3);
                    String content = cursor.getString(4);
                    comments.add(new Comment(commentId, productId, email, content));
                }
            }
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "CRUDComment-->listByProductId() : "+ex.getMessage());
        }
        finally {
            return comments;
        }
    }

    // Add new comment
    public boolean add(Comment comment){
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(COL_2, comment.getCommentId());
            contentValues.put(COL_3, comment.getProductId());
            contentValues.put(COL_4, comment.getEmail());
            contentValues.put(COL_5, comment.getContent());
            long result = db.insert(TABLE_NAME, null, contentValues);
            db.close();
            return !(result == -1);
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "CRUDComment-->add() : "+ex.getMessage());
        }
        finally {
            return false;
        }
    }

    // Delete comment
    public boolean deleteAll(){
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            int result = db.delete(TABLE_NAME, COL_1+" > ?", new String[]{"0"});
            return (result > 0);
        }
        catch (Exception ex){
            Log.e("TAG_ERROR", "CRUDComment-->deleteAll() : "+ex.getMessage());
        }
        finally {
            return false;
        }
    }
}
