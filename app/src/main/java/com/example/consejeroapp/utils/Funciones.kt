package com.example.consejeroapp.utils

import android.content.Context
import android.util.Log
import com.example.consejeroapp.data.Consejo
import com.example.consejeroapp.data.ConsejoDAO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection

public class Funciones {

     companion object {
         private lateinit var consejoDAO: ConsejoDAO

         fun obtenerConsejosByQuery(query: String?): Consejo {
             var result: String = ""
             lateinit var res :Consejo

             if (query.isNullOrEmpty()) {
                 return obtenerConsejos()
             }
             else{
                 // Llamada en hilo secundario
                 //CoroutineScope(Dispatchers.IO).launch {
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
                         result = json.getJSONObject("slips").getString("advice")
                         res.texto = json.getJSONObject("slips").getString("advice")
                         res.adviceID = json.getJSONObject("slips").getString("id").toInt()

                         // Ejecutamos en el hilo principal
                         /*CoroutineScope(Dispatchers.Main).launch {

                        }*/
                         /*runOnUiThread {
                            mostrarDialog(result.toString(),result.toString(),false)
                        }*/


                     } else { // Hubo un error
                         Log.w("HTTP", "Response :: Hubo un error")
                     }
                 } catch (e: Exception) {
                     Log.e("HTTP", "Response Error :: ${e.stackTraceToString()}")
                 } finally {
                     return res //result
                     //return@launch
                 }
             }
         }

         fun obtenerConsejos(): Consejo {
             var result: String = ""
             lateinit var res :Consejo



             // Llamada en hilo secundario
             //CoroutineScope(Dispatchers.IO).launch {
                 try {
                     res = Consejo(-1, -1,"")

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
                         result =  json.getJSONObject("slip").getString("advice")
                         res.texto = json.getJSONObject("slip").getString("advice")
                         res.adviceID = json.getJSONObject("slip").getString("id").toInt()


                         // Ejecutamos en el hilo principal
                         /*CoroutineScope(Dispatchers.Main).launch {

                         }*/
                         /* runOnUiThread {
                             //mostrarDialog(result.toString(),result.toString(),false)
                             evaluaBusqueda(result.toString())
                         }*/

                     } else { // Hubo un error
                         Log.w("HTTP", "Response :: Hubo un error")
                     }
                 } catch (e: Exception) {
                     Log.e("HTTP", "Response Error :: ${e.stackTraceToString()}")
                     //mostrarDialog("ERROR", "error aqui")
                 } finally {
                     return res //result?: "No Advice, sorry"
                 //return@launch
             }
             //}
         }

         fun GuardaResultadoConsulta(context: Context, query:String)
         {
             consejoDAO = ConsejoDAO(context)
             //consejoDAO.insert(Consejo(-1, obtenerConsejosByQuery(query)))
             consejoDAO.insert(obtenerConsejosByQuery(query))
         }

         fun GuardaResultadoConsultaDAO(consejoDAO: ConsejoDAO, query:String)
         {
             //consejoDAO.insert(Consejo(-1, obtenerConsejosByQuery(query)))
             consejoDAO.insert(obtenerConsejosByQuery(query))
         }
         fun GuardaResultadoAleatorio(context: Context)
         {
             consejoDAO = ConsejoDAO(context)
             //consejoDAO.insert(Consejo(-1, obtenerConsejos()))
             consejoDAO.insert(obtenerConsejos())
         }
     }
}