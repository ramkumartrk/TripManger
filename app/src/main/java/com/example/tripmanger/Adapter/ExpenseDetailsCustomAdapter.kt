package com.example.tripmanger.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tripmanger.Model.ExpenseDetail
import com.example.tripmanger.R

class ExpenseDetailsCustomAdapter(context : Context, list : ArrayList<ExpenseDetail> ):RecyclerView.Adapter<ExpenseDetailsCustomAdapter.ViewHolder>()
{
    var mlist : ArrayList<ExpenseDetail>  = list;
    var mcontext = context;
    var mlistener : IonItemLongClickListener?=null
    var meditListener : IeditItemClickListener ?= null




    interface  IonItemLongClickListener {
        fun onItemLongClick(position: Int) :Boolean
    }


    fun SetOnItemLongClickListener(listener :ExpenseDetailsCustomAdapter.IonItemLongClickListener){
        mlistener = listener
    }


    interface IeditItemClickListener{
        fun editItemOnClick(position: Int)
    }


    fun SetEditItemOnClickListener(listener: ExpenseDetailsCustomAdapter.IeditItemClickListener) {
        meditListener = listener
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        val view : View = LayoutInflater.from(mcontext).inflate(R.layout.layout_expense_cardview,parent,false)
        println("Expense List::" + mlist.toString())

            return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mlist.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentList = mlist.get(position)
        println("Expense current List::" + currentList.toString())
        holder.payerTextView.text = currentList.payer
        holder.expenseNameTextView.text  = currentList.expenseType
        holder.amountTextView.text = currentList.amount.toString()
        holder.expenseIDTextView.text  = currentList.expenseID.toString()
        holder.expenseDateTextView.text = currentList.expenseDate.toString()

    }


    inner class ViewHolder(itemView : View): RecyclerView.ViewHolder(itemView) {

        val payerTextView = itemView.findViewById<TextView>(R.id.cardViewPayerTextView)
        val expenseNameTextView = itemView.findViewById<TextView>(R.id.cardViewExpenseNameTextView)
        val amountTextView = itemView.findViewById<TextView>(R.id.cardViewAmountTextView)
        val expenseIDTextView = itemView.findViewById<TextView>(R.id.cardViewExpenseIDTextView)
        val expenseDateTextView = itemView.findViewById<TextView>(R.id.cardViewExpenseDateTextView)
        val editImageButton = itemView.findViewById<ImageButton>(R.id.cardViewExpenseEditImageButton)



        init {
            expenseIDTextView.visibility = View.GONE
            itemView.setOnLongClickListener {

                mlistener?.onItemLongClick(adapterPosition)!!
            }


            editImageButton.setOnClickListener {
                meditListener?.editItemOnClick(adapterPosition)

            }



        }

    }
}