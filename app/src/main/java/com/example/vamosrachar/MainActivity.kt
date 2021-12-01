package com.example.vamosrachar
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.KeyEvent
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*

private var mode:Boolean = false

class MainActivity : AppCompatActivity(),TextToSpeech.OnInitListener {
    private var tts: TextToSpeech? = null
    private var speaker: FloatingActionButton? = null
    private var res:TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (mode == false){
            setTheme(R.style.Theme_VamosRachar)
        }
        else setTheme(R.style.Theme_VamosRacharAcessivel)

        setContentView(R.layout.activity_main)

        val n1 = findViewById<EditText>(R.id.editNumber1)
        val n2 = findViewById<EditText>(R.id.editNumber2)
        res = findViewById(R.id.res)
        val share = findViewById<FloatingActionButton>(R.id.ShareButton)
        speaker = findViewById(R.id.speakerButton)
        val constrast = findViewById<FloatingActionButton>(R.id.contrastButton)


        constrast.setOnClickListener{
            mode = mode == false
            recreate()
        }

        n1.setOnKeyListener { v, keyCode, event ->
            var handled = false
            if (keyCode == KeyEvent.KEYCODE_DEL || keyCode >= 0) {
                if (n1.text.toString().trim().isNotEmpty() && n2.text.toString().trim().isNotEmpty()) {
                    val val1 = n1.text.toString().toFloat()
                    val val2 = n2.text.toString().toFloat()
                    val div = val1 / val2
                    res!!.text = div.toString()
                }else{
                    res!!.text = 0.toString()
                }
            }
            handled
        }

        n2.setOnKeyListener { v, keyCode, event ->
            var handled = false
            if (keyCode == KeyEvent.KEYCODE_DEL || keyCode >= 0) {
                if (n1.text.toString().trim().isNotEmpty() && n2.text.toString().trim().isNotEmpty()) {
                    val val1 = n1.text.toString().toFloat()
                    val val2 = n2.text.toString().toFloat()
                    val div = val1 / val2
                    res!!.text = div.toString()
                }else{
                    res!!.text = 0.toString()
                }
            }
            handled
        }

        share.setOnClickListener{

            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, res!!.text.toString())
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }

        speaker!!.isEnabled = false
        tts = TextToSpeech(this,this)
        speaker!!.setOnClickListener{
            speak()
        }
    }

    override fun onInit(status: Int) {
        if(status == TextToSpeech.SUCCESS){
            val result = tts!!.setLanguage(Locale.getDefault())
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED){
                Toast.makeText(this, "not suported", Toast.LENGTH_SHORT).show()
            }else{
                speaker!!.isEnabled = true
            }
        }else{
            Toast.makeText(this, "falha!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun speak() {
        val sp = res!!.text.toString()
        tts!!.speak(sp, TextToSpeech.QUEUE_FLUSH, null, "")
    }

    override fun onDestroy() {
        if (tts != null){
            tts!!.stop()
            tts!!.shutdown()
        }
        super.onDestroy()
    }
}
