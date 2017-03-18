package org.usfirst.frc.team95.robot;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import edu.wpi.first.wpilibj.I2C;

public class GyroReader
	{
		private I2C m_i2c;
		final double unitsToDeg = 14.375;
		final int deviceAddress = 0x68;

		public GyroReader()
			{
				m_i2c = new I2C(I2C.Port.kOnboard, deviceAddress);
			}

		// private static final byte kDataRegister = 0x32;

		public double getXAng()
			{

				ByteBuffer rawAccel = ByteBuffer.allocateDirect(2);
				m_i2c.read(0x1D, 2, rawAccel);

				// Sensor is little endian... swap bytes
				rawAccel.order(ByteOrder.BIG_ENDIAN);

				// return rawAccel.get();// 0.00390625
				return rawAccel.getShort(0) / unitsToDeg;
			}

		public double getYAng()
			{

				ByteBuffer rawAccel = ByteBuffer.allocateDirect(2);
				m_i2c.read(0x1F, 2, rawAccel);

				// Sensor is little endian... swap bytes
				rawAccel.order(ByteOrder.BIG_ENDIAN);

				// return rawAccel.get();// 0.00390625
				return rawAccel.getShort(0) / unitsToDeg;
			}

		public double getZAng()
			{

				ByteBuffer rawAccel = ByteBuffer.allocateDirect(2);
				m_i2c.read(0x21, 2, rawAccel);

				// Sensor is little endian... swap bytes
				rawAccel.order(ByteOrder.BIG_ENDIAN);

				// return rawAccel.get();// 0.00390625
				return rawAccel.getShort(0) / unitsToDeg;
			}
	}
