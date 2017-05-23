package com.nanda.contactsyncfromapp.utils;

public class Log {

  private static String TAG = "contacts account";

  public static void I(String message) {
    android.util.Log.e(TAG, message);
  }
}