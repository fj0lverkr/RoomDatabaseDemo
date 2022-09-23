package com.nilsnahooy.roomdatabasedemo

import android.app.AlertDialog
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.nilsnahooy.roomdatabasedemo.databinding.ActivityMainBinding
import com.nilsnahooy.roomdatabasedemo.databinding.DialogUpdateBinding
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private var b:ActivityMainBinding? = null
    private lateinit var employeeDao: EmployeeDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityMainBinding.inflate(layoutInflater)
        employeeDao = (application as EmployeeApp).db.employeeDao()
        setContentView(b?.root)

        b?.btnAddRecord?.setOnClickListener{
            addRecord()
        }

        lifecycleScope.launch {
            employeeDao.getAllEmployees().collect{
                val list = ArrayList(it)
                setupDataList(list)
            }
        }
    }

    private fun addRecord(){
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

    private fun setupDataList(items: ArrayList<EmployeeEntity>){
        if(items.isNotEmpty()){
            val itemAdapter = ItemAdapter(items, ::showUpdateItemDialog, ::showDeleteItemDialog)
            b?.rvRecords?.layoutManager = LinearLayoutManager(this)
            b?.rvRecords?.adapter = itemAdapter
            b?.tvNoRecords?.visibility = View.GONE
            b?.rvRecords?.visibility = View.VISIBLE
        }else{
            b?.tvNoRecords?.visibility = View.VISIBLE
            b?.rvRecords?.visibility = View.GONE
        }
    }

    private fun showDeleteItemDialog(id: Int){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete Record")
        builder.setIcon(android.R.drawable.ic_dialog_alert)
        builder.setCancelable(false)
        builder.setPositiveButton("Yes"){dialogInterface, _ ->
            lifecycleScope.launch {
                employeeDao.delete(EmployeeEntity(id))
                Toast.makeText(applicationContext, "Record deleted.", Toast.LENGTH_LONG).show()
            }
            dialogInterface.dismiss()
        }
        builder.setNegativeButton("No"){dialogInterface, _ ->
            dialogInterface.dismiss()
        }
        val alertDialog = builder.create()
        alertDialog.show()
    }


    private fun showUpdateItemDialog(id: Int){
        val updateDialog = Dialog(this, androidx.appcompat.R.style.Theme_AppCompat_Dialog)
        updateDialog.setCancelable(false)
        val dB = DialogUpdateBinding.inflate(layoutInflater)
        updateDialog.setContentView(dB.root)

        lifecycleScope.launch {
            employeeDao.getEmployeeById(id).firstOrNull().also{
                dB.etInputName.setText(it?.name)
                dB.etInputMail.setText(it?.email)
            }
        }

        dB.btnUpdate.setOnClickListener {
            val name = dB.etInputName.text.toString()
            val email = dB.etInputMail.text.toString()
            if (name.isNotEmpty() && email.isNotEmpty()){
                lifecycleScope.launch{
                    employeeDao.getEmployeeById(id).firstOrNull().also{
                        employeeDao.update(EmployeeEntity(id, name, email))
                    }
                }
                Toast.makeText(applicationContext, "Record updated.",
                    Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(applicationContext, "Fields cannot be empty.",
                    Toast.LENGTH_LONG).show()
            }
            updateDialog.dismiss()
        }

        dB.btnCancel.setOnClickListener { updateDialog.dismiss() }
        updateDialog.show()
    }
}