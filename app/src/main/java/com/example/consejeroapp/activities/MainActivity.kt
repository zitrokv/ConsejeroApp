package com.example.consejeroapp.activities

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.GridLayoutManager
import com.example.consejeroapp.data.Consejo
import com.example.consejeroapp.data.ConsejoDAO
import com.example.consejeroapp.databinding.ActivityMainBinding
import com.example.consejeroapp.utils.ConsejoAdapter
import com.example.consejeroapp.utils.Funciones
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.util.Calendar
import javax.net.ssl.HttpsURLConnection


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private lateinit var adapter: ConsejoAdapter
    private lateinit var consejoDAO: ConsejoDAO
    lateinit var consejoList: List<Consejo>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id = intent.getStringExtra(Consejo.PUTEXTRA_ID)

        Funciones.GuardaResultadoConsulta(this, (id).toString())

        //Funciones.GuardaResultadoConsultaDAO(consejoDAO, (id.toString()) )
                /*
                Funciones.GuardaResultadoConsulta(this, (id
            ?: (Funciones.GuardaResultadoAleatorio((this))) ))
*/
                consejoDAO = ConsejoDAO (this)
                //consejoDAO.insert(Consejo(-1, "Ir al banco"))
                //consejoDAO.insert(Consejo(-1, "Entrenar"))


                adapter = ConsejoAdapter (
                emptyList(), {
                /*Toast.makeText(
                    this,
                    "Click en ${tareaList[it].nombre}",
                    Toast.LENGTH_LONG
                ).show()*/
            },
            {
                if (consejoDAO.delete(consejoList[it]) == 0) {
                    //Toast.makeText(this, id.toString(), Toast.LENGTH_SHORT).show()
                    /*val intent : Intent = Intent(this, BuscaActivity::class.java) //::class.java
                    intent.putExtra(Consejo.SEARCH_ID, id.toString())
                    //si quieres ver la activity esto es necesario
                    startActivity(intent)*/

                    obtenerConsejosByID(id.toString())

                   //obtenerConsejosByQuery(id.toString())
                    //Funciones.obtenerConsejos()
                    //evaluaBusqueda(id)
                    //evaluaBusqueda(consejoList[it].texto)
                    //mostrarDialog(consejoList[it].texto, consejoList[it].texto)
                }
                else {
                    consejoDAO.update(consejoList[it])
                    loadData()
                }
            },

            {
                consejoList[it].leida = !consejoList[it].leida
                consejoDAO.update(consejoList[it])
                loadData()
            },
            {
                //showDialog()
                //mostrarDialog("Modificar tarea", consejoList[it].texto)
                loadData()
            }
        )

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = GridLayoutManager(this, 2)
        //binding.recyclerView.layoutManager = LinearLayoutManager(this)


        evaluaBusqueda(id)
    }

    override fun onResume() {
        super.onResume()

        loadData()
    }

    private fun loadData() {
        //consejoDAO.insert(Consejo())
        //llamada a API para obtener por query
        Funciones.Companion.obtenerConsejosByQuery(intent.getStringExtra(Consejo.PUTEXTRA_ID).toString())
        consejoList = consejoDAO.findAll()
        //consejoDAO.insert(Consejo(-1, "Ir al otro sitio"))
        adapter.updateData(consejoList)
    }

    fun mostrarDialog(title: String, message: String, esNuevaTarea:Boolean = false) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)

        builder.setMessage(title)
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

    fun evaluaBusqueda(codigo:String?)
    {
        mostrarDialog("Evalua Busqueda", "valor codigo")
        if (codigo == null)
            obtenerConsejos()
        else
            obtenerConsejosByQuery(codigo)



    }

    fun p1_bay(view: View?) {
        Toast.makeText(this, "You have clicked P1", Toast.LENGTH_LONG).show()
    }

    fun obtenerConsejos() {
        // Llamada en hilo secundario
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Declaramos la url
                val url = URL("https://api.adviceslip.com/advice")
                val con = url.openConnection() as HttpsURLConnection
                con.requestMethod = "GET"
                val responseCode = con.responseCode
                Log.i("HTTP", "Response Code :: $responseCode")

                // Preguntamos si hubo error o no
                if (responseCode == HttpsURLConnection.HTTP_OK) { // Ha ido bien
                    // Metemos el cuerpo de la respuesta en un BurfferedReader
                    val bufferedReader = BufferedReader(InputStreamReader(con.inputStream))
                    var inputLine: String?
                    val response = StringBuffer()
                    while (bufferedReader.readLine().also { inputLine = it } != null) {
                        response.append(inputLine)
                    }
                    bufferedReader.close()

                    // Parsear JSON
                    val json = JSONObject(response.toString())
                    val result =  json.getJSONObject("slip").getString("advice")

                    // Ejecutamos en el hilo principal
                    /*CoroutineScope(Dispatchers.Main).launch {

                    }*/
                    runOnUiThread {
                        //mostrarDialog(result.toString(),result.toString(),false)
                        evaluaBusqueda(result.toString())
                    }

                } else { // Hubo un error
                    Log.w("HTTP", "Response :: Hubo un error")
                }
            } catch (e: Exception) {
                Log.e("HTTP", "Response Error :: ${e.stackTraceToString()}")
                mostrarDialog("ERROR", "error aqui")
            }
        }
    }




    fun obtenerConsejosByID(queryID:String?) {
        if (queryID.isNullOrEmpty()) {
            obtenerConsejos()
            //mostrarDialog(queryID.toString(), queryID.toString())
        }else
        // Llamada en hilo secundario
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    // Declaramos la url
                    //val url = URL("https://api.adviceslip.com/advice")
                    val url = URL(" https://api.adviceslip.com/advice/$queryID")
                    val con = url.openConnection() as HttpsURLConnection
                    con.requestMethod = "GET"
                    val responseCode = con.responseCode
                    Log.i("HTTP", "Response Code :: $responseCode")

                    // Preguntamos si hubo error o no
                    if (responseCode == HttpsURLConnection.HTTP_OK) { // Ha ido bien
                        // Metemos el cuerpo de la respuesta en un BurfferedReader
                        val bufferedReader = BufferedReader(InputStreamReader(con.inputStream))
                        var inputLine: String?
                        val response = StringBuffer()
                        while (bufferedReader.readLine().also { inputLine = it } != null) {
                            response.append(inputLine)
                        }
                        bufferedReader.close()

                        // Parsear JSON
                        val json = JSONObject(response.toString())
                        val result =  json.getJSONObject("slip").getString("advice")

                        // Ejecutamos en el hilo principal
                        /*CoroutineScope(Dispatchers.Main).launch {

                        }*/
                        runOnUiThread {
                            mostrarDialog(result.toString(),result.toString(),false)
                        }

                    } else { // Hubo un error
                        Log.w("HTTP", "Response :: Hubo un error")
                    }
                } catch (e: Exception) {
                    Log.e("HTTP", "Response Error :: ${e.stackTraceToString()}")
                }
            }
    }
    fun obtenerConsejosByQuery(query:String?) {
        if (query.isNullOrEmpty()) {
            //obtenerConsejos()
            mostrarDialog(query.toString(), query.toString())
        }else
        // Llamada en hilo secundario
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Declaramos la url
                //val url = URL("https://api.adviceslip.com/advice")
                val url = URL("https://api.adviceslip.com/advice/search/$query")
                val con = url.openConnection() as HttpsURLConnection
                con.requestMethod = "GET"
                val responseCode = con.responseCode
                Log.i("HTTP", "Response Code :: $responseCode")

                // Preguntamos si hubo error o no
                if (responseCode == HttpsURLConnection.HTTP_OK) { // Ha ido bien
                    // Metemos el cuerpo de la respuesta en un BurfferedReader
                    val bufferedReader = BufferedReader(InputStreamReader(con.inputStream))
                    var inputLine: String?
                    val response = StringBuffer()
                    while (bufferedReader.readLine().also { inputLine = it } != null) {
                        response.append(inputLine)
                    }
                    bufferedReader.close()

                    // Parsear JSON
                    val json = JSONObject(response.toString())
                    val result =  json.getJSONObject("slip").getString("advice")

                    // Ejecutamos en el hilo principal
                    /*CoroutineScope(Dispatchers.Main).launch {

                    }*/
                    runOnUiThread {
                        mostrarDialog(result.toString(),result.toString(),false)
                    }

                } else { // Hubo un error
                    Log.w("HTTP", "Response :: Hubo un error")
                }
            } catch (e: Exception) {
                Log.e("HTTP", "Response Error :: ${e.stackTraceToString()}")
            }
        }
    }

}