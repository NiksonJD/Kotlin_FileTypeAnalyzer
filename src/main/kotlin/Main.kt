package analyzer

import java.io.File
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

fun rabinKarpSearch(text: String, pattern: MutableMap<String, String>): String {
    for ((key, value) in pattern.entries.reversed()) {
        val patternHash = key.hashCode()
        for (i in 0..text.length - key.length) {
            val substring = text.substring(i, i + key.length)
            if (substring.hashCode() == patternHash && substring == key) return value
        }
    }
    return "Unknown file type"
}

fun kmpSearch(text: String, pattern: MutableMap<String, String>): String {
    for ((key, value) in pattern.entries.reversed()) {
        val pi = IntArray(key.length) { 0 };
        var a = 0
        key.forEachIndexed { i, ch ->
            if (i > 0 && key[pi[i - 1]] == ch) {
                pi[i] = pi[i - 1] + 1
            }
        }
        text.forEachIndexed { i, ch ->
            while (a > 0 && key[a] != ch) a = pi[a - 1]
            if (key[a] == ch && ++a == key.length) return value
        }
    }
    return "Unknown file type"
}

fun naiveSearch(text: String, pattern: MutableMap<String, String>): String {
    for ((key, value) in pattern.entries.reversed()) {
        if (text.contains(key)) return value
    }
    return "Unknown file type"
}

fun main(s: Array<String>) {
    val files = File(s[0]).walkTopDown().toList()
    val mm = mutableMapOf<String, String>()
    File(s[1]).useLines { l -> l.forEach { line -> val el = line.split(";"); mm[el[1].trim('"')] = el[2].trim('"') } }
    val executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())
    files.forEach { if (it.exists()) executor.execute { println("${it.name}: " + rabinKarpSearch(it.readText(), mm)) } }
    executor.shutdown().also { executor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS) }
}