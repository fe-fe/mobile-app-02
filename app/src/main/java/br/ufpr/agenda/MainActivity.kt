package br.ufpr.agenda

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import br.ufpr.agenda.data.dao.PrioridadeDAO
import br.ufpr.agenda.data.dao.StatusDAO
import br.ufpr.agenda.data.dao.TarefaDAO
import br.ufpr.agenda.data.model.TarefaModel
import br.ufpr.agenda.data.model.PrioridadeModel
import br.ufpr.agenda.data.model.StatusModel

class MainActivity : AppCompatActivity() {
    lateinit var listView: ListView
    lateinit var adapter: ArrayAdapter<TarefaModel>
    val lista = mutableListOf<TarefaModel>()
    lateinit var tarefaDAO: TarefaDAO
    lateinit var prioridadeDAO: PrioridadeDAO
    lateinit var statusDAO: StatusDAO

    val lista_prioridades = mutableListOf<PrioridadeModel>()
    val lista_status = mutableListOf<StatusModel>()


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
        statusDAO = StatusDAO(this)
        prioridadeDAO = PrioridadeDAO(this)

        lista_prioridades.add(PrioridadeModel(id = 0, nome = "Todas", peso = 0))
        lista_prioridades.addAll(prioridadeDAO.findAll())
        lista_status.add(StatusModel(id = 0, nome = "Todos", peso = 0))
        lista_status.addAll(statusDAO.findAll())

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

        val prioridadeSpinner = findViewById<Spinner>(R.id.prioridadeInput)
        val statusSpinner = findViewById<Spinner>(R.id.statusInput)

        prioridadeSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, lista_prioridades)
            .also { it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }

        statusSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, lista_status)
            .also { it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }
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

    fun filtrarTarefas(view: View) {
        val tituloInput = findViewById<EditText>(R.id.tituloInput)
        val prioridadeInput = findViewById<Spinner>(R.id.prioridadeInput)
        val statusInput = findViewById<Spinner>(R.id.statusInput)

        val titulo = tituloInput.text.toString().takeIf { it.isNotBlank() }
        val prioridade = (prioridadeInput.selectedItem as? PrioridadeModel)?.takeIf { it.id != 0 }
        val status = (statusInput.selectedItem as? StatusModel)?.takeIf { it.id != 0 }

        lista.clear()
        lista.addAll(tarefaDAO.find(
            titulo = titulo,
            prioridadeId = prioridade?.id,
            statusId = status?.id
        ))
        adapter.notifyDataSetChanged()
    }
}