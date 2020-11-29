package me.haliksar.securityalgorithms.poker.card

interface CardDisplay {

    fun showCards(cardDeck: CardDeck)

    fun showCardsBack()
}