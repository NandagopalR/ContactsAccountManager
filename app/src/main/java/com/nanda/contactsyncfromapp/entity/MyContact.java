package com.nanda.contactsyncfromapp.entity;

import java.util.ArrayList;
import java.util.List;

public class MyContact {

  public String name;
  public String number;
  public static List<String> mNumbersList = new ArrayList<>();
  public String Id;

  public MyContact(String name, String Id, String number) {
    this.name = name;
    this.Id = Id;
    this.number = number;
    mNumbersList.add(number);
  }
}