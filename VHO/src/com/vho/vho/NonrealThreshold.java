package com.vho.vho;

public class NonrealThreshold {
	private final int rss=-110;	// Non-RealTime Application Context
	private final double bandwidth=662;
	private final int packetLoss=25;
	private final double rtt=5000;
	private final double jitter=850;
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
