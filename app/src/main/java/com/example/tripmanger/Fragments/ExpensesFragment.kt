package com.example.tripmanger.Fragments

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tripmanger.Adapter.ExpenseDetailsCustomAdapter
import com.example.tripmanger.DBHelp.DataBaseHelper
import com.example.tripmanger.Dialog.ExpenseDialog
import com.example.tripmanger.Model.ExpenseDetail
import com.example.tripmanger.R
import com.example.tripmanger.Util.Message
import kotlinx.android.synthetic.main.layout_expensesfragment.*
import java.util.*
import kotlin.collections.ArrayList

public class ExpensesFragment(context : Context) : Fragment() ,ExpenseDetailsCustomAdapter.IonItemLongClickListener,ExpenseDetailsCustomAdapter.IeditItemClickListener,ExpenseDialog.IonInputExpenseChange {
    override fun onInputExpenseDetailUpdate(expenseDetail: ExpenseDetail) {

        println("Got updated list :" + expenseDetail.toString())
        madapter.notifyDataSetChanged()
    }


    var tripID: Long = -1
    var mcontext = context
    var expensePayer: String? = null
    val mContext: Context = context;
    lateinit var dataBaseHelper: DataBaseHelper;
    lateinit var madapter: ExpenseDetailsCustomAdapter


    var datePickerDialog: DatePickerDialog? = null
    var calendar: Calendar? = null
    var day: Int = 0
    var month: Int = 0
    var year: Int = 0


    public var expenseDetailsList: ArrayList<ExpenseDetail>? = ArrayList()
    public lateinit var expenseDetail: ExpenseDetail;
    public var listTripMemberNames: List<String> = ArrayList<String>()
    var memberID: Long = 0;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        println("Expenses fragment : OnCreate::")

        setHasOptionsMenu(true)


        var bundle: Bundle = arguments!!;

        tripID = bundle.getLong("TRIPID")
        println("Receiving getLongID() @ExpensesFragment ...." + tripID)


    }





    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.layout_expensesfragment, container, false)

        dataBaseHelper = DataBaseHelper(mContext)
        listTripMemberNames = dataBaseHelper.readMemberNames(tripID)

        println("Expenses fragment : OnCreateView::")

        println("TRIP member names:" + listTripMemberNames.toString())

        val expensePayerSpinner: Spinner = view.findViewById(R.id.expensePayerSpinner)
        val adapter: ArrayAdapter<String> =
            ArrayAdapter<String>(mContext, R.layout.support_simple_spinner_dropdown_item, listTripMemberNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        expensePayerSpinner.adapter = adapter



        expensePayerSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                expensePayer = parent.getItemAtPosition(position).toString()

                var dataBaseHelper: DataBaseHelper = DataBaseHelper(mcontext)
                memberID = dataBaseHelper.readMemberIdByMemberName(expensePayer ?: "Thilagamani")
                Message.Message(mContext, " " + expensePayer + " Clicked having memberId as " + memberID)
            }
        }

        expenseDetailsList = dataBaseHelper.getAllExpenseDetails(tripID)

        madapter = ExpenseDetailsCustomAdapter(mcontext, expenseDetailsList!!)


        val expenseRecyclerView = view.findViewById(R.id.expenseRecyclerView) as RecyclerView
        expenseRecyclerView.setHasFixedSize(true)
        expenseRecyclerView.layoutManager = LinearLayoutManager(mcontext)
        expenseRecyclerView.adapter = madapter
        madapter.SetOnItemLongClickListener(this)

         madapter.SetEditItemOnClickListener(this)


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        println("Expenses fragment : OnViewCreated::")


        expenseDatePickerImageButton.setOnClickListener(View.OnClickListener {

            calendar = Calendar.getInstance()
            year = calendar!!.get(Calendar.YEAR)
            month = calendar!!.get(Calendar.MONTH)
            day = calendar!!.get(Calendar.DAY_OF_MONTH)

            datePickerDialog = DatePickerDialog(mcontext, DatePickerDialog.OnDateSetListener { view, year, month, day ->

                expenseDateTextView.text = " " + day + "/" + (month + 1) + "/" + year;

            }, year, month, day)

            datePickerDialog!!.show()
        })


        addNewExpenseButton.setOnClickListener(View.OnClickListener {

            val expenseName = expenseNameEditText.text.toString()
            val expenseAmount= expenseAmountEditText.text.toString()
            val expenseDate = expenseDateTextView.text.toString()

            if (expenseName.equals("") || expenseAmount.equals("") || expenseAmount.length <1 ) {
                Message.Message(mcontext, "Please fill all details!!!")
            } else {

                var expenseDetail: ExpenseDetail =
                    ExpenseDetail(0, tripID, memberID, expenseName, expensePayer!!, expenseAmount.toFloat(), expenseDate)

                val status: Long = dataBaseHelper.insertExpenseDetails(expenseDetail)


                if (status > 0) {
                    println("Successfully added expense details into sqlite database::" + status)
                    Message.Message(mcontext, " " + "Added successfully!")

                    expenseDetail = dataBaseHelper.getExpenseDetailsByID(status)


                    println("Last inserted - ExpenseDetail List:" + expenseDetail.toString())
                    expenseDetailsList?.add(expenseDetail)
                    madapter.notifyDataSetChanged()

                } else {
                    println("Couldn't add expense details into sqlite database!! please add admin!!")
                    Message.Message(
                        mcontext,
                        " " + "Couldn't add expense details into sqlite database!! please add admin!!"
                    )
                }

                expenseNameEditText.text.clear()
                expenseAmountEditText.text.clear()
            }

        })


    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.menu_main, menu)

        val defaultItem: MenuItem? = menu?.findItem(R.id.action_settings)
        defaultItem?.setVisible(false)

        val assessmentItem: MenuItem? = menu?.findItem(R.id.reportButton)
        assessmentItem?.setVisible(true)

        val addNewExpenseItem: MenuItem = menu.findItem(R.id.addNewExpense)
        addNewExpenseItem.setVisible(false)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {


        val id: Int? = item?.itemId
        println("OnOptionsItemSeelcted workgin it think")

        when (id) {


            R.id.reportButton -> {
                println("OnOptionsItemSeelcted workgin it think!@#$%")

                val reportGenerationFragment: ReportGenerationFragment = ReportGenerationFragment(mcontext)

                val fragmentTransaction: FragmentTransaction? =
                    fragmentManager?.beginTransaction()?.replace(R.id.frameLayoutMainActivity, reportGenerationFragment)

                fragmentTransaction?.addToBackStack("TripManager")

                fragmentTransaction?.commit()


                val bundle: Bundle = Bundle()
                bundle.putLong("TRIPID", tripID)

                reportGenerationFragment.arguments = bundle
            }


        }
        return super.onOptionsItemSelected(item)

    }


    override fun editItemOnClick(position: Int) {
      expenseDetail =   expenseDetailsList!!.get(position)
        println("Inside editItemOnClick: " + expenseDetail.expenseID + " " + expenseDetail.expenseType + " " + expenseDetail.expenseDate + " " + expenseDetail.amount + " " + expenseDetail.payer)

        var expenseDialog : ExpenseDialog = ExpenseDialog(mcontext,expenseDetail)
        expenseDialog.setTargetFragment(this,0)


        expenseDialog.setInstance(this)

        expenseDialog.show(fragmentManager!!,"Expense Dialog")



    }


    override fun onItemLongClick(position: Int): Boolean {

        var alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(mcontext)
        alertDialogBuilder.setMessage("Are you sure, to delte this expense?").setCancelable(false)

            .setPositiveButton("Yes", DialogInterface.OnClickListener()
            { dialogInterface: DialogInterface, i: Int ->

                val currentList: ExpenseDetail = expenseDetailsList!!.get(position)


                if (dataBaseHelper.deleteExpenseDetailsByExpenseID(currentList.expenseID) > 0) {
                    //expenseDetailsList.removeAt(position)

                    println("Successfully deleted expensedetails with given  expense - id:" + currentList.expenseID)
                    expenseDetailsList!!.remove(currentList)
                    madapter.notifyItemRemoved(position)


                } else {
                    println("couldnt delete ExpenseDetails with given id:" + currentList.expenseID)

                }


            })
            .setNegativeButton("No", DialogInterface.OnClickListener()
            { dialogInterface: DialogInterface?, i: Int ->


            })
        alertDialogBuilder.create()
        alertDialogBuilder.show()

        return true;
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    println("Expenses fragment : OnAttach::")
    }


    override fun onDetach() {
        super.onDetach()
        println("Expenses fragment : OnDetach::")

    }

    override fun onStart() {
        super.onStart()
        println("Expenses fragment : OnStart::")
    }

    override fun onResume() {
        super.onResume()
        println("Expenses fragment : OnResume::")
    }


    override fun onDestroy() {
        super.onDestroy()
        println("Expenses fragment : OnDestroy::")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        println("Expenses fragment : OnDestroyView::")
    }

    override fun onStop() {
        super.onStop()
        println("Expenses fragment : OnStop::")
    }

    override fun onPause() {
        super.onPause()
        println("Expenses fragment : OnPause::")
    }


}