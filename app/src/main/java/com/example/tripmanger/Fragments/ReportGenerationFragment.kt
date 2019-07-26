package com.example.tripmanger.Fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tripmanger.Adapter.ReportGenerationCustomAdapter
import com.example.tripmanger.DBHelp.DataBaseHelper
import com.example.tripmanger.Model.ReportDetail
import com.example.tripmanger.R

class ReportGenerationFragment(context : Context): Fragment()
{
       var mcontext = context
    var list : ArrayList<ReportDetail> = ArrayList()
    var dataBaseHelper : DataBaseHelper =DataBaseHelper(mcontext)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val  view : View = inflater.inflate(R.layout.layout_report_generation,container,false)

        var bundle : Bundle = arguments!!
        var tripID : Long = bundle.getLong("TRIPID")
        println("Receiving tripID @ReportGenerationFragment::  " + tripID)

        list = dataBaseHelper.calculate(tripID)

       val recyclerviewReplacementTextView   :TextView = view.findViewById(R.id.recyclerviewReplacementTextView) as TextView



            var madapter: ReportGenerationCustomAdapter = ReportGenerationCustomAdapter(mcontext, list)
            var reportGenerationRecyclerView: RecyclerView =
                view.findViewById(R.id.reportGenerationRecyclerView) as RecyclerView


        if(list.isEmpty())
        {
            println("list is empty @recyclerview in report generation fragment")

            reportGenerationRecyclerView.visibility = View.GONE
            recyclerviewReplacementTextView.visibility = View.VISIBLE
            recyclerviewReplacementTextView.setText("No data Available")

        }
            reportGenerationRecyclerView.setHasFixedSize(true)
            reportGenerationRecyclerView.layoutManager = LinearLayoutManager(mcontext)
            reportGenerationRecyclerView.adapter = madapter



        return view;
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }



}