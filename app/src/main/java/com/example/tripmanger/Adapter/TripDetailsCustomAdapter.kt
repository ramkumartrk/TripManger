package com.example.tripmanger.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tripmanger.Model.TripDetail
import com.example.tripmanger.R

class TripDetailsCustomAdapter (context : Context, list : ArrayList<TripDetail>): RecyclerView.Adapter<TripDetailsCustomAdapter.ViewHolder>()
{
    var  mlist : List<TripDetail> = ArrayList<TripDetail>()
    val mcontext  = context;
    lateinit var mClickListener : OnItemClickListener

    lateinit var mLongClickListener: OnItemLongClickListener

    lateinit var mOnEditTripClickListener: IonEditTripClickListener


    interface OnItemClickListener {
        fun onItemClick(position : Int)

    }

    interface  OnItemLongClickListener
    {
        fun onItemLongClick(position: Int) : Boolean
    }

    interface IonEditTripClickListener
    {
        fun onEditTripClick(position: Int)
    }

    fun SetOnItemClickListener(listener: OnItemClickListener) {
        println("single click Listener sets successfully")
        mClickListener = listener
    }

    fun SetOnItemLongClickListener(listener : OnItemLongClickListener)
    {
        println("double click lsitener sets successfully")
        mLongClickListener = listener
    }

    fun SetOnEditTripClickListener(listener: IonEditTripClickListener)
    {
        mOnEditTripClickListener = listener
    }

    init { mlist = list }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view : View = LayoutInflater.from(mcontext).inflate(R.layout.layout_trip_details_cardview,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int { return mlist.size }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentList = mlist.get(position)

        holder.tripNameView.text = currentList.tripName
        holder.tripLocationView.text = currentList.tripLocation
        holder.budgetView.text = currentList.budget.toString()
        holder.tripDateView.text = currentList.tripDate
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

            val tripNameView = itemView.findViewById<TextView>(R.id.tripNameTextView)
            val tripLocationView = itemView.findViewById<TextView>(R.id.tripLocationTextView)
            val tripDateView = itemView.findViewById<TextView>(R.id.tripDateTextView)
            val budgetView  = itemView.findViewById<TextView>(R.id.budgetTextView)
            val editTripCardViewImageButton = itemView.findViewById<ImageButton>(R.id.editTripCardViewImageButton)

            init {

                itemView.setOnLongClickListener(View.OnLongClickListener {
                    mLongClickListener.onItemLongClick(adapterPosition)
                })


                itemView.setOnClickListener( View.OnClickListener {

                    if(mClickListener !=  null) {
                        //println("Listener.OnItemClick (adapterPosition) sets successfully")
                        mClickListener.onItemClick(adapterPosition)

                    }
                })


                editTripCardViewImageButton.setOnClickListener {
                    mOnEditTripClickListener.onEditTripClick(adapterPosition)
                }




            }
    }
}