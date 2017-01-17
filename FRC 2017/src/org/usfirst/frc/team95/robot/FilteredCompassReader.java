package org.usfirst.frc.team95.robot;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JDialog;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.ejml.data.DenseMatrix64F;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class FilteredCompassReader extends CompassReader {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TestWithRecData(0, true);
	}

	public static void TestWithRecData(int dataset, boolean terminateAfter) {
		final int NUM_DATAPOINTS;
		final double DT;
		final String DATA_FILENAME;
		
		switch(dataset) {
		case 0:
		default:
			NUM_DATAPOINTS = 7162;
			DT = 0.1;
			DATA_FILENAME = "data/2017-1-13 IMU sensor data with corrections - Kovaka.csv";
			break;
		}
		
		CSVParser parser = null;
		try {
			parser = new CSVParser(new FileReader(DATA_FILENAME), CSVFormat.DEFAULT);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// t for Theta, the heading to magnetic north
		// w for lowercase omega, angular velocity
		double[] time        = new double[NUM_DATAPOINTS];
		double[] t_measured  = new double[NUM_DATAPOINTS];
		double[] t_estimated = new double[NUM_DATAPOINTS];
		double[] t_demod     = new double[NUM_DATAPOINTS];
		double   t_cum_err   = 0;
		double[] d_theta     = new double[NUM_DATAPOINTS];
		double[] w_measured  = new double[NUM_DATAPOINTS];
		double[] w_estimated = new double[NUM_DATAPOINTS];
		
		

		// Configure filter.
		// State vector (n = 2): 
		// [ position (theta, t) ]
		// [ angular speed (omega, w) ]
		// Sensor vector (m = 3):
		// [ magnetometer theta ]
		// [ d(magnetometer theta) ]
		// [ gyro angular speed ] 
		KalmanFilterSimple kf = new KalmanFilterSimple();
		DenseMatrix64F F = new DenseMatrix64F(new double[][]{
			{1.0, DT / 45},
			{0.0, 1.0},
		}); 
		DenseMatrix64F Q = new DenseMatrix64F(new double[][]{
			{0.05, 0.0},
			{0.0,  1.0},
		}); 
		DenseMatrix64F H = new DenseMatrix64F(new double[][]{
			{1.0, 0.0},
			{0.0, 1.0},
			{0.0, 1.0},
		}); 
		DenseMatrix64F R = new DenseMatrix64F(new double[][]{
			{10.0, 0.0, 0.0},
			{ 0.0, 1.0, 0.0},
			{ 0.0, 0.0, 0.0},
		}); 
		kf.configure(F, Q, H);
		
		// Run filter
		DenseMatrix64F x_init = new DenseMatrix64F(new double [][]{
			{2.0},
			{0},
		}); 
		DenseMatrix64F p_init = new DenseMatrix64F(new double [][]{
			{1, 0},
			{0, 1},
		}); 
		kf.setState(x_init, p_init);
		
		
		int i = 0;
		try {
			// I deep-sixed the column headers, they were:
			// X,Y,Z,gX,gY,gZ,Hard Fe Y a=-36,Hard Fe Z ß=-221.5
			final int COL_CORR_MAG_Y = 6;
			final int COL_CORR_MAG_Z = 7;
			final int COL_GYRO_Z = 5;
			double heading_boost = 0.0;
			for(CSVRecord record : parser.getRecords()) {
				t_measured[i] = Math.atan2(Double.parseDouble(record.get(COL_CORR_MAG_Y)), Double.parseDouble(record.get(COL_CORR_MAG_Z)));
				w_measured[i] = Double.parseDouble(record.get(COL_GYRO_Z)) * Math.PI / 180.0;
				if((i > 0) && (
						(t_measured[i] >  Math.PI / 2.0 && t_measured[i - 1] < -Math.PI / 2.0) ||
						(t_measured[i] < -Math.PI / 2.0 && t_measured[i - 1] >  Math.PI / 2.0)
						 )) {
					// Defunct the modulo
					heading_boost += 2 * Math.PI;
				}
				t_demod[i] = t_measured[i] + heading_boost;
				time[i] = i * DT;
				// In this case we've been sampling the magnetometer faster than it updates.
				// The estimate has an update every timepoint, so there aren't zeroes most of
				// the samples.  The downfall is a 1-sample lag.
				d_theta[i] = i>1? t_estimated[i - 1] - t_estimated[i - 2] : 0;
//				d_theta[i] = i>0? t_demod[i] - t_demod[i - 1] : 0;
				
				// The above computes the change over one dt, while the units of w are in rad/s
				// We didn't measure the dt in the data capture.  So here we approximate dividing by that dt.
				d_theta[i] *= 250;
				
				DenseMatrix64F z = new DenseMatrix64F(new double [][]{
					{t_demod[i]},
					{d_theta[i]},
					{w_measured[i]},
				});
				kf.predict();
				double x_prediction = kf.getState().data[0];
				double v_prediction = kf.getState().data[1];
				kf.update(z, R);
				t_estimated[i] = kf.getState().data[0];
				w_estimated[i] = kf.getState().data[1];

				// This should eventually add up to zero
				t_cum_err += (t_estimated[i] - t_demod[i]);
				
				++i;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Cumulative error: " + t_cum_err);
	
		
		// Graph results
		XYSeries x_m_s = new XYSeries("Measured position");
		XYSeries x_e_s = new XYSeries("Estimated position");
		XYSeries d_m_s = new XYSeries("Change in position");
		XYSeries v_m_s = new XYSeries("Measured speed");
		XYSeries v_e_s = new XYSeries("Estimated speed");
		for(i = 0; i < NUM_DATAPOINTS; ++i) {
			x_m_s.add(time[i], t_demod[i]);
			x_e_s.add(time[i], t_estimated[i]);
			d_m_s.add(time[i], d_theta[i]);
			v_m_s.add(time[i], w_measured[i]);
			v_e_s.add(time[i], w_estimated[i]);
		}
		XYSeriesCollection sc = new XYSeriesCollection();
		sc.addSeries(x_e_s);
		sc.addSeries(x_m_s);
		sc.addSeries(d_m_s);
		sc.addSeries(v_e_s);
		sc.addSeries(v_m_s);
		
		JFreeChart chart = ChartFactory.createXYLineChart("KF test w/rec data", "Time (s)", "Value", sc);
		
		showChart(chart, terminateAfter);

	}


	
	/**
	 * Display a chart in a dialog window
	 * @param chart
	 * @param terminateAfter
	 */
	private static void showChart(JFreeChart chart, boolean terminateAfter) {
		ChartPanel panel = new ChartPanel(chart);
        panel.setFillZoomRectangle(true);
        panel.setMouseWheelEnabled(true);
        JDialog dialog = new JDialog();
        if(terminateAfter) {
	        dialog.addWindowListener(new WindowListener() {
				@Override
				public void windowOpened(WindowEvent e) {}
				@Override
				public void windowIconified(WindowEvent e) {}
				@Override
				public void windowDeiconified(WindowEvent e) {}
				@Override
				public void windowDeactivated(WindowEvent e) {}
				@Override
				public void windowClosing(WindowEvent e) { System.exit(0);}
				@Override
				public void windowClosed(WindowEvent e) {}
				@Override
				public void windowActivated(WindowEvent e) {}
			});
        }
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.add(panel);
        dialog.pack();
        dialog.setVisible(true);        
	}

}
