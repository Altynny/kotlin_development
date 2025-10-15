abstract class Figure(var x: Int, var y: Int) : Movable, Transforming {
    abstract val figureName: String
    abstract val area: Float
    override fun move(dx: Int, dy: Int) {
        x += dx; y += dy
    }
    abstract override fun resize(zoom: Int)
    abstract override fun rotate(direction: RotateDirection, centerX: Int, centerY: Int)
    abstract override fun toString(): String
}