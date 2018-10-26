package com.vho.vho;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

public class Manager extends Activity {
	private Network network;
	private Terminal terminal;
	private ApplicationThreshold threshold;
	
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.manager);
		
		try{
			
			network=new Network(getApplicationContext());
			threshold=new ApplicationThreshold();
			network.setMobileDataEnableOn();							//  Mobile Data Enabled
			network.getMobileNetwork();									//  Type of  network and RSS 
			terminal=new Terminal(getApplicationContext());
			terminal.getVelocity();										//  Speed of mobile
			network.getWifi(terminal,threshold);						// Scans for WIFI
		}catch(Exception e){
			Toast.makeText(getApplicationContext(), "Exception occured",Toast.LENGTH_LONG).show();
		}	

	}
}
