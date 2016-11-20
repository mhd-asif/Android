package com.bscheme.linkkin.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by newton on 8/30/2015.
 */
public class kindividualDataOrganizer {

    SQLiteDatabase db;
    DbHelper_linkkin mDbHelper;
    Context mContext;

    public kindividualDataOrganizer(Context context) {
        this.mContext = context;
    }

  /*  public long insertORupdateData(String employee_code,String tag_name, String tag_value) {
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
    }  */

    public void delete_kindividual_personal_TableData() {
        mDbHelper = new DbHelper_linkkin(mContext);
        db = mDbHelper.getReadableDatabase();

        db.delete(DbHelper_linkkin.KindividualPersonal.TABLE_NAME, null, null);
        db.close();
    }

}
