package me.haliksar.securityalgorithms.poker

class Table(
    p: Long,
    override val name: String = "Стол",
    override val count: Int = COUNT_CART_ON_TABLE
) : Recipient(p) {

    private companion object {

        const val COUNT_CART_ON_TABLE = 5
    }
}