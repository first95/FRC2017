/*
 * Copyright (c) 2009-2015, Peter Abeles. All Rights Reserved.
 * 
 * Copied into team95.robot package by John Walthour in 2017;
 * license information below continues to apply.  Original file can be
 * found here:
 * https://github.com/lessthanoptimal/ejml/blob/v0.27/examples/src/org/ejml/example/KalmanFilterSimple.java
 *
 * This file is part of Efficient Java Matrix Library (EJML).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.usfirst.frc.team95.robot;

import org.ejml.data.DenseMatrix64F;
import org.ejml.simple.SimpleMatrix;

/**
 * A Kalman filter implemented using SimpleMatrix.  The code tends to be easier to
 * read and write, but the performance is degraded due to excessive creation/destruction of
 * memory and the use of more generic algorithms.  This also demonstrates how code can be
 * seamlessly implemented using both SimpleMatrix and DenseMatrix64F.  This allows code
 * to be quickly prototyped or to be written either by novices or experts.
 *
 * @author Peter Abeles
 */
public class KalmanFilterSimple {

    // kinematics description
    private SimpleMatrix F,Q,H;

    // sytem state estimate
    private SimpleMatrix x,P;

    public void configure(DenseMatrix64F F, DenseMatrix64F Q, DenseMatrix64F H) {
        setF(F);
        this.Q = new SimpleMatrix(Q);
        this.H = new SimpleMatrix(H);
    }
    
    /** 
     * Update system model.  The system model includes dt, which is
     * why this is not fixed at initialization.
     *
     */
    public void setF(DenseMatrix64F F) { this.F = new SimpleMatrix(F); }

    public void setState(DenseMatrix64F x, DenseMatrix64F P) {
        this.x = new SimpleMatrix(x);
        this.P = new SimpleMatrix(P);
    }

    public void predict() {
        // x = F x
        x = F.mult(x);

        // P = F P F' + Q
        P = F.mult(P).mult(F.transpose()).plus(Q);
    }

    public void update(DenseMatrix64F _z, DenseMatrix64F _R) {
        // a fast way to make the matrices usable by SimpleMatrix
        SimpleMatrix z = SimpleMatrix.wrap(_z);
        SimpleMatrix R = SimpleMatrix.wrap(_R);

        // y = z - H x
        SimpleMatrix y = z.minus(H.mult(x));

        // S = H P H' + R
        SimpleMatrix S = H.mult(P).mult(H.transpose()).plus(R);

        // K = PH'S^(-1)
        SimpleMatrix K = P.mult(H.transpose().mult(S.invert()));

        // x = x + Ky
        x = x.plus(K.mult(y));

        // P = (I-kH)P = P - KHP
        P = P.minus(K.mult(H).mult(P));
    }

    public DenseMatrix64F getState() {
        return x.getMatrix();
    }

    public DenseMatrix64F getCovariance() {
        return P.getMatrix();
    }
}
