package com.example.tripmanger.DBHelp
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import com.example.tripmanger.Model.*
import com.example.tripmanger.Util.Message
import java.util.*
import kotlin.collections.ArrayList

class DataBaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    val mcontext: Context = context;
   lateinit var cursor : Cursor
    lateinit var cursor1: Cursor
    lateinit var cursor2: Cursor
    lateinit var cursor3: Cursor
    lateinit var cursor4: Cursor


    companion object {
        val DATABASE_NAME: String = "TripManagerDB.db";
        var DATABASE_VERSION: Int = 4

        val TRIP_TABLE_NAME: String = "TripTable"
        val TRIP_MEMBERS_TABLE_NAME: String = "TripMembersTable"
        val TRIP_EXPENSE_TABLE_NAME: String = "TripExpenseTable"


        /*TRip_TABLE*/
        val TRIPID: String = "TRIPID"
        val TRIPNAME: String = "TRIPNAME"
        val TRIPLOCATION: String = "TRIPLOCATION"
        val BUDGET: String = "BUDGET"
        val TRIPDATE: String = "TRIPDATE"


        /*TRIP_MEMBERS_TABLE*/
        val MEMBERID: String = "MEMBERID"
        val MEMBERNAME: String = "MEMBERNAME"


        /*TRIP_EXPENSE_TABLE*/
        val EXPENSEID: String = "EXPENSEID"
        val EXPENSENAME: String = "EXPENSENAME"
        val PAYER: String = "PAYER"
        val AMOUNT: String = "AMOUNT"
        var EXPENSEDATE : String = "EXPENSEDATE"


        /*Creatiion  & Deletion tables - Queries*/

        val CREATE_TRIP_TABLE =
            "CREATE TABLE " + TRIP_TABLE_NAME + "(" + TRIPID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TRIPNAME + " VARCHAR(100), " + TRIPLOCATION + " VARCHAR(100), " +
                    BUDGET + " INTEGER," + TRIPDATE + " STRING)"

        val CREATE_MEMBERS_TABLE =
            "CREATE TABLE " + TRIP_MEMBERS_TABLE_NAME + "(" + MEMBERID + " INTEGER PRIMARY KEY AUTOINCREMENT," + TRIPID + " INTEGER," + MEMBERNAME + " VARCHAR(100)," + " CONSTRAINT fk_constraints_tripid FOREIGN KEY " + "(" + TRIPID + ") REFERENCES " + TRIP_TABLE_NAME + "(" + TRIPID + "))"


        val CREATE_EXPENSE_TABLE =
            "CREATE TABLE " + TRIP_EXPENSE_TABLE_NAME + "(" + EXPENSEID + " INTEGER PRIMARY KEY AUTOINCREMENT," + TRIPID + " INTEGER," + MEMBERID + " VARCHAR(100)," + EXPENSENAME + " VARCHAR(100)," + PAYER + " VARCHAR(100)," + AMOUNT + " REAL," + EXPENSEDATE + " STRING," + " CONSTRAINT fk_constraints_tripid FOREIGN KEY " + "(" + TRIPID + ") REFERENCES " + TRIP_TABLE_NAME + "(" + TRIPID + ")," + " CONSTRAINT fk_constraints_memberid FOREIGN KEY " + "(" + MEMBERID + ")REFERENCES " + TRIP_MEMBERS_TABLE_NAME + "(" + MEMBERID + "))"


        val DROP_TRIP_TABLE = " DROP TABLE " + TRIP_TABLE_NAME + " IF EXISTS"


        val DROP_MEMBERS_TABLE = " DROP TABLE " + TRIP_MEMBERS_TABLE_NAME + " IF EXISTS"

        val DROP_EXPENSE_TABLE = " DROP TABLE " + TRIP_EXPENSE_TABLE_NAME + " IF EXISTS"

    }


    override fun onCreate(sqlitedatabase: SQLiteDatabase?) {
        try {
            Message.Message(mcontext, "SQLite-OnCreate:TRIP:Table Created successfully")

            sqlitedatabase?.execSQL(CREATE_TRIP_TABLE)

        } catch (exception: Exception) {
            Message.Message(mcontext, "TRIP_TABLE" + exception)
        }


        try {
            Message.Message(mcontext, "SQLite-OnCreate:MEMBERS:Table Created successfully")

            sqlitedatabase?.execSQL(CREATE_MEMBERS_TABLE)
        } catch (exception: SQLiteException) {
            Message.Message(mcontext, "MEMBERS_TABLE" + exception)
        }


        try {
            Message.Message(mcontext, "SQLite-OnCreate:EXPENSE:Table Created successfully")

            sqlitedatabase?.execSQL(CREATE_EXPENSE_TABLE)

        } catch (exception: SQLiteException) {
            Message.Message(mcontext, "EXPENSE_TABLE" + exception)
        }
    }


    override fun onUpgrade(sqlitedatabase: SQLiteDatabase?, p1: Int, p2: Int) {
        //OnUpgrade must be called whenever there is upgrade in database add tables,drop tables and upgraded to newer version
        try {
            sqlitedatabase?.execSQL(DROP_TRIP_TABLE)
            Message.Message(mcontext, "DataBaseHelper-OnUpgrade called --> Deleted/Updated TRIP_TABLE successfully...")
            onCreate(sqlitedatabase)
        } catch (exception: SQLiteException) {
            Message.Message(mcontext, " " + exception)
        }
        try {
            sqlitedatabase?.execSQL(DROP_MEMBERS_TABLE)
            Message.Message(
                mcontext,
                "DataBaseHelper-OnUpgrade called --> Deleted/Updated TRIP_ MEMBERS_TABLE successfully..."
            )
        } catch (exception: SQLiteException) {
            Message.Message(mcontext, " " + exception)
        }

        try {
            sqlitedatabase?.execSQL(DROP_EXPENSE_TABLE)
            Message.Message(
                mcontext,
                "DataBaseHelper-OnUpgrade called --> Deleted/Updated TRIP_ EXPENSE_TABLE successfully..."
            )
        } catch (exception: SQLiteException) {
            Message.Message(mcontext, " " + exception)
        }
    }

    fun readMemberDetailsByTripID(id:Long) : List<TripMemberDetail>
    {
        var list  = ArrayList<TripMemberDetail>()
        val dbreadable =this.readableDatabase

        val SELECT_QUERY = "select * from $TRIP_MEMBERS_TABLE_NAME where $TRIPID=$id"

        try {
            cursor =dbreadable.rawQuery(SELECT_QUERY,null)

            if(cursor.moveToFirst())
            {
                do {

                    var memberID = cursor.getLong(cursor.getColumnIndex(MEMBERID))
                    var  memberName = cursor.getString(cursor.getColumnIndex(MEMBERNAME))
                    var tripID = cursor.getLong(cursor.getColumnIndex(TRIPID))

                    list.add(TripMemberDetail(memberID,tripID,memberName))

                }while(cursor.moveToNext())
            }
        }
        catch(exception  :SQLiteException)
        {
            println("Exception at -> read memberDetails by TripID::" + exception)
        }
        return list;
    }


//    fun readMemberIDNamesByTripID(id : Long):List<MemberIDNameSpinner>
//    {
//        var list  = ArrayList<MemberIDNameSpinner>()
//        val dbreadable =this.readableDatabase
//
//        val SELECT_QUERY = "select * from $TRIP_MEMBERS_TABLE_NAME where $TRIPID=$id"
//
//        try {
//            cursor =dbreadable.rawQuery(SELECT_QUERY,null)
//
//            if(cursor.moveToFirst())
//            {
//                do {
//
//                    var memberID = cursor.getLong(cursor.getColumnIndex(MEMBERID))
//                    var  memberName = cursor.getString(cursor.getColumnIndex(MEMBERNAME))
//
//                    list.add(MemberIDNameSpinner(memberID,memberName))
//
//                }while(cursor.moveToNext())
//            }
//        }
//        catch(exception  :SQLiteException)
//        {
//
//        }
//        return list;
//    }

    fun readMemberDetailByID(id: Long): TripMemberDetail {


        val dbreadable =this.readableDatabase

       lateinit var tripMemberDetail: TripMemberDetail;

        val SELECT_BY_LAST_INSERTED_ID = "select * from $TRIP_MEMBERS_TABLE_NAME where $MEMBERID=$id"

        try {
            cursor = dbreadable.rawQuery(SELECT_BY_LAST_INSERTED_ID, null)

            if (cursor.moveToFirst()) {
                val memberID: Long = cursor.getLong(cursor.getColumnIndex(MEMBERID))
                val tripID: Long = cursor.getLong(cursor.getColumnIndex(TRIPID))
                val memberName: String = cursor.getString(cursor.getColumnIndex(MEMBERNAME))

                tripMemberDetail = TripMemberDetail(memberID, tripID, memberName)
                return tripMemberDetail;
            }
        } catch (exception: SQLiteException) {
            println(" " + exception)
            Message.Message(mcontext, " " + exception)
        }
        return tripMemberDetail;
    }

    fun readTripDetailByID(id: Long): TripDetail? {



val dbreadable= this.readableDatabase

        val SELECT_BY_LAST_INSERTED_ID = "select * from  $TRIP_TABLE_NAME where TRIPID=$id"
        var tripDetail: TripDetail? = null;

        try {
            cursor = dbreadable.rawQuery(SELECT_BY_LAST_INSERTED_ID, null)

            if (cursor.moveToFirst()) {
                val tripID: Long = cursor.getLong(cursor.getColumnIndex(TRIPID))
                val tripName: String = cursor.getString(cursor.getColumnIndex(TRIPNAME))
                val location: String = cursor.getString(cursor.getColumnIndex(TRIPLOCATION))
                val budget: Int = cursor.getInt(cursor.getColumnIndex(BUDGET))
                val tripDate: String = cursor.getString(cursor.getColumnIndex(TRIPDATE))


                tripDetail = TripDetail(tripID, tripName, location, budget, tripDate)
            }
        } catch (exception: java.lang.Exception) {
            println(exception)
        }

        return tripDetail;
    }


    /*For reading memberID on each memberName for Spinner Actions*/
    fun readMemberIdByMemberName(membername: String): Long {

        var memberId: Long=0
        val dbreadable = this.readableDatabase


        var SELECT_QUERY = "select $MEMBERID from $TRIP_MEMBERS_TABLE_NAME where $MEMBERNAME="+ "'$membername'"

        try {
            cursor = dbreadable.rawQuery(SELECT_QUERY, null)

            if (cursor.moveToFirst()) {
                do {
                    memberId = cursor.getLong(cursor.getColumnIndex(MEMBERID))
                } while (cursor.moveToNext())
            } else {
                println("No data found!! couldnt read memberid by member name:")
                Message.Message(mcontext, " " + "No data found!! couldnt read memberid by member name:")
            }
        } catch (exception: SQLiteException) {
            println(exception)
            Message.Message(mcontext, " " + " " + exception + " Please contact Admin! coudnt read ID by memberName")
        }

        cursor.close()
        dbreadable.close()
        return memberId
    }

    fun getMemberNameByMemberID(id : Long) : String
    {
        var db =this.readableDatabase
        var SELECT_QUERY = "select $MEMBERNAME from $TRIP_MEMBERS_TABLE_NAME where $MEMBERID=" + id;
        lateinit var memberName : String;
        try {
            cursor = db.rawQuery(SELECT_QUERY,null)

        }
        catch (sqliteException  : SQLiteException)
        {
            println("getMemberNameByMemberID cursor may be null");
            Message.Message(mcontext," " + sqliteException)
        }

        if(cursor.moveToFirst())
        {
            do {
                memberName  = cursor.getString(0)
            }while(cursor.moveToNext())
        }
        return memberName
    }

    /*To populate Spinner Adapter*/
    fun readMemberNames(tripID : Long): ArrayList<String>
    {
        var db = readableDatabase
        var list = ArrayList<String>()

        //  var cursor: Cursor? = null
        var SELECT_QUERY = "select $MEMBERNAME from $TRIP_MEMBERS_TABLE_NAME where $TRIPID="+ tripID
        try {

            cursor = db.rawQuery(SELECT_QUERY, null)
        } catch (exception: SQLiteException) {
            println(exception)
            Message.Message(mcontext, " " + exception)
            return ArrayList()
        }
        var memberName: String

        if (cursor.moveToFirst()) {
            do {
                memberName = (cursor.getString(cursor.getColumnIndex(MEMBERNAME)))

                list.add(memberName)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return list

    }
//
//    fun readMemberNamesWithID(): ArrayList<MemberIDNameSpinner>
//    {
//        var db = readableDatabase
//        var list = ArrayList<MemberIDNameSpinner>()
//
//
//        var SELECT_QUERY = "select $MEMBERID,$MEMBERNAME from $TRIP_MEMBERS_TABLE_NAME"
//        try {
//
//            cursor = db.rawQuery(SELECT_QUERY, null)
//        } catch (exception: SQLiteException) {
//            println(exception)
//            Message.Message(mcontext, " " + exception)
//            return ArrayList()
//        }
//        var memberName: String
//        var memberId : Long
//
//        if (cursor.moveToFirst()) {
//            do {
//                memberName = (cursor.getString(cursor.getColumnIndex(MEMBERNAME)))
//                memberId = (cursor.getLong(cursor.getColumnIndex(MEMBERID)))
//                list.add(MemberIDNameSpinner(memberId,memberName))
//            } while (cursor.moveToNext())
//        }
//
//        cursor.close()
//        db.close()
//        return list
//
//    }



    fun readTripDetails(): List<TripDetail> {
        val list = ArrayList<TripDetail>()



val dbreadable =this.readableDatabase

        val SELECT_QUERY = "select * from $TRIP_TABLE_NAME"

        try {
            cursor = dbreadable.rawQuery(SELECT_QUERY, null)
        } catch (exception: Exception) {
            dbreadable.execSQL(CREATE_TRIP_TABLE)
            Message.Message(mcontext, " " + exception)
            return ArrayList()
        }

        var tripId: Long
        var tripName: String
        var tripLocation: String

        var budget: Int
        var tripDate: String

        if (cursor.moveToFirst()) {
            //  The cursor starts before the first result row, so on the first iteration you move the cursor to the first result if it exists.
            do {
                tripId = (cursor.getLong(cursor.getColumnIndex("TRIPID")))
                tripName = cursor.getString(cursor.getColumnIndex("TRIPNAME"))
                tripLocation = cursor.getString(cursor.getColumnIndex("TRIPLOCATION"))
                budget = cursor.getInt(cursor.getColumnIndex("BUDGET"))
                tripDate = cursor.getString(cursor.getColumnIndex("TRIPDATE"))

                list.add(
                    TripDetail(
                        tripId,
                        tripName,
                        tripLocation,
                        budget,
                        tripDate
                    )
                )

                Message.Message(mcontext, "Within readTripDetails Listing Data:" + list.toString())

            } while (cursor.moveToNext())
        }
        cursor.close()
        dbreadable.close()

        return list;
    }

    fun getAllExpenseDetails(tripID: Long): ArrayList<ExpenseDetail> {
        var expenseDetailsList = ArrayList<ExpenseDetail>()
        var db = this.readableDatabase
        //var cursor: Cursor
        var SELECT_QUERY = "select * from $TRIP_EXPENSE_TABLE_NAME where $TRIPID=" + tripID

        try {

            cursor = db.rawQuery(SELECT_QUERY, null)
        }

        catch (exception: Exception) {

            // db.execSQL(CREATE_EXPENSE_TABLE)

            println("Unable to getAllExpenseDetails() return ArrayList" + exception)
            Message.Message(mcontext, " " + "Unable to getAllExpenseDetails() return ArrayList" + exception)

            return ArrayList();
        }

        if (cursor.moveToNext()) {
                do {
                    var expenseID: Long = cursor.getLong(cursor.getColumnIndex("EXPENSEID"))
                    var tripID: Long = cursor.getLong(cursor.getColumnIndex("TRIPID"))
                    var memberID: Long = cursor.getLong(cursor.getColumnIndex("MEMBERID"))
                    var expenseName: String = cursor.getString(cursor.getColumnIndex("EXPENSENAME"))
                    var payer: String = cursor.getString(cursor.getColumnIndex("PAYER"))
                    var amount: Float = cursor.getFloat(cursor.getColumnIndex("AMOUNT"))

//                    if(cursor.getType(cursor.getColumnIndex("EXPENSEDATE")) == FIELD_TYPE_STRING)
//                    {
//                        println("expense date is of type string")
//                    }



                    var expenseDate : String  = cursor.getString(cursor.getColumnIndex("EXPENSEDATE"))
                    expenseDetailsList.add(ExpenseDetail(expenseID, tripID, memberID, expenseName, payer, amount,expenseDate))


                } while (cursor.moveToNext())
            }

        return expenseDetailsList;
    }

    fun insertTripDetails(tripDetail: TripDetail): Long {


        val dbwritable = this.writableDatabase
        val values = ContentValues();
        values.put(TRIPNAME, tripDetail.tripName);
        values.put(TRIPLOCATION, tripDetail.tripLocation);
        values.put(BUDGET, tripDetail.budget)
        values.put(TRIPDATE, tripDetail.tripDate)




        val status: Long = dbwritable.insert(TRIP_TABLE_NAME, null, values)

        dbwritable.close()

        return status;
    }

    fun insertMemberDetails(memberDetail: TripMemberDetail): Long {
        val values = ContentValues()

        val dbwritable =this.writableDatabase
        values.put(TRIPID, memberDetail.tripID)
        values.put(MEMBERNAME, memberDetail.memberName)



        val status: Long = dbwritable.insert(TRIP_MEMBERS_TABLE_NAME, null, values)
        dbwritable.close()

        return status
    }

    fun insertExpenseDetails(expenseDetail: ExpenseDetail): Long {
        val values = ContentValues()

        val dbwritable = this.writableDatabase

        values.put(TRIPID, expenseDetail.tripID)
        values.put(MEMBERID, expenseDetail.memberID)
        values.put(EXPENSENAME, expenseDetail.expenseType)
        values.put(PAYER, expenseDetail.payer)
        values.put(AMOUNT, expenseDetail.amount)
        values.put(EXPENSEDATE,expenseDetail.expenseDate)



        val status: Long = dbwritable.insert(TRIP_EXPENSE_TABLE_NAME, null, values)
        dbwritable.close()
        return status
    }







    fun getExpenseDetailsByID(id: Long): ExpenseDetail {


        val dbreadable = this.readableDatabase
        var expenseDetail: ExpenseDetail?=null  // ExpenseDetail(0,0,0,"","",0f,"")

        var SELECT_QUERY = "select * from $TRIP_EXPENSE_TABLE_NAME where $EXPENSEID=" + id
       // var cursor: Cursor? = null
        try {
            cursor = dbreadable.rawQuery(SELECT_QUERY, null)
        }
        catch (exception: Exception) {
            Message.Message(mcontext, " " + exception)
            println("coudln't getExpenseDetailsByID:" + id + "::Please Contact Admin!!!")
        }

            if (cursor.moveToFirst()) {
                do {
                    var expenseID: Long = cursor.getLong(cursor.getColumnIndex(EXPENSEID))
                    println("ExpenseID with getExpenseDetailsByID:" + expenseID)

                    var tripID: Long = cursor.getLong(cursor.getColumnIndex(TRIPID))

                    var memberID: Long = cursor.getLong(cursor.getColumnIndex(MEMBERID))

                    var expenseName: String = cursor.getString(cursor.getColumnIndex(EXPENSENAME))

                    var payer: String = cursor.getString(cursor.getColumnIndex(PAYER))

                    var amount: Float = cursor.getFloat(cursor.getColumnIndex(AMOUNT))

                    var expenseDate : String = cursor.getString(cursor.getColumnIndex(EXPENSEDATE))
                    expenseDetail = ExpenseDetail(expenseID, tripID, memberID, expenseName, payer, amount,expenseDate)

                } while (cursor.moveToNext())
            } else {
                println("No details found,regarding...Please contact Admin!!!")
            }
        dbreadable.close()

        return expenseDetail!!;
    }


//    fun readMemberDetails(): ArrayList<TripMemberDetail> {
//
//        var list = ArrayList<TripMemberDetail>()
//
//        val dbreadable = this.readableDatabase
//        val SELECT_QUERY = "select * from $TRIP_MEMBERS_TABLE_NAME"
//
//        try {
//            cursor = dbreadable.rawQuery(SELECT_QUERY, null)
//        } catch (exception: Exception) {
//            dbreadable.execSQL(CREATE_MEMBERS_TABLE)
//            Message.Message(mcontext, " " + exception)
//            return ArrayList()
//        }
//
//        var tripID: Long;
//        var memberID: Long
//        var memberName: String
//
//        if (cursor.moveToFirst()) {
//            do {
//                tripID = (cursor.getLong(cursor.getColumnIndex(TRIPID)))
//                memberID = (cursor.getLong(cursor.getColumnIndex(MEMBERID)))
//                memberName = (cursor.getString(cursor.getColumnIndex(MEMBERNAME)))
//
//                list.add(TripMemberDetail(memberID, tripID, memberName))
//            } while (cursor.moveToNext());
//        } else {
//            Message.Message(mcontext, " " + "Cursor is empty")
//        }
//        cursor.close()
//        dbreadable.close()
//
//        return list;
//
//    }

    fun calculate(tripID : Long) : ArrayList<ReportDetail>
    {

        val dbreadable=this.readableDatabase


        var mean : Float = 0F;

        var listMemberIDAmount : ArrayList<MemberIDAmount> = ArrayList<MemberIDAmount>()


        var sum_query=  "select SUM($AMOUNT) from $TRIP_EXPENSE_TABLE_NAME where $TRIPID=" + tripID

        var total_members_in_a_trip = "select COUNT($MEMBERID) from $TRIP_MEMBERS_TABLE_NAME where $TRIPID=" + tripID

        var individual_amount = "select ${MEMBERID},SUM($AMOUNT) from $TRIP_EXPENSE_TABLE_NAME where $TRIPID="+tripID +" GROUP BY $MEMBERID"


        var zero_spent_persons = "select ${MEMBERID} from ${TRIP_MEMBERS_TABLE_NAME} where $TRIPID=" + tripID +" and $MEMBERID NOT IN (select $MEMBERID from $TRIP_EXPENSE_TABLE_NAME where $TRIP_EXPENSE_TABLE_NAME.$TRIPID =" + tripID + ")"






                try {
            cursor1 = dbreadable.rawQuery(sum_query,null)

            if(cursor1.moveToFirst()) {
               mean = cursor1.getFloat(0)
           }

            cursor2 = dbreadable.rawQuery(total_members_in_a_trip, null)

            if(cursor2.moveToFirst()) {
                var distinct_members_count = cursor2.getInt(0)

                mean /= distinct_members_count;
                println(mean)
            }

            cursor3 = dbreadable.rawQuery(individual_amount,null)

            if(cursor3.moveToFirst())
            {
                do {


                    var amount = mean - cursor3.getFloat(1)
                     listMemberIDAmount.add(MemberIDAmount(cursor3.getLong(0),amount))

                }while(cursor3.moveToNext())
            }

                    println(listMemberIDAmount.toString())

                    cursor4 = dbreadable.rawQuery(zero_spent_persons,null)

                    if(cursor4.moveToFirst())
                    {
                        do {
                            var amount = mean-cursor4.getLong(0)
                            listMemberIDAmount.add(MemberIDAmount(cursor4.getLong(0),amount))
                        }while(cursor4.moveToNext())
                    }

                }
        catch(exception : Exception)
        {
            println("Exception@Calculate::" + exception)
        }

        dbreadable.close()
        return expenseSharing(listMemberIDAmount)
    }

    fun expenseSharing(list : ArrayList<MemberIDAmount>): ArrayList<ReportDetail>
    {
        val resultant_list :ArrayList<ReportDetail> = ArrayList<ReportDetail>()

      Collections.sort(list,SortByAmount())

        println("Sorted list is....." + list)

        var neg:Boolean = false;
        var j:Int;

        for(i in 0..list.size-1)
        {
            for(j in list.size-1 downTo 0 step 1)
            {
                println("@Loop Begins stage::" + list[j].amount + " " + list[i].amount  + " ")

                if(list[j].amount < 0 && list[i].amount > 0)
                    {

                        var owes : Float;
                        if(list[j].amount - (-list[i].amount) < 0f)
                        {
                            list[j].amount = list[j].amount -(-list[i].amount)
                            owes = list[i].amount
                                list[i].amount = 0f


                        }
                        else
                        {
                           list[i].amount = list[j].amount -(-list[i].amount)
                            owes = list[j].amount
                            list[j].amount=0f

                        }
                        if(owes < 0f) owes = -owes;
                        var reportDetail : ReportDetail = ReportDetail(" " + getMemberNameByMemberID(list[i].memberID ) +  " owes Rs. " + owes + " to " + getMemberNameByMemberID(list[j].memberID))
                        resultant_list.add(reportDetail)

                        reportDetail = ReportDetail(" " + getMemberNameByMemberID(list[j].memberID ) +  " gets Rs. " + owes + " from " + getMemberNameByMemberID(list[i].memberID))
                        resultant_list.add(reportDetail)




                        neg = true
                        println("@LoopEndingStage::" + list[j].amount + " " + list[i].amount  + " ")
                    }
            }
            if(neg == false)
                break;
        }
        println("Result is..." + resultant_list.toString())

        return resultant_list;
    }




    fun deleteTripByTripID(id : Long) :Int
    {
        val dbwritable=this.writableDatabase
        var status : Int

        status =  dbwritable.delete(TRIP_TABLE_NAME,"$TRIPID=?", arrayOf(id.toString()))
        dbwritable.close()
        return status;

    }


    fun deleteMemberByTripID(id : Long) :Int
    {
        val dbwritable =this.writableDatabase
        var status : Int

        status =  dbwritable.delete(TRIP_MEMBERS_TABLE_NAME,"$TRIPID=?" , arrayOf(id.toString()))
            dbwritable.close()
        return status;

    }


    fun deleteExpenseByTripID(id : Long) :Int
    {
        val dbwritable =this.writableDatabase
        var status : Int=0


        println("@DataBaseHelper DeleteExpenseByID::" + id)
        try {
          //  status = db.delete(TRIP_EXPENSE_TABLE_NAME,"$TRIPID=?", arrayOf(1.toString()))
            status = dbwritable.delete(TRIP_EXPENSE_TABLE_NAME, "$TRIPID=?" , arrayOf(id.toString()))
        }
        catch (exception : SQLiteException)
        {
            println(" " + exception + "@SQLite database")
        }
       dbwritable.close()
        return status;

    }

    fun deleteMemberByMemberID(id : Long):Int{

        val db =this.writableDatabase
        var status : Int=0


        println("@DataBaseHelper DeleteMemberByMemberID::" + id)
        try {

            status = db.delete(TRIP_MEMBERS_TABLE_NAME, "$MEMBERID=?" , arrayOf(id.toString()))
        }
        catch (exception : SQLiteException)
        {
            println(" " + exception + "@SQLite database")
        }
        db.close()
        return status;


    }


    fun deleteExpenseByMemberID(id : Long) : Int{

        val dbwritable =this.writableDatabase
        var status : Int=0


        println("@DataBaseHelper DeleteExpenseByMemberID::" + id)
        try {
            //  status = db.delete(TRIP_EXPENSE_TABLE_NAME,"$TRIPID=?", arrayOf(1.toString()))
            status = dbwritable.delete(TRIP_EXPENSE_TABLE_NAME, "$MEMBERID=?" , arrayOf(id.toString()))
        }
        catch (exception : SQLiteException)
        {
            println(" " + exception + "@SQLite database")
        }
        dbwritable.close()
        return status;

    }


    fun deleteExpenseDetailsByExpenseID(id : Long) : Int
    {
        val dbwritable =this.writableDatabase
        var status : Int=0


        println("@DataBaseHelper DeleteExpenseByExpenseID::" + id)
        try {

            status = dbwritable.delete(TRIP_EXPENSE_TABLE_NAME, "$EXPENSEID=?" , arrayOf(id.toString()))
        }
        catch (exception : SQLiteException)
        {
            println(" " + exception + "@SQLite database")
        }
        dbwritable.close()
        return status;

    }

    fun updateTripDeatils(tripDetail :TripDetail):Int
    {
        var status : Int = 0
        val dbwritable = this.writableDatabase
        var values = ContentValues()

        values.put(TRIPNAME,tripDetail.tripName)
        values.put(TRIPLOCATION,tripDetail.tripLocation)
        values.put(TRIPDATE,tripDetail.tripDate)
        values.put(BUDGET,tripDetail.budget)

        status = dbwritable.update(TRIP_TABLE_NAME, values,"$TRIPID=?", arrayOf(tripDetail.tripID.toString()))


        return status;
    }

    fun updateMemberDetails(memberDetail: TripMemberDetail) : Int{
        var status  :Int = 0

        val dbwritable =  this.writableDatabase
        var values = ContentValues()

        values.put(MEMBERID,memberDetail.memberID)
        values.put(MEMBERNAME,memberDetail.memberName)
        values.put(TRIPID,memberDetail.tripID)

        status = dbwritable.update(TRIP_MEMBERS_TABLE_NAME,values,"$MEMBERID=?",arrayOf(memberDetail.memberID.toString()))
    return status;
    }

    fun updateExpenseDetailsByMemberID(memberDetail: TripMemberDetail) : Int
    {
        var status : Int = 0
        val dbwritable = this.writableDatabase

        var values = ContentValues()
        values.put(PAYER,memberDetail.memberName)
        values.put(TRIPID,memberDetail.tripID)

        status = dbwritable.update(TRIP_EXPENSE_TABLE_NAME,values,"$MEMBERID=?", arrayOf(memberDetail.memberID.toString()))
        return status;

    }

    fun updateExpenseDetailsByExpenseID(expenseDetail: ExpenseDetail):Int
    {
        var status : Int = 0
        val dbwritable = this.writableDatabase

        var values = ContentValues()


        values.put(EXPENSEID,expenseDetail.expenseID)
        values.put(TRIPID,expenseDetail.tripID)
        values.put(MEMBERID,expenseDetail.memberID)
        values.put(EXPENSENAME,expenseDetail.expenseType)
        values.put(PAYER,expenseDetail.payer)
        values.put(AMOUNT,expenseDetail.amount)
        values.put(EXPENSEDATE,expenseDetail.expenseDate)

        status  = dbwritable.update(TRIP_EXPENSE_TABLE_NAME, values,"$EXPENSEID=?", arrayOf(expenseDetail.expenseID.toString()))

        return status
    }

    fun countTrips() : Int{
        var COUNT_QUERY = "select count($TRIPID) from $TRIP_TABLE_NAME"

            var dbreadable = this.readableDatabase
        var count : Int =0

        try {

            cursor1 = dbreadable.rawQuery(COUNT_QUERY, null)
        }
        catch(exception : SQLiteException)
        {
            println(" " + exception)
        }

        if(cursor1.moveToFirst())
        {
            count = cursor1.getInt(0)

        }
        dbreadable.close()
          return count;
    }

    fun countMembers(tripID  : Long) : Int{
        var COUNT_QUERY = "select count($MEMBERID) from ${TRIP_MEMBERS_TABLE_NAME} WHERE #TRIPID=" + tripID

        var dbreadable = this.readableDatabase
        var count : Int =0

        try {

            cursor1 = dbreadable.rawQuery(COUNT_QUERY, null)
        }
        catch(exception : SQLiteException)
        {
            println(" " + exception)
        }

        if(cursor1.moveToFirst())
        {
            count = cursor1.getInt(0)

        }
        dbreadable.close()
        return count;
    }

    fun countExpenses(tripID  : Long) : Int{
        var COUNT_QUERY = "select count($EXPENSEID) from ${TRIP_EXPENSE_TABLE_NAME} WHERE #TRIPID=" + tripID

        var dbreadable = this.readableDatabase
        var count : Int =0

        try {

            cursor1 = dbreadable.rawQuery(COUNT_QUERY, null)
        }
        catch(exception : SQLiteException)
        {
            println(" " + exception)
        }

        if(cursor1.moveToFirst())
        {
            count = cursor1.getInt(0)

        }
        dbreadable.close()
        return count;
    }
}




public class SortByAmount() : Comparator<MemberIDAmount>
{
    override fun compare(a: MemberIDAmount, b: MemberIDAmount): Int {
        val first : Int = Math.round(a.amount)
        val second : Int =Math.round(b.amount)
        return second-first
    }

}



