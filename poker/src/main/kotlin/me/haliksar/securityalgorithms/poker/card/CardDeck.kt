package me.haliksar.securityalgorithms.poker.card

import me.haliksar.securityalgorithms.libs.core.prime.mutuallyPrime

class CardDeck(p: Long) {

    private companion object {

        val SUITS = listOf("♠", "♣", "♥", "♦")
        val TYPES = listOf("2", "3", "4", "5", "6", "7", "8", "9", "10", "В", "Д", "К", "T")
        val CARDS = mutableListOf<Card>().apply {
            SUITS.forEach { suit ->
                addAll(
                    TYPES.map { type ->
                        Card(
                            suit,
                            type
                        )
                    })
            }
        }
    }

    var cardsWithId: Map<Long, Card>
        private set

    init {
        cardsWithId = CARDS.map { name -> Long.mutuallyPrime(p - 1L) to name }.toMap()
    }

    operator fun invoke() = CARDS

    override fun toString(): String = CARDS.joinToString("\n")
}