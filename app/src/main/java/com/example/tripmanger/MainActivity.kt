package com.example.tripmanger

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.example.tripmanger.Adapter.TripDetailsCustomAdapter
import com.example.tripmanger.Fragments.TripDetailsFragment
import com.example.tripmanger.Model.TripDetail
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    interface IOnBackPressedListener
    {
        fun onBackPressed() : Boolean
    }




    var list: ArrayList<TripDetail> = ArrayList<TripDetail>()
    var madapter: TripDetailsCustomAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



       setSupportActionBar(toolbar)



        /*Do undo below code*/

        val tripDetailsFragment: TripDetailsFragment = TripDetailsFragment(this)
        tripDetailsFragment.onBackPressed()

        val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction().replace(R.id.frameLayoutMainActivity, tripDetailsFragment)
        fragmentTransaction.addToBackStack("TripManager")
        fragmentTransaction.commit()



        /*Do undo above code*/



//        val fab = findViewById<FloatingActionButton>(R.id.fab) as FloatingActionButton
//        fab.show()

//
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {


        return super.onCreateView(name, context, attrs)
    }


    override fun onBackPressed() {
        super.onBackPressed()
        Log.d("mainactivity", " stack count --> "+supportFragmentManager.backStackEntryCount)
        if(supportFragmentManager.backStackEntryCount == 0){
            finish();
        }

    }
//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        menuInflater.inflate(R.menu.menu_main, menu)
//        return true
//    }
//
//
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return when (item.itemId) {
//            R.id.action_settings -> true
//            else -> super.onOptionsItemSelected(item)
//        }
//    }

}



