package com.nilsnahooy.roomdatabasedemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.nilsnahooy.roomdatabasedemo.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.collect
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

        lifecycleScope.launch {
            employeeDao.getAllEmployees().collect{
                val list = ArrayList(it)
                setupDataList(list, employeeDao)
            }
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

    private fun setupDataList(items: ArrayList<EmployeeEntity>, dao: EmployeeDao){
        if(items.isNotEmpty()){
            val itemAdapter = ItemAdapter(items, ::updateItem, ::deleteItem)
            b?.rvRecords?.layoutManager = LinearLayoutManager(this)
            b?.rvRecords?.adapter = itemAdapter
            b?.tvNoRecords?.visibility = View.GONE
            b?.rvRecords?.visibility = View.VISIBLE
        }else{
            b?.tvNoRecords?.visibility = View.VISIBLE
            b?.rvRecords?.visibility = View.GONE
        }
    }

    private fun deleteItem(id: Int){

    }

    private fun updateItem(id: Int){

    }
}