package me.haliksar.securityalgorithms.libs.gcd.long

data class GcdPack
@ExperimentalUnsignedTypes
constructor(var nod: Long, var x: Long, var y: Long)

/**
 * Функция, реализующая обобщённый алгоритм Евклида.
 *
 * @receiver [Long] первое число
 *
 * @param b [Long] второе число
 *
 * @return [GcdPack] функция возвращает группу где первый элемент это НОД,
 * втророй и третий - две неизвестных функции ax + by
 */
@ExperimentalUnsignedTypes
infix fun Long.gcd(b: Long): GcdPack {
    var packU = GcdPack(this, 1L, 0L)
    var packV = GcdPack(b, 0L, 1L)
    while (packV.nod != 0L) {
        val q = packU.nod / packV.nod
        val packT = GcdPack(
            packU.nod % packV.nod,
            packU.x - q * packV.x,
            packU.y - q * packV.y
        )
        packU = packV
        packV = packT
    }
    check(this * packU.x + b * packU.y == packU.nod) { "Ошибка при вычислении НОД" }
    return packU
}

/**
 * Функция, реализующая обобщённый алгоритм Евклида через хвостовую рекурсию.
 * https://ru.wikipedia.org/wiki/%D0%A5%D0%B2%D0%BE%D1%81%D1%82%D0%BE%D0%B2%D0%B0%D1%8F_%D1%80%D0%B5%D0%BA%D1%83%D1%80%D1%81%D0%B8%D1%8F
 *
 * @receiver [Long] первое число
 *
 * @param b [Long] второе число
 *
 * @return [Triple] функция возвращает группу где первый элемент это НОД,
 * втророй и третий - две неизвестных функции ax + by
 */
@ExperimentalUnsignedTypes
infix fun Long.gcdTailRec(b: Long): GcdPack = this.goTailRec(b, 0L, 1L, 1L, 0L)

@ExperimentalUnsignedTypes
private tailrec fun Long.goTailRec(
    b: Long,
    x: Long,
    y: Long,
    prevX: Long,
    prevY: Long
): GcdPack =
    if (b == 0L) {
        GcdPack(this, prevX, prevY)
    } else {
        val q = this / b
        b.goTailRec(this % b, prevX - q * x, prevY - q * y, x, y)
    }


@ExperimentalUnsignedTypes
fun main() {
    println(22L gcd 44L)
    println(22L gcdTailRec 44L)
}