package com.roberto.concierto

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DetallesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalles)

        val nombre = intent.getStringExtra("nombre")
        val fecha = intent.getStringExtra("fecha")
        val lugar = intent.getStringExtra("lugar")
        val genero = intent.getStringExtra("genero")
        val precio = intent.getStringExtra("precio")

        val detallesTextView = findViewById<TextView>(R.id.detalles_text)
        detallesTextView.text = """
            Nombre: $nombre
            Fecha: $fecha
            Lugar: $lugar
            Género: $genero
            Precio: $$precio
        """.trimIndent()

        findViewById<Button>(R.id.btn_regresar).setOnClickListener {
            finish()
        }
    }
}
