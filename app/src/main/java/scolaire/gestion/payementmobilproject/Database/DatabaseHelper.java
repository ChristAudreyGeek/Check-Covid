package scolaire.gestion.payementmobilproject.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {


    private static final String db_name = "db_name";
    private static  final String table = "notification";
    private static final String table_notif = "table_notif";

    private static final String ID = "id";
    private static final String date = "date";
    private static final String MESSAGE = "name";
    private static final String image = "img";


    String CREATE_ANNEE = " CREATE TABLE " + table + "("
            + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + MESSAGE + " TEXT, "
            + date + " TEXT, "
            + image + " BLOB "
            + ")";

    String CREATE_ANNE_E = " CREATE TABLE " + table_notif + "("
            + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + MESSAGE + " TEXT, "
            + date + " TEXT "
            + ")";


    public DatabaseHelper (Context context) {
        super(context, db_name, null, 1);
    }



    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_ANNEE);
        sqLiteDatabase.execSQL(CREATE_ANNE_E);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ table);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ table_notif);
        onCreate(sqLiteDatabase);

    }


    public boolean insertdata(String name, String date,String image){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name",name);
        contentValues.put("date",date);
        contentValues.put("img",image);

        long result = sqLiteDatabase.insert(table,null,contentValues);
        return result != 1;

    }


    public boolean insertdatanotification(String name, String date){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name",name);
        contentValues.put("date",date);

        long result = sqLiteDatabase.insert(table_notif,null,contentValues);
        return result != 1;

    }

    public Cursor viewdata(){
        SQLiteDatabase database = this.getReadableDatabase();
        String query = "select * from " + table;
        Cursor cursor = database.rawQuery(query,null);
        return cursor;

    }


    public Cursor viewdatanotif(){
        SQLiteDatabase database = this.getReadableDatabase();
        String query = "select * from " + table_notif;
        Cursor cursor = database.rawQuery(query,null);

        return cursor;

    }

    public long getProfilesCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        long count = DatabaseUtils.queryNumEntries(db, table_notif);
        db.close();
        return count;
    }

}
