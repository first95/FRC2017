package org.usfirst.frc.team95.robot;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import edu.wpi.first.wpilibj.Solenoid;

public class SystemLogger
	{

		private String systemDate;
		private String logFileTitleCSV;
		private String logFileTitleRAW;
		private File logFileCSV;
		private File logFileRaw;
		private FileWriter fWCSV;
		private FileWriter fWRAW;

		public Double pixHeight = 0.0;
		public Double pixDis = 0.0;

		public SystemLogger() throws IOException
			{
				SystemLoggerInit();
			}

		private void SystemLoggerInit() throws IOException
			{
				Date currentDate = new Date();
				systemDate = currentDate.toString();
				logFileTitleCSV = systemDate;
				logFileTitleRAW = systemDate + "_RAW";

				File logFileCSV = new File("/home/lvuser/Logs/" + logFileTitleCSV + ".csv");
				File logFileRAW = new File("/home/lvuser/Logs/" + logFileTitleRAW + ".txt");

				fWCSV = new FileWriter(logFileCSV);
				fWRAW = new FileWriter(logFileRAW);

				fWCSV.write("time, currentR, currentL, voltageR, voltageL, encoderR, encoderL,  rpmR, rpmL, heightoftargetinpix, distancetotarget, compressoronbol, groundloadgearbol, brakedeploybol, faceextendbol, tiphatbol, jazzhandsbol, automovechosen");
			}

		public void SystemLoggerWrite(String mData)
			{

				try
					{
						fWCSV.write(mData + "\n");
						fWCSV.flush();
					}
				catch (Exception e)
					{
						e.printStackTrace();
					}

			}

		public void SystemLoggerWriteRAW(String mData)
			{

				try
					{
						fWRAW.write(RobotMap.systemLoggerTimer.get() + " -- " + mData);
						fWRAW.flush();
					}
				catch (IOException e)
					{
						e.printStackTrace();
					}

			}

		//

		public void SystemLoggerNullCheck(Double doubleNullCheckHolder[], Boolean booleanNullCheckHolder[], String stringNullCheckHolder)
			{

				pixHeight = 0.0;
				pixDis = 0.0;

				if (RobotMap.gearLiftFinder == null)
					{
						pixHeight = 0.0;
						pixDis = 0.0;
					}
				else
					{
						pixDis = RobotMap.gearLiftFinder.getDistanceFromCamToTarget();
						pixHeight = RobotMap.gearLiftFinder.getHightOfObjectInPixels();
					}

				for (Double i : doubleNullCheckHolder)
					{
						if (i == null)
							{
								i = 0.0;
							}
					}

				for (Boolean i : booleanNullCheckHolder)
					{
						if (i == null)
							{
								i = false;
							}
					}

				if (stringNullCheckHolder == null)
					{
						stringNullCheckHolder = "";
					}

			}

		public void SystemLoggerClose()
			{
				try
					{
						fWCSV.close();
						fWRAW.close();
					}
				catch (IOException e)
					{
						e.printStackTrace();
					}
			}
	}
