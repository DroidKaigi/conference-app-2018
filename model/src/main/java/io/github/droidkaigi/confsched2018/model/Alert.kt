package io.github.droidkaigi.confsched2018.model

data class Alert(
        val type: Type,
        val message: Int
) {
    enum class Type {
        Toast,
        AlertDialog
    }
}
