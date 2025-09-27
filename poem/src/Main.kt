import java.io.FileInputStream
import java.util.*

fun main() {
    val scRoles = Scanner(FileInputStream("roles.txt"))

    val roles = arrayListOf<String>()
    while (scRoles.hasNextLine())
        roles.add(scRoles.nextLine())

    println(roles)

    val scLines = Scanner(FileInputStream("lines.txt"))

    val lines = arrayListOf<String>()
    while (scLines.hasNextLine())
        lines.add(scLines.nextLine())

    println(lines)

    val roleLines = mutableMapOf<String, MutableSet<String>>()
    var lineNum = 1
    for (line in lines) {
        val roleEnd = line.indexOf(':')
        val role = line.substring(0, roleEnd)
        val line = "${lineNum++}) ${line.substring(roleEnd+2)}"
        if (role in roleLines) roleLines[role]?.add(line)
        else roleLines[role] = mutableSetOf<String>(line)

    }
    for (role in roleLines.keys) {
        println("$role:")
        println(roleLines[role]?.joinToString("\n"))
        println()
    }
}