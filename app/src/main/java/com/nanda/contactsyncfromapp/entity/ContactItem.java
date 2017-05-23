package com.nanda.contactsyncfromapp.entity;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;

/**
 * Created by nandagopal on 5/6/17.
 */

public class ContactItem implements Parcelable {

  private String id;
  private String name;
  private String number;
  public ArrayList<ContactPhone> contactNumbers;

  private boolean isMultiSelect = false;

  public ContactItem(String id, String name) {
    this.id = id;
    this.name = name;
    contactNumbers = new ArrayList<>();
  }

  protected ContactItem(Parcel in) {
    id = in.readString();
    name = in.readString();
    number = in.readString();
    contactNumbers = in.createTypedArrayList(ContactPhone.CREATOR);
  }

  public static final Creator<ContactItem> CREATOR = new Creator<ContactItem>() {
    @Override public ContactItem createFromParcel(Parcel in) {
      return new ContactItem(in);
    }

    @Override public ContactItem[] newArray(int size) {
      return new ContactItem[size];
    }
  };

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public boolean isMultiSelect() {
    return isMultiSelect;
  }

  public void setMultiSelect(boolean multiSelect) {
    isMultiSelect = multiSelect;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getNumber() {
    return number;
  }

  public void setNumber(String number) {
    this.number = number;
  }

  public void addNumber(String number, String type) {
    contactNumbers.add(new ContactPhone(number, type));
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel parcel, int i) {
    parcel.writeString(id);
    parcel.writeString(name);
    parcel.writeString(number);
    parcel.writeTypedList(contactNumbers);
  }
}
