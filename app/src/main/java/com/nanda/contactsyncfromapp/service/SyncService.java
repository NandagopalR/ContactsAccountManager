package com.nanda.contactsyncfromapp.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import com.nanda.contactsyncfromapp.adapter.SyncAdapter;
import com.nanda.contactsyncfromapp.utils.Log;

public class SyncService extends Service {

  private static final Object sSyncAdapterLock = new Object();
  private static SyncAdapter mSyncAdapter = null;

  @Override public void onCreate() {
    Log.I("Sync Service created.");
    synchronized (sSyncAdapterLock) {
      if (mSyncAdapter == null) {
        mSyncAdapter = new SyncAdapter(getApplicationContext(), true);
      }
    }
  }

  @Nullable @Override public IBinder onBind(Intent intent) {
    Log.I("Sync Service binded.");
    return mSyncAdapter.getSyncAdapterBinder();
  }
}
