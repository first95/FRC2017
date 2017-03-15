package org.usfirst.frc.team95.robot;

import com.ctre.CANTalon;

public class AdjustedTalon extends CANTalon {
	static final double BACKWARDS_MULTIPLIER = 1.0 / 0.92; // Main CIMs run about 8% less efficiently going backwards.  Reverse that. 
	
	public AdjustedTalon(int deviceNumber) {
		super(deviceNumber);
	}

	public AdjustedTalon(int deviceNumber, int controlPeriodMs) {
		super(deviceNumber, controlPeriodMs);
	}

	public AdjustedTalon(int deviceNumber, int controlPeriodMs, int enablePeriodMs) {
		super(deviceNumber, controlPeriodMs, enablePeriodMs);
	}

	
	@Override
	public void set(double rate) {
		if(rate < 0.0) {
			rate *= BACKWARDS_MULTIPLIER;
		}
		super.set(rate);
	}

}
