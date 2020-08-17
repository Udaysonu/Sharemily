package com.example.sharemliy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_tab.*

class TabActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tab)


        // setting the default fragment in display

        val fragmentManager=supportFragmentManager
        val fragmentTransaction=fragmentManager.beginTransaction()
        val chatfragment=chatsFragment()
        fragmentTransaction.add(R.id.fragment_layout, chatfragment)
        fragmentTransaction.commit()



        //Handling tab change events
        tab_layout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab?) {

                //test toast
                Toast.makeText(baseContext,"${tab?.position}",Toast.LENGTH_LONG).show()

                //checking the position of the tab clicked and displaying corresponding fragments
                if(tab?.position==0)
                {
                        val fragmentManager=supportFragmentManager
                        val fragmentTransaction=fragmentManager.beginTransaction()
                        val chatfragment=chatsFragment()
                    fragmentTransaction.replace(R.id.fragment_layout, chatfragment)
                    fragmentTransaction.addToBackStack(null)
                    fragmentTransaction.commit()
                }
                else
                {
                    val newFragment = peopleFragment()
                    val transaction = supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.fragment_layout, newFragment)
                    transaction.addToBackStack(null)
                    transaction.commit()

                }
                // Handle tab select
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // Handle tab reselect
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // Handle tab unselect
            }
        })
    }

}



