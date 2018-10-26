package com.vho.vho;

import java.util.ArrayList;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.telephony.SmsManager;
import android.widget.Toast;

public class ExecuteSwitch {
	Context context;
	String phoneNo=new String("9623846111");
	StringBuilder messageBody;
	static ArrayList<Long> list=new  ArrayList<Long>();
	
	public ExecuteSwitch(Context context) {
		this.context=context;
	}
	
	// Calculate No. of Handovers	in Last 5 secs
	public void calculateHandover(Network network,NetworkManager networkManager)
	{														 
		try{												
		long time=System.currentTimeMillis()-5000;
		if(!(list.isEmpty()))		
		{
			for(int i=0;i<list.size();i++)
			{
					if(list.get(i).longValue()<time)		
							list.remove(i);
			}
		}
		Toast.makeText(context, "No of ho :"+list.size(),Toast.LENGTH_LONG).show();
		if(list.size()<2)
			executeSwitch(network,networkManager);
		}catch(Exception e){}
	}
	
	// Switching Function
	public void executeSwitch(Network network,NetworkManager networkManager){
		messageBody= new StringBuilder();
		long switchstart=System.currentTimeMillis();
		try{
		if(networkManager.index==-99)
		{
			Toast.makeText(context,"No network available to switch",Toast.LENGTH_LONG).show();
		}
		if(networkManager.index==-9)
		{
		
			ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);				
		    NetworkInfo wifiNetwork = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if(wifiNetwork.isConnected())
		    {										// if wifi available, disaconnect it.
				network.setMobileDataEnableOn();
				Toast.makeText(context.getApplicationContext(), "Switch to Mobile Network from wifi", Toast.LENGTH_LONG).show();
				try{
		    	network.getMainWifi().disconnect();
		    	}catch(Exception e){}
		    	list.add(System.currentTimeMillis());
		    	messageBody.append("Switched from wifi to mobile");
		    	try{
		    	SmsManager smsManager=SmsManager.getDefault();
				smsManager.sendTextMessage(phoneNo, null,messageBody.substring(0), null, null);
		    	}catch(Exception e){
		    		Toast.makeText(context, "Exception "+e.toString(),Toast.LENGTH_LONG).show();
		    	}
		    }
			long switchend=System.currentTimeMillis();
			Toast.makeText(context, "Switching latency :"+(switchend-switchstart),Toast.LENGTH_LONG).show();
		
		}
		else
		{	
		    // Connect to selected Wifi
			WifiInfo wi=network.getMainWifi().getConnectionInfo();
			String checkWith=wi.getSSID();
		    String networkSSID=network.getWifiList().get(networkManager.index).SSID;
		    Toast.makeText(context, "checkWith value :"+checkWith,Toast.LENGTH_LONG).show();
		    if(!checkWith.equals(networkSSID))
			{	
				WifiConfiguration wc=new WifiConfiguration();
				wc.SSID="\""+networkSSID+"\"";
				wc.status=WifiConfiguration.Status.ENABLED;
				wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
				wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
				wc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
				wc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
				wc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
				wc.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
				try{
						int netId=network.getMainWifi().addNetwork(wc);
						network.getMainWifi().enableNetwork(netId,true);
						network.getMainWifi().setWifiEnabled(true);
						network.getMainWifi().reconnect();	
											
						if(checkWith.equals("")||checkWith==null)
							 messageBody.append("Switched from mobile to wifi netowrk");
						 else
							 messageBody.append("Switched from wifi to wifi netowrk");
					    try{
						SmsManager smsManager=SmsManager.getDefault();
						smsManager.sendTextMessage(phoneNo, null,messageBody.substring(0), null, null);
						Toast.makeText(context, "Message sent...",Toast.LENGTH_LONG).show();
					    }catch(Exception e)
					    {
					    	Toast.makeText(context,"Exception occured while m",Toast.LENGTH_LONG).show();
					    }
				}catch(Exception e){
					Toast.makeText(context, "Exception "+e.toString(),Toast.LENGTH_LONG).show();
				}
				
				list.add(System.currentTimeMillis());
				long switchend=System.currentTimeMillis();	
				Toast.makeText(context, "Switching latency :"+(switchend-switchstart),Toast.LENGTH_LONG).show();
		
			}
		    
		}
		}catch(Exception e){}
		
	} 
}
