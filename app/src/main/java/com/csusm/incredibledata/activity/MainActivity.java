package com.csusm.incredibledata.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.net.wifi.WifiInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.csusm.incredibledata.IDApplication;
import com.csusm.incredibledata.R;
import com.csusm.incredibledata.util.NetworkUtil;
import com.github.akashandroid90.googlesupport.location.AppLocationActivity;
import com.github.nkzawa.socketio.client.Socket;
import javax.inject.Inject;
import javax.inject.Named;

public class MainActivity extends AppLocationActivity {

  private final static String ACTION_FILTER = "android.net.conn.CONNECTIVITY_CHANGE";

  @Inject @Named("uniqueId") String mUniqueId;
  @Inject WifiInfo mWifiInfo;
  @Inject boolean mIsWifiConnection;
  @Inject @Nullable Socket mSocket;

  @BindView(R.id.wifi_status) TextView mWifiStatus;
  @BindView(R.id.location_status) TextView mLocationStatus;

  private Location mLocation;

  private ConnectionReceiver mReceiver;
  private IntentFilter mFilter;

  private boolean mIsLocationAvailable;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    ButterKnife.bind(this);
    initializeInjector();

    mFilter = new IntentFilter(ACTION_FILTER);
    mReceiver = new ConnectionReceiver();

    if (mSocket != null) {
      mSocket.connect();
    }

    checkWifi();
  }

  private void initializeInjector() {
    IDApplication.get(this)
        .getApplicationComponent()
        .inject(this);
  }

  private void checkWifi() {
    if (mIsWifiConnection) {
      connectedWifi();
    } else {
      disconnectedWifi();
    }
  }

  @OnClick(R.id.send_data)
  public void onFabClick() {
    if (mLocation != null) {
      final double latitude = mLocation.getLatitude();
      final double longitude = mLocation.getLongitude();

      Log.d("MainActivity", "Latitude : " + latitude + " Longitude : " + longitude);
    }
  }

  @Override
  protected void onStart() {
    super.onStart();
    LocalBroadcastManager.getInstance(this)
        .registerReceiver(mReceiver, mFilter);
  }

  @Override
  protected void onStop() {
    super.onStop();
    LocalBroadcastManager.getInstance(this)
        .unregisterReceiver(mReceiver);
  }

  @Override
  public void newLocation(Location location) {
    mLocation = location;
    availableLocation();
  }

  @Override
  public void myCurrentLocation(Location currentLocation) {
    mLocation = currentLocation;
    availableLocation();
  }

  private void connectedWifi() {
    mWifiStatus.setText(getString(R.string.wifi_state_connected));
    mWifiStatus.setTextColor(ContextCompat.getColor(this, android.R.color.holo_green_dark));
  }

  private void disconnectedWifi() {
    mWifiStatus.setText(getString(R.string.wifi_state_disconnected));
    mWifiStatus.setTextColor(ContextCompat.getColor(this, android.R.color.holo_red_dark));
  }

  private void availableLocation() {
    mLocationStatus.setText(getString(R.string.location_state_available));
    mLocationStatus.setTextColor(ContextCompat.getColor(this, android.R.color.holo_green_dark));
    mIsLocationAvailable = true;
  }

  private void unavailableLocation() {
    mLocationStatus.setText(getString(R.string.location_state_unavailable));
    mLocationStatus.setTextColor(ContextCompat.getColor(this, android.R.color.holo_red_dark));
    mIsLocationAvailable = false;
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    if (mSocket != null) {
      mSocket.disconnect();
    }
  }

  private class ConnectionReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
      if (intent.getAction()
          .equals(ACTION_FILTER)) {
        mIsWifiConnection = NetworkUtil.isConnectedToWifi(context);
        checkWifi();
      }
    }
  }
}
