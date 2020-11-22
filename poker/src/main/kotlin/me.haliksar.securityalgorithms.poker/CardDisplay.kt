package me.haliksar.securityalgorithms.poker

interface CardDisplay {

    fun showCards(cardDeck: CardDeck)

    fun showCardsBack()
}