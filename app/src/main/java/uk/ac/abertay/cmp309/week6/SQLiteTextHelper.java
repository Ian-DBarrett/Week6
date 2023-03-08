package uk.ac.abertay.cmp309.week6;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteTextHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "LAB6_DB";
    private static final String TABLE_NAME = "TextEntries";
    private static final String[] COLUMN_NAMES = {"Name", "Email","Phone"};

    private static SQLiteTextHelper instance = null;

    // Build a table creation query string

    private String createCreateString(String[] colNames){
        String s = "CREATE TABLE " + TABLE_NAME + " (";
        for (int i = 0; i < colNames.length; i++) {
            s += colNames[i] + " TEXT";
            if(i < colNames.length - 1){
                s+= ", ";
            } else {
                s+= ");";
            }
        }
        return s;
    }

    private SQLiteTextHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static SQLiteTextHelper getInstance(Context context){
        if(instance == null){
            instance = new SQLiteTextHelper(context);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createCreateString(COLUMN_NAMES));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }

    // Saves a single text string to SQLite database, overwriting existing one in row 0
    public void saveText(String[] contact){
        // Check the number of rows
        SQLiteDatabase db = this.getReadableDatabase();
        // Get all table rows
        Cursor result = db.query(TABLE_NAME, COLUMN_NAMES, null, null, null, null, null, null);
        int count = 0;
        if(result != null) {
            // See how many entries there are
            count = result.getCount();
            db.close();
            db = getWritableDatabase();
            ContentValues row = new ContentValues();
            // Prepare a row for saving, use 0 as the ID

            row.put(COLUMN_NAMES[0], contact[0]);
            row.put(COLUMN_NAMES[1], contact[1]);
            row.put(COLUMN_NAMES[2], contact[2]);
            // if no rows, INSERT one

                 db.insert(TABLE_NAME, null, row);


            db.close();
        }
    }

    public String loadText(){
        // check the number of rows
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result = db.query(TABLE_NAME, COLUMN_NAMES, null, null, null, null, null, null);
        int count = 0;

        // if there is text to load, return it, otherwise return error message
        if(result.getCount() > 0){
            result.moveToPosition(0);
            return result.getString(1);
        } else {
            return "NO TEXT STORED IN DB!";
        }
    }
}
