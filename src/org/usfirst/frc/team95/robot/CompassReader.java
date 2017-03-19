package org.usfirst.frc.team95.robot;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import edu.wpi.first.wpilibj.I2C;

public class CompassReader
	{
		private I2C m_i2c;
		private VariableStore m_variables;
		final int deviceAddress = 0x1E;
		static double alphaOrig = -164;
		static double betaOrig = -25;
		public double alpha = alphaOrig;// these ones can be overwritten
		public double beta = betaOrig;
		static String compassAlphaVariableName = "compass_alpha";
		static String compassBetaVariableName = "compass_beta";

		public CompassReader(VariableStore variables)
			{
				m_i2c = new I2C(I2C.Port.kOnboard, deviceAddress);
				m_i2c.write(2, 0);
				m_variables = variables;
				alpha = variables.GetDouble(compassAlphaVariableName, alphaOrig);
				beta = variables.GetDouble(compassBetaVariableName, betaOrig);
			}

		// private static final byte kDataRegister = 0x32;

		public double getRawCompX()
			{
				ByteBuffer angle = ByteBuffer.allocateDirect(2);
				m_i2c.read(3, 2, angle);

				// Sensor is little endian... swap bytes
				angle.order(ByteOrder.BIG_ENDIAN);

				// return angle.get();// 0.00390625
				return angle.getShort(0);
			}

		public double getRawCompZ()
			{
				ByteBuffer angle = ByteBuffer.allocateDirect(2);
				m_i2c.read(5, 2, angle);

				// Sensor is little endian... swap bytes
				angle.order(ByteOrder.BIG_ENDIAN);

				// return angle.get();// 0.00390625
				return angle.getShort(0);
			}

		public double getRawCompY()
			{
				ByteBuffer angle = ByteBuffer.allocateDirect(2);
				m_i2c.read(7, 2, angle);

				// Sensor is little endian... swap bytes
				angle.order(ByteOrder.BIG_ENDIAN);

				// return angle.get();// 0.00390625
				return angle.getShort(0);
			}

		public double getHeading()
			{
				double ang;// , yrot, zrot, ysig;
				double y = getRawCompY() - alpha;
				double z = getRawCompZ() - beta;
				// Just using Hard Fe correction
				// double theta = -0.77964007;
				// double sigma = 2.397130726;

				/*
				 * yrot = (y * Math.cos(theta)) + (z * -Math.sin(theta)); zrot = (y * Math.sin(theta)) + (z* Math.cos(theta)); ysig = yrot/ sigma; y = (ysig * Math.cos(-theta)) + (zrot * -Math.sin(-theta)); z = (ysig * Math.sin(-theta)) + (zrot * Math.cos(-theta));
				 */

				ang = Math.atan2(z, y);
				return ang;
			}

		public void compCal(double newAlpha, double newBeta)
			{
				alpha = newAlpha;
				beta = newBeta;
				m_variables.StoreValue(compassAlphaVariableName, newAlpha);
				m_variables.StoreValue(compassBetaVariableName, newBeta);

			}

		public void compReset()
			{
				compCal(alphaOrig, betaOrig);
			}
	}