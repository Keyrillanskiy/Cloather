package domain.models.values

/**
 * @author Keyrillanskiy
 * @since 18.01.2019, 20:39.
 */

enum class Gender(val value: Char) {
    MALE('m'), FEMALE('f'), UNDEFINED('u')
}

fun String.toGender(): Gender {
    val genders = Gender.values()
    return genders.first { it.value == this.first() }
}