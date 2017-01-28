package org.usfirst.frc.team95.robot;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.ADXL345_I2C.Axes;



public class CompassReader {
	private I2C m_i2c;
	final int deviceAddress = 0x1E;
	double alphaOrig = -164;
	double betaOrig = -25;
	double alpha = -164;
	double beta = -25;
	public CompassReader() {
		m_i2c = new I2C(I2C.Port.kOnboard, deviceAddress);	
		m_i2c.write(2, 0);
	}
	
	//private static final byte kDataRegister = 0x32;
	
	public double getRawCompX() {
		
	    ByteBuffer angle = ByteBuffer.allocateDirect(2);
	    m_i2c.read(3, 2, angle);
	    
	    // Sensor is little endian... swap bytes
	    angle.order(ByteOrder.BIG_ENDIAN);
	    
	    //return angle.get();// 0.00390625
	    return angle.getShort(0);
	}
	
	public double getRawCompZ() {
		
	    ByteBuffer angle = ByteBuffer.allocateDirect(2);
	    m_i2c.read(5, 2, angle);
	    
	    // Sensor is little endian... swap bytes
	    angle.order(ByteOrder.BIG_ENDIAN);
	    
	    //return angle.get();// 0.00390625
	    return angle.getShort(0);
	}
	
	public double getRawCompY() {
		
	    ByteBuffer angle = ByteBuffer.allocateDirect(2);
	    m_i2c.read(7, 2, angle);
	    
	    // Sensor is little endian... swap bytes
	    angle.order(ByteOrder.BIG_ENDIAN);
	    
	    //return angle.get();// 0.00390625
	    return angle.getShort(0);
	}

	public double getHeading() {
		
		double ang;//, yrot, zrot, ysig;
		double y = getRawCompY() - alpha;
		double z = getRawCompZ() - beta;
		//Just using Hard Fe correction
		//double theta = -0.77964007;
		//double sigma = 2.397130726;
		
		/*
		yrot = (y * Math.cos(theta)) + (z * -Math.sin(theta));
		zrot = (y * Math.sin(theta)) + (z* Math.cos(theta));
		ysig = yrot/ sigma;
		y = (ysig * Math.cos(-theta)) + (zrot * -Math.sin(-theta));
		z = (ysig * Math.sin(-theta)) + (zrot * Math.cos(-theta));
		*/
		
		ang = Math.atan2(z, y);
		return ang;
	}
	
	public void compCal(double newAlpha, double newBeta) {
		alpha = newAlpha;
		beta = newBeta;
		/*double ymin, ymax, zmin, zmax;
		ymax = 100000;//start temp val
		ymin = 100000;//start temp val
		zmax = 100000;//start temp val
		zmin = 100000;//start temp val
		
		if(ymax == 100000) {
			ymax = getRawCompY();
		}
		if(ymin == 100000) {
			ymin = getRawCompY();
		}
		if(zmax == 100000) {
			zmax = getRawCompZ();
		}
		if(zmin == 100000) {
			zmin = getRawCompZ();
		}
		
		if (getRawCompY() > ymax) {
			ymax = getRawCompY();
			System.out.println("ymax" + ymax);
		} else if (getRawCompY() < ymin) {
			ymin = getRawCompY();
			System.out.println("ymin" + ymin);
		}
		
		if (getRawCompZ() > zmax) {
			zmax = getRawCompZ();
			System.out.println("zmax" + zmax);
		} else if (getRawCompZ() < zmin) {
			zmin = getRawCompZ();
			System.out.println("zmin" + zmin);
		}
		
		alpha = (ymax - ymin) / 2;
		beta = (zmax - zmin) / 2;
		System.out.println("alpha" + alpha);
		System.out.println("beta" + beta); */
	}
	
	public void compReset() {
		alpha = alphaOrig;
		beta = betaOrig;
	}
	
	
}

