package com.example.consejeroapp.activities

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.DatePicker
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.GridLayoutManager
import com.example.consejeroapp.data.Consejo
import com.example.consejeroapp.data.ConsejoDAO
import com.example.consejeroapp.databinding.ActivityMainBinding
import com.example.consejeroapp.utils.ConsejoAdapter
import java.util.Calendar


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private lateinit var adapter: ConsejoAdapter
    private lateinit var taskDAO: ConsejoDAO
    lateinit var consejoList: List<Consejo>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        taskDAO = ConsejoDAO(this)
        taskDAO.insert(Consejo(-1, "Ir al banco"))
        taskDAO.insert(Consejo(-1, "Entrenar"))

        adapter = ConsejoAdapter(
            emptyList(), {
                /*Toast.makeText(
                    this,
                    "Click en ${tareaList[it].nombre}",
                    Toast.LENGTH_LONG
                ).show()*/
            },
            {
                taskDAO.delete(consejoList[it])
                /*Toast.makeText(
                    this,
                    "Click en ${tareaList[it].nombre}",
                    Toast.LENGTH_LONG
                ).show()*/
                loadData()
            },

            {
                consejoList[it].leida = !consejoList[it].leida
                taskDAO.update(consejoList[it])
                loadData()
            },
            {
                //showDialog()
                mostrarDialog("Modificar tarea", consejoList[it].texto)
                loadData()
            }
        )

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = GridLayoutManager(this, 2)
        //binding.recyclerView.layoutManager = LinearLayoutManager(this)
    }

    override fun onResume() {
        super.onResume()

        loadData()
    }

    private fun loadData() {
        consejoList = taskDAO.findAll()

        adapter.updateData(consejoList)
    }

    fun mostrarDialog(title: String, message: String, esNuevaTarea:Boolean = false) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)

        //builder.setMessage("Fecha a rellenar...")
//builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))

        /*val dialogBinding = ActivityDetalleBinding.inflate(layoutInflater)

        builder.setView(dialogBinding.root)
*/


        //botones comunes OK Cancel
        builder.setPositiveButton(android.R.string.yes)
        { dialog, which ->


            /*Toast.makeText(
                this,
                android.R.string.yes, Toast.LENGTH_SHORT
            ).show()*/
        }

        builder.setNegativeButton(android.R.string.no)
        { dialog, which ->
            /*Toast.makeText(
                this,
                android.R.string.no, Toast.LENGTH_SHORT
            ).show()*/
        }

        //mostrar en pantalla
        val dialog = builder.create()


        //si es modificación muestra los botones
        if (!esNuevaTarea) {
            //dialogBinding.nameEditText.visibility = View.GONE
            builder.setMessage(message)

        } else
        {

            /*dialogBinding.saveButton.setOnClickListener {
                val taskName = dialogBinding.nameEditText.text.toString()
                val task = Consejo(-1, taskName)
                taskDAO.insert(task)
                Toast.makeText(this, "Tarea guardad correctamente", Toast.LENGTH_SHORT).show()
                loadData()
                dialog.dismiss()
                //finish()*/
            //}

        }

        //dialog.show()
    }

    class DatePickerFragment(val listener: (day: Int, month: Int, year: Int) -> Unit) :
        DialogFragment(), DatePickerDialog.OnDateSetListener {
        override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
            TODO("Not yet implemented")
        }

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
            val picker = DatePickerDialog(activity as Context, this, year, month, day)
            return picker
        }
    }

    fun showDialog() {
        val fragmentManager = supportFragmentManager
        //val newFragment = DetalleActivity()
        //val newFragment = fragmentManager.
        //if (isLargeLayout) {
        if (true){
            // The device is using a large layout, so show the fragment as a
            // dialog.
            //newFragment.show(fragmentManager, "dialog")

            //newFragment.showNoticeDialog()
        } else {
            // The device is smaller, so show the fragment fullscreen.
            val transaction = fragmentManager.beginTransaction()
            // For a polished look, specify a transition animation.
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            // To make it fullscreen, use the 'content' root view as the container
            // for the fragment, which is always the root view for the activity.

            transaction
                //.add(binding.root, newFragment())
                .addToBackStack(null)
                .commit()
        }
    }
}