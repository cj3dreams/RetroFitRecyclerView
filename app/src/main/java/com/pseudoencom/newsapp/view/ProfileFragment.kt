package com.pseudoencom.newsapp.view

import ProfileAdapter
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.pseudoencom.newsapp.MainActivity
import com.pseudoencom.newsapp.R
import com.pseudoencom.newsapp.model.ProfileModel
import com.pseudoencom.newsapp.vm.SharedViewModel

class ProfileFragment : Fragment(), View.OnClickListener {

    lateinit var recyclerView: RecyclerView
    lateinit var adapter: ProfileAdapter
    lateinit var viewModel: SharedViewModel
    lateinit var imageViewAvatar: ImageView
    lateinit var textViewName: TextView
    lateinit var textViewLog: TextView


    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = LayoutInflater.from(context).inflate(R.layout.fragment_profile, container, false)
        recyclerView = view.findViewById(R.id.rRViewP)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        imageViewAvatar = view.findViewById(R.id.avatarUser)
        textViewName = view.findViewById(R.id.nameOfUser)
        textViewLog = view.findViewById(R.id.log)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.mPFragment().observe(viewLifecycleOwner, Observer {
            adapter = ProfileAdapter(it, this, requireContext())
            recyclerView.adapter = adapter
        })
        viewModel.getProfile()
        viewModel.fetchMPF()

        val act = activity as MainActivity
        if (act.auth.currentUser != null){
          loadImage(requireActivity(),imageViewAvatar, FirebaseAuth.getInstance().currentUser?.photoUrl.toString())
            textViewName.text = act.auth.currentUser?.displayName + "\n" + act.auth.currentUser?.email
            textViewLog.text = "Log Out"
            textViewLog.setOnClickListener {
                FirebaseAuth.getInstance().signOut()
                Toast.makeText(requireContext(), "Log Out Successes", Toast.LENGTH_SHORT).show()
                if (act.auth.currentUser == null) onRefresh()
            }
        }
        if (act.auth.currentUser == null) {
            textViewLog.text = "Log In"
            textViewLog.setOnClickListener {
                act.signInWithGoogle()
            }
        }
    }

    override fun onClick(v: View?) {
        v?.let {
            val itemData = v.tag as ProfileModel
            fun toast(text: String) {
                Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
            }
            when (itemData.id) {
                1 -> showdialog("Change API Token","Enter token", View.VISIBLE, true,"none")
                2 -> toast("2")
                3 -> toast("3")
                4 -> {context?.cacheDir?.deleteRecursively(); toast("Cleaned at " + (context?.cacheDir?.path).toString()); }
                5 -> showdialog("About me","none", View.INVISIBLE, false, "You can feel free to contact with me \n \n jamshed.saleh@livo.tj \n" + getString(R.string.telegram))
                else -> toast("Error of WHEN Operator")
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val act = activity as MainActivity
        act.backButton.visibility = View.GONE
        act.toolbar.elevation = 7F
        act.search.visibility = View.INVISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        val act = activity as MainActivity
        act.search.visibility = View.VISIBLE
    }

    fun showdialog(title: String, inputText:String, inputVis:Int, showCancel: Boolean, ifShowCancel:String) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder.setTitle(title)

        val input = EditText(requireContext())
        input.setHint(inputText)
        input.inputType = InputType.TYPE_CLASS_TEXT
        input.visibility = inputVis
        builder.setView(input)

        builder.setPositiveButton(
            "OK",
            DialogInterface.OnClickListener { dialog, which ->  var m_Text = input.text.toString()
            })
        if (showCancel) {
            builder.setNegativeButton(
                "Cancel",
                DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })
        }
        if (!showCancel){
            builder.setMessage(ifShowCancel)
        }
        builder.show()
    }
    fun loadImage(
        activity: Context?, ivUser: ImageView,
        originalImage: String?
    ) {

        activity?.let {
            Glide.with(it).load(originalImage)
                .thumbnail(Glide.with(activity).load(originalImage))
                .into(ivUser)
        }
    }
    fun onRefresh() {
        activity?.supportFragmentManager?.beginTransaction()?.apply {
            replace(R.id.frgChanger, ProfileFragment())
            commit()
        }
    }
}