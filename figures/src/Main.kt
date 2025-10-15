fun main() {
    val figures = listOf<Figure>(
        Rect(1,1,2,1),
        Square(1, 1, 2),
        Circle(1,1,2),
        )
    for (figure in figures) {
        println("Before zoom:\n\t${figure}")
        figure.resize(2)
        println("After x2 zoom:\n\t${figure}")
        println("Before rotation:\n\t${figure}")
        figure.rotate(RotateDirection.Clockwise, 0, 0)
        println("After 90 deg clockwise rotation:\n\t${figure}\n")
    }
}