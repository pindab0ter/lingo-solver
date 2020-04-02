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
            solve(".a..........", "clebsnnhani".toList())
        }.let { (candidates, duration) ->
            println("Solving took $duration")
            println("The candidates are:")
            candidates.forEach { candidate -> println("\t$candidate") }
        }
    }

    private fun solve(word: String, letters: List<Char>): List<String> {
        val knownLetters = word.filter { c -> c.isLetter() }.toList()
        val regex = Regex(word)

        return wordList.useLines { words ->
            words.filter { line -> line.all { it.isLowerCase() } }
                .filter { line -> regex.matches(line) }
                .filter { line ->
                    line.toList().intersect(letters).map { c ->
                        min(line.toList().count { it == c }, letters.count { it == c })
                    }.sum() == line.length - knownLetters.size
                }
                .toList()
        }
    }
}