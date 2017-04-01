package org.usfirst.frc.team95.robot.auto;

public class SequentialMove extends Auto
	{
		Auto[] moves;
		int moveNum = 0;

		boolean done = false;
		boolean succeeded = false;

		public SequentialMove(Auto[] moves)
			{
				this.moves = moves.clone();
			}

		protected SequentialMove()
			{
				this.moves = null;
			}

		protected void SetMoves(Auto[] moves)
			{
				this.moves = moves.clone();
			}

		@Override
		public void init()
			{
				for (Auto move : moves)
					{
						move.init();
					}
			}

		@Override
		public void start()
			{
				moveNum = 0;
				done = false;
				moves[0].start();
			}

		@Override
		public void update()
			{

				if (moves[moveNum].isDone())
					{
						moves[moveNum].stop();
						if ((moveNum < moves.length - 1) && moves[moveNum].succeeded())
							{
								moveNum++;
								moves[moveNum].start();
							}
						else
							{
								succeeded = moves[moveNum].succeeded();
								done = true;
								stop();
							}
					}
				else
					{
						moves[moveNum].update();
					}
			}

		@Override
		public void stop()
			{
				moves[moveNum].stop();
			}

		@Override
		public boolean isDone()
			{
				return done;
			}

		@Override
		public boolean succeeded()
			{
				return succeeded;
			}
	}