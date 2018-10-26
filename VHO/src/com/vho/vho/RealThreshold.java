package com.vho.vho;

public class RealThreshold {
	private final int rss=-110;
	private final double bandwidth=10100;
	private final int packetLoss=50;
	private final double rtt=1700;
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
