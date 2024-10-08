package com.example.readerapp.screens.login

import android.content.Context
import android.util.Log
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.readerapp.R
import com.example.readerapp.model.MUser
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class LoginScreenViewModel : ViewModel() {
    //val loadingState = MutableStateFlow(LoadingState.IDLE)
    private val auth: FirebaseAuth = Firebase.auth

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    fun signInUserWithEmailAndPassword(
        email: String,
        password: String,
        failed: (String) -> Unit = {},
        home: () -> Unit
    ) = viewModelScope.launch {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                Log.d("FB", "signInWithEmailAndPassword: Success!")
                home()
            }
            .addOnFailureListener { ex ->
                Log.d("FB", "signInWithEmailAndPassword Failure: ${ex.message}")
                ex.localizedMessage?.let { failed(it.toString()) }
            }
    }


    fun createUserWithEmailAndPassword(
        email: String,
        password: String,
        context : Context,
        failed: (String) -> Unit = {},
        home: () -> Unit
    ) {
        if (_loading.value == false) {
            _loading.value = true
            auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    Log.d("FB", "createUserWithEmailAndPassword: Success!")
                    val displayName = it.user?.email?.split('@')?.get(0)
                    createUser(displayName)
                    home()
                    _loading.value = false
                }
                .addOnFailureListener { ex ->
                    if (ex.message == "The email address is already in use by another account.") {
                        signInUserWithEmailAndPassword(email, password, failed = {
                            failed(it)
                        }) {
                            failed(context.getString(R.string.AccountAlreadyExistsLogin))
                            home()
                        }
                    } else {
                        Log.d("FB", "createUserWithEmailAndPassword Failure: ${ex.message}")
                        ex.localizedMessage?.let { failed(it.toString()) }
                    }
                    _loading.value = false
                }
        }
    }

    private fun createUser(displayName: String?) {
        val userId = auth.currentUser?.uid

        val user = MUser(
            id = null,
            userId = userId.toString(),
            displayName = displayName.toString(),
            avatarUrl = "",
            qoute = "Life is great",
            profession = "Android Developer"
        ).toMap()

        FirebaseFirestore.getInstance().collection("users")
            .add(user)
    }
}