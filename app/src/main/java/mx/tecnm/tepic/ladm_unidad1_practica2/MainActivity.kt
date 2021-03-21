package mx.tecnm.tepic.ladm_unidad1_practica2

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.io.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        radioInterna.isChecked
        botonGuardar.setOnClickListener {
            try {
                 if(!texto.text.isNullOrEmpty())   {
                     if(radioInterna.isChecked){
                         if(guardarInterna(texto.text.toString())){
                             alertDialog("Listo!","Mensaje guardado con exito en Memoria Interna")
                         }
                     }
                     if (radioExterna.isChecked){
                         guardarExterna(archivo.text.toString())
                             alertDialog("Listo!","Mensaje guardado con exito en Memoria Externa")

                     }

                 }else{
                     alertDialog("Error","Por favor escribe algo en el recruado")
                 }
            }catch (io:IOException){
                alertDialog("Error","Error al guardar el archivo ")

            }
        }
        botonAbrir.setOnClickListener {
            try {
                if(radioInterna.isChecked){
                    abrirInterna()

                }
                if(radioExterna.isChecked){
                    abrirExterna()
                }
            }catch (io:IOException){
                alertDialog("Error","No se pudo abrir el archivo socilitado")
            }
        }
    }

    private fun guardarInterna(data:String):Boolean{
        try {
            if (!archivo.text.isNullOrEmpty()){
                var flujo = OutputStreamWriter(openFileOutput(archivo.text.toString(), MODE_PRIVATE))
                flujo.write(data)
                texto.setText("")
                flujo.flush()
                flujo.close()
            }
        }catch (io:IOException){
            return false
        }
        return true
    }
    private fun abrirInterna():String{
        var data=""
        try {
            if(!archivo.text.toString().isNullOrEmpty()){
                var flujo = BufferedReader(InputStreamReader(openFileInput(archivo.text.toString())))
                val br = BufferedReader(flujo)
                var dato = br.readLine()
                val todo = StringBuilder()
                while (dato != null) {
                    todo.append(dato + "\n")
                    dato = br.readLine()
                }
                br.close()
                flujo.close()
                data=todo.toString()
                texto.setText(data)
            }else{
                alertDialog("Error","Introduce un nombre de archivo valido")
            }

        }catch (io:IOException){return ""}
        return data
    }

    private fun guardarExterna(data:String){
        try {
            permisos()
            if (Environment.getExternalStorageState()!=Environment.MEDIA_MOUNTED){
                alertDialog("Error","No se encuentra SD")
                return
            }
            var rutaSD = getExternalFilesDir(null)!!.absolutePath
            var archivoSD = File(rutaSD, data)
            var flujo = OutputStreamWriter(FileOutputStream(archivoSD))

            flujo.write(texto.text.toString())
            texto.setText("")
            flujo.flush()
            flujo.close()

        }catch (io:IOException){
            alertDialog("Error",io.message.toString())
        }
    }
    private fun abrirExterna(){
        try {
            if (Environment.getExternalStorageState()!=Environment.MEDIA_MOUNTED){
                alertDialog("Error","No hay SD")
                return
            }
            var rutaSD = getExternalFilesDir(null)!!.absolutePath
            var archivoSD = File(rutaSD, archivo.text.toString())
            var flujo = BufferedReader(InputStreamReader(FileInputStream(archivoSD)))


            val br = BufferedReader(flujo)
            var dato = br.readLine()
            val todo = StringBuilder()
            while (dato != null) {
                todo.append(dato + "\n")
                dato = br.readLine()
            }

            texto.setText(todo.toString())
            br.close()

            flujo.close()
        }catch (io:IOException){alertDialog("Error",io.message.toString())}
    }
    private fun alertDialog(titulo:String,mensaje:String){
        AlertDialog.Builder(this).setTitle(titulo)
            .setMessage(mensaje)
            .setPositiveButton("ok"){d,i->d.dismiss()}
            .show()
    }

    private fun permisos(){
        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),0)
        }
    }
}