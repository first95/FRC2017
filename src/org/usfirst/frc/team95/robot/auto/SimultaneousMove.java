package org.usfirst.frc.team95.robot.auto;

public class SimultaneousMove extends Auto {
	Auto[] table;

	public SimultaneousMove(Auto[] moves) {
		table = moves;
	}

	@Override
	public void init() {
		for (Auto move : table) {
			move.init();
		}
	}

	@Override
	public void update() {
		for (Auto move : table) {
			move.update();
		}
	}

	@Override
	public void stop() {
		for (Auto move : table) {
			move.stop();
		}

	}

	@Override
	public boolean done() {
		boolean acc = true;
		for (Auto move : table) {
			acc = acc && move.done();
		}
		return acc;
	}

}