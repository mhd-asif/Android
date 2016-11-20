package com.bscheme.linkkin.db;

import java.util.ArrayList;

public class Constants {
//-------------------------------------------------------------	
    public static final int DATABASE_VERSION = 1;
    
    public static final int STRING_ONLY=1;
    public static final int STRING_ANY=2;
    public static final int STRING_FIRST=3;
    public static final int STRING_LAST=4;
    
    public static final String IS_EQUAL="=";
    public static final String IS_NOT_EQUAL="!=";
    public static final String IS_GREATER=">";
    public static final String IS_LESS="<";
    public static final String IS_GREATER_EQUAL=">=";
    public static final String IS_LESS_EQUAL="<=";
    
    public static final int IS_NUMBER=1;
    public static final int IS_STRING=2;
    
    //-----------------------------------------------------------
    
    
    // Database Name
    public static final String DATABASE_NAME = "linkkin_off.db";
 
    // Contacts table name
    public static final String TABLE_SEEN_DATE = "seen_date";
    public static final String SEEN_DATE_ID="id";
    public static final String SEEN_DATE_DATE="date";
    public static final String SEEN_DATE_TYPE="type";
   // public static final String SEEN_DATE_CATEGORY="category";

    public static final String TABLE_SEEN_ID = "seen_id";
    public static final String SEEN_ID_ID="id";
    public static final String SEEN_ID_TYPE_ID="type_id";
    public static final String SEEN_ID_TYPE="type";
    public static final String SEEN_ID_CATEGORY="category";

    public static final String TABLE_KINDOM = "kindom";
    public static final String KINDOM_ID="id";
    public static final String KINDOM_TITLE="title";
    public static final String KINDOM_SHORT_DESC="short_desc";
    public static final String KINDOM_LOGO="logo";
    public static final String KINDOM_BANNER="banner";

    public static final String TABLE_KINDOM_OPTION = "kindom_option";
    public static final String KINDOM_OPTION_ID="id";
    public static final String KINDOM_OPTION_TITLE="title";
    public static final String KINDOM_OPTION_CONTENT="content";

    public static final String TABLE_EVENT = "event";
    public static final String EVENT_ID="id";
    public static final String EVENT_DATE="date";
    public static final String EVENT_DETAIL="detail";

    public static final String TABLE_INFORMATION = "information";
    public static final String INFORMATION_ID="id";
    public static final String INFORMATION_DATE="date";
    public static final String INFORMATION_DETAILS="details";
    public static final String INFORMATION_TYPE="type";
    public static final String INFORMATION_IMAGE_LINK="image_link";

    public static final String TABLE_ENTERTAINMENT = "entertainment";
    public static final String ENTERTAINMENT_ID="id";
    public static final String ENTERTAINMENT_TITLE="title";
    public static final String ENTERTAINMENT_DETAIL="detail";
    public static final String ENTERTAINMENT_TYPE="type";
    public static final String ENTERTAINMENT_IMAGE_LINK="image_link";
    public static final String ENTERTAINMENT_TIME="time";

    public static final String TABLE_INTERACT_COMMENT = "interact_comment";
    public static final String INTERACT_COMMENT_ID="id";
    public static final String INTERACT_COMMENT_POST_ID="post_id";
    public static final String INTERACT_COMMENT_EMPLOYEE_ID="employee_id";
    public static final String INTERACT_COMMENT_POST="post";
    public static final String INTERACT_COMMENT_COMMENTOR="commentor";
    public static final String INTERACT_COMMENT_DATE="date";
    public static final String INTERACT_COMMENT_DESCRIPTION="description";

    
    
    public ArrayList<String> getColumnName(String tableName)
    {
    	ArrayList<String> allColumn=new ArrayList<String>();
    	allColumn.clear();
    	if(tableName.equals(TABLE_SEEN_DATE))
    	{
    		allColumn.add(SEEN_DATE_ID);
            allColumn.add(SEEN_DATE_DATE);
            allColumn.add(SEEN_DATE_TYPE);
           // allColumn.add(SEEN_DATE_CATEGORY);
    	}
    	else if(tableName.equals(this.TABLE_SEEN_ID))
    	{
    		allColumn.add(SEEN_ID_ID);
            allColumn.add(SEEN_ID_TYPE_ID);
            allColumn.add(SEEN_ID_TYPE);
            allColumn.add(SEEN_ID_CATEGORY);
    	}
        else if(tableName.equals(this.TABLE_KINDOM))
        {
            allColumn.add(KINDOM_ID);
            allColumn.add(KINDOM_TITLE);
            allColumn.add(KINDOM_SHORT_DESC);
            allColumn.add(KINDOM_LOGO);
            allColumn.add(KINDOM_BANNER);

        }
        else if(tableName.equals(this.TABLE_KINDOM_OPTION))
        {
            allColumn.add(KINDOM_OPTION_ID);
            allColumn.add(KINDOM_OPTION_TITLE);
            allColumn.add(KINDOM_OPTION_CONTENT);
        }
        else if(tableName.equals(this.TABLE_EVENT))
        {
            allColumn.add(EVENT_ID);
            allColumn.add(EVENT_DATE);
            allColumn.add(EVENT_DETAIL);
        }
        else if(tableName.equals(this.TABLE_INFORMATION))
        {
            allColumn.add(INFORMATION_ID);
            allColumn.add(INFORMATION_DATE);
            allColumn.add(INFORMATION_DETAILS);
            allColumn.add(INFORMATION_TYPE);
            allColumn.add(INFORMATION_IMAGE_LINK);
        }
        else if(tableName.equals(this.TABLE_ENTERTAINMENT))
        {
            allColumn.add(ENTERTAINMENT_ID);
            allColumn.add(ENTERTAINMENT_TITLE);
            allColumn.add(ENTERTAINMENT_DETAIL);
            allColumn.add(ENTERTAINMENT_TYPE);
            allColumn.add(ENTERTAINMENT_TIME);
            allColumn.add(ENTERTAINMENT_IMAGE_LINK);
        }
        else if(tableName.equals(this.TABLE_INTERACT_COMMENT))
        {
            allColumn.add(INTERACT_COMMENT_ID);
            allColumn.add(INTERACT_COMMENT_POST_ID);
            allColumn.add(INTERACT_COMMENT_EMPLOYEE_ID);
            allColumn.add(INTERACT_COMMENT_POST);
            allColumn.add(INTERACT_COMMENT_COMMENTOR);
            allColumn.add(INTERACT_COMMENT_DATE);
            allColumn.add(INTERACT_COMMENT_DESCRIPTION);
        }
   	
    	return allColumn;
    }

}
