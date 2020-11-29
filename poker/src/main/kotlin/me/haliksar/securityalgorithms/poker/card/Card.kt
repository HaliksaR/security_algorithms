package me.haliksar.securityalgorithms.poker.card

data class Card(
    private val s: String,
    private val t: String
) {

    companion object {

        fun printBack() {
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
    }

    fun printFront() {
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
}