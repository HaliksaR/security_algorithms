package me.haliksar.securityalgorithms.poker.recipient

class Player(
    p: Long,
    number: Int,
    override val name: String = "Игрок N$number",
    override val count: Int = COUNT_CART_TO_PLAYER
) : Recipient(p) {

    private companion object {

        const val COUNT_CART_TO_PLAYER = 2
    }
}