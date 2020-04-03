package nl.pindab0ter.lingosolver

import java.nio.file.Paths
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

object LingoSolver {

    private val wordList = Paths.get("src", "main", "resources", "wordlist.txt").toFile().bufferedReader()

    @ExperimentalTime
    @JvmStatic
    fun main(args: Array<String>) {
        measureTimedValue {
            solve("oo.RD", "ate")
        }.let { (candidates, duration) ->
            println("Solving took $duration")
            println("The candidates are:")
            candidates.forEach { candidate -> println("\t$candidate") }
        }
    }

    fun solve(word: String, blacklist: String = ""): List<String> {
        val placedLetters = word.filter { c -> c.isUpperCase() }.toLowerCase().toList()
        val unplacedLetters = word.filter { c -> c.isLowerCase() }.toList()
        val pattern = Regex(word.replace(Regex("""[a-z]"""), ".").toLowerCase())

        return wordList.useLines { words ->
            words
                // Lingo uses one character for the Dutch "ij"
                .map { word -> word.replace("ij", "y") }
                // Filter out words that don't have the known letters in the right place
                .filter { word -> pattern.matches(word) }
                // Filter out words where non-known letters contain blacklisted letters
                .filter { word ->
                    word.filter { wordLetter -> wordLetter !in placedLetters }
                        .none { unplacedWordLetter -> unplacedWordLetter in blacklist }
                }
                // Filter out words that don't have the right amount of unplaced letters
                .filter { word ->
                    word.toList()
                        .intersect(unplacedLetters)
                        .map { letter ->
                            minOf(
                                word.count { wordLetter -> wordLetter == letter },
                                unplacedLetters.count { unplacedLetter -> unplacedLetter == letter }
                            )
                        }.sum() == unplacedLetters.count()
                }
                .toList()
        }
    }
}