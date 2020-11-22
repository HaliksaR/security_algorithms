package me.haliksar.securityalgorithms.libs.ciphers.encrypt

import me.haliksar.securityalgorithms.libs.ciphers.contract.Encrypt
import kotlin.random.Random

// https://thecode.media/vernam/
class VernamCipher : Encrypt<Long, VernamCipher.DataRet, Unit> {

    data class DataRet(val text: Long, val key: Long)

    override fun encrypt(message: Long, keys: Unit): DataRet {
        val keys = Random.nextLong(Long.MIN_VALUE, message - 1L)
        return DataRet(keys.xor(message), keys)
    }

    override fun decrypt(encryptData: DataRet, keys: Unit): Long =
        encryptData.text.xor(encryptData.key)


    override fun generate() {
        // ключ генерируется по сообщению
    }

    override fun validate(keys: Unit) {
        // ключ генерируется по сообщению
    }
}