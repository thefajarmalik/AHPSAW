package id.ugm.ahpsaw.data

import java.io.Serializable

data class HasilData(
    var nama: String,
    var skor: Double,
    var zona: Int): Serializable{
}