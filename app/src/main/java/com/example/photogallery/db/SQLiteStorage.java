package com.example.photogallery.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class SQLiteStorage {
    SQLiteDatabase db = null;
    public int init(Context context, String dbName) {
        db = context.openOrCreateDatabase(dbName, 0, null);
        if(!db.isOpen()) { return -1; }
        db.beginTransaction();
        try{
            db.execSQL("CREATE TABLE IF NOT EXISTS Photos (timestamp TEXT, caption TEXT, fullpath TEXT);");
            db.setTransactionSuccessful();
        } catch (Exception e) { return -1; }
        finally {
            db.endTransaction();
        }
        return 0;
    }
    public ArrayList<String> findPhotos(String startTimestamp, String endTimestamp, String keyword) {
        ArrayList<String> photos = new ArrayList<String>();
        Cursor dbCursor = null;
        String query = null;
        if(!db.isOpen() || (startTimestamp == "" && endTimestamp == "" && keyword == "")) {
            return null;
        }
        if (keyword == "")
            query = "select fullpath from photos where timestamp between '"+ startTimestamp + "' and '" + endTimestamp + "';";
        else if (startTimestamp == "")
            query = "select fullpath from photos where caption like '%" + keyword + "%';";
        else
            query = "select fullpath from photos where timestamp between '"+ startTimestamp + "' and '" + endTimestamp +
                    "' and caption like '%" + keyword + "%';";
        try{
            dbCursor = db.rawQuery(query, null);
            int fullpathCol = dbCursor.getColumnIndex("fullpath");
            if (dbCursor != null) {
                dbCursor.moveToFirst();
                if (dbCursor.getCount() != 0) {
                    do {
                        photos.add(dbCursor.getString(fullpathCol));
                    } while (dbCursor.moveToNext());
                }
            }
        }
        catch (Exception e) {
            return null;
        }
        return  photos;
    }
    public int addPhoto(String caption, String timestamp, String fullpath) {
        if(!db.isOpen()) { return -1; }
        db.beginTransaction();
        try{
            String sqlStmt = "insert into photos (timestamp, caption, fullpath) values ('"
                    + timestamp + "','" + caption +"','" + fullpath+"');";
            db.execSQL(sqlStmt);
            db.setTransactionSuccessful();
        } catch (Exception e) { return -1; }
        finally {
            db.endTransaction();
        }
        return 0;
    }
    public int deletePhoto(String fullpath) {
        if(!db.isOpen()) { return -1; }
        db.beginTransaction();
        try{
            String sqlStmt = "delete from photos where fullpath ='"+ fullpath+ "';";
            db.execSQL(sqlStmt);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            return -1;
        } finally {
            db.endTransaction();
        }
        return 0;
    }

}

