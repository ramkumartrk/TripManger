package com.example.tripmanger.Fragments

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tripmanger.Adapter.TripDetailsCustomAdapter
import com.example.tripmanger.DBHelp.DataBaseHelper
import com.example.tripmanger.Dialog.TripDialog
import com.example.tripmanger.MainActivity
import com.example.tripmanger.Model.TripDetail
import com.example.tripmanger.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.layout_trip_details_fragment.*

class TripDetailsFragment (context : Context): Fragment(), TripFormFragment.TripFormFragmentListener, TripDetailsCustomAdapter.OnItemClickListener,TripDetailsCustomAdapter.OnItemLongClickListener,TripDetailsCustomAdapter.IonEditTripClickListener,TripDialog.IOnInputTripChangeListener,MainActivity.IOnBackPressedListener
{


    fun TripDetailsFragment()
    {

    }


    override fun onBackPressed(): Boolean {

        println("BackStackCount::" + activity?.supportFragmentManager?.backStackEntryCount)
        activity?.finish()
        //activity?.supportFragmentManager?.popBackStackImmediate()
        return true
    }

    override fun onInputTripChange(tripDetail: TripDetail) {

        println(list.toString())
        madapter!!.notifyDataSetChanged()
    }

    val  bundle : Bundle  = Bundle()
    val mcontext  : Context = context;
    var madapter : TripDetailsCustomAdapter?=null
    var list : ArrayList<TripDetail>  = ArrayList()
    val dataBaseHelper  : DataBaseHelper = DataBaseHelper(mcontext)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
            }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        println("TripDetailsFragment-onCreateView called...")
        val view : View =  inflater.inflate(R.layout.layout_trip_details_fragment, container,false)

        return view;
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        println("TripDetailsFragment-onViewCreated called...")
        list = dataBaseHelper.readTripDetails() as ArrayList<TripDetail>

        println("TripDetailsFragment - onViewCreated - TRIPCOUNTS: " + dataBaseHelper.countTrips())

        println("TripDetailsFragment -> Got List:" + list.toString())
        madapter = TripDetailsCustomAdapter(mcontext, list)

        recyclerViewTrips.setHasFixedSize(true)
        recyclerViewTrips?.layoutManager = LinearLayoutManager(mcontext)
        madapter!!.SetOnItemClickListener(this)
        madapter!!.SetOnItemLongClickListener(this)
        madapter!!.SetOnEditTripClickListener(this)

        recyclerViewTrips.adapter = madapter

        val fab =  view.findViewById<FloatingActionButton>(R.id.fab) as FloatingActionButton
        fab.show()

        fab.setOnClickListener {
            val tripFormFragment : TripFormFragment =TripFormFragment(mcontext)
            tripFormFragment.setTripFormFragmentListener(this)

            val fragmentTransaction : FragmentTransaction? = fragmentManager?.beginTransaction()?.replace(R.id.frameLayoutMainActivity,tripFormFragment)
            fragmentTransaction?.addToBackStack("TripManager")
            fragmentTransaction?.commit()
        }
    }

    override fun inputLastTripDetail(lastTripDetail : TripDetail) {
        list.add(lastTripDetail)
        println("list added successfully::" + list.toString())
        madapter?.notifyDataSetChanged()
        }


   override fun onItemClick(position: Int) {

       println("Single ItemClick on TripDetails working...")

       val currentList = list.get(position)

       Toast.makeText(mcontext,"TRIPDetails ItemClicked @position::" + currentList.tripID,Toast.LENGTH_SHORT).show()

       // println("CurretnList.TipID:" + currentList.tripID)
       bundle.putLong("TRIPID",currentList.tripID)


       val tripMembersListFragment : TripMembersListFragment = TripMembersListFragment(mcontext)
           tripMembersListFragment.arguments = bundle

       val fragmentTransaction : FragmentTransaction = fragmentManager?.beginTransaction()!!.replace(R.id.frameLayoutMainActivity,tripMembersListFragment)
       fragmentTransaction.addToBackStack("TripManager")
       fragmentTransaction.commit()
   }

    override fun onItemLongClick(position: Int) :Boolean{

        val currentList  = list.get(position)
        Toast.makeText(mcontext," TripDetailsFragment - OnItemLongClick pressed",Toast.LENGTH_SHORT).show()

        var alertDialogBuilder : AlertDialog.Builder =AlertDialog.Builder(mcontext)
        alertDialogBuilder.setMessage("Are you sure do you want talertDo delete this?").setCancelable(false)
            .setPositiveButton("Delete", DialogInterface.OnClickListener()
            {

                dialogInterface : DialogInterface, i : Int ->

                if(dataBaseHelper.deleteExpenseByTripID(currentList.tripID) > 0 || dataBaseHelper.countExpenses(currentList.tripID)==0)
                {
                    println("successfully deleted all expenses details with given id:" + currentList.tripID)

                    if(dataBaseHelper.deleteMemberByTripID(currentList.tripID) > 0 || dataBaseHelper.countMembers(currentList.tripID)==0)
                    {
                        println("Successfully deleted all members details with given id:" + currentList.tripID)

                        if(dataBaseHelper.deleteTripByTripID(currentList.tripID) > 0 || dataBaseHelper.countTrips()==0)
                        {
                            println("Successfully deleted trip with given id:"  + currentList.tripID)
                            list.remove(currentList)
                            madapter?.notifyItemRemoved(position)

                        }
                        else
                        {
                            println("couldnt delete trip details with given id:" + currentList.tripID)
                        }

                    }
                    else
                    {
                        println("coudlnt delete member details with given id:" + currentList.tripID)
                    }

                }
                else
                {
                    println("couldnt delete expense details with given id:" + currentList.tripID)
                }






            })
            .setNegativeButton("No",DialogInterface.OnClickListener()
            {
                dialogInterface: DialogInterface?, i: Int ->

                    println("You clicked No!!")


            })
        alertDialogBuilder.create()
        alertDialogBuilder.show()

        return true

         }


    override fun onEditTripClick(position: Int)
    {
        println("OnEditTripClick works with position:" + position)

      //  var tripIDtoEdit = list.get(position).tripID
        val tripDialog : TripDialog = TripDialog(mcontext,list.get(position))
        tripDialog.setTargetFragment(this,0)
        tripDialog.setInstance(this);
        tripDialog.show(fragmentManager!!,"TRIP DIALOG")
    }


    override fun onResume() {
        println("TripDetailsFragment-onResume called...")
        super.onResume()

        recyclerViewTrips.adapter = madapter
        madapter!!.notifyDataSetChanged()

    }



    override fun onDestroy() {
//        activity?.finish()
        println("TripDetailsFragment-onDestroy called...")
        super.onDestroy()
    }

    override fun onAttach(context: Context) {

        super.onAttach(context)
        println("TripDetailsFragment-onAttach called...")
    }

    override fun onDetach() {
        println("TripDetailsFragment-onDetach called...")
        super.onDetach()
    }

    override fun onPause() {
        println("TripDetailsFragment-onPause called...")
        super.onPause()
    }

    override fun onStart() {

        println("TripDetailsFragment-onStart called...")

        println("TripDetailsFragment - onStart - TRIPCOUNTS: " + dataBaseHelper.countTrips())
        super.onStart()
    }

    override fun onStop() {
        println("TripDetailsFragment-onStopcalled...")
        super.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        println("TripDetailsFragment-onSaveInstanceState called...")
        super.onSaveInstanceState(outState)
    }


    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        println("TripDetailsFragment-onViewStateRestored called...")
        super.onViewStateRestored(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return super.onOptionsItemSelected(item)

    }


}


