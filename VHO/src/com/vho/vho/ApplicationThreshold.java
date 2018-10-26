package com.vho.vho;

public class ApplicationThreshold {
	
	private int rss;
	private double bandwidth;
	private int packetLoss;
	private double rtt;
	private double jitter;
	
	public int getRss() {
		return rss;
	}

	public void setRss(int rss) {
		this.rss = rss;
	}

	public double getBandwidth() {
		return bandwidth;
	}

	public void setBandwidth(double bandwidth) {
		this.bandwidth = bandwidth;
	}

	public int getPacketLoss() {
		return packetLoss;
	}

	public void setPacketLoss(int packetLoss) {
		this.packetLoss = packetLoss;
	}

	public double getRtt() {
		return rtt;
	}

	public void setRtt(double rtt) {
		this.rtt = rtt;
	}

	public double getJitter() {
		return jitter;
	}

	public void setJitter(double jitter) {
		this.jitter = jitter;
	}



	// Select Based on Applocation Type
	public ApplicationThreshold() {
		
		switch(Application.getType())
		{
			case 2:
				RealThreshold realThreshold=new RealThreshold();
				rss=realThreshold.getRss();
				bandwidth=realThreshold.getBandwidth();
				packetLoss=realThreshold.getPacketLoss();
				rtt=realThreshold.getRtt();
				jitter=realThreshold.getJitter();
				break;
			case 3:
				NonrealThreshold nonrealThreshold=new NonrealThreshold();
				rss=nonrealThreshold.getRss();
				bandwidth=nonrealThreshold.getBandwidth();
				packetLoss=nonrealThreshold.getPacketLoss();
				rtt=nonrealThreshold.getRtt();
				jitter=nonrealThreshold.getJitter();
				break;
		}
		}
}
