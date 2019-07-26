package com.example.tripmanger.Fragments

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.example.tripmanger.DBHelp.DataBaseHelper
import com.example.tripmanger.Model.TripDetail
import com.example.tripmanger.R
import com.example.tripmanger.Util.Message
import kotlinx.android.synthetic.main.layout_tripformfragment.*
import java.util.*


public class TripFormFragment(context : Context) : Fragment() {

    val mcontext  :Context = context;
    var listner : TripFormFragmentListener? = null
    var datePickerDialog : DatePickerDialog? = null;

    var year : Int =0;
    var month : Int = 0;
    var day : Int = 0;
    var calendar : Calendar? = null;




    public interface TripFormFragmentListener {
        fun inputLastTripDetail(list : TripDetail)
    }

    fun setTripFormFragmentListener(mlistener: TripDetailsFragment) { listner = mlistener; }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view : View =  inflater.inflate(R.layout.layout_tripformfragment,container,false)


         return view;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


//
//        val fab = activity?.findViewById<FloatingActionButton>(R.id.fab) as FloatingActionButton
//        fab.hide()

        val  datePickerButton = view.findViewById(R.id.datePickerButton) as ImageButton

        datePickerButton.setOnClickListener(View.OnClickListener {

            println("DatePickerButton Clicked!!!!")
            Message.Message(mcontext,"Date Picker Button Clicked!!!")
            calendar = Calendar.getInstance()

            year = calendar!!.get(Calendar.YEAR)
            month = calendar!!.get(Calendar.MONTH)
            day = calendar!!.get(Calendar.DAY_OF_MONTH)

            datePickerDialog = DatePickerDialog(mcontext,DatePickerDialog.OnDateSetListener { View,year,month,day ->

                tripDateTextView.text = " " + day + "/" + (month+1) + "/" + year;

            },year,month,day)


            datePickerDialog!!.show()
        })

        tripFormSubmit.setOnClickListener(View.OnClickListener {

            val dataBaseHelper: DataBaseHelper = DataBaseHelper(mcontext)



            /*....................Fetching Input From UI....................*/

            val tripName = tripNameEditText.text.toString()
            val location = locationEditText.text.toString()


            var budget = 0
            try {
                budget = Integer.parseInt(budgetEditText.text.toString())
            } catch (exception: Exception) {
                println(exception)
            }
            val tripDate = tripDateTextView.text.toString()

            /*.................................................................*/


            if(tripName.equals("") || location.equals("") || budget.equals(""))
            {
                Message.Message(mcontext,"Please fill all details!")
            }

            else {
                val tripDetail = TripDetail(0, tripName, location, budget, tripDate)

                var status = dataBaseHelper.insertTripDetails(tripDetail)


                if (status > 0) {

                    // var tripDetailsFragment = TripDetailsFragment(mcontext)
                    //tripDetailsFragment.list.add()

                    val tripDetailLastInserted = dataBaseHelper.readTripDetailByID(status)
                    println("TripDetailLastInserted:" + tripDetailLastInserted);

                    if (listner != null) {
                        println("listener initialized successfully..not null")
                        if (tripDetailLastInserted != null) {
                            listner!!.inputLastTripDetail(tripDetailLastInserted)
                        }
                    }

                }


                if (fragmentManager!!.backStackEntryCount > 0) {
                    println("popBackStack()  is working i think!!!" + fragmentManager!!.backStackEntryCount)

                    activity?.supportFragmentManager?.popBackStackImmediate()

                    println("fragmentManager!!.popBackStackImmediate() not yet all working properly")

                } else {
                    println("PopBackStack may not be working properly")
                }
            }

//            else
//            {
//                Message.Message(
//                    mcontext,
//                    "Something Went Wrong! Please Contact Admin!"
//                )
//            }
        })
    }



}
