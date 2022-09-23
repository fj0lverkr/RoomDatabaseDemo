package com.nilsnahooy.roomdatabasedemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.nilsnahooy.roomdatabasedemo.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private var b:ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityMainBinding.inflate(layoutInflater)
        val employeeDao = (application as EmployeeApp).db.employeeDao()
        setContentView(b?.root)

        b?.btnAddRecord?.setOnClickListener{
            addRecord(employeeDao)
        }
    }

    private fun addRecord(employeeDao: EmployeeDao){
       val name = b?.etInputName?.text.toString()
       val email =  b?.etInputMail?.text.toString()

        if (name.isNotEmpty() && email.isNotEmpty()){
            lifecycleScope.launch{
                employeeDao.insert(EmployeeEntity(name=name, email=email))
            }
            Toast.makeText(applicationContext, "Record saved!", Toast.LENGTH_LONG).show()
            b?.etInputName?.text?.clear()
            b?.etInputMail?.text?.clear()
        } else {
            Toast.makeText(applicationContext, "Fields cannot be blank.", Toast.LENGTH_LONG)
                .show()
        }
    }
}