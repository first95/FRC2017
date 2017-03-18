package org.usfirst.frc.team95.robot;

import com.ctre.CANTalon;

public class AdjustedTalon extends CANTalon
	{
		static final double BACKWARDS_MULTIPLIER = 1.0 / 0.92; // Main CIMs run about 8% less efficiently going backwards. Reverse that.
		static final double MIN_CURRENT = 40.0;
		static final double MAX_CURRENT = 90.0;
		static final double MIN_ATTEN = 0.95;
		static final double MAX_ATTEN = 0.05;
		static final double SLOPE = ((MAX_ATTEN - MIN_ATTEN) / (MAX_CURRENT - MIN_CURRENT));
		static final double INTERCEPT = (MIN_ATTEN - (SLOPE * MIN_CURRENT));

		public AdjustedTalon(int deviceNumber)
			{
				super(deviceNumber);
			}

		public AdjustedTalon(int deviceNumber, int controlPeriodMs)
			{
				super(deviceNumber, controlPeriodMs);
			}

		public AdjustedTalon(int deviceNumber, int controlPeriodMs, int enablePeriodMs)
			{
				super(deviceNumber, controlPeriodMs, enablePeriodMs);
			}

		@Override
		public void set(double rate)
			{
				double current = super.getOutputCurrent();
				double newAtten;

				if (rate < 0.0)
					{
						rate *= BACKWARDS_MULTIPLIER;

						rate = Math.min(rate, 1);
						rate = Math.max(rate, -1);
					}

				if (current > MIN_CURRENT && current < MAX_CURRENT)
					{
						newAtten = (SLOPE * current) + INTERCEPT;
						rate *= newAtten;
					}

				super.set(rate);
			}

	}