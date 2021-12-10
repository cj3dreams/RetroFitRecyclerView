package com.pseudoencom.retrofitrecyclerview

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.snackbar.Snackbar
import com.pseudoencom.retrofitrecyclerview.view.HomeFragment
import com.pseudoencom.retrofitrecyclerview.view.NewsFragment
import com.pseudoencom.retrofitrecyclerview.view.ReadLaterFragment
import com.pseudoencom.retrofitrecyclerview.vm.MyViewModelFactory
import com.pseudoencom.retrofitrecyclerview.vm.SharedViewModel

class MainActivity : AppCompatActivity(), NavigationBarView.OnItemSelectedListener {
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var fragment: Fragment
    private lateinit var search: SearchView
//    private lateinit var viewModel: SharedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(p0: String?): Boolean {
//
//                return false
//            }
//
//            override fun onQueryTextChange(p0: String?): Boolean {
//                println("hello")
//                return false
//            }
//
//
//        })
        search = findViewById(R.id.searchView)
        bottomNavigationView = findViewById(R.id.botNav)
        bottomNavigationView.setOnItemSelectedListener(this)
        bottomNavigationView.selectedItemId = R.id.mainHome
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.mainHome -> fragment = HomeFragment()
            R.id.readLater -> fragment = ReadLaterFragment()
        }
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frgChanger, fragment)
                .commit()
        }
        return true
    }
}