package com.example.tripmanger.Dialog

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatDialogFragment
import com.example.tripmanger.DBHelp.DataBaseHelper
import com.example.tripmanger.Model.TripDetail
import com.example.tripmanger.R
import com.example.tripmanger.Util.Message
import java.util.*

class TripDialog(context: Context, tripDetail: TripDetail) : AppCompatDialogFragment()
{
   val mcontext = context;
    val tripDetailToUpdate : TripDetail = tripDetail;

    val dataBaseHelper : DataBaseHelper = DataBaseHelper(mcontext)
    var datePickerDialog : DatePickerDialog ?=null
    var year : Int = 0;
    var month : Int = 0;
    var date : Int = 0;
    var calendar : Calendar? =null

    lateinit var monInputChangelistener :IOnInputTripChangeListener

    interface IOnInputTripChangeListener
    {
        fun onInputTripChange(tripDetail : TripDetail)
    }
    fun  setInstance(listener:IOnInputTripChangeListener){
        monInputChangelistener = listener;
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog{

        val alertDialogBuilder : AlertDialog.Builder = AlertDialog.Builder(mcontext)

        val view : View = LayoutInflater.from(mcontext).inflate(R.layout.layout_trip_cardview_alertdialog,null)

        val tripNameEditTextCardViewAlertDialog= view.findViewById<EditText>(R.id.tripNameEditTextCardViewAlertDialog)
        val tripLocationEditTextCardViewAlertDialog = view.findViewById<EditText>(R.id.tripLocationEditTextCardViewAlertDialog)

        val tripBudgetEditTextCardViewAlertDialog = view.findViewById<EditText>(R.id.tripBudgetEditTextCardViewAlertDialog)
        val tripDateTextViewCardViewAlertDialog  = view.findViewById<TextView>(R.id.tripDateTextViewCardViewAlertDialog)
        val tripDatePickerImageButtonCardViewAlertDialog  = view.findViewById<ImageButton>(R.id.tripDatePickerImageButtonCardViewAlertDialog)



        tripNameEditTextCardViewAlertDialog.setText(tripDetailToUpdate.tripName,TextView.BufferType.EDITABLE)
        tripLocationEditTextCardViewAlertDialog.setText(tripDetailToUpdate.tripLocation,TextView.BufferType.EDITABLE)
        tripBudgetEditTextCardViewAlertDialog.setText(Integer.toString(tripDetailToUpdate.budget),TextView.BufferType.EDITABLE)



        tripDateTextViewCardViewAlertDialog.text = tripDetailToUpdate.tripDate


        alertDialogBuilder.setView(view).setCancelable(false)
            .setNegativeButton("cancel",DialogInterface.OnClickListener()
            {
                dialogInterface, i ->

                    println("Card View AlertDialog ->No button clicked")


            })
            .setPositiveButton("Update",DialogInterface.OnClickListener()
            {
                dialogInterface, i ->


            println(tripLocationEditTextCardViewAlertDialog.text.toString())
                println(tripDateTextViewCardViewAlertDialog.text.toString())
                println(tripBudgetEditTextCardViewAlertDialog.text.toString())

                tripDetailToUpdate.tripName = tripNameEditTextCardViewAlertDialog.text.toString()
                tripDetailToUpdate.tripLocation =tripLocationEditTextCardViewAlertDialog.text.toString()
                tripDetailToUpdate.tripDate = tripDateTextViewCardViewAlertDialog.text.toString()
                tripDetailToUpdate.budget = Integer.parseInt(tripBudgetEditTextCardViewAlertDialog.text.toString());

                if(tripDetailToUpdate.tripName.equals("") || tripDetailToUpdate.tripLocation.equals("") || tripDetailToUpdate.budget.equals(""))
                {
                    Message.Message(mcontext,"Please fill all details!!!")
                }
                else {
                    dataBaseHelper.updateTripDeatils(tripDetailToUpdate)
                    println("CardView alertDialog ->Updatebutton clicked")
                    monInputChangelistener.onInputTripChange(tripDetailToUpdate)
                }

            })





        tripDatePickerImageButtonCardViewAlertDialog.setOnClickListener (  View.OnClickListener {

            calendar = Calendar.getInstance()

            year = calendar!!.get(Calendar.YEAR)
            month = calendar!!.get(Calendar.MONTH)
            date  = calendar!!.get(Calendar.DAY_OF_MONTH)

            datePickerDialog = DatePickerDialog(mcontext,DatePickerDialog.OnDateSetListener { view,year,month,date ->

                tripDateTextViewCardViewAlertDialog.setText("" + date + "/" + (month+1) + "/" + year);

            },year,month,date)

            datePickerDialog!!.show()


        })


           return alertDialogBuilder.create()

    }


}