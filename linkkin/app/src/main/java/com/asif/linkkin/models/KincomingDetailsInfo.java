package com.asif.linkkin.models;

import android.os.Parcel;
import android.os.Parcelable;

public class KincomingDetailsInfo
  implements Parcelable
{
  public static final Creator<KincomingDetailsInfo> CREATOR = new Creator()
  {
    public KincomingDetailsInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      return new KincomingDetailsInfo(paramAnonymousParcel);
    }

    public KincomingDetailsInfo[] newArray(int paramAnonymousInt)
    {
      return new KincomingDetailsInfo[paramAnonymousInt];
    }
  };
  public String title;
  public String trainingNo;
  public String startDate;
  public String endDate;
  public String duration;
  public String startTime;
  public String endTime;
  public String totalTime;
  public String venue;
  public String participant;
  public String group;
  public String resource;
  public String target;
  public String status;
  public String day;

  public KincomingDetailsInfo() {}

  public KincomingDetailsInfo(Parcel paramParcel)
  {
    this.title = paramParcel.readString();
    this.trainingNo = paramParcel.readString();
    this.startDate = paramParcel.readString();
    this.endDate = paramParcel.readString();
    this.duration = paramParcel.readString();
    this.startTime = paramParcel.readString();
    this.endTime = paramParcel.readString();
    this.totalTime = paramParcel.readString();
    this.venue = paramParcel.readString();
    this.participant = paramParcel.readString();
    this.group = paramParcel.readString();
    this.resource = paramParcel.readString();
    this.target = paramParcel.readString();
    this.status = paramParcel.readString();
    this.day = paramParcel.readString();

  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(this.title);
    paramParcel.writeString(this.trainingNo);
    paramParcel.writeString(this.startDate);
    paramParcel.writeString(this.endDate);
    paramParcel.writeString(this.duration);
    paramParcel.writeString(this.startTime);
    paramParcel.writeString(this.endTime);
    paramParcel.writeString(this.totalTime);
    paramParcel.writeString(this.venue);
    paramParcel.writeString(this.participant);
    paramParcel.writeString(this.group);
    paramParcel.writeString(this.resource);
    paramParcel.writeString(this.target);
    paramParcel.writeString(this.status);
    paramParcel.writeString(this.day);
  }
}
