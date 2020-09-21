package me.haliksar.securityalgorithms.modexp

/**
 * Функция быстрого возведения числа в степень по модулю рекурсивно.
 * https://prog-cpp.ru/pow-mod/
 * @receiver [ULong] число которое нужно возвести в степень
 * @param pow [ULong] степень в которое нужно возвести число
 * @param mod [ULong] модуль возведения в степень
 * @return [this]^[pow] (mod [mod])
 **/
@ExperimentalUnsignedTypes
fun ULong.modExpRec(pow: ULong, mod: ULong): ULong {
    if (pow == 0uL) return 1uL
    val result = (this * this % mod).modExpRec(pow / 2u, mod)
    return if (pow % 2u == 1uL) result * this % mod else result
}

/**
 * Функция быстрого возведения числа в степень по модулю.
 *
 * @receiver [ULong] число которое нужно возвести в степень
 * @param pow [ULong] степень в которое нужно возвести число
 * @param mod [ULong] модуль возведения в степень
 * @return [this]^[pow] (mod [mod])
 **/
@ExperimentalUnsignedTypes
fun ULong.modExp(pow: ULong, mod: ULong): ULong {
    var a: ULong = this
    var b: ULong = pow
    var res = 1uL
    a = a % mod
    while (b > 0uL) {
        if (b and 1uL == 1uL)
            res = res * a % mod
        b = b shr 1
        a = a * a % mod
    }
    return res
}

/**
 * Функция быстрого возведения числа в степень c множетелем по модулю рекурсивно.
 * https://prog-cpp.ru/pow-mod/
 * @receiver [ULong] число которое нужно возвести в степень
 * @param pow [ULong] степень в которое нужно возвести число
 * @param mod [ULong] модуль возведения в степень
 * @param factor [ULong] множитель
 * @return [this]^[pow] (mod [mod])
 **/
@ExperimentalUnsignedTypes
fun ULong.modExpRec(pow: ULong, mod: ULong, factor: ULong): ULong = (this.modExpRec(pow, mod) * factor % mod) % mod

/**
 * Функция быстрого возведения числа в степень c множетелем по модулю.
 *
 * @receiver [ULong] число которое нужно возвести в степень
 * @param pow [ULong] степень в которое нужно возвести число
 * @param mod [ULong] модуль возведения в степень
 * @param factor [ULong] множитель
 * @return [this]^[pow] (mod [mod])
 **/
@ExperimentalUnsignedTypes
fun ULong.modExp(pow: ULong, mod: ULong, factor: ULong): ULong = (this.modExp(pow, mod) * factor % mod) % mod

@ExperimentalUnsignedTypes
fun main() {
    println(10uL.modExpRec(15uL, 7uL))
    println(10uL.modExp(15uL, 7uL))
    println(10uL.modExp(15uL, 7uL, 3uL))
    println(10uL.modExpRec(15uL, 7uL, 3uL))
}