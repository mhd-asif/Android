package com.bscheme.linkkin.models;

import android.os.Parcel;
import android.os.Parcelable;

public class KindomInfo
  implements Parcelable
{
  public static final Creator<KindomInfo> CREATOR = new Creator()
  {
    public KindomInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      return new KindomInfo(paramAnonymousParcel);
    }
    
    public KindomInfo[] newArray(int paramAnonymousInt)
    {
      return new KindomInfo[paramAnonymousInt];
    }
  };
  public String about_us;
  public String business;
  public String contact_us;
  public String id;
  public String industries;
  public String statistics;
  public String subsidiaries;
 // public String short_desc;
 // public String title;

  public String name;
  public String description;
  public String logo;
  public String banner;
  
  public KindomInfo() {}
  
  public KindomInfo(Parcel paramParcel)
  {
    this.id = paramParcel.readString();
    this.name = paramParcel.readString();
    this.description = paramParcel.readString();
    this.logo = paramParcel.readString();
    this.banner = paramParcel.readString();
    this.about_us = paramParcel.readString();
    this.business = paramParcel.readString();
    this.industries = paramParcel.readString();
    this.statistics = paramParcel.readString();
    this.subsidiaries = paramParcel.readString();
    this.contact_us = paramParcel.readString();
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(this.id);
    paramParcel.writeString(this.name);
    paramParcel.writeString(this.description);
    paramParcel.writeString(this.logo);
    paramParcel.writeString(this.banner);
    paramParcel.writeString(this.about_us);
    paramParcel.writeString(this.business);
    paramParcel.writeString(this.industries);
    paramParcel.writeString(this.statistics);
    paramParcel.writeString(this.subsidiaries);
    paramParcel.writeString(this.contact_us);
  }
}
