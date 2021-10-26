package kz.madina.chemistrydictionary;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

public class DatabaseRepository {

    private DatabaseHelper databaseHelper;
    private String firstLang;
    private String secondLang;

    public DatabaseRepository(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    public ArrayList<Term> getData(String firstLang, String secondLang, String searchWord) {
        this.firstLang = firstLang;
        this.secondLang = secondLang;

        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        ArrayList<Term> list = new ArrayList<Term>();

        String query;
        if (!firstLang.equals(secondLang)) {
            query = "SELECT terms._id, terms.bookmarked, " + firstLang + ".word, " + firstLang + ".definition, " + secondLang + ".word, " + secondLang + ".definition FROM terms " +
                    "INNER JOIN " + firstLang + " ON terms._id=" + firstLang + ".term_id INNER JOIN " + secondLang + " ON terms._id=" + secondLang + ".term_id " +
                    "WHERE " + firstLang + ".word LIKE ? ORDER BY " + firstLang + ".word";


        } else {
            query = "SELECT terms._id, terms.bookmarked, " + firstLang + ".word, " + firstLang + ".definition FROM terms " +
                    "INNER JOIN " + firstLang + " ON terms._id=" + firstLang + ".term_id " +
                    "WHERE " + firstLang + ".word LIKE ? ORDER BY " + firstLang + ".word";
        }
        Cursor cursor = db.rawQuery(query, new String[]{"%" + searchWord + "%"});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            if (!firstLang.equals(secondLang)) list.add(new Term(cursor.getInt(0), cursor.getInt(1), new Definition(cursor.getString(2),cursor.getString(3)), new Definition(cursor.getString(4),cursor.getString(5))));
                else list.add(new Term(cursor.getInt(0), cursor.getInt(1), new Definition(cursor.getString(2),cursor.getString(3)), new Definition(cursor.getString(2),cursor.getString(3))));
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return list;
    }

    public ArrayList<Term> getBookmarkedData(String firstLang, String secondLang) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        ArrayList<Term> list = new ArrayList<Term>();
        Cursor cursor;
        if (!firstLang.equals(secondLang)) {
             cursor = db.rawQuery("SELECT terms._id, terms.bookmarked, " + firstLang + ".word, " + firstLang + ".definition, " + secondLang + ".word, " + secondLang + ".definition " +
                    "FROM terms INNER JOIN " + firstLang + " ON terms._id=" + firstLang + ".term_id INNER JOIN " + secondLang + " ON terms._id=" + secondLang + ".term_id WHERE terms.bookmarked = 1;", null);
        } else {
             cursor = db.rawQuery("SELECT terms._id, terms.bookmarked, " + firstLang + ".word, " + firstLang + ".definition " +
                    "FROM terms INNER JOIN " + firstLang + " ON terms._id=" + firstLang + ".term_id WHERE terms.bookmarked = 1 ORDER BY " + firstLang + ".word", null);
        }
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            if (!firstLang.equals(secondLang)) list.add(new Term(cursor.getInt(0), cursor.getInt(1), new Definition(cursor.getString(2),cursor.getString(3)), new Definition(cursor.getString(4),cursor.getString(5))));
            else list.add(new Term(cursor.getInt(0), cursor.getInt(1), new Definition(cursor.getString(2),cursor.getString(3)), new Definition(cursor.getString(2),cursor.getString(3))));
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return list;
    }

    public void addBookmark(int id) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        db.execSQL("UPDATE terms SET bookmarked = 1 WHERE _id = " + id + ";");
        db.close();
    }

    public void deleteBookmark(int id) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        db.execSQL("UPDATE terms SET bookmarked = '' WHERE _id = " + id + ";");
        db.close();
    }
}
