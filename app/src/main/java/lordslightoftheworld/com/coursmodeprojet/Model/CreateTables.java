package lordslightoftheworld.com.coursmodeprojet.Model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import lordslightoftheworld.com.coursmodeprojet.Presenter.CommonPresenter;

public class CreateTables extends SQLiteOpenHelper {

    // Database name
    private static final String DATABASE_NAME = "coursEnModeProjet.db";
    // Data base version number
    private static final int VERSION_NUMBER = 1;

    public CreateTables(Context context) {
        super(context, DATABASE_NAME, null, VERSION_NUMBER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+CRUDProduct.TABLE_NAME+" ("+CRUDProduct.COL_1+" INTEGER PRIMARY KEY AUTOINCREMENT, "+CRUDProduct.COL_2+" INTEGER, "+CRUDProduct.COL_3+" TEXT, "+CRUDProduct.COL_4+" TEXT)");
        db.execSQL("CREATE TABLE "+CRUDFavorite.TABLE_NAME+" ("+CRUDFavorite.COL_1+" INTEGER PRIMARY KEY AUTOINCREMENT, "+CRUDFavorite.COL_2+" INTEGER, "+CRUDFavorite.COL_3+" TEXT, "+CRUDFavorite.COL_4+" TEXT)");
        db.execSQL("CREATE TABLE "+CRUDComment.TABLE_NAME+" ("+CRUDComment.COL_1+" INTEGER PRIMARY KEY AUTOINCREMENT, "+CRUDComment.COL_2+" INTEGER, "+CRUDComment.COL_3+" INTEGER, "+CRUDComment.COL_4+" TEXT, "+CRUDComment.COL_5+" TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ CRUDFavorite.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+ CRUDComment.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+ CRUDProduct.TABLE_NAME);
    }
}
