import java.io.File

class GamePrototypeParser(
    private val gamePrototypeFile: File
) {

    data class GamePrototype(val title: String, val description: String, val rules: List<String>)

    fun parse(): GamePrototype {
        val lines = gamePrototypeFile.readLines()
        var title = ""
        var description = ""
        val rules = mutableListOf<String>()

        var currentState = "title"
        for (line in lines) {
            when (currentState) {
                "title" -> {
                    if (line.startsWith("---")) {
                        currentState = "description"
                    } else {
                        title = line
                    }
                }
                "description" -> {
                    if (line.startsWith("== ")) {
                        currentState = "rules"
                    } else {
                        description += line + "\n"
                    }
                }
                "rules" -> {
                    if (line.isNotBlank()) {
                        rules.add(line)
                    }
                }
            }
        }

        return GamePrototype(title, description.trim(), rules)
    }
}

fun main() {
    val gamePrototypeFile = File("game_prototype.txt")
    val parser = GamePrototypeParser(gamePrototypeFile)
    val gamePrototype = parser.parse()

    println("Title: ${gamePrototype.title}")
    println("Description: ${gamePrototype.description}")
    println("Rules:")
    gamePrototype.rules.forEachIndexed { index, rule ->
        println("${index + 1}. $rule")
    }
}