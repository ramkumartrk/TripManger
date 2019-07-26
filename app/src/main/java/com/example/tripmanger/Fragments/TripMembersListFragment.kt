package com.example.tripmanger.Fragments

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.tripmanger.Adapter.MembersNameIDCustomAdapter
import com.example.tripmanger.DBHelp.DataBaseHelper
import com.example.tripmanger.Dialog.AddNewMemberDialog
import com.example.tripmanger.Dialog.MemberDialog
import com.example.tripmanger.Model.TripMemberDetail
import com.example.tripmanger.R
import kotlinx.android.synthetic.main.layout_trip_members_list_fragment.*

class TripMembersListFragment (context : Context): Fragment(),MemberDialog.IonMemberNameChangedListener,AddNewMemberDialog.OnNewMemberAddListener
{


    var mcontext = context

    var mlist : ArrayList<TripMemberDetail> = ArrayList()


  // var list : ArrayList<MemberIDNameSpinner> = ArrayList()

    var dataBaseHelper : DataBaseHelper = DataBaseHelper(mcontext)

     var  tripID : Long=1

    var adapter : MembersNameIDCustomAdapter?=null


    override fun addNewMember(memberName: String)
    {
        var tripMemberDetail : TripMemberDetail = TripMemberDetail(0,tripID,memberName)

        var status  :Long = dataBaseHelper.insertMemberDetails(tripMemberDetail)

        if(status > 0)
        {
           tripMemberDetail =  dataBaseHelper.readMemberDetailByID(status)

            mlist.add(tripMemberDetail)
            adapter?.notifyDataSetChanged()
        }
        else
        {
            println("couldn't add TripMemberDetails!! Please contact Admin!")
        }

    }



    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
       // activity?.setActionBar(members_list_ToolBar)
        var bundle  : Bundle?  = arguments;
        tripID = bundle!!.getLong("TRIPID")


        println("Receiving  in OnViewCreated -> getLongID() ...." + tripID + " " + mlist.toString())

/*read all memberdetails right?*/

        //mlist.add(dataBaseHelper.readMemberDetailByID(tripID))

        mlist= dataBaseHelper.readMemberDetailsByTripID(tripID) as ArrayList<TripMemberDetail>

        //list = dataBaseHelper.readMemberNames(tripID = tripID)
        println("TripMembersListFragment : OnCreate :: " + mlist.toString())


    }




    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.layout_trip_members_list_fragment,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        println("TripMembersListFragment : onViewCreated ::" + mlist.toString())
        adapter = MembersNameIDCustomAdapter(mcontext,mlist)



        membersListView.adapter = adapter


        membersListView.setOnItemLongClickListener { adapterView, view, position, id ->
            val alertDialogBuilder :AlertDialog.Builder = AlertDialog.Builder(mcontext)
            alertDialogBuilder.setMessage("Are you sure to delete this member?").setCancelable(false)
                .setPositiveButton("Yes",DialogInterface.OnClickListener()
                {
                        dialogInterface: DialogInterface?, i: Int ->

                    println("yes clicked to delete member")

                  //  var memberID : Long = dataBaseHelper.readMemberIdByMemberName(list.get(position))

                    var memberID : Long = mlist.get(position).memberID
                    if( dataBaseHelper.deleteMemberByMemberID(memberID) > 0)
                    {
                        println("delete member by member id works fine:"  + memberID)
                    }
                    else
                    {
                        println("couldnt delete member by member id ::"  + memberID)


                    }
                    if(dataBaseHelper.deleteExpenseByMemberID(memberID)>0)
                    {
                        println("delete expense by member id works fine:" + memberID)
                    }
                    else
                    {
                        println("coudlt delete expense by member id::"  + memberID)

                    }

                    mlist.removeAt(position)
                    adapter!!.notifyDataSetChanged()



                })
                .setNegativeButton("No",DialogInterface.OnClickListener()
                {
                        dialogInterface: DialogInterface?, i: Int ->

                    println("No clicked to delete member")
                })



            alertDialogBuilder.create()
            alertDialogBuilder.show()


            true;

        }


        membersListView.setOnItemClickListener { parent, view, position, id ->

            var memberDialog : MemberDialog = MemberDialog(mcontext,mlist.get(position))

            memberDialog.setTargetFragment(this,0)
            memberDialog.setInstance(this)
            memberDialog.show(fragmentManager!!,"Member Dialog")

            println("MemberListView onItemClickListener works fine!!!")
        }





//    newMemberAddButton.setOnClickListener(View.OnClickListener {
//
//        val  newMemberEditText = view.findViewById<EditText>(R.id.newMemberEditText)
//
//        val tripMemberNameEditText : String = newMemberEditText.text.toString()
//
//        println("What is entered in edit text box is:" + tripMemberNameEditText)
//
//        if(tripMemberNameEditText.equals(""))
//        {
//            Message.Message(mcontext,"Member name can't be left empty")
//
//        }
//        else {
//
//             var tripMemberDetail :TripMemberDetail= TripMemberDetail(0, tripID, tripMemberNameEditText)
//
//            val status = dataBaseHelper.insertMemberDetails(tripMemberDetail)
//
//            println("Trip Members List Fragment - status::" + status)
//            if (status > 0) {
//
//                tripMemberDetail = dataBaseHelper.readMemberDetailByID(status)
//
//
//                mlist.add(TripMemberDetail(status,tripID,tripMemberNameEditText))
//
//
//
//
//                //println("Got list?????:::" + tripMemberDetail)
////                list.add(tripMemberDetail.memberName)
////                println("list added successfully")
//
//                adapter!!.notifyDataSetChanged()
//                println("adapter changed...i hope so....")
//
//            } else {
//                Message.Message(mcontext, " Please contact Admin!!!")
//            }
//
//
//            println(" Got: " + newMemberEditText.text.toString() + " working ")
//
//            newMemberEditText.text.clear()
//        }
//            }) // NewMemberAdd - Button Click
//
//
//

        addNewMemberFloatingButton.setOnClickListener(View.OnClickListener {


            var addNewMemberDialog : AddNewMemberDialog = AddNewMemberDialog(mcontext)
            addNewMemberDialog.setTargetFragment(this,0)
            addNewMemberDialog.SetOnNewMemberAddListener(this)
            addNewMemberDialog.show(fragmentManager!!,"Add New Member Dialog")
            //addNewMemberEditText.text.clear()


        })

       // addExpenseButtonInMembersListFrag.setOnClickListener(View.OnClickListener {
//
//            val expensesFragment : ExpensesFragment = ExpensesFragment(mcontext)
//
//
//            val bundle : Bundle =Bundle()
//            println("tripID value within TripMembersListFragment:" + tripID + " ")
//            bundle.putLong("TRIPID",tripID)
//            println("Bundle value from MemberListFragment : " + bundle.getLong("TRIPID")  + " ")
//            expensesFragment.arguments = bundle
//
//            val fragmentTransaction : FragmentTransaction? = fragmentManager?.beginTransaction()?.replace(R.id.frameLayoutMainActivity,expensesFragment)
//            fragmentTransaction?.addToBackStack("TripManager")
//
//            fragmentTransaction?.commit()

        //})



    }

    override fun onMemberNameChanged(memberDetailToUpdate: TripMemberDetail) {
        println("Adapter.notifyDataSetChanged works fine for TripMembersListFragment...")
        adapter!!.notifyDataSetChanged()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_main,menu)

        val defaultItem : MenuItem = menu.findItem(R.id.action_settings)
        defaultItem.setVisible(false)

        val addNewExpenseItem  : MenuItem = menu.findItem(R.id.addNewExpense)
        addNewExpenseItem.setVisible(true)

        val assessmentItem : MenuItem = menu.findItem(R.id.reportButton)
        assessmentItem.setVisible(false)


    }




    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id : Int = item.itemId

        when(id)
        {
            R.id.addNewExpense ->
            {
                val bundle : Bundle =Bundle()
                println("tripID value within TripMembersListFragment:" + tripID + " ")
                bundle.putLong("TRIPID",tripID)
                println("Bundle value from MemberListFragment : " + bundle.getLong("TRIPID")  + " ")


                val expensesFragment : ExpensesFragment = ExpensesFragment(mcontext)
                expensesFragment.arguments = bundle



                val fragmentTransaction : FragmentTransaction = fragmentManager!!.beginTransaction()?.replace(R.id.frameLayoutMainActivity,expensesFragment)
                fragmentTransaction.addToBackStack("Trip Manager")
                fragmentTransaction.commit()

            }

        }



        return super.onOptionsItemSelected(item)
    }
}