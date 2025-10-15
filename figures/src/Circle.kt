import kotlin.math.PI

class Circle(x: Int, y: Int, var radius: Int) : Figure(x, y) {
    override val figureName = "Circle"
    override val area: Float get() = (PI * radius * radius).toFloat()

    override fun resize(zoom: Int) {
        radius *= zoom
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
        return "${figureName}: (x = $x, y = $y), radius = $radius, area = $area"
    }
}