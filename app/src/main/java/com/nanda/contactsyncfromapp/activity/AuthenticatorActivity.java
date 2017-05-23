package com.nanda.contactsyncfromapp.activity;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import com.nanda.contactsyncfromapp.R;
import com.nanda.contactsyncfromapp.app.AppConstants;

public class AuthenticatorActivity extends AccountAuthenticatorActivity {

  private AccountManager mAccountManager;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_authenticator);

    Intent res = new Intent();
    res.putExtra(AccountManager.KEY_ACCOUNT_NAME, AppConstants.ACCOUNT_NAME);
    res.putExtra(AccountManager.KEY_ACCOUNT_TYPE, AppConstants.ACCOUNT_TYPE);
    res.putExtra(AccountManager.KEY_AUTHTOKEN, AppConstants.ACCOUNT_TOKEN);
    Account account = new Account(AppConstants.ACCOUNT_NAME, AppConstants.ACCOUNT_TYPE);
    mAccountManager = AccountManager.get(this);
    mAccountManager.addAccountExplicitly(account, null, null);
    //      mAccountManager.setAuthToken(account, Constants.AUTHTOKEN_TYPE_FULL_ACCESS, Constants.ACCOUNT_TOKEN);
    ContentResolver.setSyncAutomatically(account, ContactsContract.AUTHORITY, true);
    setAccountAuthenticatorResult(res.getExtras());
    setResult(RESULT_OK, res);
    finish();
  }
}