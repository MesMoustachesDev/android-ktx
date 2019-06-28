package kot

import java.text.DecimalFormat

val formatEurosCents = DecimalFormat("0.00")

fun Double.toEurosAndCents() = "${formatEurosCents.format(this)} €"

fun Float.toEurosAndCents() = "${formatEurosCents.format(this)} €"