@OptIn(ExperimentalUnsignedTypes::class)
fun main() {
    val n = 85
    val m = 19
    val rabbits = ULongArray(m, { 0u })
    rabbits[0] = 1u
    println("Month 1\n${rabbits.joinToString()}")
    for (month in 2..n) {
        println("Month $month")
        var offspring: ULong = 0u
        for (i in m-1 downTo 1) {
            offspring += rabbits[i]
            rabbits[i] = rabbits[i-1]
        }
        rabbits[0] = offspring
        println(rabbits.joinToString())
    }
    println("Total rabbits: ${ rabbits.sum() }")
}