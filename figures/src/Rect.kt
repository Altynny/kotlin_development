class Rect(x: Int, y: Int, var width: Int, var height: Int) : Figure(x, y) {
    override val figureName = "Rect"
    override val area: Float get() = (width * height).toFloat()

    override fun resize(zoom: Int) {
        width *= zoom; height *= zoom
    }

    override fun rotate(direction: RotateDirection, centerX: Int, centerY: Int) {
        val tX = x - centerX
        val tY = y - centerY
        when(direction) {
            RotateDirection.Clockwise        -> {x = centerX + tY; y = centerY - tX}
            RotateDirection.CounterClockwise -> {x = centerX - tY; y = centerY + tX}
        }
        val temp = width
        width = height
        height = temp
    }

    override fun toString(): String {
        return "${figureName}: (x = $x, y = $y), width = $width, height = $height, area = $area"
    }
}