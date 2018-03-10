package com.asif.linkkin.db;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class DbHelper extends SQLiteOpenHelper {

	
	public static String DB_PATH;
	
	public static String DB_NAME;
	public SQLiteDatabase database;
	public final Context context;
	
	public SQLiteDatabase getDb() {
		return database;
	}

	public DbHelper(Context context, String databaseName) {
		super(context, databaseName, null, 1);
		this.context = context;
		
		String packageName = context.getPackageName();
		DB_PATH = String.format("//data//data//%s//databases//", packageName);
		DB_NAME = databaseName;
		openDataBase();
	}

   public DbHelper(Context context,String databaseName,int x)
   {
	   super(context, databaseName, null, 1);
		this.context = context;
		
		String packageName = context.getPackageName();
		DB_PATH = String.format("//data//data//%s//databases//", packageName);
		DB_NAME = databaseName;
		   
   }
	public void createDataBase() {
		boolean dbExist = checkDataBase();
		if (!dbExist) {
			this.getReadableDatabase();
			try {
				copyDataBase();
			} catch (IOException e) {
				Log.e(this.getClass().toString(), "Copying error");
				throw new Error("Error copying database!");
			}
		} else {
			Log.i(this.getClass().toString(), "Database already exists");
		}
	}

	private boolean checkDataBase() {
		SQLiteDatabase checkDb = null;
		try {
			String path = DB_PATH + DB_NAME;
			checkDb = SQLiteDatabase.openDatabase(path, null,
					SQLiteDatabase.OPEN_READONLY);
		} catch (SQLException e) {
			Log.e(this.getClass().toString(), "Error while checking db");
		}
		
		if (checkDb != null) {
			checkDb.close();
		}
		return checkDb != null;
	}
	
	private void copyDataBase() throws IOException {
		
		InputStream externalDbStream = context.getAssets().open(DB_NAME);

		
		String outFileName = DB_PATH + DB_NAME;

	
		OutputStream localDbStream = new FileOutputStream(outFileName);

		
		byte[] buffer = new byte[1024];
		int bytesRead;
		while ((bytesRead = externalDbStream.read(buffer)) > 0) {
			localDbStream.write(buffer, 0, bytesRead);
		}
	
		localDbStream.close();
		externalDbStream.close();

	}

	public SQLiteDatabase openDataBase() throws SQLException {
		String path = DB_PATH + DB_NAME;
		if (database == null) {
			createDataBase();
			database = SQLiteDatabase.openDatabase(path, null,
				SQLiteDatabase.OPEN_READWRITE);
		}
		return database;
	}
	@Override
	public synchronized void close() {
		if (database != null) {
			database.close();
		}
		super.close();
	}
	@Override
	public void onCreate(SQLiteDatabase db) {}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
	
	
	// THis method return all row and all column
	public HashMap<String,ArrayList<String>> getAllRow(String tableName, String orderBy)
	{
		HashMap<String,ArrayList<String>> allRow=new HashMap<String,ArrayList<String>>();
	
		
		Constants con=new Constants();
	ArrayList<String>columnName=con.getColumnName(tableName);
	allRow.put("column_name", columnName);
	
	
	int count=columnName.size();
	for(int i=0;i<count;i++)
	{
		ArrayList<String> tempList=new ArrayList<String>();
		
		allRow.put(columnName.get(i), tempList);
		
	}
	
		SQLiteDatabase database=openDataBase();
		String sql="SELECT * FROM "+tableName+ " ORDER BY "+orderBy;
		Cursor cursor=database.rawQuery(sql, null);
		
		cursor.moveToFirst();
		if(!cursor.isAfterLast()) {
		do {
		
		for(int i=0;i<count;i++)
		{
			allRow.get(columnName.get(i)).add(cursor.getString(i));
		}
		
				
		
		} while (cursor.moveToNext());
		}
		cursor.close();
		
	
		
	return allRow;
	}
	
	
	
// this method return all row of specific column
	
	public HashMap<String,ArrayList<String>> getAllRowByColumn(String tableName, ArrayList<String>columnName,String orderBy)
	{
		HashMap<String,ArrayList<String>> allRow=new HashMap<String,ArrayList<String>>();
	
		
		Constants con=new Constants();
	
	allRow.put("column_name", columnName);
	
	String sql="SELECT ";
	int count=columnName.size();
	for(int i=0;i<count;i++)
	{
		ArrayList<String> tempList=new ArrayList<String>();
		sql=sql+columnName.get(i)+" ";
		if(i!=(count-1))
			sql=sql+", ";
		
		allRow.put(columnName.get(i), tempList);
		
	}
	
		SQLiteDatabase database=openDataBase();
		 sql=sql+" FROM "+tableName+ " ORDER BY "+orderBy;
		Cursor cursor=database.rawQuery(sql, null);
		
		cursor.moveToFirst();
		if(!cursor.isAfterLast()) {
		do {
		
		for(int i=0;i<count;i++)
		{
			allRow.get(columnName.get(i)).add(cursor.getString(i));
		}
		
				
		
		} while (cursor.moveToNext());
		}
		cursor.close();
		
	
		
	return allRow;
	}
	
	
	
	// This method return the row of specific column that match the Where criteria in string format
	public HashMap<String,ArrayList<String>> getSelectedRowString(String tableName,ArrayList<String>columnName, String where,int matchPosition,String value,String orderBy)
	{
		HashMap<String,ArrayList<String>> allRow=new HashMap<String,ArrayList<String>>();
	
		
		
	allRow.put("column_name", columnName);
	
	String sql="SELECT ";
	int count=columnName.size();
	for(int i=0;i<count;i++)
	{
		ArrayList<String> tempList=new ArrayList<String>();
		
		allRow.put(columnName.get(i), tempList);
		sql=sql+columnName.get(i)+" ";
		
		if(i!=(count-1))
			sql=sql+",";
	}
	
		SQLiteDatabase database=openDataBase();
		String match="";
	 switch(matchPosition)
	 {
	 case Constants.STRING_ANY:
		         match=match+"'%"+value+"%'";
		         break;
	 case Constants.STRING_ONLY:
		         match=match+"'"+value+"'";
	               break;
	              
	 case Constants.STRING_FIRST:
		         match=match+"'"+value+"%'";
		         
	 case Constants.STRING_LAST:
         match=match+"'%"+value+"'";
	 
	 }
	
		 
		 
		 
		
		sql=sql+" FROM "+tableName+" WHERE "+where+" LIKE "+match+" ORDER BY "+orderBy;
		
		Log.d("ERRORRRRRRRRRRR", sql);
		Cursor cursor=database.rawQuery(sql, null);
		
		cursor.moveToFirst();
		if(!cursor.isAfterLast()) {
		do {
		
		for(int i=0;i<count;i++)
		{
			allRow.get(columnName.get(i)).add(cursor.getString(i));
		}
		
				
		
		} while (cursor.moveToNext());
		}
		cursor.close();
		
	
		
	return allRow;
	}
		
	
	
// This method return the row of specific column that match the where criteria in number format
	public HashMap<String,ArrayList<String>> getSelectedRowNumber(String tableName,ArrayList<String>columnName, String where,String expression,String value,String orderBy)
	{
		HashMap<String,ArrayList<String>> allRow=new HashMap<String,ArrayList<String>>();
	
		
		
	allRow.put("column_name", columnName);
	
	String sql="SELECT ";
	int count=columnName.size();
	for(int i=0;i<count;i++)
	{
		ArrayList<String> tempList=new ArrayList<String>();
		
		allRow.put(columnName.get(i), tempList);
		sql=sql+columnName.get(i)+" ";
		
		if(i!=(count-1))
			sql=sql+",";
	}
	
		SQLiteDatabase database=openDataBase();

	
		 
		 
		
		sql=sql+" FROM "+tableName+" WHERE "+where +" "+expression+" "+value+" ORDER BY "+orderBy;
		
		Log.d("ERRORRRRRRRRRRR", sql);
		Cursor cursor=database.rawQuery(sql, null);
		
		cursor.moveToFirst();
		if(!cursor.isAfterLast()) {
		do {
		
		for(int i=0;i<count;i++)
		{
			allRow.get(columnName.get(i)).add(cursor.getString(i));
		}
		
				
		
		} while (cursor.moveToNext());
		}
		cursor.close();
		
	
		
	return allRow;
	}
	
	
	
	
	
	public HashMap<String,ArrayList<String>> getSelectedRowStringMore(String tableName,ArrayList<String>columnName, ArrayList<String>wheres,int[] matchPositions,ArrayList<String>values,String orderBy)
	{
		HashMap<String,ArrayList<String>> allRow=new HashMap<String,ArrayList<String>>();
	
		
		
	allRow.put("column_name", columnName);
	
	String sql="SELECT ";
	int count=columnName.size();
	for(int i=0;i<count;i++)
	{
		ArrayList<String> tempList=new ArrayList<String>();
		
		allRow.put(columnName.get(i), tempList);
		sql=sql+columnName.get(i)+" ";
		
		if(i!=(count-1))
			sql=sql+",";
	}
	
	SQLiteDatabase database=openDataBase();
	
	String whereClause;
	if(wheres.size()>0) whereClause=" WHERE ";
	else whereClause="";
	
	for(int i=0;i<wheres.size();i++)
	{
		int matchPosition=matchPositions[i];
		String where=wheres.get(i);
		String value=values.get(i);
	    String match="";
	 switch(matchPosition)
	 {
	 case Constants.STRING_ANY:
		         match=match+"'%"+value+"%'";
		         break;
	 case Constants.STRING_ONLY:
		         match=match+"'"+value+"'";
	               break;
	              
	 case Constants.STRING_FIRST:
		         match=match+"'"+value+"%'";
		         
	 case Constants.STRING_LAST:
         match=match+"'%"+value+"'";
	 
	 }
	 
	 if(i<(wheres.size()-1))
	 {
		 whereClause+=where+" LIKE "+match+ " AND ";
	 }
	 else
	 {
		 whereClause+=where+" LIKE "+match;
	 }
	 
		
	}	 
		 
		 
		
	//	sql=sql+" FROM "+tableName+" WHERE "+where+" LIKE "+match+" ORDER BY "+orderBy;
	sql=sql+" FROM "+tableName+whereClause+" ORDER BY "+orderBy;
		
		Log.d("ERRORRRRRRRRRRR", sql);
		Cursor cursor=database.rawQuery(sql, null);
		
		cursor.moveToFirst();
		if(!cursor.isAfterLast()) {
		do {
		
		for(int i=0;i<count;i++)
		{
			allRow.get(columnName.get(i)).add(cursor.getString(i));
		}
		
				
		
		} while (cursor.moveToNext());
		}
		cursor.close();
		
	
		
	return allRow;
	}
	
	
	
	
	
	public boolean deleteRowUsingString(String tableName, String columnName,int position ,String value)
	{
		boolean response =false;
		//String response;
		
		String match="";
		 switch(position)
		 {
		 case Constants.STRING_ANY:
			         match=match+"'%"+value+"%'";
			         break;
		 case Constants.STRING_ONLY:
			         match=match+"'"+value+"'";
		               break;
		              
		 case Constants.STRING_FIRST:
			         match=match+"'"+value+"%'";
			         
		 case Constants.STRING_LAST:
	         match=match+"'%"+value+"'";
		 
		 }
		
		String sql="DELETE FROM "+tableName+" WHERE "+columnName+ " LIKE "+match; 
		//response=sql;
		
		Log.d("QUERYYYYYYY", sql);
		try
		{
			SQLiteDatabase database=openDataBase();
			database.execSQL(sql);
			
			response=true;
		}
		catch(Exception e)
		{
		//	
			response=false;
		}
		
		return response;
	}
	
	
	
	public boolean deleteRowUsingNumber(String tableName, String columnName,String expression ,String value)
	{
		boolean response =false;
		//String response;
		
		
		
		String sql="DELETE FROM "+tableName+" WHERE "+columnName+ expression+" "+value; 
		//response=sql;
		
		Log.d("QUERYYYYYYY", sql);
		try
		{
			SQLiteDatabase database=openDataBase();
			database.execSQL(sql);
			
			response=true;
		}
		catch(Exception e)
		{
		//	
			response=false;
		}
		
		return response;
	}
	
	
	// Inserting multiple record
	public boolean  insertRowByColumn(String tableName, LinkedHashMap<String,Integer>columnOrder,HashMap<Integer,ArrayList<String>>value)
//	public ArrayList<String>  insertRowByColumn(String tableName, LinkedHashMap<String,Integer>columnOrder,HashMap<Integer,ArrayList<String>>value,String orderBy)
	
	{
		//ArrayList<String> response =new ArrayList<String>();
		boolean response = false;
		Constants con=new Constants();
	
	String sql="INSERT INTO "+tableName+" ( ";
	int count=columnOrder.size();
	
	

	Set set = columnOrder.entrySet();
    // Get an iterator
    Iterator i = set.iterator();
    // Display elements
    int index=0;
    ArrayList<String>columnName=new ArrayList<String>();
    while(i.hasNext()) {
       Map.Entry me = (Map.Entry)i.next();
       System.out.print(me.getKey() + ": ");
      // System.out.println(me.getValue());
      
      columnName.add(me.getKey()+"");
       sql=sql+ me.getKey()+" ";
       if(index!=(count-1))
			sql=sql+", ";
       else
       {
    	   sql=sql+" ) VALUES (";
       }
    		   
    		   index++;
       
    }
	
   
   
		SQLiteDatabase database=openDataBase();
		
		
		for(int j=0; j<value.size();j++)  // joto ta row totobar ghurbe
		
		{
			ArrayList<String> temp=value.get(j);
			
			String query=sql;
			for(int k=0; k<temp.size();k++)
			{
				if(columnOrder.get(columnName.get(k))==Constants.IS_NUMBER)
				{
					query=query+" "+temp.get(k);
				}
				else
				{
					query=query+" '"+temp.get(k)+"' ";
				}
				
						
						if(k!=(temp.size()-1))
						{
							query=query+" , ";
						}
						else
						{
							query=query+" ) ";
						}
			}
			
			try
			{
			database.execSQL(query);
			//response.add(query);
			response=true;
			}
			catch(Exception e)
			{
				response=false;
			}
			
		}
		
	return response;
	}
	
	
	public boolean deleteTable(String name)
	{
		try
		{
			SQLiteDatabase database=this.getWritableDatabase();
			String sql="DELETE FROM "+name+";";
			database.execSQL(sql);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
	
	
	
	public SQLiteDatabase getEditableDB()
	{
		try
		{
			SQLiteDatabase database=this.getWritableDatabase();
			return database;
		}
		catch(Exception e)
		{
			return null;
		}
	}
	
	
	
	
}
