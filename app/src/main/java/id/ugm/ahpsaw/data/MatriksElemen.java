package id.ugm.ahpsaw.data;

import java.io.Serializable;

public class MatriksElemen implements Serializable {
    double[][] comparison;
    double[] weight;
    int n;
    double weightOfParent;
    String[] nameOfCriteria;


    public MatriksElemen(double[][] comparison, String[] name) {
        this.comparison = comparison;
        this.nameOfCriteria = name;
        this.n = name.length;
        this.weight = new double[n];
        this.weightOfParent = 1;
    }
    public MatriksElemen(double[][] comparison, String[] name, double weightOfParent) {
        this.comparison = comparison;
        this.nameOfCriteria = name;
        this.n = name.length;
        this.weight = new double[n];
        this.weightOfParent = weightOfParent;
    }

    private double[] calcWeight() {
        double[][] norm = new double[n][n];
        double[] sumColumn = new double[n];
        double[] sumRow = new double[n];
        //NORMALIZATION

        for (int i = 0; i < n; i++) {
            sumColumn[i] = 0.0;
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                sumColumn[j] += comparison[i][j];
            }
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                norm[i][j] = comparison[i][j] / sumColumn[j];
                //System.out.println("norm[" + i + "][" + j +"]= "+ norm[i][j]);
            }
        }

        //CALCULATE WEIGHT
        for (int i = 0; i < n; i++) {
            sumRow[i] = 0.0;
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                sumColumn[j] += comparison[i][j];
            }
        }
        for (int i = 0; i < n; i++) {
            sumRow[i] = 0.0;
            for (int j = 0; j < n; j++) {
                sumRow[i] += norm[i][j];
            }
            //System.out.println("sumRow" + i +"= " + sumRow[i]);
            weight[i] = sumRow[i] / n;
        }
        return weight;
    }

    public double[] calcAbsoluteWeight() {
        double[] localWeight = calcWeight();
        double[] absoluteWeight = new double[calcWeight().length];
        for (int i=0; i<calcWeight().length; i++){
            absoluteWeight[i] = localWeight[i]*weightOfParent;
        }
        return absoluteWeight;
    }

    public double consistencyRatio() {
        double[] WSV = new double[n];
        double[] CV = new double[n];
        double eigenMaks;
        double CI;
        double CR;
        double[] RC = new double[]{0.0, 0.0, 0.58, 0.9, 1.12, 1.24, 1.32, 1.41, 1.45, 1.49, 1.51, 1.48, 1.56, 1.57, 1.59};

        //CALCULATE WSV
        calcWeight();
        for (int i = 0; i < n; i++) {
            WSV[i] = 0.0;
            for (int j = 0; j < n; j++) {
                WSV[i] += comparison[i][j] * weight[j];
            }
        }

        //CALCULATE CV
        for (int i = 0; i < n; i++) {
            CV[i] = WSV[i] / weight[i];
        }

        //CALCULATE EIGENMAKS
        double sumCV = 0.0;
        for (int i = 0; i < n; i++) {
            sumCV += CV[i];
        }
        eigenMaks = sumCV / n;

        //CALCULATE CI
        CI = (eigenMaks - n) / (n - 1);

        //CALCULATE CR
        CR = CI / RC[n - 1];
        return CR;
    }

    public String[] getNameOfCriteria() {
        return nameOfCriteria;
    }

    public void setNameOfCriteria(String[] nameOfCriteria) {
        this.nameOfCriteria = nameOfCriteria;
    }

    public double[][] getComparison() {
        return comparison;
    }

    public void setComparison(double[][] comparison) {
        this.comparison = comparison;
    }
}

