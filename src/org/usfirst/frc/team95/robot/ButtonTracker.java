/*
 * This File is released under the LGPL.
 * You may modify this software, use it in a project, and so on,
 * as long as this header remains intact.
 */

package org.usfirst.frc.team95.robot;

import java.util.HashMap;

import org.usfirst.frc.team95.robot.auto.Auto;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * 
 * @author daroc
 */
public class ButtonTracker implements PollableSubsystem {

	static HashMap<Joystick, ButtonTracker[]> usedNumbers = new HashMap<Joystick, ButtonTracker[]>();

	int mChannel;
	Joystick mJoystick;
	boolean mNow, mLast;

	Auto move;

	public ButtonTracker(Joystick Joystick, int Channel) {
		mChannel = Channel;
		mJoystick = Joystick;

		if (!usedNumbers.containsKey(Joystick)) {
			usedNumbers.put(Joystick, new ButtonTracker[17]);
		}

		if (usedNumbers.get(Joystick)[Channel] != null) {
			// SmartDashboard.putBoolean("ERROR", true);
			// System.out.println("MORE THAN ONE BUTTON TRACKER PER BUTTON.");
			DriverStation.reportError("MORE THAN ONE BUTTON TRACKER PER BUTTON!", false);
		}

		usedNumbers.get(Joystick)[Channel] = this;
	}

	public ButtonTracker(Joystick joystick, int channel, Auto move) {
		this(joystick, channel);

		this.move = move;
	}

	public boolean isPressed() {
		return mNow;
	}

	public boolean wasJustPressed() {
		return (mNow && (!mLast));
	}

	public boolean wasJustReleased() {
		return (!mNow && mLast);
	}

	public void update() {
		mLast = mNow;
		mNow = mJoystick.getRawButton(mChannel);

		if (this.move != null) {
			if (wasJustPressed()) {
				this.move.init();
			} else if (isPressed()) {
				this.move.update();
			} else if (wasJustReleased()) {
				this.move.stop();
			}
		}
	}

	public void init() {
		;
	}
}