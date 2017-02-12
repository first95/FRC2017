package org.usfirst.frc.team95.robot;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.PowerDistributionPanel;

public class VoltageCompensatedShooter {
	private final double m_maximumVoltage = 12;
	private CANTalon m_shooter;
	private PowerDistributionPanel m_panel;
	private final double m_voltageLevelToMaintain;
	private boolean m_on;
	
	public VoltageCompensatedShooter(CANTalon shooter, double voltageLevelToMaintain) {
		m_shooter = shooter;
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
			m_shooter.set(currentVoltage / m_maximumVoltage);
		}
		else
		{
			m_shooter.set(0);
		}
	}
	
}

