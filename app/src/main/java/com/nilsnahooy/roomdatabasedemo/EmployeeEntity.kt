package com.nilsnahooy.roomdatabasedemo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tblEmployee")
data class EmployeeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "employeeName")
    val name: String = "",
    @ColumnInfo(name = "employeeEmail")
    val email: String = ""
)
