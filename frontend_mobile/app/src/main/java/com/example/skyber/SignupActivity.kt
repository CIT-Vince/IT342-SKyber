package com.example.skyber

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.skyber.viewmodel.AuthViewModel
import java.util.*

class SignupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup)
        val viewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        //Input fields
        val editTextDate = findViewById<EditText>(R.id.editTextDate)
        val firstName = findViewById<EditText>(R.id.firstname).text.toString()
        val lastName = findViewById<EditText>(R.id.lastname).text.toString()

        val emailEditText = findViewById<EditText>(R.id.email)
        val email = emailEditText.text.toString()

        editTextDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(this,
                { _, selectedYear, selectedMonth, selectedDay ->
                    val date = "${selectedYear}-${selectedMonth + 1}-${selectedDay}"
                    editTextDate.setText(date)
                }, year, month, day
            )
            datePickerDialog.show()
        }
    }
}