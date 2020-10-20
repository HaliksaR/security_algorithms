package me.haliksar.securityalgorithms.poker

import me.haliksar.securityalgorithms.libs.core.prime.mutuallyPrime
import me.haliksar.securityalgorithms.libs.gcd.long.gcdTailRec
import me.haliksar.securityalgorithms.libs.modexp.long.modExpRec

class Player(private val p: Long) {

    private var ca: Long = Long.mutuallyPrime(p - 1L)
    private var da: Long = (p - 1L + ca.gcdTailRec(p - 1L).x) % (p - 1L)

    private val cardsInHandsEncrypt = mutableSetOf<Long>()
    fun getCardsInHands() = cardsInHandsEncrypt.toSet()

    private val cardDeckInHands = mutableSetOf<Long>()
    fun getCardDeckInHands() = cardDeckInHands.toSet()

    init {
        check((ca * da) % (p - 1L) == 1L) { "Нарушено условие: (ca * da) % (p - 1) == 1" }
    }

    fun encryptDeck(cardsId: Set<Long>): Set<Long> =
        cardsId.map { it.modExpRec(ca, p) }.shuffled().toSet()

    fun getEncryptCart(cardsId: Set<Long>, countCart: Int): Set<Long> =
        cardsId.shuffled().toMutableSet().apply {
            cardsInHandsEncrypt.addAll(take(countCart))
            removeAll(cardsInHandsEncrypt)
        }

    fun decryptDeck(cardsId: Set<Long>): Set<Long> =
        cardsId.map { it.modExpRec(da, p) }.toSet()

    fun takeDesk(userDecryptCardDeck: Set<Long>) {
        val decryptDesk = decryptDeck(userDecryptCardDeck)
        cardDeckInHands.addAll(decryptDesk)
    }

    fun showDeckInHand(cardsWithId: Map<Long, CardDeck.Card>) =
        cardDeckInHands.forEach {
            cardsWithId[it]?.printCart()
        }

    fun showDeckInHandBack() = repeat(cardsInHandsEncrypt.size) { printCartBack() }
}
