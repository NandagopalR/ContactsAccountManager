package com.nanda.contactsyncfromapp.activity;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import com.nanda.contactsyncfromapp.R;
import com.nanda.contactsyncfromapp.entity.ContactItem;
import com.nanda.contactsyncfromapp.entity.MyContact;
import com.nanda.contactsyncfromapp.helper.ContactFetcher;
import com.nanda.contactsyncfromapp.helper.ContactsManager;
import com.nanda.contactsyncfromapp.utils.Log;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

  private ArrayList<String> mNames = new ArrayList<>();
  private ArrayList<String> mIDs = new ArrayList<>();
  private ArrayList<String> mNumbers = new ArrayList<>();
  private List<ContactItem> contactItemList = new ArrayList<>();

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home);

    //      Retrieve names from phone's contact list and save in mNames
    //getContactDataBefore();

    contactItemList.addAll(new ContactFetcher(this).fetchAll());

    //      Apply changes to phone's contact list
    new AsyncTask<String, String, String>() {

      @Override protected String doInBackground(String... params) {
        String name, number, id;
        for (int i = 0; i < contactItemList.size(); i++) {
          if (i < 10) {
            name = contactItemList.get(i).getName();
            id = contactItemList.get(i).getId();
            number = contactItemList.get(i).contactNumbers.get(0).number;
            android.util.Log.e("Contacts ", name + " - " + number);
            ContactsManager.addContact(MainActivity.this, new MyContact(name, id, number));
          }
        }
        return null;
      }

      @Override protected void onPostExecute(String s) {
        //getContactDataAfter();
      }
    }.execute();
  }

  /**
   * Method to fetch contact's from device
   */
  private void getContactDataBefore() {
    int i = 0;

    // query all contact id's from device
    Cursor c1 = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,
        new String[] { ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME },
        null, null, null);

    if ((c1 != null) && c1.moveToFirst()) {

      // add contact id's to the mIDs list
      do {
        mIDs.add(c1.getString(c1.getColumnIndexOrThrow(ContactsContract.Contacts._ID)));
        mNames.add(c1.getString(c1.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME)));

        // query all contact numbers corresponding to current id
        Cursor c2 = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            new String[] { ContactsContract.CommonDataKinds.Phone.NUMBER },
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?", new String[] { mIDs.get(i) },
            null);

        if (c2 != null && c2.moveToFirst()) {
          // add contact number's to the mNumbers list
          do {
            mNumbers.add(c2.getString(
                c2.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER)));
          } while (c2.moveToNext());
          c2.close();
        }

        i++;
      } while (c1.moveToNext() && i < c1.getCount());

      c1.close();
    }
  }

  /**
   * Method to fetch contacts after updation (for logging purposes)
   */
  private void getContactDataAfter() {
    Cursor c =
        getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

    List<String> RIds = new ArrayList<>();
    mIDs = new ArrayList<>();
    mNumbers = new ArrayList<>();
    int i = 0;

    if (c != null && c.moveToFirst()) {
      do {
        mIDs.add(c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID)));
        mNames.add(c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME)));

        Cursor c2 = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            new String[] { ContactsContract.CommonDataKinds.Phone.NUMBER },
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?", new String[] { mIDs.get(i) },
            null);

        if (c2 != null && c2.moveToFirst()) {
          do {
            mNumbers.add(c2.getString(
                c2.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER)));
          } while (c2.moveToNext());
          c2.close();
        }

        Cursor rawcontacts = getContentResolver().query(ContactsContract.RawContacts.CONTENT_URI,
            new String[] { ContactsContract.RawContacts._ID },
            ContactsContract.RawContacts.CONTACT_ID + "=?", new String[] { mIDs.get(i) }, null);

        if (rawcontacts != null && rawcontacts.moveToFirst()) {
          do {
            RIds.add(rawcontacts.getString(
                rawcontacts.getColumnIndexOrThrow(ContactsContract.RawContacts._ID)));
          } while (rawcontacts.moveToNext());
          rawcontacts.close();
        }

        for (int j = 0; j < RIds.size(); j++) {
          Log.I(RIds.get(j));
        }
        i++;
      } while (c.moveToNext());
      c.close();
    }
  }
}