package me.haliksar.securityalgorithms.poker.recipient

import me.haliksar.securityalgorithms.libs.core.prime.mutuallyPrime
import me.haliksar.securityalgorithms.libs.gcd.gcdTailRec
import me.haliksar.securityalgorithms.libs.modexp.modExpRec
import me.haliksar.securityalgorithms.poker.card.Card
import me.haliksar.securityalgorithms.poker.card.CardDeck
import me.haliksar.securityalgorithms.poker.card.CardDisplay

abstract class Recipient(private val p: Long) :
    CardDisplay {

    abstract val name: String
    abstract val count: Int

    private var ca: Long = Long.mutuallyPrime(p - 1L)
    private var da: Long = (p - 1L + ca.gcdTailRec(p - 1L).x) % (p - 1L)

    private val cardsEncrypt = mutableSetOf<Long>()
    private val cards = mutableSetOf<Long>()

    init {
        check((ca * da) % (p - 1L) == 1L) { "Нарушено условие: (ca * da) % (p - 1) == 1" }
    }

    fun encrypt(cardsId: Set<Long>): Set<Long> =
        cardsId.map { it.modExpRec(ca, p) }.toSet()

    fun getEncryptCarts(cardsId: Set<Long>): Set<Long> {
        cardsEncrypt.addAll(cardsId.take(count))
        cardsId.toMutableSet().removeAll(cardsEncrypt)
        return cardsId
    }

    private fun decryptCarts(cardsId: Set<Long>): Set<Long> =
        cardsId.map { it.modExpRec(da, p) }.toSet()

    fun decrypt(other: Set<Recipient>) {
        val othersRecipients = other.filter { other -> other != this }
        val userDecryptCardDeck = othersRecipients.fold(cardsEncrypt.toSet()) { acc, recipient ->
            recipient.decryptCarts(acc)
        }
        cards.addAll(decryptCarts(userDecryptCardDeck))
    }

    override fun showCards(cardDeck: CardDeck) {
        println(name)
        val cardsIds = cardDeck.cardsWithId
        cards.forEach { cardsIds[it]?.printFront() }
    }

    override fun showCardsBack() {
        println(name)
        repeat(cardsEncrypt.size) { Card.printBack() }
    }
}
