package com.vho.vho;

public class HSDPAThreshold {

	private final int rss=-95;
	private final double bandwidth=1000;
	private final int packetLoss=25;
	private final double rtt=450;
	private final double jitter=350;
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
