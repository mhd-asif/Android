package com.asif.linkkin.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by newton-pc on 3/16/2015.
 */
public class DbHelper_linkkin extends SQLiteOpenHelper{
    public static final String DB_NAME = "linkkin_database";
    public static final int DB_VERSION = 2;

    public DbHelper_linkkin(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public static abstract class Kindom implements BaseColumns {
        public static final String TABLE_NAME = "kindom";
        public static final String NAME = "name";
        public static final String DESCRIPTION = "description";
        public static final String LOGO = "logo";
        public static final String BANNER = "banner";
    }
    public static abstract class KindomOption implements BaseColumns {
        public static final String TABLE_NAME = "kindom_option";
        public static final String ID = "id";
        public static final String NAME = "name";
        public static final String TITLE = "title";
        public static final String CONTENT = "content";
    }
    public static abstract class KindividualPersonal implements BaseColumns {
        public static final String TABLE_NAME = "kindividual_personal";
        public static final String EMPLOYEE_ID = "employee_id";
        public static final String NAME = "name";
        public static final String DESIGNATION = "designation";
        public static final String COMPANY = "company";
        public static final String ADDR_PRESENT = "addr_present";
        public static final String ADDR_PERMANENT = "addr_permanent";
        public static final String PHONE = "phone";
        public static final String EMAIL = "email";
        public static final String FATHER = "father";
        public static final String MOTHER = "mother";
        public static final String MARITAL_STATUS = "marital_status";
        public static final String GENDER = "gender";
        public static final String RELIGION = "religion";
        public static final String DOB = "dob";
        public static final String BLOOD_GROUP = "blood_group";
        public static final String PASSPORT = "passport";
        public static final String BANK_ACCOUNT = "bank_account";
        public static final String NATIONAL_ID = "national_id";
        public static final String PROFILE_PIC = "profile_pic";
    }
    public static abstract class KindividualOfficial implements BaseColumns {
        public static final String TABLE_NAME = "kindividual_official";
        public static final String EMPLOYEE_ID = "employee_id";
        public static final String TAG_NAME = "tag_name";
        public static final String TAG_VALUE = "tag_value";
    }
    public static abstract class Kicoming implements BaseColumns {
        public static final String TABLE_NAME = "kincoming";
        public static final String ID = "id";
        public static final String COLUMN_IMAGE_FOR_CATEGORY = "category_name";
        public static final String IMAGE_PATH = "image_path";
    }
    public static abstract class Kinteract implements BaseColumns {
        public static final String TABLE_NAME = "kinteract";
        public static final String ID = "id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_MOBILE = "mobile";
    }
    public static abstract class Kinformation implements BaseColumns {
        public static final String TABLE_NAME = "kinformation";
        public static final String ID = "id";
        public static final String COLUMN_IMAGE_FOR_CATEGORY = "category_name";
        public static final String IMAGE_PATH = "image_path";
    }
    public static abstract class kintertainment implements BaseColumns {
        public static final String TABLE_NAME = "kitertainment";
        public static final String ID = "id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_MOBILE = "mobile";
    }

    String kindom_table = "CREATE TABLE "+ Kindom.TABLE_NAME+" ("+ Kindom.NAME+" text, " +
            Kindom.DESCRIPTION+" text ," +
            Kindom.LOGO+" text ," +
            Kindom.BANNER +" text)";
    String kindom_option_table = "CREATE TABLE "+ KindomOption.TABLE_NAME+" ("+ KindomOption.ID+" integer primary key AUTOINCREMENT, " +
            KindomOption.NAME+" text ," +
            KindomOption.TITLE+" text ," +
            KindomOption.CONTENT +" text)";
    String kindividual_personal_table = "CREATE TABLE "+ KindividualPersonal.TABLE_NAME+" ("+ KindividualPersonal.EMPLOYEE_ID+" text, " +
            KindividualPersonal.NAME+" text ," +
            KindividualPersonal.DESIGNATION+" text ," +
            KindividualPersonal.COMPANY+" text ," +
            KindividualPersonal.ADDR_PRESENT+" text ," +
            KindividualPersonal.ADDR_PERMANENT+" text ," +
            KindividualPersonal.PHONE+" text ," +
            KindividualPersonal.EMAIL+" text ," +
            KindividualPersonal.FATHER+" text ," +
            KindividualPersonal.MOTHER+" text ," +
            KindividualPersonal.MARITAL_STATUS+" text ," +
            KindividualPersonal.GENDER+" text ," +
            KindividualPersonal.RELIGION+" text ," +
            KindividualPersonal.DOB+" text ," +
            KindividualPersonal.BLOOD_GROUP+" text ," +
            KindividualPersonal.PASSPORT+" text ," +
            KindividualPersonal.BANK_ACCOUNT+" text ," +
            KindividualPersonal.NATIONAL_ID+" text ," +
            KindividualPersonal.PROFILE_PIC +" text)";
    String kindividual_official_table = "CREATE TABLE "+ KindividualOfficial.TABLE_NAME+" ("+ KindividualOfficial.EMPLOYEE_ID+" text, " +
            KindividualOfficial.TAG_NAME+" text ," +
            KindividualOfficial.TAG_VALUE +" text)";

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(kindom_table);
        db.execSQL(kindom_option_table);
        db.execSQL(kindividual_personal_table);
        db.execSQL(kindividual_official_table);
//        db.execSQL(kinteract_table);
//        db.execSQL(kincoming_table);
//        db.execSQL(kintertainment_table);
//        db.execSQL(kinformation_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS "+ Kindom.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+ KindomOption.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+ KindividualPersonal.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+ KindividualOfficial.TABLE_NAME);
//        db.execSQL("DROP TABLE IF EXISTS "+ Kicoming.TABLE_NAME);
//        db.execSQL("DROP TABLE IF EXISTS "+ Kinteract.TABLE_NAME);
//        db.execSQL("DROP TABLE IF EXISTS "+ Kinformation.TABLE_NAME);
//        db.execSQL("DROP TABLE IF EXISTS "+ kintertainment.TABLE_NAME);
        onCreate(db);
    }
}
