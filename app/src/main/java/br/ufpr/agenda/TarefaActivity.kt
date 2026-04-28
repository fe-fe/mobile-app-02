package br.ufpr.agenda

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.ufpr.agenda.data.dao.PrioridadeDAO
import br.ufpr.agenda.data.dao.StatusDAO
import br.ufpr.agenda.data.dao.TarefaDAO
import br.ufpr.agenda.data.model.PrioridadeModel
import br.ufpr.agenda.data.model.StatusModel
import br.ufpr.agenda.data.model.TarefaModel

class TarefaActivity : AppCompatActivity() {

    private lateinit var etTitulo: EditText
    private lateinit var etDescricao: EditText
    private lateinit var spPrioridade: Spinner
    private lateinit var spStatus: Spinner
    private lateinit var btnSalvar: Button
    private lateinit var btnExcluir: Button
    private lateinit var btnCancelar: Button

    private lateinit var tarefaDAO: TarefaDAO
    private lateinit var statusDAO: StatusDAO
    private lateinit var prioridadeDAO: PrioridadeDAO

    private var tarefaId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tarefa)

        tarefaDAO = TarefaDAO(this)
        statusDAO = StatusDAO(this)
        prioridadeDAO = PrioridadeDAO(this)

        etTitulo = findViewById(R.id.etTitulo)
        etDescricao = findViewById(R.id.etDescricao)
        spPrioridade = findViewById(R.id.spPrioridade)
        spStatus = findViewById(R.id.spStatus)
        btnSalvar = findViewById(R.id.btnSalvar)
        btnExcluir = findViewById(R.id.btnExcluir)
        btnCancelar = findViewById(R.id.btnCancelar)

        carregarSpinners()

        tarefaId = intent.getIntExtra("TAREFA_ID", 0)
        if (tarefaId != 0) {
            preencherCampos(tarefaId)
            btnExcluir.visibility = View.VISIBLE
        }

        btnSalvar.setOnClickListener { salvar() }
        btnExcluir.setOnClickListener { excluir() }
        btnCancelar.setOnClickListener { finish() }
    }

    private fun carregarSpinners() {
        val listaStatus = statusDAO.findAll()
        val adapterStatus = ArrayAdapter(this, android.R.layout.simple_spinner_item, listaStatus)
        adapterStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spStatus.adapter = adapterStatus

        val listaPrioridades = prioridadeDAO.findAll()
        val adapterPrioridade = ArrayAdapter(this, android.R.layout.simple_spinner_item, listaPrioridades)
        adapterPrioridade.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spPrioridade.adapter = adapterPrioridade
    }

    private fun preencherCampos(id: Int) {
        val tarefa = tarefaDAO.findById(id)
        tarefa?.let {
            etTitulo.setText(it.titulo)
            etDescricao.setText(it.descricao)

            val adapterStatus = spStatus.adapter as ArrayAdapter<StatusModel>
            for (i in 0 until adapterStatus.count) {
                if (adapterStatus.getItem(i)?.id == it.id_status) {
                    spStatus.setSelection(i)
                    break
                }
            }

            val adapterPrioridade = spPrioridade.adapter as ArrayAdapter<PrioridadeModel>
            for (i in 0 until adapterPrioridade.count) {
                if (adapterPrioridade.getItem(i)?.id == it.id_prioridade) {
                    spPrioridade.setSelection(i)
                    break
                }
            }
        }
    }

    private fun salvar() {
        val titulo = etTitulo.text.toString()
        val descricao = etDescricao.text.toString()
        val status = spStatus.selectedItem as StatusModel
        val prioridade = spPrioridade.selectedItem as PrioridadeModel

        if (titulo.isEmpty()) {
            Toast.makeText(this, "O título é obrigatório", Toast.LENGTH_SHORT).show()
            return
        }

        val tarefa = TarefaModel(
            id = tarefaId,
            titulo = titulo,
            descricao = descricao,
            id_status = status.id,
            id_prioridade = prioridade.id
        )

        val resultado = if (tarefaId == 0) {
            tarefaDAO.create(tarefa)
        } else {
            tarefaDAO.update(tarefa).toLong()
        }

        if (resultado > 0) {
            Toast.makeText(this, "Tarefa salva com sucesso!", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, "Erro ao salvar tarefa.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun excluir() {
        if (tarefaId != 0) {
            val resultado = tarefaDAO.delete(tarefaId)
            if (resultado > 0) {
                Toast.makeText(this, "Tarefa excluída!", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Erro ao excluir tarefa.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
