package com.vho.vho;

public class EDGEThreshold {

	private final int rss=-95;				// Real-Time Application Context
	private final double bandwidth=661;
	private final int packetLoss=50;
	private final double rtt=2000;
	private final double jitter=1000;
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

	
}
