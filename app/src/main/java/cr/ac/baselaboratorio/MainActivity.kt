package cr.ac.baselaboratorio

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.documentfile.provider.DocumentFile
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    companion object{
        var OPEN_DIRECTORY_REQUEST_CODE = 1
    }
    private lateinit var buttonPlay: Button
    private lateinit var buttonPause: Button
    private lateinit var buttonStop: Button
    private lateinit var buttonPrevious: Button
    private lateinit var buttonNext: Button

    lateinit var rootTree : DocumentFile
    var mediaPlayer = MediaPlayer()
    var iterador = 1
    var reproduce = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
        startActivityForResult(intent, OPEN_DIRECTORY_REQUEST_CODE)

        buttonPlay = findViewById(R.id.buttonPlay)
        buttonPause = findViewById(R.id.buttonPause)
        buttonStop = findViewById(R.id.buttonStop)
        buttonNext = findViewById(R.id.buttonNext)
        buttonPrevious = findViewById(R.id.buttonPrevious)
        setOnClickListeners(this)
    }
    private fun setOnClickListeners(context: Context) {
        buttonPlay.setOnClickListener {

            if(reproduce == iterador){
                mediaPlayer.start()
                Toast.makeText(context,"Archivo: " + rootTree.listFiles()?.get(iterador).name, Toast.LENGTH_SHORT).show()
            }
            else {
                try {
//
                    Toast.makeText(context,"Archivo: " + rootTree.listFiles()?.get(2).name, Toast.LENGTH_SHORT).show()
                    mediaPlayer.setDataSource(this, rootTree.listFiles()?.get(iterador).uri)
                    mediaPlayer.prepare()
                    mediaPlayer.start()
                    reproduce = iterador
                } catch (e: Exception) {
                    Log.e(
                        "Error",
                        "No puede ejecutar el archivo" + rootTree.listFiles()?.get(iterador).uri
                    )
                }
            }
            Toast.makeText(context, "Reproduciendo...", Toast.LENGTH_SHORT).show()
        }

        buttonPause.setOnClickListener {
            mediaPlayer.pause()
            Toast.makeText(context, "En pausa...", Toast.LENGTH_SHORT).show()
        }

        buttonStop.setOnClickListener {
            mediaPlayer.stop()
            iterador = 1
            reproduce = 0

            Toast.makeText(context, "Parando...", Toast.LENGTH_SHORT).show()
        }
        buttonNext.setOnClickListener {
            mediaPlayer.stop()
            mediaPlayer = MediaPlayer()

            if(iterador == rootTree.listFiles()?.size ){
                iterador = 1
                reproduce = 0
            }else {
                iterador = iterador + 1
                reproduce = iterador
            }
            try {

                Toast.makeText(context,"Archivo: " + rootTree.listFiles()?.get(iterador).name, Toast.LENGTH_SHORT).show()
                mediaPlayer.setDataSource(this, rootTree.listFiles()?.get(iterador).uri)
                mediaPlayer.prepare()
                mediaPlayer.start()
            } catch (e: Exception) {
                Log.e(
                    "Error",
                    "No puede ejecutar el archivo" + rootTree.listFiles()?.get(iterador).uri
                )
            }
        }
        buttonPrevious.setOnClickListener {
            mediaPlayer.stop()
            mediaPlayer = MediaPlayer()

            iterador = iterador - 1
            reproduce=iterador
            try {
                //
                Toast.makeText(context,"Archivo: " + rootTree.listFiles()?.get(iterador).name, Toast.LENGTH_SHORT).show()
                mediaPlayer.setDataSource(this, rootTree.listFiles()?.get(iterador).uri)
                mediaPlayer.prepare()
                mediaPlayer.start()
            } catch (e: Exception) {
                Log.e(
                    "Error",
                    "No puede ejecutar el archivo" + rootTree.listFiles()?.get(iterador).uri
                )
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == OPEN_DIRECTORY_REQUEST_CODE ){
            if(resultCode == Activity.RESULT_OK){
                var directoryUri = data?.data ?: return
                // Log.e("Directorio",directoryUri.toString())
                rootTree = DocumentFile.fromTreeUri(this, directoryUri)!!
            }
        }
    }

}