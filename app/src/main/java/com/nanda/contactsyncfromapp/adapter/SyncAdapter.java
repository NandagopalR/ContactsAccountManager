package com.nanda.contactsyncfromapp.adapter;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import com.nanda.contactsyncfromapp.utils.Log;

public class SyncAdapter extends AbstractThreadedSyncAdapter {

  private Context mContext;

  public SyncAdapter(Context context, boolean autoInitialize) {
    super(context, autoInitialize);
    Log.I("Sync Adapter created.");
    mContext = context;
  }

  @Override public void onPerformSync(Account account, Bundle extras, String authority,
      ContentProviderClient provider, SyncResult syncResult) {
    Log.I("Sync Adapter called.");
  }
}
