package org.usfirst.frc.team95.robot.auto;

import java.util.ArrayList;
import java.util.Iterator;

public class SequentialMove extends Auto {
	Auto move;
	Iterator<Auto> table;

	boolean done = false;

	public SequentialMove(Auto[] moves) {
		ArrayList<Auto> a = new ArrayList<Auto>();
		for (Auto x : moves) {
			a.add(x);
		}
		table = a.iterator();
		move = table.next();
	}

	public SequentialMove(Iterator<Auto> moves) {
		this.table = moves;
	}

	@Override
	public void init() {
		move.init();
	}

	@Override
	public void update() {
		if (move.done()) {
			move.stop();
			if (table.hasNext()) {
				move = table.next();
				move.init();
			} else {
				done = true;
			}
		}
		move.update();
	}

	@Override
	public void stop() {
		move.stop();

	}

	@Override
	public boolean done() {
		return done;
	}

}