package com.example.tripmanger.Dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatDialogFragment
import com.example.tripmanger.DBHelp.DataBaseHelper
import com.example.tripmanger.Model.TripMemberDetail
import com.example.tripmanger.R
import com.example.tripmanger.Util.Message

class MemberDialog(context  :Context,memberDetail: TripMemberDetail) : AppCompatDialogFragment()
{
    val memberDetailToUpdate : TripMemberDetail = memberDetail;
        var mcontext = context

    var mlistener : IonMemberNameChangedListener?=null

    val dataBaseHelper : DataBaseHelper = DataBaseHelper(mcontext)



    interface IonMemberNameChangedListener
    {
        fun onMemberNameChanged(memberDetailToUpdate  :TripMemberDetail)
    }

    fun setInstance( listener : IonMemberNameChangedListener)
    {
         mlistener =  listener
    }



    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        println(" " + memberDetailToUpdate.tripID + "  " + memberDetailToUpdate.memberID + " " + memberDetailToUpdate.memberName)

        val alertDialogBuilder :AlertDialog.Builder = AlertDialog.Builder(mcontext)


        val view : View =  LayoutInflater.from(mcontext).inflate(R.layout.layout_membersname_update_alertdialog,null)

        val editMemberNameAlertDialogEditText : EditText = view.findViewById(R.id.editMemberNameAlertDialogEditText) as EditText
        editMemberNameAlertDialogEditText.setText(memberDetailToUpdate.memberName)



        alertDialogBuilder.setView(view).setCancelable(false)
            .setPositiveButton("Update",DialogInterface.OnClickListener {
                    dialogInterface, i ->

                if(editMemberNameAlertDialogEditText.text.equals(""))
                {
                    Message.Message(mcontext,"MemberName couldnt be left empty!!!")
                }
                else {

                    memberDetailToUpdate.memberName = editMemberNameAlertDialogEditText.text.toString()
                    if (dataBaseHelper.updateMemberDetails(memberDetailToUpdate) > 0)
                        Message.Message(mcontext, "Successfully Updated")
                    else {
                        Message.Message(mcontext, "problem in updating membername in memberTable")
                        println("problem in updating membername in memberTable")
                    }

                    if(dataBaseHelper.updateExpenseDetailsByMemberID(memberDetailToUpdate) > 0)
                        Message.Message(mcontext,"successfully updated")
                    else
                    {
                        Message.Message(mcontext,"problem in updating membername in expense table")
                        println("problem in updating membername in expense table")
                    }
                    mlistener?.onMemberNameChanged(memberDetailToUpdate)

                }
            })
            .setNegativeButton("No",DialogInterface.OnClickListener {
                    dialogInterface, i ->

            })
        return alertDialogBuilder.create()

    }

}