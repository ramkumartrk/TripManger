package com.example.tripmanger.Dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.example.tripmanger.R
import com.example.tripmanger.Util.Message


public class AddNewMemberDialog(context: Context) : DialogFragment()
{




    var mcontext :Context = context
    var mlistener : OnNewMemberAddListener?=null

    interface OnNewMemberAddListener
    {
        fun addNewMember(memberName  :String)
    }

    fun SetOnNewMemberAddListener(listener : OnNewMemberAddListener)
    {
        this.mlistener  = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val alertDialogBuilder : AlertDialog.Builder = AlertDialog.Builder(mcontext)


        val view : View = LayoutInflater.from(mcontext).inflate(R.layout.layout_addmember_alertdialog,null)

        val addMemberEditText  : EditText = view.findViewById<EditText>(R.id.addNewMemberEditText) as EditText



        alertDialogBuilder.setView(view).setCancelable(false)
            .setPositiveButton("Add Member",DialogInterface.OnClickListener {
                    dialogInterface, i ->
                println("Add New Member Dialog --> Pressed Yes.. added new member?")

                if(addMemberEditText.text.equals(""))
                {
                    println("You couldn't left Member Name as empty!!!")
                    Message.Message(mcontext,"You couldn't left Member Name as empty!!!")
                }
                else
                {
                    var  memberName : String = addMemberEditText.text.toString()
                    println("New Member Name is...." + memberName)

                    mlistener!!.addNewMember(memberName)




                }
            })

            .setNegativeButton("No",DialogInterface.OnClickListener{
                dialogInterface, i ->
                println("Add New Member dialog -> pressed NO!!!")


            })



        return alertDialogBuilder.create()
    }


}