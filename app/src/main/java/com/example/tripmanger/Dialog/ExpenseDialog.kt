package com.example.tripmanger.Dialog

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatDialogFragment
import com.example.tripmanger.DBHelp.DataBaseHelper
import com.example.tripmanger.Model.ExpenseDetail
import com.example.tripmanger.R
import java.util.*
import kotlin.collections.ArrayList

class ExpenseDialog(context : Context,expenseDetail : ExpenseDetail) : AppCompatDialogFragment()
{

    val mcontext : Context = context
    val mExpenseDetail : ExpenseDetail = expenseDetail
    val dataBaseHelper : DataBaseHelper = DataBaseHelper(mcontext)
    var memberNamesList = ArrayList<String>()

    var mInputChangeListener :IonInputExpenseChange?=null;


    var calendar : Calendar?=null
    var month  :Int = 0
    var year : Int = 0
    var date : Int = 0

    var selectedMember : String?= null;

    interface IonInputExpenseChange
    {
        fun onInputExpenseDetailUpdate(expenseDetail: ExpenseDetail)
    }

    fun setInstance(listener: IonInputExpenseChange)
    {
        mInputChangeListener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val alertDialogBuilder : AlertDialog.Builder =AlertDialog.Builder(mcontext)

        var view : View = LayoutInflater.from(mcontext).inflate(R.layout.layout_expense_update_alertdialog,null)


       var expenseAlerDialogSpinner =  view.findViewById<Spinner>(R.id.expenseAlertDialogSpinner) as Spinner
       var expenseNameAlertDialogEditText =  view.findViewById<EditText>(R.id.expenseNameAlertDialogEditText)
       var expenseAmountAlertDialogEditText =  view.findViewById<EditText>(R.id.expenseAmountAlertDialogEditText)
       var expenseDateAlertDialogTextView  =  view.findViewById<TextView>(R.id.expenseDateAlertDialogTextView)
       var expenseDateAlertDialogImageButton =  view.findViewById<ImageButton>(R.id.expenseDateAlertDialogImageButton)


        expenseNameAlertDialogEditText.setText(mExpenseDetail.expenseType)
        expenseAmountAlertDialogEditText.setText(mExpenseDetail.amount.toString())
        expenseDateAlertDialogTextView.setText(mExpenseDetail.expenseDate)



        memberNamesList = dataBaseHelper.readMemberNames(mExpenseDetail.tripID)

        val adapter : ArrayAdapter<String> = ArrayAdapter<String>(mcontext,R.layout.support_simple_spinner_dropdown_item,memberNamesList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        expenseAlerDialogSpinner.adapter =adapter


        expenseAlerDialogSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener
        {

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(adapter: AdapterView<*>?, view: View?, position: Int, id: Long) {

                 selectedMember = adapter?.getItemAtPosition(position).toString()

                mExpenseDetail.memberID = dataBaseHelper.readMemberIdByMemberName(selectedMember!!)
                println("Selected Member is....." + selectedMember)

            }
        }


        expenseDateAlertDialogImageButton.setOnClickListener {
            calendar = Calendar.getInstance()
            year = calendar!!.get(Calendar.YEAR)
            month = calendar!!.get(Calendar.MONTH)
            date = calendar!!.get(Calendar.DAY_OF_MONTH)

            var datePickerDialog = DatePickerDialog(mcontext,DatePickerDialog.OnDateSetListener { view,year,month,date ->
                expenseDateAlertDialogTextView.setText(" " + date + "/" + month + "/" + year)
            },year,month,date)

            datePickerDialog!!.show()

        }




    alertDialogBuilder.setView(view).setCancelable(false)
        .setPositiveButton("Update",DialogInterface.OnClickListener {
                dialogInterface, i ->

            mExpenseDetail.expenseType = expenseNameAlertDialogEditText.text.toString()
            mExpenseDetail.expenseDate = expenseDateAlertDialogTextView.text.toString()
            mExpenseDetail.amount = expenseAmountAlertDialogEditText.text.toString().toFloat()
            mExpenseDetail.payer = selectedMember.toString();

            if(mExpenseDetail.expenseType.equals("") || mExpenseDetail.amount.equals(0) || mExpenseDetail.payer.equals(""))
            {
                com.example.tripmanger.Util.Message.Message(mcontext,"Please fill all details! You cant left anything empty")
            }

            else {
                if (dataBaseHelper.updateExpenseDetailsByExpenseID(mExpenseDetail) > 0) {

                    println("Successfully updated")
                    mInputChangeListener?.onInputExpenseDetailUpdate(mExpenseDetail)

                }
                else {
                    println("not successfully updated! please contact admin!!")
                }
                println("Expense Detail update button clicked!!!")
            }
        })
        .setNegativeButton("No",DialogInterface.OnClickListener {
                dialogInterface, i ->  })

        return alertDialogBuilder.create()
    }
}
