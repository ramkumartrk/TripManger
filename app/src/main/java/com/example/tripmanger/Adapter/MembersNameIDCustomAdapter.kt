package com.example.tripmanger.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.tripmanger.Model.TripMemberDetail
import com.example.tripmanger.R

class MembersNameIDCustomAdapter(context : Context,list : ArrayList<TripMemberDetail>): BaseAdapter()
{

    val mcontext : Context = context
    val  mlist : ArrayList<TripMemberDetail> =  list



    override fun getView(postition: Int, view: View?, parent: ViewGroup?): View
    {

        println("MemberNameIDCustomAdapter : getVIEW::" + mlist.toString())
        val currentItem : TripMemberDetail = mlist.get(postition)

        val rowView  : View =  LayoutInflater.from(mcontext).inflate(R.layout.layout_newmembers_listview_model,parent,false)

        val newMemberInListTextView : TextView = rowView.findViewById(R.id.newMemberInListTextView)
        val newMemberIdInListTextView  :TextView  = rowView.findViewById(R.id.newMemberIDInListTextView)
        val newMemberTripIdInListTextView : TextView = rowView.findViewById(R.id.newMemberTripIDInListTextView)

        newMemberIdInListTextView.text = currentItem.memberID.toString()
        newMemberInListTextView.setText(currentItem.memberName)
        newMemberTripIdInListTextView.text = currentItem.tripID.toString()
        return rowView
    }

    override fun getItem(postition: Int): Any
    {
        println("MemberNameIDCustomAdapter : getItem(position)::" + mlist.toString())
                return mlist.get(postition)
    }

    override fun getItemId(postition: Int): Long
    {
            return postition.toLong()
    }

    override fun getCount(): Int
    {
           return mlist.size
    }

}