package org.usfirst.frc.team95.robot.auto;

public class ScoreGear extends SequentialMove {

	public ScoreGear() {
		super(new Auto[]{new GoToLiftAdvanced(), new TimedMove(0.2, 0.2, 500), new OpenPooper(500), 
				new TimedMove(-0.2, -0.2, 500), new ClosePooper()});
	}

}
