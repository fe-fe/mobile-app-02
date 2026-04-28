package br.ufpr.agenda

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import br.ufpr.agenda.data.dao.TarefaDAO
import br.ufpr.agenda.data.model.TarefaModel

class MainActivity : AppCompatActivity() {
    lateinit var listView: ListView
    lateinit var adapter: ArrayAdapter<TarefaModel>
    val lista = mutableListOf<TarefaModel>()
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

        listView = findViewById(R.id.tasksListView)
        adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            lista
        )
        listView.adapter = adapter

        listView.setOnItemClickListener { _, _, position, _ ->
            val tarefa = lista[position]
            val intent = Intent(this, TarefaActivity::class.java)
            intent.putExtra("TAREFA_ID", tarefa.id)
            startActivity(intent)
        }

        // Clique para ADIÇÃO
        val btnAdd: Button = findViewById(R.id.addTaskButton)
        btnAdd.setOnClickListener {
            val intent = Intent(this, TarefaActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        carregarTarefas()
    }

    fun carregarTarefas() {
        lista.clear()
        val tarefas = tarefaDAO.find()
        lista.addAll(tarefas)
        adapter.notifyDataSetChanged()
    }
}
