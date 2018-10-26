package com.vho.vho;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.widget.Toast;

public class NetworkManager {
	int currentRSS;
	int currentNetwork;
	Context context;
	Network network;
	ApplicationThreshold threshold;
	int index;
	int minRSS;
	MobileNetworkThreshold mobileNetworkThreshold;
	Terminal terminal;
	WifiThreshold wifiThreshold= new WifiThreshold();
	ExecuteSwitch executeSwitch;
	
	public NetworkManager(Context context) {
		this.context=context;
	}
	
												// Select Algorithm based on rss and speed
	public void compareRSSandSpeed(Network network,ApplicationThreshold threshold,MobileNetworkThreshold mobileNetworkThreshold,Terminal terminal)
	{										
		try{
		Toast.makeText(context, "Comparing RSS and speed with threshold",Toast.LENGTH_LONG).show();
		this.network=network;
		this.terminal=terminal;
		this.threshold=threshold;
		this.mobileNetworkThreshold=mobileNetworkThreshold;
		if(network.getConnectedNetworkNo()==-9)					// if mobile Network
		{
			if(network.getMobileSignalStrength()>threshold.getRss()&&terminal.getSpeed()<5.0f)
				AHP();
			else
				reflexive(0);
		}
		else											// if Wifi network
		{
			if(network.getCurrentRSS()>threshold.getRss()&&terminal.getSpeed()<5.0f)
				AHP();
			else
				reflexive(1);
		}
		}catch(Exception e){}
	}

	public void AHP(){									// AHP Algorithm
		long ahpstart=System.currentTimeMillis();
		
		network.RTTCalculation();						// calculate RTT and Jitter
		network.bandwidthCalculation();
		
		if(compareThresholds()==1)
			if(compareBattery()==1)						//compare threshold values
			{
				rssComparision();
				executeSwitch=new ExecuteSwitch(context);		// switch to best Network
				executeSwitch.calculateHandover(network,this);
				
			}
		long ahpend=System.currentTimeMillis();
		Toast.makeText(context,"AHP latency "+(ahpend-ahpstart),Toast.LENGTH_LONG).show();
	}
	public void reflexive(int no){					// Reflexive Algorithm					
		if(no==0)
		{
			int count=0;				
			for(ScanResult i:network.getWifiList())			// Mobile Network Active
   		 	{
   		 		if(i.level>threshold.getRss())					// Get Wifi Network with greater RSS
   		 		{
   		 			index=count;
   		 			break;
   		 		}
   		 		count++;
   		 	}
			executeSwitch.executeSwitch(network, this);
		}
		else
		{
			if(network.getMobileSignalStrength()>threshold.getRss())			// get Mobile Network
				index=-9;
			else					
			{
				int count=0;
				for(ScanResult i:network.getWifiList())					// Get Wifi Network with greater RSS
	   		 	{
	   		 		if(i.level>threshold.getRss())
	   		 		{
	   		 			index=count;
	   		 			break;
	   		 		}
	   		 		count++;
	   		 	}
			}
			executeSwitch.executeSwitch(network, this);
		}		
	}
	
	public int compareThresholds()
	{
		int weight=0;
		if(network.getConnectedNetworkNo()==-9)						// If Mobile Network
		{
			if(network.getBandwidth()>threshold.getBandwidth()&&network.getBandwidth()>mobileNetworkThreshold.getBandwidth())
				weight+=9;
			if(network.getPacketLoss()<threshold.getPacketLoss()&&network.getPacketLoss()<mobileNetworkThreshold.getPacketLoss())
				weight+=8;
			if(network.getRtt()<threshold.getRtt()&&network.getRtt()<mobileNetworkThreshold.getRtt())
				weight+=7;
			if(network.getJitter()<threshold.getJitter()&&network.getJitter()<mobileNetworkThreshold.getJitter())
				weight+=6;
		}
		else {													// if Wifi Network 
				
			if(network.getBandwidth()>threshold.getBandwidth()&&network.getBandwidth()>wifiThreshold.getBandwidth())
				weight+=9;
			if(network.getPacketLoss()<threshold.getPacketLoss()&&network.getPacketLoss()<wifiThreshold.getPacketLoss())
				weight+=8;
			if(network.getRtt()<threshold.getRtt()&&network.getRtt()<wifiThreshold.getRtt())
				weight+=7;
			if(network.getJitter()<threshold.getJitter()&&network.getJitter()<wifiThreshold.getJitter())
				weight+=6;
		}
		
		if(Application.getType()==2&&weight>=22)  				//Decide the count
				return 0;									// If Real-Time Application
		else if(Application.getType()==3&&weight>=17)				
				return 0;									// If Non-RealTime Application
		else
		{
				return 1;
		}
	}
	
	public int compareBattery()							//  Compare BatteryPower Level
	{
		terminal.getBatteryPercentage();
		if(terminal.getBatteryLevel()!=0&&terminal.getBatteryLevel()<7)
		{
			network.disableWifi();
			return 0;
		}
		Toast.makeText(context, "Battery percent "+terminal.getBatteryLevel(),Toast.LENGTH_LONG).show();
		return 1;
	}
	
	public void rssComparision()
	{
        if(network.getWifiList().isEmpty())				// Select Mobile Network			
        {
       	 index=-99;
       	 Toast.makeText(context, "No wifi is available.... ", Toast.LENGTH_LONG).show();
         }
        else											// Select WIFI with greater RSS
        {
        	minRSS=network.getWifiList().get(0).level;	
        	int count=0;
        	index=0;
   		 	for(ScanResult i:network.getWifiList())
   		 	{
   		 		if(i.level>minRSS&&i.level>threshold.getRss())
   		 		{
   		 			minRSS=i.level;
   		 			index=count;
   		 		}
   		 		count++;
   		 	}
   
   		 try{
         	
   		 	if(minRSS<network.getMobileSignalStrength())		// if Mobile Network RSS 
   		 	{													// greater than Wifi RSS
   		 		minRSS=network.getMobileSignalStrength();
   		 		index=-9;
   		 	}
   		 Toast.makeText(context, "RSS of selected network is "+minRSS+" and index "+index, Toast.LENGTH_LONG).show();
        	  }catch(Exception e){
           		 Toast.makeText(context, "Exception occured", Toast.LENGTH_LONG).show();
             }
        }
	}
	
	
}
