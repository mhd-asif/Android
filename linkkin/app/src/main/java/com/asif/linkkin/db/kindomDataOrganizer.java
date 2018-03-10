package com.asif.linkkin.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by newton on 8/30/2015.
 */
public class kindomDataOrganizer {

    SQLiteDatabase db;
    DbHelper_linkkin mDbHelper;
    Context mContext;

    public kindomDataOrganizer(Context context) {
        this.mContext = context;
    }

 /*   public long insertORupdateData(String employee_code,String tag_name, String tag_value) {
        mDbHelper = new DbHelper_linkkin(mContext);
        db = mDbHelper.getWritableDatabase();
        long newRowId = 0;

        String queryToGetId = "SELECT 1 FROM " + DbHelper_linkkin.Kindividual.TABLE_NAME + " WHERE "
                + DbHelper_linkkin.Kindividual.COLUMN_TAG + " = ?" + " ;";

        Cursor cursor = db.rawQuery(queryToGetId, new String[]{tag_name});

        boolean exists = (cursor.getCount() > 0);

        String queryInsertORreplace = "insert or replace into "+ DbHelper_linkkin.Kindividual.TABLE_NAME +"(" +
                DbHelper_linkkin.Kindividual.EMPLOYEE_ID +" ,"+
                DbHelper_linkkin.Kindividual.COLUMN_TAG +" ,"+
                DbHelper_linkkin.Kindividual.COLUMN_TAG_VALUE + ") values (" +
                employee_code +
                "(select "+ DbHelper_linkkin.Kindividual.COLUMN_TAG +" from "
                + DbHelper_linkkin.Kindividual.TABLE_NAME +" where "
                + DbHelper_linkkin.Kindividual.COLUMN_TAG +" = "+
                tag_name + "), "+
                tag_value+")";

        ContentValues values = new ContentValues();
        values.put(DbHelper_linkkin.Kindividual.EMPLOYEE_ID, employee_code);
        values.put(DbHelper_linkkin.Kindividual.COLUMN_TAG, tag_name);
        values.put(DbHelper_linkkin.Kindividual.COLUMN_TAG_VALUE, tag_value);


        if (!exists) {
            newRowId = db.insert(DbHelper_linkkin.Kindividual.TABLE_NAME, null, values);
        }else {
            newRowId = db.update(DbHelper_linkkin.Kindividual.TABLE_NAME, values, DbHelper_linkkin.Kindividual.COLUMN_TAG +" = ?",
                    new String[]{tag_name});
        }
        db.close();
        return newRowId;
    }    */

    public void insertORupdateKindomData(String name,String description, String logo, String banner) {
        mDbHelper = new DbHelper_linkkin(mContext);
        db = mDbHelper.getWritableDatabase();
        long newRowId = 0;

        String query="insert or replace into "+ DbHelper_linkkin.Kindom.TABLE_NAME+" ("+ DbHelper_linkkin.Kindom.NAME+", "+
                DbHelper_linkkin.Kindom.DESCRIPTION+", "+
                DbHelper_linkkin.Kindom.LOGO+", "+
                DbHelper_linkkin.Kindom.BANNER+") values ('"+name+"', "+
                "'"+description+"', "+
                "'"+logo+"', "+
                "'"+banner+"');";
        db.execSQL(query);
        db.close();
    }

    public void delete_kindom_data() {
        mDbHelper = new DbHelper_linkkin(mContext);
        db = mDbHelper.getWritableDatabase();

        db.delete(DbHelper_linkkin.Kindom.TABLE_NAME, null, null);
        db.close();
    }

    public void delete_kindom_data(String column, String value) {
        mDbHelper = new DbHelper_linkkin(mContext);
        db = mDbHelper.getWritableDatabase();

        String query="delete from "+ DbHelper_linkkin.Kindom.TABLE_NAME+" where "+column+" is like "+value+";";
        db.execSQL(query);
        db.close();
    }

    public void delete_kindom_option_data() {
        mDbHelper = new DbHelper_linkkin(mContext);
        db = mDbHelper.getWritableDatabase();

        db.delete(DbHelper_linkkin.KindomOption.TABLE_NAME, null, null);
        db.close();
    }

    public void delete_kindom_option_data(String column, String value) {
        mDbHelper = new DbHelper_linkkin(mContext);
        db = mDbHelper.getWritableDatabase();

        String query="delete from "+ DbHelper_linkkin.KindomOption.TABLE_NAME+" where "+column+" is like "+value+";";
        db.execSQL(query);
        db.close();
    }

}
