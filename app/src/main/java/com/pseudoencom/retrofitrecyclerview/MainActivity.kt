package com.pseudoencom.retrofitrecyclerview

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.SearchView
import android.widget.Toast
import android.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.snackbar.Snackbar
import com.pseudoencom.retrofitrecyclerview.model.Article
import com.pseudoencom.retrofitrecyclerview.view.*
import com.pseudoencom.retrofitrecyclerview.vm.MyViewModelFactory
import com.pseudoencom.retrofitrecyclerview.vm.SharedViewModel

class MainActivity : AppCompatActivity(), NavigationBarView.OnItemSelectedListener {
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var search: SearchView
    lateinit var backButton: Button
    lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private var listener:OnSearchListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        search = findViewById(R.id.searchView)
        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(p0: String?): Boolean {
                    onSearch(p0!!)
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                    onSearch(p0!!)
                return false
            }
        })
        toolbar = findViewById(R.id.toolbar)
        bottomNavigationView = findViewById(R.id.botNav)
        bottomNavigationView.setOnItemSelectedListener(this)
        bottomNavigationView.selectedItemId = R.id.mainHome
        backButton = findViewById(R.id.backButton)
        backButton.setOnClickListener{
            onBackPressed()
        }
        backButton.visibility = View.GONE
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.mainHome -> {
                val fragment = HomeFragment()
                listener= fragment
                changerOfFrg(fragment)
            }
            R.id.readLater -> changerOfFrg(ReadLaterFragment())
            R.id.favorites -> changerOfFrg(FavoritiesFragment())
            R.id.profile -> changerOfFrg(ProfileFragment())
        }
        return true
    }

    private fun changerOfFrg(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frgChanger, fragment)
                .commit()
        }
    }
    fun onSearch(text: String) {
        listener?.onSearch(text)
    }
}