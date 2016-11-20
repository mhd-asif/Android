package com.bscheme.linkkin.models;

import android.os.Parcel;
import android.os.Parcelable;

public class PolicyInfo
  implements Parcelable
{
  public static final Creator<PolicyInfo> CREATOR = new Creator()
  {
    public PolicyInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      return new PolicyInfo(paramAnonymousParcel);
    }
    
    public PolicyInfo[] newArray(int paramAnonymousInt)
    {
      return new PolicyInfo[paramAnonymousInt];
    }
  };
  public String desc;
  public String policy_date;
  public String policy_title;
  public String sport_title;
  public String team_id;
  public String team_title;
  
  public PolicyInfo() {}
  
  public PolicyInfo(Parcel paramParcel)
  {
    this.policy_title = paramParcel.readString();
    this.policy_date = paramParcel.readString();
    this.desc = paramParcel.readString();
    this.sport_title = paramParcel.readString();
    this.team_id = paramParcel.readString();
    this.team_title = paramParcel.readString();
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(this.policy_title);
    paramParcel.writeString(this.policy_date);
    paramParcel.writeString(this.desc);
    paramParcel.writeString(this.sport_title);
    paramParcel.writeString(this.team_id);
    paramParcel.writeString(this.team_title);
  }
}
