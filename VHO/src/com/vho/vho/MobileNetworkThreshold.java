package com.vho.vho;

public class MobileNetworkThreshold {
	private int rss;
	private double bandwidth;
	private int packetLoss;
	private double rtt;
	private double jitter;
	
	
	public int getRss() {
		return rss;
	}


	public double getBandwidth() {
		return bandwidth;
	}


	public int getPacketLoss() {
		return packetLoss;
	}


	public double getRtt() {
		return rtt;
	}


	public double getJitter() {
		return jitter;
	}


	public MobileNetworkThreshold(Network network) {
		if(network.getNetworkName().equals("HSDPA"))
		{
			HSDPAThreshold hsdpaThreshold=new HSDPAThreshold();
			this.rss=hsdpaThreshold.getRss();
			this.bandwidth=hsdpaThreshold.getBandwidth();
			this.packetLoss=hsdpaThreshold.getPacketLoss();
			this.rtt=hsdpaThreshold.getRtt();
			this.jitter=hsdpaThreshold.getJitter();
		}
		else if(network.getNetworkName().equals("EDGE"))
		{
			EDGEThreshold edgeThreshold=new EDGEThreshold();
			this.rss=edgeThreshold.getRss();
			this.bandwidth=edgeThreshold.getBandwidth();
			this.packetLoss=edgeThreshold.getPacketLoss();
			this.rtt=edgeThreshold.getRtt();
			this.jitter=edgeThreshold.getJitter();
		}
		else
		{
			EDGEThreshold edgeThreshold=new EDGEThreshold();
			this.rss=edgeThreshold.getRss();
			this.bandwidth=edgeThreshold.getBandwidth();
			this.packetLoss=edgeThreshold.getPacketLoss();
			this.rtt=edgeThreshold.getRtt();
			this.jitter=edgeThreshold.getJitter();
		}
	}
}
