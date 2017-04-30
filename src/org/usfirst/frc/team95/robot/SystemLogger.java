package org.usfirst.frc.team95.robot;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import edu.wpi.first.wpilibj.Solenoid;

public class SystemLogger
	{

		private String systemDate;
		private String systemDay;
		private String systemMonth;
		private String systemNoDay;
		private String systemHour;
		private String systemMinute;
		private String systemSecond;
		private String systemTimeZone;
		private String systemYear;

		private Date finalDate;
		private Date currentDate;
		private SimpleDateFormat dateFormat;

		private String logFileTitleCSV;
		private String logFileTitleTimeline;
		private File logFileCSV;
		private File logFileTimeline;
		private FileWriter fWCSV;
		private FileWriter fWTimeline;

		public Double pixHeight = 0.0;
		public Double pixDis = 0.0;

		public SystemLogger() throws IOException
			{
				SystemLoggerInit();
			}

		private void SystemLoggerInit() throws IOException
			{
				currentDate = new Date();
				systemDate = currentDate.toString();

				// systemDay = systemDate.substring(1, 3);
				// systemMonth = systemDate.substring(5, 7);
				// systemNoDay = systemDate.substring(9, 10);
				// systemHour = systemDate.substring(12, 13);
				// systemMinute = systemDate.substring(15, 16);
				// systemSecond = systemDate.substring(18, 19);
				// systemTimeZone = systemDate.substring(21, 23);
				// systemYear = systemDate.substring(25, 28);
				//
				// systemDate = systemYear + systemMonth + systemDay + "-" + systemHour + systemMinute + systemSecond;

				dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

				try
					{
						finalDate = dateFormat.parse(systemDate);
					}
				catch (ParseException e)
					{
						e.printStackTrace();
					}

				logFileTitleCSV = finalDate.toString();
				logFileTitleTimeline = finalDate.toString() + "_Timeline";
				// logFileTitleCSV = systemDate;
				// logFileTitleTimeline = systemDate + "_Timeline";

				File logFileCSV = new File("/home/lvuser/Logs/" + logFileTitleCSV + ".csv");
				File logFileTimeline = new File("/home/lvuser/Logs/" + logFileTitleTimeline + ".txt");

				fWCSV = new FileWriter(logFileCSV);
				fWTimeline = new FileWriter(logFileTimeline);

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

		public void SystemLoggerWriteTimeline(String mData)
			{
				try
					{
						fWTimeline.write(RobotMap.systemLoggerTimer.get() + " -- " + mData + "\n");
						fWTimeline.flush();
					}
				catch (IOException e)
					{
						e.printStackTrace();
					}

			}

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
						fWTimeline.close();
					}
				catch (IOException e)
					{
						e.printStackTrace();
					}
			}
	}
