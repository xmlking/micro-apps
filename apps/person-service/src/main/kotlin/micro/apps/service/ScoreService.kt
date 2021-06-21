package micro.apps.service

import java.io.IOException
import java.util.Arrays
import java.util.Stack
import javax.annotation.PostConstruct
import javax.enterprise.context.ApplicationScoped
import javax.json.bind.JsonbBuilder
import javax.json.bind.JsonbConfig
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

@ApplicationScoped
class ScoreService {
    private val scoreDatabase: MutableMap<String, List<Score>> = HashMap()
    fun getScores(idNumber: String): List<Score> {
        System.err.println("======= Getting scores [$idNumber] =======")
        return scoreDatabase[idNumber]!!
    }

    fun getScores(idNumbers: List<String>): List<List<Score>> {
        System.err.println("======= Getting scores $idNumbers =======")
        val allscores: MutableList<List<Score>> = ArrayList()
        for (idNumber in idNumbers) {
            allscores.add(scoreDatabase[idNumber]!!)
        }
        return allscores
    }

    @PostConstruct
    fun init() {
        try {
            Thread.currentThread().contextClassLoader.getResourceAsStream("score.json").use { jsonStream ->
                if (jsonStream != null) {
                    val loaded = JSONB.fromJson<List<List<Score>>>(
                        jsonStream,
                        object : ArrayList<List<Score?>?>() {}.javaClass.genericSuperclass
                    )
                    for (s in loaded) {
                        scoreDatabase[ids.pop()] = s
                    }
                }
            }
        } catch (ex: IOException) {
            ex.printStackTrace()
        }
    }

    companion object {
        private val JSONB = JsonbBuilder.create(JsonbConfig().withFormatting(true))
        private val ids = Stack<String>()

        init {
            ids.addAll(
                Arrays.asList(
                    *arrayOf(
                        "797-95-4822",
                        "373-95-3047",
                        "097-87-6795",
                        "347-01-8880",
                        "733-86-4423",
                        "560-99-2165",
                        "091-07-5401",
                        "539-70-2014",
                        "029-18-5986",
                        "287-58-0690"
                    )
                )
            )
        }
    }
}
