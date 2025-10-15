# Вывод при запуске ```Main.kt```
```
Before zoom:
	Rect: (x = 1, y = 1), width = 2, height = 1, area = 2.0
After x2 zoom:
	Rect: (x = 1, y = 1), width = 4, height = 2, area = 8.0
Before rotation:
	Rect: (x = 1, y = 1), width = 4, height = 2, area = 8.0
After 90 deg clockwise rotation:
	Rect: (x = 1, y = -1), width = 2, height = 4, area = 8.0

Before zoom:
	Square: (x = 1, y = 1), width = 2, area = 4.0
After x2 zoom:
	Square: (x = 1, y = 1), width = 4, area = 16.0
Before rotation:
	Square: (x = 1, y = 1), width = 4, area = 16.0
After 90 deg clockwise rotation:
	Square: (x = 1, y = -1), width = 4, area = 16.0

Before zoom:
	Circle: (x = 1, y = 1), radius = 2, area = 12.566371
After x2 zoom:
	Circle: (x = 1, y = 1), radius = 4, area = 50.265484
Before rotation:
	Circle: (x = 1, y = 1), radius = 4, area = 50.265484
After 90 deg clockwise rotation:
	Circle: (x = 1, y = -1), radius = 4, area = 50.265484
```
# Немного о абстрактном классе ```Figure```
```kotlin
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
```
На вход своего конструктора принимает координаты центра фигуры ```x``` и ```y```.

Поскольку каждая фигура обязана иметь интерфейсы ```Movable``` и ```Transforming```, я решил добавить их сразу в асбтрактный класс, а также реализовать явно
функцию ```move``` интерфейса ```Movable```, т.к. перемещение центра у всех фигур одинаковое.

Также я решил сделать ```area``` свойством, переопределяемым через ```get()``` у наследуемых классов, чтобы можно было не использовать круглые скобки при вызове.

Для удобства вывода при демонстрации работы было добавлено свойство ```figureName``` и переопределена функция перевода к строке.
