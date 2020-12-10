package id.ugm.ahpsaw.data

data class AlternatifData (
    var nama: String,
    var positif: Double,
    var kepadatan: Double,
    var longitude: Double,
    var latitude: Double,
    var ketinggian: Double,
    var panjangJalan: Double,
    var suhu: Double,
    var kecepatanAngin: Double,
    var kelembaban: Double,
    var presipitasi: Double,
    var tekananUdara: Double) {
}