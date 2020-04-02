package nl.pindab0ter.lingosolver

import java.nio.file.Paths
import kotlin.math.min
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

object LingoSolver {

    private val wordList = Paths.get("src", "main", "resources", "wordlist.txt").toFile().bufferedReader()

    @ExperimentalTime
    @JvmStatic
    fun main(args: Array<String>) {
        measureTimedValue {
            solve("L.N.EN", "uidawiampto")
        }.let { (candidates, duration) ->
            println("Solving took $duration")
            println("The candidates are:")
            candidates.forEach { candidate -> println("\t$candidate") }
        }
    }

    private fun solve(word: String, blacklist: String = ""): List<String> {
        val placedLetters = word.filter { c -> c.isUpperCase() }.toLowerCase().toList()
        val unplacedLetters = word.filter { c -> c.isLowerCase() }.toList()
        val pattern = Regex(word.replace(Regex("""[a-z]"""), ".").toLowerCase())

        return wordList.useLines { words ->
            words.filter { word -> word.all { it.isLowerCase() } }
                .map { word -> word.replace("ij", "y") }
                .filter { word -> pattern.matches(word) }
                .filter { word -> blacklist.none { c -> word.filter { d -> d !in placedLetters }.toList().contains(c) } }
                .filter { word ->
                    word.toList().intersect(unplacedLetters).map { c ->
                        min(word.toList().count { it == c }, unplacedLetters.count { it == c })
                    }.sum() == unplacedLetters.count()
                }
                .toList()
        }
    }
}