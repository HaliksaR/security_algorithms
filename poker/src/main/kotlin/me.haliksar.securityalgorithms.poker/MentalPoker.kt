package me.haliksar.securityalgorithms.poker

import me.haliksar.securityalgorithms.libs.core.prime.randomPrimeNumber

// https://ru.wikipedia.org/wiki/%D0%9C%D0%B5%D0%BD%D1%82%D0%B0%D0%BB%D1%8C%D0%BD%D1%8B%D0%B9_%D0%BF%D0%BE%D0%BA%D0%B5%D1%80
class MentalPoker(countPlayers: Int) {
    private companion object {
        const val COUNT_CART_ON_TABLE = 5
        const val COUNT_CART_TO_PLAYER = 2
    }

    init {
        check(countPlayers in 2..23) { "Игроков должно быть от 2 до 23" }
    }

    private val p = Long.randomPrimeNumber

    private val cardDeck by lazy { CardDeck(p) }

    private var encryptCardDeck = setOf<Long>()

    private val cardOnTable = mutableSetOf<Long>()

    private val playerList = mutableListOf<Player>().apply {
        repeat(countPlayers) { add(Player(p)) }
    }

    private fun playersShuffled() {
        encryptCardDeck = playerList.fold(cardDeck.getCardsWithId().keys) { acc, player ->
            player.encryptDeck(acc)
        }
    }

    private fun playersGetCarts() {
        encryptCardDeck = playerList.fold(encryptCardDeck) { acc, player ->
            player.getEncryptCart(acc, COUNT_CART_TO_PLAYER)
        }
    }

    private fun putCardOnTable() { // TODO FIX надо обойти шифрование, либо шифровать вместе с игроками
        encryptCardDeck = encryptCardDeck.shuffled().toMutableSet().apply {
            cardOnTable.addAll(take(COUNT_CART_ON_TABLE))
            removeAll(cardOnTable)
        }
    }

    private fun decryptPlayersCarts() {
        playerList.forEach { player ->
            val userEncryptCardDeck = player.getCardsInHands()
            val othersPlayers = playerList.filter { other -> other != player }
            val userDecryptCardDeck = othersPlayers.fold(userEncryptCardDeck) { acc, player ->
                player.decryptDeck(acc)
            }
            player.takeDesk(userDecryptCardDeck)
        }
    }

    private fun showPlayersCarts() {
        playerList.forEachIndexed { index, player ->
            println("Игрок №${index + 1}:")
            player.showDeckInHand(cardDeck.getCardsWithId())
            println()
        }
    }

    private fun showPlayersCartsBack() {
        playerList.forEachIndexed { index, player ->
            println("Игрок №${index + 1}:")
            player.showDeckInHandBack()
            println()
        }
    }

    private fun showOnTableCarts(cardsWithId: Map<Long, CardDeck.Card>) {
        println("Карты на столе:")
        cardOnTable.forEach {
            cardsWithId[it]?.printCart()
        }
    }

    fun handOutCards() {
        playersShuffled()
        playersGetCarts()
        showPlayersCartsBack()
        putCardOnTable()
        decryptPlayersCarts()
        showPlayersCarts()
        showOnTableCarts(cardDeck.getCardsWithId())
    }
}

fun main() {
    MentalPoker(12).handOutCards()
}