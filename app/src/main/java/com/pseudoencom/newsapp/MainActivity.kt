package com.pseudoencom.newsapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.SearchView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.pseudoencom.newsapp.view.*

class MainActivity : AppCompatActivity(), NavigationBarView.OnItemSelectedListener {
    lateinit var bottomNavigationView: BottomNavigationView
    lateinit var search: SearchView
    lateinit var backButton: Button
    lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private var listener:OnSearchListener? = null
    lateinit var launcher: ActivityResultLauncher<Intent>
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            try {
                val account = task.getResult(ApiException::class.java)
                if (account != null){
                    fireBaseAuthWithGoogle(account.idToken!!)
                }
            }catch (e: ApiException){
                Log.d("myTag", "Error Api Exepction")
            }
        }
        auth = FirebaseAuth.getInstance()
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
            R.id.profile -> {changerOfFrg(ProfileFragment()); signInWithGoogle()}
        }
        return true
    }

    private fun changerOfFrg(fragment: Fragment) {
        supportFragmentManager.popBackStackImmediate()
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frgChanger, fragment)
                .commit()
        }
    }
    fun onSearch(text: String) {
        listener?.onSearch(text)
    }

    fun getClient(): GoogleSignInClient{
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        return GoogleSignIn.getClient(this, gso)
    }

    fun signInWithGoogle(){
        val signInClient = getClient()
        launcher.launch(signInClient.signInIntent)
    }

    fun fireBaseAuthWithGoogle(token: String){
        val credential = GoogleAuthProvider.getCredential(token, null)
        auth.signInWithCredential(credential).addOnCompleteListener {
            if(it.isSuccessful){

            }else if (it.isCanceled){

            }else {
                ///
            }
        }
    }
}