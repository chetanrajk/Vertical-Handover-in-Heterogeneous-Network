package com.vho.vho;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.widget.Toast;

public class Terminal {
	Context mcontext;
	private float speed;
	private int batteryLevel;
	
	public Terminal(Context context) {
		this.mcontext=context;
	}
		
	// Get Velocity of Mobile
	public float getSpeed() {
		return speed;
	}

	// Get Mobile Battery Level
	public int getBatteryLevel() {
		return batteryLevel;
	}

// Broadcast For BatteryLevel
	public void getBatteryPercentage() {
		BroadcastReceiver batteryLevelReceiver = new BroadcastReceiver() {
						@Override
					 public void onReceive(Context context, Intent intent) {
			             context.unregisterReceiver(this);
			             int currentLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
			             int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
			              batteryLevel = -1;
			             if (currentLevel >= 0 && scale > 0) {
			                 batteryLevel = (currentLevel * 100) / scale;
			                 
			             }
			            
			         }
			     };	
			     IntentFilter batteryLevelFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
			     mcontext.registerReceiver(batteryLevelReceiver, batteryLevelFilter);
	 }
	
	
	// Velocity Capture using  Location Manager
	public void getVelocity(){
		LocationManager locationManager = (LocationManager)mcontext.getSystemService(Context.LOCATION_SERVICE);
		// Define a listener that responds to location updates 
		LocationListener locationListener = new LocationListener() { 
			public void onLocationChanged(Location location) { 

				// Capture Lattitude  - Based on GPS
				try{
				location.getLatitude();
				
				// Capture Speed 
				speed=location.getSpeed();
				Toast.makeText(mcontext, "Speed :"+speed,Toast.LENGTH_LONG).show();
				}catch(Exception e){}
			} 
			
			public void onStatusChanged(String provider, int status, Bundle extras) { } 
			public void onProviderEnabled(String provider) { } 
			public void onProviderDisabled(String provider) { } 
			}; 
			try{
				
			// Intent for GPS nad Network Provider
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
			locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
			}catch(Exception e){
				
			}
	}
}
