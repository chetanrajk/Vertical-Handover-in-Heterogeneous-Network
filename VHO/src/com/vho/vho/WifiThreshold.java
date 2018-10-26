package com.vho.vho;

public class WifiThreshold {
	private final int rss=-90;
	private final double bandwidth=800;
	private final int packetLoss=50;
	private final double rtt=5500;
	private final double jitter=800;
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
