package br.ufpr.agenda.data.model

data class TarefaModel(
    val id: Int = 0,
    val titulo: String,
    val descricao: String,
    val id_prioridade: Int,
    val id_status: Int
) {
    override fun toString(): String {
        var str = "$titulo: ${descricao.take(100)}"
        if (descricao.length > 100) {
            str += "..."
        }
        return str
    }
}
