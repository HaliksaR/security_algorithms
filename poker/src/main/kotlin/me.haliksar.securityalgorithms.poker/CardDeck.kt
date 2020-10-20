package me.haliksar.securityalgorithms.poker

import me.haliksar.securityalgorithms.libs.core.prime.mutuallyPrime

class CardDeck(p: Long) {
    private val suits: List<String> = listOf("♠", "♣", "♥", "♦")

    private val typeCart: List<String> = listOf("2", "3", "4", "5", "6", "7", "8", "9", "10", "В", "Д", "К", "T")

    data class Card(val s: String, val t: String)

    private var cards: MutableList<Card> = mutableListOf<Card>().apply {
        suits.forEach { suit -> addAll(typeCart.map { type -> Card(suit, type) }) }
    }

    fun count(): Int = cards.size

    operator fun invoke() = cards

    override fun toString(): String = cards.joinToString("\n")

    private val cardsWithId: MutableMap<Long, Card> = mutableMapOf<Long, Card>().apply {
        do {
            putAll(cards.map { name -> Long.mutuallyPrime(p - 1L) to name })
        } while (keys.size != keys.distinct().size)
    }

    fun getCardsWithId(): Map<Long, Card> = cardsWithId.toMap()
}

fun CardDeck.Card.printCart() {
    if (t.length == 1) {
        println(
            """
            ┏──────────┒
            │ $t        │
            │ $s  ╳╳    │
            │    ╳╳  $s │
            │        $t │
            ┗──────────┛
        """.trimIndent()
        )
    } else {
        println(
            """
            ┏──────────┒
            │ $t       │
            │ $s  ╳╳    │
            │    ╳╳  $s │
            │       $t │
            ┗──────────┛
        """.trimIndent()
        )
    }
}

fun printCartBack() {
    println(
        """
            ┏──────────┒
            │ ╳╳╳╳╳╳╳╳ │
            │ ╳╳╳╳╳╳╳╳ │
            │ ╳╳╳╳╳╳╳╳ │
            │ ╳╳╳╳╳╳╳╳ │
            ┗──────────┛
        """.trimIndent()
    )
}