package com.roberto.concierto

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var inputNombre: EditText
    private lateinit var inputFecha: EditText
    private lateinit var inputLugar: EditText
    private lateinit var inputPrecio: EditText
    private lateinit var spinnerGenero: Spinner

    private var generoSeleccionado: String = "Pop"
    private val listaConciertos = mutableListOf<Concierto>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        inputNombre = findViewById(R.id.input_nombre)
        inputFecha = findViewById(R.id.input_fecha)
        inputLugar = findViewById(R.id.input_lugar)
        inputPrecio = findViewById(R.id.input_precio)
        spinnerGenero = findViewById(R.id.spinner_genero)

        val registrarButton = findViewById<Button>(R.id.btn_registrar)
        val buscarButton = findViewById<Button>(R.id.btn_buscar)
        val limpiarButton = findViewById<Button>(R.id.btn_limpiar)
        val eliminarButton = findViewById<Button>(R.id.btn_eliminar)

        val generos = listOf("Pop", "Hip-Hop", "Clásica", "Reggae", "Indie", "Techno", "Cumbia")
        val adaptador = ArrayAdapter(this, android.R.layout.simple_spinner_item, generos)
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerGenero.adapter = adaptador
        spinnerGenero.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                generoSeleccionado = generos[position]
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        registrarButton.setOnClickListener { registrar() }
        buscarButton.setOnClickListener { buscar() }
        limpiarButton.setOnClickListener { limpiar() }
        eliminarButton.setOnClickListener { eliminar() }
    }

    private fun registrar() {
        val nombre = inputNombre.text.toString().trim()
        val fecha = inputFecha.text.toString().trim()
        val lugar = inputLugar.text.toString().trim()
        val precioStr = inputPrecio.text.toString().trim()

        if (nombre.isEmpty() || fecha.isEmpty() || lugar.isEmpty() || precioStr.isEmpty()) {
            mostrarMensaje("Por favor, complete todos los campos.")
            return
        }

        val precio = precioStr.toDoubleOrNull()
        if (precio == null || precio <= 0) {
            mostrarMensaje("Ingrese un precio válido.")
            return
        }

        listaConciertos.add(Concierto(nombre, fecha, lugar, generoSeleccionado, precio))
        mostrarMensaje("Concierto registrado correctamente.")
        limpiar()
    }

    private fun buscar() {
        val nombreBuscar = inputNombre.text.toString().trim()
        val concierto = listaConciertos.find { it.nombre.equals(nombreBuscar, ignoreCase = true) }

        if (concierto != null) {
            val intent = Intent(this, DetallesActivity::class.java).apply {
                putExtra("nombre", concierto.nombre)
                putExtra("fecha", concierto.fecha)
                putExtra("lugar", concierto.lugar)
                putExtra("genero", concierto.genero)
                putExtra("precio", concierto.precio.toString())
            }
            startActivity(intent)
        } else {
            mostrarMensaje("Concierto no encontrado.")
        }
    }

    private fun eliminar() {
        val nombreEliminar = inputNombre.text.toString().trim()
        val concierto = listaConciertos.find { it.nombre.equals(nombreEliminar, ignoreCase = true) }

        if (concierto != null) {
            listaConciertos.remove(concierto)
            mostrarMensaje("Concierto eliminado correctamente.")
            limpiar()
        } else {
            mostrarMensaje("Concierto no encontrado.")
        }
    }

    private fun limpiar() {
        inputNombre.text.clear()
        inputFecha.text.clear()
        inputLugar.text.clear()
        inputPrecio.text.clear()
        spinnerGenero.setSelection(0)
    }

    private fun mostrarMensaje(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }
}
