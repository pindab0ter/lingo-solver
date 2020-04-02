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
            solve("cAlebsnnhani")
        }.let { (candidates, duration) ->
            println("Solving took $duration")
            println("The candidates are:")
            candidates.forEach { candidate -> println("\t$candidate") }
        }
    }

    private fun solve(word: String): List<String> {
        val knownLetters = word.filter { c -> c.isUpperCase() }.toList()
        val unknownLetters = word.filter { c -> c.isLowerCase() }.toList()
        val pattern = Regex(word.replace(Regex("""[a-z]"""), ".").toLowerCase())

        return wordList.useLines { words ->
            words.filter { line -> line.all { it.isLowerCase() } }
                .filter { line -> pattern.matches(line) }
                .filter { line ->
                    line.toList().intersect(unknownLetters).map { c ->
                        min(line.toList().count { it == c }, unknownLetters.count { it == c })
                    }.sum() == line.length - knownLetters.size
                }
                .toList()
        }
    }
}