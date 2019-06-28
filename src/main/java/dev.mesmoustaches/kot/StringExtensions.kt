package kot

import java.text.Normalizer

fun String.capitalizeEachWord() =
        String(toCharArray().mapIndexed { index, c ->
            if (index == 0 || index < this.length && (this[index - 1] == ' ' || this[index - 1] == '-')) c.toUpperCase() else c
        }.toCharArray())

val removeReg by lazy {
    Regex("[^\\p{ASCII}]")
}

fun String.stripAccents() = Normalizer.normalize(this, Normalizer.Form.NFD)
        .replace(removeReg, "")

fun String?.nullifyTriming() = if (this == null || this.trim().isEmpty()) null else this.trim()
