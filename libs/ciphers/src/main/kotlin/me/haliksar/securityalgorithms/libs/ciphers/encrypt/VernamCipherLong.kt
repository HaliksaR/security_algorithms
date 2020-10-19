package me.haliksar.securityalgorithms.libs.ciphers.encrypt

import me.haliksar.securityalgorithms.libs.ciphers.contract.Encrypt
import kotlin.properties.Delegates
import kotlin.random.Random

class VernamCipherLong : Encrypt<Long, VernamCipherLong.DataRet, Long> {

    data class DataRet(val text: Long, val key: Long)

    override var keys: Long? = null
    override var keysData: Long by Delegates.notNull()
    private var message: Long by Delegates.notNull()

    override fun encrypt(message: Long): DataRet {
        this.message = message
        keysData = Random.nextLong(Long.MIN_VALUE, this.message - 1L)
        keys = keysData
        val ciphertext = keysData.xor(this.message)
        return DataRet(ciphertext, keysData)
    }

    override fun decrypt(encryptData: DataRet): Long =
        encryptData.text.xor(encryptData.key)


    override fun generate() {
        // ключ генерируется по сообщению
    }

    override fun validate() {
        check(message in 0..keysData) { "Ключ и сообщение должны быть одинакого размера" }
    }
}