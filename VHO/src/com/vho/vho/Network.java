package com.vho.vho;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.TrafficStats;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class Network {
Context context;
	
	private String networkName;
	private int mobileSignalStrength;
	private String currentConnectedNetwork;
	private int connectedNetworkNo;
	private int currentRSS;
	private int mobileNetworkNo;
	private double bandwidth;
	private int packetLoss;
	private double rtt;
	private double jitter;

	private List<ScanResult> wifiList;
	private Network networkObject;
	private WifiManager mainWifi;
	private WifiReceiver receiverWifi;
	MobileNetworkThreshold mobileNetworkThreshold;
	ApplicationThreshold threshold;
	Terminal terminal;
	NetworkManager networkManager;
	
	public Network(Context context) {
		this.context=context;
	}
	
	
	// Name of Network
	public String getNetworkName() {
		return networkName;
	}

	// Mobile Network Signal Strength
	public int getMobileSignalStrength() {
		return mobileSignalStrength;
	}

	
	// List of WIFI networks
	public List<ScanResult> getWifiList() {
		return wifiList;
	}

	// get network class object
	public Network getNetworkObject() {
		return networkObject;
	}

	
	// get WifiReceiver class Object
	public WifiReceiver getReceiverWifi() {
		return receiverWifi;
	}

	
	// get Current Connected Network
	public String getCurrentConnectedNetwork() {
		return currentConnectedNetwork;
	}

	
	//  get Network ID
	public int getConnectedNetworkNo() {
		return connectedNetworkNo;
	}

	
	//  get Wifi RSS
	public int getCurrentRSS() {
		return currentRSS;
	}

	
	// get Bandwidth
	public double getBandwidth()
	{
		return bandwidth;
	}

	
	// get Packetloss
	public int getPacketLoss() {
		return packetLoss;
	}

	
//  get RTT value
	public double getRtt() {
		return rtt;
	}
//  get WifiManager Object
	public WifiManager getMainWifi() {
		return mainWifi;
	}

	
	public void setMainWifi(WifiManager mainWifi) {
		this.mainWifi = mainWifi;
	}

	// get Jitter value
	public double getJitter() {
		return jitter;
	}

	

//  get Mobile Network Name and RSS
	public void getMobileNetwork()
	{	

		try{
		ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);				
	    NetworkInfo mobileNetwork = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
	    if (mobileNetwork != null) {
	    	mobileNetworkNo=mobileNetwork.getSubtype();
	    	networkName=String.valueOf(mobileNetwork.getSubtypeName());
	    	mobileNetworkThreshold=new MobileNetworkThreshold(this);
	    	Toast.makeText(context,"Network name:"+networkName+"Threshold RSS value:"+mobileNetworkThreshold.getRss(),Toast.LENGTH_LONG).show();
	    }
	    else
	    {
		   // No values
	    } 
	    if(mobileNetwork.isConnected())		//   IF Mobile network Connected
	    {
	    	currentConnectedNetwork=networkName;
	    	connectedNetworkNo=-9;
	    }
	    
	    // get Mobile Rss
	    SignalStrengthListener signalStrengthListener = new SignalStrengthListener();	           
		   ((TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE)).listen(signalStrengthListener,SignalStrengthListener.LISTEN_SIGNAL_STRENGTHS);
		}
		catch(Exception e)
		{}
		
	   //start the signal strength listener
	}
		
		private class SignalStrengthListener extends PhoneStateListener{

			// capture RSS of Mobile Network
		  @Override
		  public void onSignalStrengthsChanged(android.telephony.SignalStrength signalStrength) {
		    
		     // get the signal strength (a value between 0 and 31)
			  if (signalStrength.isGsm())								// For GSM Network
			  {//display.setText(signalStrength.getGsmSignalStrength());
				  mobileSignalStrength = (2*signalStrength.getGsmSignalStrength())-113;
				  if(mobileSignalStrength>0)
					  mobileSignalStrength=-125;
				  Toast.makeText(context,"Mobile signal strength"+mobileSignalStrength,Toast.LENGTH_LONG).show();
		   	  }
			  else												// For CDMA Network
			  {
				 mobileSignalStrength= signalStrength.getCdmaDbm();
			  }
			 super.onSignalStrengthsChanged(signalStrength);
		  
		}
	}
		

	
	//  GET Wifi Network
	public void getWifi(Terminal terminal,ApplicationThreshold threshold)
	{
		try{
		this.terminal=terminal;
		this.threshold=threshold;
		networkObject=this;

		// WIFI disabled, Make it ENABLED.
		mainWifi = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);

	    if (mainWifi.isWifiEnabled() == false)
	    {   
	        Toast.makeText(context.getApplicationContext(), "wifi is disabled..making it enabled", Toast.LENGTH_LONG).show();
	         mainWifi.setWifiEnabled(true);
	    } 
	    
	    // GET SSID and RSS of Wifi Network
	    ConnectivityManager connManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		  if (networkInfo.isConnected()) {
		    final WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
		    final WifiInfo connectionInfo = wifiManager.getConnectionInfo();
		    if (connectionInfo != null)
		      currentConnectedNetwork= connectionInfo.getSSID();
		      currentRSS=connectionInfo.getRssi();
		      Toast.makeText(context, "Wifi network connected", Toast.LENGTH_LONG).show();
		  }
		  
		  //  Intent for BROADCAST RECEIVER  
	    receiverWifi = new WifiReceiver();      
	    context.registerReceiver(receiverWifi, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
	    context.registerReceiver(receiverWifi, new IntentFilter(WifiManager.ACTION_PICK_WIFI_NETWORK));
	       mainWifi.startScan(); 
		}catch(Exception e){}
		}
	class WifiReceiver extends BroadcastReceiver {
    	
		//   When Broadcasted, OnReceive method Invoked.
        public void onReceive(Context c, Intent intent) {	
        	try{
             wifiList = mainWifi.getScanResults(); 
             Toast.makeText(context.getApplicationContext(), "wifi scaned"+wifiList.size(), Toast.LENGTH_LONG).show();
         	 networkManager=new NetworkManager(context);
             networkManager.compareRSSandSpeed(networkObject,threshold,mobileNetworkThreshold,terminal);
        	}catch(Exception e){}
        }    	
	   
	 }

	
	// Calculate Bandwidth...
	public void bandwidthCalculation(){
		long startTime=System.currentTimeMillis();
	  	long startData=TrafficStats.getTotalRxBytes()+TrafficStats.getTotalTxBytes();
        try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        long endTime=System.currentTimeMillis();
	  	long endData=TrafficStats.getTotalRxBytes()+TrafficStats.getTotalTxBytes();
        double st=(double)(endTime-startTime);
        double sd=(double)(endData-startData);
        st=st/1000;
        bandwidth=sd/st;
        Toast.makeText(context,"bandwidth "+bandwidth,Toast.LENGTH_LONG).show();

	}
	
	
	//  RTT Calculation Method
	public void RTTCalculation(){
	     String str;
	    StringBuilder sb=new StringBuilder(); 
	     StringBuilder builder=new StringBuilder();

		try
	    {
	    	Process process;
	    	
	    	// Ping to Google.com
	    	process=Runtime.getRuntime().exec("system/bin/ping -c 4 www.google.com");
	    	BufferedReader reader=new BufferedReader(new InputStreamReader(process.getInputStream()));
	    	while((str=reader.readLine())!=null)
	    		builder.append(str);
	    	
	    	// packet loss upto 3 digits
	    	String packetloss="\\d{1,3}%";
	    	Pattern patternPacket=Pattern.compile(packetloss);
	        Matcher matcher = patternPacket.matcher(builder);
	     
	        // Conversion of String to Integer
	        if (matcher.find( )) {
	    		 String packet="";
	    		 packet+=matcher.group().subSequence(0, matcher.group().length()-1);
	    		 packetLoss=Integer.parseInt(packet);  //packetloss
	        
	    	 } else {
	 
	         }
	        
	        // Extract rtt value from Ping String
	        String rttString="\\d+[.]\\d+/";
	    	patternPacket=Pattern.compile(rttString);
	        matcher = patternPacket.matcher(builder);
	        int rttCount=0;
	        String packet="";
	        String packString="";
			
	        while(matcher.find( )) {
	        	 if(rttCount==0)
	        		sb.append(String.valueOf(builder.substring(matcher.start())));
	        	if(rttCount==1)
	        		packet+=matcher.group().subSequence(0, matcher.group().length()-1);
	    		if(rttCount==2)
	    		{
	    			int val=matcher.end();
	        		int end=builder.indexOf(" ", val);
	        		// Extracy JITTER FROM STRING
	        		packString+=builder.substring(val, end);
	    		}
	    		rttCount++;
	    	 }
	        try{
	        	rtt=Double.parseDouble(packet);
	        	jitter=Double.parseDouble(packString);
	        	Toast.makeText(context,"RTT "+rtt+" Jitter "+jitter+" PacketLoss "+packetLoss,Toast.LENGTH_LONG).show();
	        }catch(Exception e){
	        	
	        }
	        
	     
	    }catch(Exception e)
	    {
		  
	    }

	}
	
	public void disableWifi(){
		// Wifi Disabled
		
		if (mainWifi.isWifiEnabled() == true)
		    {   
		        Toast.makeText(context, "Making wifi disabled...", Toast.LENGTH_LONG).show();
		         mainWifi.setWifiEnabled(false);
		    }
	}
	
	
	// Check if Mobile connected.
	 public boolean isMobileDataEnable() {
		  boolean mobileDataEnabled = false; // Assume disabled
		  ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		  try {
		   Class<?> cmClass = Class.forName(cm.getClass().getName());
		   Method method = cmClass.getDeclaredMethod("getMobileDataEnabled");
		   method.setAccessible(true); // Make the method callable
		   // get the setting for "mobile data"
		   mobileDataEnabled = (Boolean)method.invoke(cm);
		 //  Toast.makeText(getApplicationContext(), " inside isMobileDataEnable mobileDataEnabled :  "+mobileDataEnabled, Toast.LENGTH_LONG).show();
		  } 
		  catch (Exception e) {
		//		 Toast.makeText(context.getApplicationContext(), "exception occured : "+e, Toast.LENGTH_LONG).show();
		   // Some problem accessible private API and do whatever error handling you want here
		  }
		  return mobileDataEnabled;
		 }
		 

	 // Activate Mobile Data
		 public void setMobileDataEnableOn()
		 {
			 try {
				   
				 // Using Connectivity Service
				  //boolean mobileDataEnabled = false;
				  ConnectivityManager dataManager;
				 	dataManager  = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
				 	Method dataMtd = ConnectivityManager.class.getDeclaredMethod("setMobileDataEnabled",Boolean.TYPE);
				 	dataMtd.setAccessible(true);
				 	dataMtd.invoke(dataManager, true);        //True - to enable data connectivity .
				 	Toast.makeText(context.getApplicationContext(), " Mobile Data Enabled ", Toast.LENGTH_LONG).show();
				 	Log.i("SetMobileDataOn Method","MobileDataEnabled");
			  }
			  
			  catch (Exception e){
				  Toast.makeText(context.getApplicationContext(), " Exception occured "+e, Toast.LENGTH_LONG).show();
			  } 
		 }
		 

		 // Deactivate Mobile Data
		 public void setMobileDataEnableOff()
		 {
			 try {
				   
				 // Using Connecivity Service
				  //boolean mobileDataEnabled = false;
				  ConnectivityManager dataManager;
				 	dataManager  = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
				 	Method dataMtd = ConnectivityManager.class.getDeclaredMethod("setMobileDataEnabled",Boolean.TYPE);
				 	dataMtd.setAccessible(true);
				 	dataMtd.invoke(dataManager, false);        //True - to enable data connectivity .
			//	 	Toast.makeText(context.getApplicationContext(), " Mobile Data Disabled. ", Toast.LENGTH_LONG).show();
				 	
			  }
			  
			  catch (Exception e){
		//		  Toast.makeText(context.getApplicationContext(), " Exception occurred "+e, Toast.LENGTH_LONG).show();
			  } 
		 }


}

	
	

