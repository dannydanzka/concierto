package com.roberto.concierto

import android.view.View
import android.content.SharedPreferences
import android.os.Bundle
import android.content.Intent
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var inputNombre: EditText
    private lateinit var inputFecha: EditText
    private lateinit var spinnerGenero: Spinner
    private lateinit var sharedPreferences: SharedPreferences
    private var generoSel: String = "Cumbia"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        inputNombre = findViewById(R.id.input_nombre)
        inputFecha = findViewById(R.id.input_fecha)
        spinnerGenero = findViewById(R.id.spinner_genero)

        sharedPreferences = getSharedPreferences("ConciertoPrefs", MODE_PRIVATE)

        val registrarButton = findViewById<Button>(R.id.btn_registrar)
        val buscarButton = findViewById<Button>(R.id.btn_buscar)
        val limpiarButton = findViewById<Button>(R.id.btn_limpiar)
        val eliminarButton = findViewById<Button>(R.id.btn_eliminar)

        val lstGeneros = listOf("Pop", "Hip-Hop", "Clásica", "Reggae", "Indie", "Techno", "Cumbia")
        val adaptador = ArrayAdapter(this, android.R.layout.simple_spinner_item, lstGeneros)
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerGenero.adapter = adaptador
        spinnerGenero.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                generoSel = lstGeneros[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        registrarButton.setOnClickListener { registrar() }
        buscarButton.setOnClickListener { buscar() }
        limpiarButton.setOnClickListener { limpiar() }
        eliminarButton.setOnClickListener { eliminar() }
    }

    // Función para registrar el concierto
    private fun registrar() {
        val nombre = inputNombre.text.toString().trim()
        val fecha = inputFecha.text.toString().trim()

        if (nombre.isNotEmpty() && fecha.isNotEmpty()) {
            // Guardar en SharedPreferences
            val editor = sharedPreferences.edit()
            editor.putString("nombre", nombre)
            editor.putString("fecha", fecha)
            editor.putString("genero", generoSel)
            val success = editor.commit() // Usamos commit() en lugar de apply() para asegurarnos de que se guarde inmediatamente

            // Verificación de éxito
            if (success) {
                Toast.makeText(this, "Datos guardados correctamente", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Error al guardar los datos", Toast.LENGTH_SHORT).show()
            }

            limpiar()
            hideKeyboard()
        } else {
            Toast.makeText(this, "Por favor ingrese toda la información", Toast.LENGTH_SHORT).show()
        }
    }

    // Función para buscar un concierto
    private fun buscar() {
        hideKeyboard()

        // Verificar qué texto está capturando el EditText
        val nombreBuscar = inputNombre.text.toString().trim().lowercase()

        Toast.makeText(this, "Nombre en campo capturado: ${inputNombre.text}", Toast.LENGTH_SHORT).show()

        // Si el campo está vacío, pedimos que ingrese algo
        if (nombreBuscar.isEmpty()) {
            Toast.makeText(this, "Por favor ingrese un nombre para buscar", Toast.LENGTH_SHORT).show()
            return // Salimos de la función si está vacío
        }

        // Obtener los datos guardados
        val nombreGuardado = sharedPreferences.getString("nombre", "")?.lowercase()?.trim()
        val fechaGuardada = sharedPreferences.getString("fecha", "")
        val generoGuardado = sharedPreferences.getString("genero", "")

        // Verificación con logs
        Toast.makeText(this, "Nombre ingresado: $nombreBuscar, Guardado: $nombreGuardado", Toast.LENGTH_LONG).show()

        // Comparación exacta
        if (nombreBuscar == nombreGuardado) {
            val intent = Intent(this, DetallesActivity::class.java)
            intent.putExtra("nombre", nombreGuardado)
            intent.putExtra("fecha", fechaGuardada)
            intent.putExtra("genero", generoGuardado)
            startActivity(intent)
        } else {
            Toast.makeText(this, "Concierto no encontrado", Toast.LENGTH_SHORT).show()
        }
    }

    // Función para eliminar un concierto
    private fun eliminar() {
        hideKeyboard()

        val nombreEliminar = inputNombre.text.toString().trim().lowercase()

        // Obtener los datos guardados
        val nombreGuardado = sharedPreferences.getString("nombre", "")?.lowercase()?.trim()

        // Verificación con logs
        Toast.makeText(this, "Nombre ingresado para eliminar: $nombreEliminar, Guardado: $nombreGuardado", Toast.LENGTH_LONG).show()

        // Comparación exacta
        if (nombreEliminar == nombreGuardado) {
            // Eliminar concierto de SharedPreferences
            val editor = sharedPreferences.edit()
            editor.clear() // Elimina todos los datos guardados
            val success = editor.commit() // Usamos commit() para asegurarnos de que los datos se borren inmediatamente

            // Verificación de éxito
            if (success) {
                limpiar()
                Toast.makeText(this, "Concierto eliminado correctamente", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Error al eliminar los datos", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Concierto no encontrado", Toast.LENGTH_SHORT).show()
        }
    }

    // Función para limpiar los campos
    private fun limpiar() {
        inputNombre.text.clear()
        inputFecha.text.clear()
        spinnerGenero.setSelection(0)
    }

    // Función para ocultar el teclado
    private fun hideKeyboard() {
        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }
}
