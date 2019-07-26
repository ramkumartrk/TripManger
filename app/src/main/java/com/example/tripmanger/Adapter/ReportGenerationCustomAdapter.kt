package com.example.tripmanger.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tripmanger.Model.ReportDetail
import com.example.tripmanger.R

class ReportGenerationCustomAdapter (context : Context, list : ArrayList<ReportDetail>): RecyclerView.Adapter<ReportGenerationCustomAdapter.ViewHolder>()
{
    var mcontext  : Context = context
    var mlist : ArrayList<ReportDetail> = list

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view : View = LayoutInflater.from(mcontext).inflate(R.layout.layout_report_generation_cardview,parent,false)
        return ViewHolder(view)

    }

    override fun getItemCount(): Int {
     return mlist.size
     }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

            val reportDetail  =mlist.get(position)
            holder.reportTextView.text =reportDetail.reports.toString()
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView)
    {
            var reportTextView  = itemView.findViewById<TextView>(R.id.reportTextView)
    }
}