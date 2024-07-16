package com.example.consejeroapp.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.consejeroapp.R
import com.example.consejeroapp.data.Consejo
import com.example.consejeroapp.data.ConsejoDAO
import com.example.consejeroapp.databinding.ActivityBuscaBinding
import com.example.consejeroapp.utils.Funciones
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection


class BuscaActivity : AppCompatActivity() {

    lateinit var binding: ActivityBuscaBinding
    lateinit var tabLayout:TabLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBuscaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val id = intent.getStringExtra(Consejo.SEARCH_ID)

        binding.newAdviceEditText.setOnEditorActionListener(OnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_DONE || keyEvent.action == KeyEvent.ACTION_DOWN || keyEvent.action == KeyEvent.KEYCODE_ENTER) {
                //Toast.makeText(this, "Enter", Toast.LENGTH_SHORT).show()
                val intent : Intent = Intent(this, MainActivity::class.java) //::class.java
                intent.putExtra(Consejo.PUTEXTRA_ID, textView.text.toString())


                //progressBar!!.visibility = View.GONE
                //si quieres ver la activity esto es necesario
                startActivity(intent)

                return@OnEditorActionListener true
            }
            false
        })

        //obtenerConsejosByQuery(id.toString())
        obtenerConsejos()
        //mostrarDialog(Funciones.obtenerConsejos().toString(),Funciones.obtenerConsejos().toString(),true)
    }

    //tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

      /* override fun onTabSelected(tab: TabLayout.Tab?) {
            // Handle tab select
        }

        override fun onTabReselected(tab: TabLayout.Tab?) {
            // Handle tab reselect
        }

        override fun onTabUnselected(tab: TabLayout.Tab?) {
            // Handle tab unselect
        }*/
    //})



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
    fun obtenerConsejosByQuery(query:String) {
        if (query.isNullOrEmpty())
            obtenerConsejos()
        else
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
                    val result =  json.getJSONObject("slips").getString("advice")

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

        dialog.show()
    }

}


