enum class Direction {
    UP, DOWN, LEFT, RIGHT
}

fun moveRobot(r: Robot, toX: Int, toY: Int) {
    if (toX > r.x) {
        while (r.direction != Direction.RIGHT) {
            r.turnRight()
        }
    }
    else if (toX < r.x) {
        while (r.direction != Direction.LEFT) {
            r.turnLeft()
        }
    }
    while (r.x != toX) r.stepForward()

    if (toY > r.y) {
        while (r.direction != Direction.UP) {
            r.turnRight()
        }
    }
    else if (toY < r.y) {
        while (r.direction != Direction.DOWN) {
            r.turnLeft()
        }
    }
    while (r.y != toY) r.stepForward()
}

fun main() {
    val r = Robot(0,1,Direction.UP)
    moveRobot(r, 3, 7)
    println("${r.x} ${r.y}")
}