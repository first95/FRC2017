package org.usfirst.frc.team95.robot.auto;

public class ScoreGear extends SequentialMove {

	public ScoreGear() {
		//super(new Auto[]{new GoToLiftAdvanced(), new TimedMove(-0.3, -0.3, .5), new OpenPooper(.5), 
			//	new TimedMove(0.2, 0.2, .5), new ClosePooper()});
		
		super(new Auto[]{new GoToLiftAdvanced(), new TimedMove(-0.6, -0.6, .35), new OpenPooper(.5), 
				new TimedMove(0.2, 0.2, .5)});
	}

}
