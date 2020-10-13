package me.haliksar.securityalgorithms.libs.gcd.ulong

data class GcdPack
@ExperimentalUnsignedTypes
constructor(var nod: ULong, var x: ULong, var y: ULong)

/**
 * Функция, реализующая обобщённый алгоритм Евклида.
 *
 * @receiver [ULong] первое число
 *
 * @param b [ULong] второе число
 *
 * @return [GcdPack] функция возвращает группу где первый элемент это НОД,
 * втророй и третий - две неизвестных функции ax + by
 */
@ExperimentalUnsignedTypes
infix fun ULong.gcd(b: ULong): GcdPack {
    var packU = GcdPack(this, 1uL, 0uL)
    var packV = GcdPack(b, 0uL, 1uL)
    while (packV.nod != 0uL) {
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
 * @receiver [ULong] первое число
 *
 * @param b [ULong] второе число
 *
 * @return [Triple] функция возвращает группу где первый элемент это НОД,
 * втророй и третий - две неизвестных функции ax + by
 */
@ExperimentalUnsignedTypes
infix fun ULong.gcdTailRec(b: ULong): GcdPack = this.goTailRec(b, 0uL, 1uL, 1uL, 0uL)

@ExperimentalUnsignedTypes
private tailrec fun ULong.goTailRec(
    b: ULong,
    x: ULong,
    y: ULong,
    prevX: ULong,
    prevY: ULong
): GcdPack =
    if (b == 0uL) {
        GcdPack(this, prevX, prevY)
    } else {
        val q = this / b
        b.goTailRec(this % b, prevX - q * x, prevY - q * y, x, y)
    }


@ExperimentalUnsignedTypes
fun main() {
    println(22uL gcd 44uL)
    println(22uL gcdTailRec 44uL)
}