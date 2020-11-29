package me.haliksar.securityalgorithms.poker

import me.haliksar.securityalgorithms.libs.core.prime.randomPrimeNumber
import me.haliksar.securityalgorithms.poker.card.CardDeck
import me.haliksar.securityalgorithms.poker.recipient.Player
import me.haliksar.securityalgorithms.poker.recipient.Recipient
import me.haliksar.securityalgorithms.poker.recipient.Table

// https://ru.wikipedia.org/wiki/%D0%9C%D0%B5%D0%BD%D1%82%D0%B0%D0%BB%D1%8C%D0%BD%D1%8B%D0%B9_%D0%BF%D0%BE%D0%BA%D0%B5%D1%80
class MentalPoker(countPlayers: Int) {

    private val p = Long.randomPrimeNumber

    private val cardDeck by lazy { CardDeck(p) }

    private var encryptCardDeck = setOf<Long>()

    private val recipients: MutableSet<Recipient>

    private fun action(action: (Recipient) -> Unit) = recipients.forEach { action(it) }

    private fun fold(initial: Set<Long>, action: (Set<Long>, Recipient) -> Set<Long>) {
        var accumulator = initial
        for (element in recipients) {
            accumulator = action(accumulator, element)
        }
        encryptCardDeck = accumulator
    }

    init {
        check(countPlayers in 2..23) { "Игроков должно быть от 2 до 23" }
        recipients = mutableSetOf<Recipient>().apply {
            for (number in 0 until countPlayers) {
                add(Player(p, number + 1))
            }
            add(Table(p))
        }
    }

    private fun recipientsShuffled() {
        fold(cardDeck.cardsWithId.keys) { acc, recipient ->
            recipient.encrypt(acc.shuffled().toSet())
        }
    }

    private fun recipientsGetCarts() {
        fold(encryptCardDeck) { acc, recipient ->
            recipient.getEncryptCarts(acc)
        }
    }

    private fun decryptRecipientsCarts() {
        action { recipient ->
            recipient.decrypt(recipients)
        }
    }

    private fun showRecipientsCarts() {
        action { recipient ->
            recipient.showCards(cardDeck)
        }
    }

    private fun showRecipientsCartsBack() {
        action { recipient ->
            recipient.showCardsBack()
        }
    }

    fun launch() {
        recipientsShuffled()
        recipientsGetCarts()
        showRecipientsCartsBack()
        decryptRecipientsCarts()
        showRecipientsCarts()
    }
}

fun main() {
    MentalPoker(2).launch()
}