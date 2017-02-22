package org.usfirst.frc.team95.robot.auto;

import java.util.ArrayList;
import java.util.Iterator;

public class SequentialMove extends Auto {
	Auto[] moves;
	int moveNum = 0;

	boolean done = false;

	public SequentialMove(Auto[] moves) {
		this.moves = moves.clone();
	}
	
	protected SequentialMove() {
		this.moves = null;
	}
	
	protected void SetMoves(Auto[] moves) {
		this.moves = moves.clone();
	}

	@Override
	public void init() {
		for(Auto move:moves) {
			move.init();
		}
	}
	
	@Override
	public void start() {
		moveNum = 0;
		done = false;
		moves[0].start();
	}
	
	@Override
	public void update() {
		
		if (moves[moveNum].done()) {
			moves[moveNum].stop();
			if (moveNum < moves.length - 1) {
				moveNum++;
				moves[moveNum].start();
			} else {
				done = true;
			}
		}
		moves[moveNum].update();
	}

	@Override
	public void stop() {
		moves[moveNum].stop();
	}

	@Override
	public boolean done() {
		return done;
	}

}