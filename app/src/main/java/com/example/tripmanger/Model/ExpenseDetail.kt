package com.example.tripmanger.Model

data class ExpenseDetail(var expenseID : Long ,var tripID: Long,var memberID : Long,var expenseType: String,var payer:String,var amount:Float,var expenseDate : String)

