package com.bscheme.linkkin.models;

import android.os.Parcel;
import android.os.Parcelable;

public class KinformationDetailsInfo
  implements Parcelable
{
  public static final Creator<KinformationDetailsInfo> CREATOR = new Creator()
  {
    public KinformationDetailsInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      return new KinformationDetailsInfo(paramAnonymousParcel);
    }

    public KinformationDetailsInfo[] newArray(int paramAnonymousInt)
    {
      return new KinformationDetailsInfo[paramAnonymousInt];
    }
  };
  public String title;
  public String desc;

  public KinformationDetailsInfo() {}

  public KinformationDetailsInfo(Parcel paramParcel)
  {
    this.title = paramParcel.readString();
    this.desc = paramParcel.readString();
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(this.title);
    paramParcel.writeString(this.desc);
  }
}
