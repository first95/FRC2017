package org.usfirst.frc.team95.robot;

import edu.wpi.first.wpilibj.PowerDistributionPanel;

public class VoltageCompensatedShooter {
	private final double m_maximumVoltage = 12;
	private PowerDistributionPanel m_panel;
	private final double m_voltageLevelToMaintain;
	private boolean m_on;
	
	public VoltageCompensatedShooter(double voltageLevelToMaintain) {
		m_panel = new PowerDistributionPanel();
		m_voltageLevelToMaintain = voltageLevelToMaintain;
		turnOff();
	}
	
	public void turnOn()
	{
		m_on = true;
		adjustVoltage();
	}
	
	public void turnOff()
	{
		m_on = false;
		adjustVoltage();
	}
	
	public void adjustVoltage()
	{
		if (m_on)
		{
			double currentVoltage = Math.min(m_panel.getVoltage(), m_voltageLevelToMaintain);
			RobotMap.shooter.set(currentVoltage / m_maximumVoltage);
		}
		else
		{
			RobotMap.shooter.set(0);
		}
	}
	
}

