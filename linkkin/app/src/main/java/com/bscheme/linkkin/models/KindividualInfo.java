package com.bscheme.linkkin.models;

import android.os.Parcel;
import android.os.Parcelable;

public class KindividualInfo implements Parcelable
{
  public static final Creator<KindividualInfo> CREATOR = new Creator()
  {
    public KindividualInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      return new KindividualInfo(paramAnonymousParcel);
    }
    
    public KindividualInfo[] newArray(int paramAnonymousInt)
    {
      return new KindividualInfo[paramAnonymousInt];
    }
  };
  public static String birthday;
 // public static String company_name;
  public static String department;
  public static String designation;
  public static String employee_id;
  public static String father_name;
  public static String gender;
  public static String id;
  public static String joining;
  public static String marital_status;
 // public static String mobile;
  public static String mother_name;
 // public static String name;
  public static String first_name;
  public static String last_name;
  public static String email;
  public static String unit;
  public static String permanet_address;
  public static String present_address;
  public static String religion;
 // public static String section;
 // public static String shift;
  public static String blood_group;
  public static String passport_no;
  public static String bank_account;
  public static String national_id;
  public static String mobile="";
  public static String company_name="SQ Group";
  
  public KindividualInfo() {}
  
  public KindividualInfo(Parcel paramParcel)
  {
    this.id = paramParcel.readString();
    this.first_name = paramParcel.readString();
    this.last_name = paramParcel.readString();
    this.email = paramParcel.readString();
    this.unit = paramParcel.readString();
    this.designation = paramParcel.readString();
    this.permanet_address = paramParcel.readString();
    this.present_address = paramParcel.readString();
    this.father_name = paramParcel.readString();
    this.mother_name = paramParcel.readString();
    this.marital_status = paramParcel.readString();
    this.religion = paramParcel.readString();
    this.gender = paramParcel.readString();
    this.employee_id = paramParcel.readString();
    this.department = paramParcel.readString();
  //  this.section = paramParcel.readString();
 //   this.shift = paramParcel.readString();
    this.joining = paramParcel.readString();
    this.birthday = paramParcel.readString();
    this.blood_group = paramParcel.readString();
    this.passport_no = paramParcel.readString();
    this.bank_account = paramParcel.readString();
    this.national_id = paramParcel.readString();
    this.mobile = paramParcel.readString();
    this.company_name = paramParcel.readString();
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(this.id);
    paramParcel.writeString(this.first_name);
    paramParcel.writeString(this.last_name);
    paramParcel.writeString(this.email);
    paramParcel.writeString(this.unit);
    paramParcel.writeString(this.designation);
    paramParcel.writeString(this.permanet_address);
    paramParcel.writeString(this.present_address);
    paramParcel.writeString(this.father_name);
    paramParcel.writeString(this.mother_name);
    paramParcel.writeString(this.marital_status);
    paramParcel.writeString(this.religion);
    paramParcel.writeString(this.gender);
    paramParcel.writeString(this.employee_id);
    paramParcel.writeString(this.department);
 //   paramParcel.writeString(this.section);
 //   paramParcel.writeString(this.shift);
    paramParcel.writeString(this.joining);
    paramParcel.writeString(this.birthday);
    paramParcel.writeString(this.blood_group);
    paramParcel.writeString(this.passport_no);
    paramParcel.writeString(this.bank_account);
    paramParcel.writeString(this.national_id);
    paramParcel.writeString(this.mobile);
    paramParcel.writeString(this.company_name);
  }
}
