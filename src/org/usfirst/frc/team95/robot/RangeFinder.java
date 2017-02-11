package org.usfirst.frc.team95.robot;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DigitalOutput;

public class RangeFinder {
	private DigitalOutput m_sensorTrigger;
	private AnalogInput[] m_inputs;

	public RangeFinder(DigitalOutput sensorTrigger, AnalogInput[] inputs) {
		assert inputs.length > 0;
		m_sensorTrigger = sensorTrigger;
		m_inputs = inputs;
	}

	public double getRangeInFeet() {
		m_sensorTrigger.pulse(.02);
		double total = 0;
		
		for (AnalogInput input : m_inputs) {
			total += Constants.RFVoltsToFt(input.getVoltage());
		}
		
		return total / m_inputs.length;
	}
}
