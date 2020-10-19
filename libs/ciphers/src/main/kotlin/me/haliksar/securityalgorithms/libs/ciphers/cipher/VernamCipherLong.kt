package me.haliksar.securityalgorithms.libs.ciphers.cipher

import me.haliksar.securityalgorithms.libs.ciphers.contract.Encrypt
import kotlin.properties.Delegates
import kotlin.random.Random

class VernamCipherLong : Encrypt<Long, VernamCipherLong.DataRet, Long> {

    data class DataRet(val text: Long, val key: Long)

    override var keys: Long? = null
    private var dataKey: Long by Delegates.notNull()
    private var message: Long by Delegates.notNull()

    override fun encrypt(message: Long): DataRet {
        this.message = message
        dataKey = Random.nextLong(Long.MIN_VALUE, this.message - 1L)
        keys = dataKey
        val ciphertext = dataKey.xor(this.message)
        return DataRet(ciphertext, dataKey)
    }

    override fun decrypt(encryptData: DataRet): Long =
        encryptData.text.xor(encryptData.key)


    override fun generate() {
        // ключ генерируется по сообщению
    }

    override fun validate() {
        check(message in 0..dataKey) { "Ключ и сообщение должны быть одинакого размера" }
    }

}