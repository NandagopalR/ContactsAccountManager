package com.nanda.contactsyncfromapp.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;
import com.nanda.contactsyncfromapp.R;

/**
 * Created by nandagopal on 5/23/17.
 */

public class SplashActivity extends AppCompatActivity {

  private static final String[] mPermission = {
      Manifest.permission.READ_CONTACTS
  };
  private static final int REQUEST_RUNTIME_PERMISSION = 2;

  private Runnable mSplashThread = new Runnable() {
    @Override public void run() {
      startActivity(new Intent(SplashActivity.this, MainActivity.class));
      finish();
    }
  };
  private Handler mHandler = new Handler();

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_splash);

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      boolean allPermissionsGranted = true;
      for (int i = 0, mPermissionLength = mPermission.length; i < mPermissionLength; i++) {
        String permission = mPermission[i];
        if (ActivityCompat.checkSelfPermission(this, permission)
            != PackageManager.PERMISSION_GRANTED) {
          allPermissionsGranted = false;
          break;
        }
      }
      if (!allPermissionsGranted) {
        ActivityCompat.requestPermissions(this, mPermission, REQUEST_RUNTIME_PERMISSION);
      } else {
        permissionGranted();
      }
    } else {
      permissionGranted();
    }
  }

  @Override public void onRequestPermissionsResult(int requestCode, String[] permissions,
      int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    android.util.Log.e("Req Code", "" + requestCode);
    if (requestCode == REQUEST_RUNTIME_PERMISSION) {
      boolean allPermissionGranted = true;
      if (grantResults.length == permissions.length) {
        for (int i = 0; i < permissions.length; i++) {
          if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
            allPermissionGranted = false;
            break;
          }
        }
      } else {
        allPermissionGranted = false;
      }
      if (allPermissionGranted) {
        permissionGranted();
      } else {
        permissionDenied();
      }
    }
  }

  private void permissionGranted() {
    mHandler.postDelayed(mSplashThread, 1000);
  }

  private void permissionDenied() {
    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
    finish();
  }
}
