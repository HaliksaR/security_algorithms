package me.haliksar.securityalgorithms.libs.gcd

data class GcdPack
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

infix fun Long.extendedGcd(b: Long): GcdPack = if (b > this) b.gcd(this) else this.gcd(b)

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
infix fun Long.gcdTailRec(b: Long): GcdPack = this.goTailRec(b, 0L, 1L, 1L, 0L)

infix fun Long.extendedGcdTailRec(b: Long): GcdPack = if (b > this) b.gcdTailRec(this) else this.gcdTailRec(b)

private tailrec fun Long.goTailRec(
    b: Long,
    x: Long,
    y: Long,
    prevX: Long,
    prevY: Long,
): GcdPack =
    if (b == 0L) {
        GcdPack(this, prevX, prevY)
    } else {
        b.goTailRec(this % b, prevX - (this / b) * x, prevY - (this / b) * y, x, y)
    }


fun main() {
    println(22L gcd 44L)
    println(22L extendedGcdTailRec 44L)
    println(22L gcdTailRec 44L)
}