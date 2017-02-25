package org.usfirst.frc.team95.robot.auto;

import org.usfirst.frc.team95.robot.RangeFinder;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;

public class RangeBasedGearScorer extends Auto {
	private static double minimumFacePushDistanceInFeet = 1;
	private static double timeToFacePushInSeconds = 0.85;
	private Solenoid m_pooper;
	private Solenoid m_facePush;
	private RangeFinder m_rangeFinder;
	private enum State {IDLE, PUSHING, POOPING, PUSHING_POOP};
	private State m_state;
	private Timer m_timer;
	private double m_delayTime;
	
	public RangeBasedGearScorer(Solenoid pooper, Solenoid facePush, RangeFinder rangeFinder) {
		m_pooper = pooper;
		m_facePush = facePush;
		m_rangeFinder = rangeFinder;
		m_state = State.IDLE;
		m_timer = new Timer();
	}

	@Override
	public void init() {
		m_state = State.IDLE;
	}

	@Override
	public void start() {
		if (m_state == State.IDLE)
		{
			if (m_rangeFinder.getRangeInFeet() > minimumFacePushDistanceInFeet) {
				m_state = State.PUSHING;
				m_facePush.set(true);
				m_delayTime = timeToFacePushInSeconds;
				m_timer.reset();
				m_timer.start();
			}
			else {
				m_state = State.POOPING;
				m_pooper.set(true);
			}
		}
	}
	
	@Override
	public void update() {
		if (m_state != State.IDLE) {
			if (m_timer.hasPeriodPassed(m_delayTime)) {
				m_timer.stop();
				if (m_state == State.PUSHING) {
					m_state = State.PUSHING_POOP;
					m_pooper.set(true);
				}
			}
		}
	}

	@Override
	public void stop() {
		m_pooper.set(false);
		m_facePush.set(false);
		if (m_state == State.PUSHING || m_state == State.PUSHING_POOP) {
			m_state = State.IDLE; // Use a pulling state if needed?
		}
		else {
			m_state = State.IDLE;
		}
	}

	@Override
	public boolean isDone() {
		return m_state == State.IDLE;
	}

	@Override
	public boolean succeeded() {
		// TODO Auto-generated method stub
		return true;
	}
}
