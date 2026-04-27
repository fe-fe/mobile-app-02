package br.ufpr.agenda.data.model

data class StatusModel (
    val id: Int = 0,
    val nome: String,
    val peso: Int
) {
    override fun toString(): String {
        return nome.uppercase()
    }
}