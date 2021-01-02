package id.ugm.ahpsaw.data

import android.util.Log
import java.io.Serializable

class MatriksElemen : Serializable {
    var comparison: Array<DoubleArray>
    var weight: DoubleArray
    var n: Int
    var weightOfParent: Double
    var nameOfCriteria: Array<String>

    constructor(comparison: Array<DoubleArray>, name: Array<String>) {
        this.comparison = comparison
        nameOfCriteria = name
        n = name.size
        weight = DoubleArray(n)
        weightOfParent = 1.0
        comparison.forEachIndexed{i, item ->
            item.forEachIndexed{j, item2 ->
                Log.i("MatriksElemen Construction(no parent weight): ", "matriks[$i][$j]=${comparison[i][j]}")
            }
        }

    }

    constructor(
        comparison: Array<DoubleArray>,
        name: Array<String>,
        weightOfParent: Double
    ) {
        this.comparison = comparison
        nameOfCriteria = name
        n = name.size
        weight = DoubleArray(n)
        this.weightOfParent = weightOfParent
        comparison.forEachIndexed{i, item ->
            item.forEachIndexed{j, item2 ->
                Log.i("MatriksElemen Construction(WITH PARENT WEIGHT): ", "matriks[$i][$j]=${comparison[i][j]}")
            }
        }
    }

    private fun calcWeight(): DoubleArray {
        val norm = Array(n) { DoubleArray(n) }
        val sumColumn = DoubleArray(n)
        val sumRow = DoubleArray(n)
        //NORMALIZATION
        for (i in 0 until n) {
            sumColumn[i] = 0.0
        }
        for (i in 0 until n) {
            for (j in 0 until n) {
                sumColumn[j] += comparison[i][j]
            }
        }
        for (i in 0 until n) {
            for (j in 0 until n) {
                norm[i][j] = comparison[i][j] / sumColumn[j]
                //System.out.println("norm[" + i + "][" + j +"]= "+ norm[i][j]);
            }
        }

        //CALCULATE WEIGHT
        for (i in 0 until n) {
            sumRow[i] = 0.0
        }
        for (i in 0 until n) {
            for (j in 0 until n) {
                sumColumn[j] += comparison[i][j]
            }
        }
        for (i in 0 until n) {
            sumRow[i] = 0.0
            for (j in 0 until n) {
                sumRow[i] += norm[i][j]
            }
            //System.out.println("sumRow" + i +"= " + sumRow[i]);
            weight[i] = sumRow[i] / n
        }
        return weight
    }

    fun calcAbsoluteWeight(): DoubleArray {
        val localWeight = calcWeight()
        val absoluteWeight = DoubleArray(calcWeight().size)
        for (i in 0 until n) {
            absoluteWeight[i] = localWeight[i] * weightOfParent
            Log.i("calcAbsoluteWeight(): ", "absoluteWeight[$i]=${localWeight[i]} * ${weightOfParent} = ${absoluteWeight[i]}")
        }
        return absoluteWeight
    }

    fun consistencyRatio(): Double {
        val WSV = DoubleArray(n)
        val CV = DoubleArray(n)
        val eigenMaks: Double
        val CI: Double
        val CR: Double
        val RC = doubleArrayOf(
            0.0,
            0.0,
            0.58,
            0.9,
            1.12,
            1.24,
            1.32,
            1.41,
            1.45,
            1.49,
            1.51,
            1.48,
            1.56,
            1.57,
            1.59
        )

        //CALCULATE WSV
        calcWeight()
        for (i in 0 until n) {
            WSV[i] = 0.0
            for (j in 0 until n) {
                WSV[i] += comparison[i][j] * weight[j]
            }
        }

        //CALCULATE CV
        for (i in 0 until n) {
            CV[i] = WSV[i] / weight[i]
        }

        //CALCULATE EIGENMAKS
        eigenMaks = CV.sum()/ n

        //CALCULATE CI
        CI = (eigenMaks - n) / (n - 1)

        //CALCULATE CR
        CR = CI / RC[n - 1]
        return CR
    }

}