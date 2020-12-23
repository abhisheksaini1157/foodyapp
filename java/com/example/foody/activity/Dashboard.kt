package com.example.foody.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.foody.R
import com.example.foody.fragment.DashboardFragment
import com.example.foody.fragment.FaqsFragment
import com.example.foody.fragment.FavouriteRestaurantFragment
import com.example.foody.fragment.ProfileFragment
import com.google.android.material.navigation.NavigationView

class Dashboard : AppCompatActivity() {
    lateinit var coordinatiorLayout:CoordinatorLayout
    lateinit var toolbar:androidx.appcompat.widget.Toolbar
    lateinit var frameLayout: FrameLayout
    lateinit var navigationView: NavigationView
    lateinit var drawerLayout: DrawerLayout
    lateinit var txtUser:TextView
    lateinit var txtNumber:TextView
    lateinit var sharedPreferences: SharedPreferences
    var previousMenuItemSelected:MenuItem?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        sharedPreferences=getSharedPreferences(
            getString(R.string.shared_preferences),
            Context.MODE_PRIVATE
        )
        coordinatiorLayout=findViewById(R.id.coordinatorLayout)
        toolbar=findViewById(R.id.toolbar)
        frameLayout=findViewById(R.id.frameLayout)
        navigationView=findViewById(R.id.navigationView)
        drawerLayout=findViewById(R.id.drawerLayout)
        val headerView=navigationView.getHeaderView(0)
        txtUser=headerView.findViewById(R.id.txtUser)
        txtNumber=headerView.findViewById(R.id.txtNumber)
        navigationView.menu.getItem(0).isCheckable=true
        navigationView.menu.getItem(0).isChecked=true
        setToolbar()
        txtUser.text=sharedPreferences.getString("name","UserName")
        txtNumber.text="+91-${sharedPreferences.getString("mobile_number","9999999999")}"

        //hmburger icon setup for navigation drawer
        val actionBarDrawerToggle=ActionBarDrawerToggle(this@Dashboard,drawerLayout,
            R.string.open_drawer,
            R.string.close_drawer
        )
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        navigationView.setNavigationItemSelectedListener {

            if (previousMenuItemSelected!=null){
                previousMenuItemSelected?.isChecked=false
            }

            it.isCheckable=true
            it.isChecked=true
            previousMenuItemSelected=it
            when(it.itemId){
                R.id.home -> {
                    openDashboard()
                    drawerLayout.closeDrawers()
                }
                R.id.myProfile -> {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frameLayout,
                            ProfileFragment(this)
                        ).commit()
                    supportActionBar?.title="MY Profile"
                    drawerLayout.closeDrawers()
                }
                R.id.favoriteResturant -> {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frameLayout,
                            FavouriteRestaurantFragment(this)
                        ).commit()
                    supportActionBar?.title="Favourites"
                    drawerLayout.closeDrawers()
                }
                R.id.orderHistory -> {
                   val intent=Intent(this,OrderHistoryActivity::class.java)
                    drawerLayout.closeDrawers()
                     Toast.makeText(this@Dashboard,"Order History",Toast.LENGTH_SHORT).show()
                  startActivity(intent)
                }
                R.id.faqs ->{
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frameLayout,
                            FaqsFragment()
                        ).commit()
                    supportActionBar?.title="FAQs"
                    drawerLayout.closeDrawers()
                }
                R.id.logout ->{
                    drawerLayout.closeDrawers()
                    val alterDialog=androidx.appcompat.app.AlertDialog.Builder(this)
                    alterDialog.setMessage("Do you wish to log out")
                    alterDialog.setPositiveButton("Yes") { _, _ ->

                        sharedPreferences.edit().putBoolean("isLoggedIn", false).apply()

                        ActivityCompat.finishAffinity(this)
                        val intent=Intent(this,LoginRegisterActivity::class.java)
                        startActivity(intent)
                    }
                    alterDialog.setNegativeButton("No"){_,_ ->}
                    alterDialog.create()
                    alterDialog.show()

                }

            }
            return@setNavigationItemSelectedListener true

        }
        openDashboard()


    }
    fun setToolbar(){
        setSupportActionBar(toolbar)
        supportActionBar?.title="All Restaurants"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id=item.itemId
        if(id==android.R.id.home){
            drawerLayout.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
    }
    fun openDashboard(){
        val fragment= DashboardFragment(this)
        val transaction=supportFragmentManager.beginTransaction()
        transaction.replace(
            R.id.frameLayout,
            DashboardFragment(this)
        )
        transaction.commit()
        supportActionBar?.title="All Restaurants"
        navigationView.setCheckedItem(R.id.home)


    }

    override fun onBackPressed() {
        val frag=supportFragmentManager.findFragmentById(R.id.frameLayout)
        when(frag){
            !is DashboardFragment-> openDashboard()
            else -> super.onBackPressed()
        }
    }
}