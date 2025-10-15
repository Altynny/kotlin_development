class Square(x: Int, y: Int, var width: Int) : Figure(x, y) {
    override val figureName = "Square"
    override val area: Float get() = (width * width).toFloat()

    override fun resize(zoom: Int) {
        width *= zoom;
    }

    override fun rotate(direction: RotateDirection, centerX: Int, centerY: Int) {
        val tX = x - centerX
        val tY = y - centerY
        when(direction) {
            RotateDirection.Clockwise        -> {x = centerX + tY; y = centerY - tX}
            RotateDirection.CounterClockwise -> {x = centerX - tY; y = centerY + tX}
        }
    }

    override fun toString(): String {
        return "${figureName}: (x = $x, y = $y), width = $width, area = $area"
    }
}