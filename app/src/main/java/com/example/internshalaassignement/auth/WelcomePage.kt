package com.example.internshalaassignement.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.internshalaassignement.R
import com.example.internshalaassignement.databinding.FragmentWelcomePageBinding
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class WelcomePage : Fragment() {
    private lateinit var binding: FragmentWelcomePageBinding
    private var RCSIGNIN: Int = 123
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var mView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWelcomePageBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mView = view
        init()
    }

    private fun init() {
        mAuth = FirebaseAuth.getInstance()
        binding.startBtn.setOnClickListener { loginUser() }
    }

    private fun loginUser() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RCSIGNIN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RCSIGNIN) {
            val result = data?.let { Auth.GoogleSignInApi.getSignInResultFromIntent(it) }
            if (result?.isSuccess!!) {
                val acct = result.signInAccount
                firebaseAuthWithGoogle(acct)
            } else {

                Toast.makeText(context, "There was a trouble signing-in Please try again", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount?) {
        val credential = GoogleAuthProvider.getCredential(acct!!.idToken, null)
        mAuth.signInWithCredential(credential).addOnCompleteListener { task ->
            run {
                if (task.isSuccessful) {
                    saveEmailInSharedPref(acct)
                    goToNotesFragment()
                }else {
                    Toast.makeText(context,"Something went wrong, Please try again!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun goToNotesFragment() {
        findNavController().navigate(R.id.action_welcomePage_to_notesHomeFragment)

    }

    private fun saveEmailInSharedPref(acct: GoogleSignInAccount?){
        val preferences = context?.getSharedPreferences(getString(R.string.preference_name), AppCompatActivity.MODE_PRIVATE)
        preferences?.edit()?.putString("email",acct?.email.toString())?.apply()
    }

    override fun onStart() {
        super.onStart()
        if (mAuth.currentUser != null){
            goToNotesFragment()
        }
    }

}