package com.nanda.contactsyncfromapp.helper;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v4.content.CursorLoader;
import com.nanda.contactsyncfromapp.entity.ContactItem;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ContactFetcher {

  private final Context context;

  private static final String SELECTION = "(("
      + ContactsContract.Contacts.DISPLAY_NAME
      + " NOTNULL) AND ("
      + ContactsContract.Contacts.HAS_PHONE_NUMBER
      + "=1) AND ("
      + ContactsContract.Contacts.DISPLAY_NAME
      + " != '' ))";

  private static final String ORDER = ContactsContract.Contacts.DISPLAY_NAME_PRIMARY + " ASC";

  private static final String[] CONTACTS_PROJECTION = new String[] {
      ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME,
      ContactsContract.Contacts.PHOTO_THUMBNAIL_URI
  };

  public ContactFetcher(Context c) {
    this.context = c;
  }

  public ArrayList<ContactItem> fetchAll() {
    ArrayList<ContactItem> listContacts = new ArrayList<>();
    listContacts.clear();
    CursorLoader cursorLoader =
        new CursorLoader(context, ContactsContract.Contacts.CONTENT_URI, CONTACTS_PROJECTION,
            // the columns to retrieve
            SELECTION, // the selection criteria (none)
            null, // the selection args (none)
            ORDER // the sort order (default)
        );

    Cursor c = cursorLoader.loadInBackground();

    final Map<String, ContactItem> contactsMap = new HashMap<>(c.getCount());

    if (c.moveToFirst()) {

      int idIndex = c.getColumnIndex(ContactsContract.Contacts._ID);
      int nameIndex = c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);

      do {
        String contactId = c.getString(idIndex);
        String contactDisplayName = c.getString(nameIndex);
        ContactItem contact = new ContactItem(contactId, contactDisplayName);
        contactsMap.put(contactId, contact);
        listContacts.add(contact);
      } while (c.moveToNext());
    }

    c.close();

    matchContactNumbers(contactsMap);

    return listContacts;
  }

  public void matchContactNumbers(Map<String, ContactItem> contactsMap) {
    // Get numbers
    final String[] numberProjection = new String[] {
        ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.TYPE,
        ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
    };

    Cursor phone = new CursorLoader(context, ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
        numberProjection, null, null, null).loadInBackground();

    if (phone.moveToFirst()) {
      final int contactNumberColumnIndex =
          phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
      final int contactTypeColumnIndex =
          phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE);
      final int contactIdColumnIndex =
          phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID);

      while (!phone.isAfterLast()) {
        final String number = phone.getString(contactNumberColumnIndex);
        final String contactId = phone.getString(contactIdColumnIndex);
        ContactItem contact = contactsMap.get(contactId);
        if (contact == null) {
          continue;
        }
        final int type = phone.getInt(contactTypeColumnIndex);
        String customLabel = "Custom";
        CharSequence phoneType =
            ContactsContract.CommonDataKinds.Phone.getTypeLabel(context.getResources(), type,
                customLabel);
        contact.addNumber(number, phoneType.toString());
        phone.moveToNext();
      }
    }

    phone.close();
  }
}
