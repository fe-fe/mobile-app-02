package br.ufpr.agenda

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import br.ufpr.agenda.data.dao.TarefaDAO

class MainActivity : AppCompatActivity() {
    lateinit var  listView: ListView
    lateinit var  adapter: ArrayAdapter<String>
    val lista = mutableListOf<String>()
    lateinit var tarefaDAO: TarefaDAO
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        tarefaDAO = TarefaDAO(this)

        tarefaDAO.create(
            br.ufpr.agenda.data.model.TarefaModel(
                id = 0,
                titulo = "Tarefa teste",
                descricao = "Teste banco",
                id_status = 1,
                id_prioridade = 1
            )
        )

        listView = findViewById(R.id.tasksListView)
        adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            lista
        )
        listView.adapter = adapter
        carregarTarefas()
    }
        fun carregarTarefas() {
            lista.clear()

            val tarefas = tarefaDAO.find()

            for (t in tarefas) {
                val texto = "${t.titulo} | Status: ${t.id_status} | Prioridade: ${t.id_prioridade}"
                lista.add(texto)
            }
            adapter.notifyDataSetChanged()
        }
}