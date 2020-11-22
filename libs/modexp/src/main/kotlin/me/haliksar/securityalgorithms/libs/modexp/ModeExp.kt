package me.haliksar.securityalgorithms.libs.modexp

/**
 * Функция быстрого возведения числа в степень по модулю рекурсивно.
 * https://prog-cpp.ru/pow-mod/
 * @receiver [Long] число которое нужно возвести в степень
 * @param pow [Long] степень в которое нужно возвести число
 * @param mod [Long] модуль возведения в степень
 * @return [this]^[pow] (mod [mod])
 **/
fun Long.modExpRec(pow: Long, mod: Long): Long {
    if (pow == 0L) return 1L
    val result = (this * this % mod).modExpRec(pow / 2, mod)
    return if (pow % 2 == 1L) result * this % mod else result
}

/**
 * Функция быстрого возведения числа в степень по модулю.
 *
 * @receiver [Long] число которое нужно возвести в степень
 * @param pow [Long] степень в которое нужно возвести число
 * @param mod [Long] модуль возведения в степень
 * @return [this]^[pow] (mod [mod])
 **/
fun Long.modExp(pow: Long, mod: Long): Long {
    var a: Long = this
    var b: Long = pow
    var res = 1L
    a %= mod
    while (b > 0L) {
        if (b and 1L == 1L)
            res = res * a % mod
        b = b shr 1
        a = a * a % mod
    }
    return res
}

/**
 * Функция быстрого возведения числа в степень c множетелем по модулю рекурсивно.
 * https://prog-cpp.ru/pow-mod/
 * @receiver [Long] число которое нужно возвести в степень
 * @param pow [Long] степень в которое нужно возвести число
 * @param mod [Long] модуль возведения в степень
 * @param factor [Long] множитель
 * @return [this]^[pow] (mod [mod])
 **/
fun Long.modExpRec(pow: Long, mod: Long, factor: Long): Long = (this.modExpRec(pow, mod) * factor % mod) % mod

/**
 * Функция быстрого возведения числа в степень c множетелем по модулю.
 *
 * @receiver [Long] число которое нужно возвести в степень
 * @param pow [Long] степень в которое нужно возвести число
 * @param mod [Long] модуль возведения в степень
 * @param factor [Long] множитель
 * @return [this]^[pow] (mod [mod])
 **/
fun Long.modExp(pow: Long, mod: Long, factor: Long): Long = (this.modExp(pow, mod) * factor % mod) % mod

fun main() {
    println(10L.modExpRec(15L, 7L))
    println(10L.modExp(15L, 7L))
    println(10L.modExp(15L, 7L, 3L))
    println(10L.modExpRec(15L, 7L, 3L))
}