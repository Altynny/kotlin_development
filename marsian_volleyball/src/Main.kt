fun main() {
    print("k, x, y: ")
    val (k, x, y) = readln().split(' ').map { it.toInt() }
    if ((x >= k && x - y >= 2) || (y >= k && y - x >= 2)) {
        println(0)
    } else {
        val n1 = maxOf(0, k - x, 2 - (x - y))
        val n2 = maxOf(0, k - y, 2 - (y - x))
        println(minOf(n1, n2))
    }
}