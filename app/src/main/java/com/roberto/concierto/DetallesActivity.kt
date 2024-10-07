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
        val genero = intent.getStringExtra("genero")

        val detallesTextView = findViewById<TextView>(R.id.detalles_text)
        detallesTextView.text = "Nombre: $nombre\nFecha: $fecha\nGénero: $genero"

        val btnRegresar = findViewById<Button>(R.id.btn_regresar)
        btnRegresar.setOnClickListener {
            finish() // Cierra esta actividad y regresa a la anterior
        }
    }
}
