package me.haliksar.securityalgorithms.graph.domain.entity

import me.haliksar.securityalgorithms.graph.data.generator.Rsa
import me.haliksar.securityalgorithms.libs.core.prime.randomNumber
import me.haliksar.securityalgorithms.libs.modexp.modExp

data class Node(
    val id: Int,
    var color: Color,
) {

    private val keys: Rsa = Rsa.generate()
    fun getD() = keys.publicKey.first
    fun getN() = keys.publicKey.second
    fun getC() = keys.privateKey.first

    private var r: Long = 0
    fun getR() = r

    private var z: Long = 0
    fun getZ() = z


    fun generate() {
        color = Color.values().random()
        calculateR()
        calculateZ()
    }

    private fun calculateZ() {
        z = r.modExp(pow = getD(), mod = getN())
    }

    private fun calculateR() {
        r = Long.randomNumber
        when (color) {
            Color.red -> {
                r = r and 1.inv()
                r = r and 2.inv()
            }
            Color.green -> {
                r = r or 1
                r = r and 2.inv()
            }
            Color.blue -> {
                r = r and 1.inv()
                r = r or 2
            }
        }
    }
}