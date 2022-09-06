package com.st4rlit.mvvmi_sample.main.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.st4rlit.mvvmi_sample.R
import com.st4rlit.mvvmi_sample.RxActivity
import com.st4rlit.mvvmi_sample.base.RxViewModel
import com.st4rlit.mvvmi_sample.common.NTViewModelProvider
import com.st4rlit.mvvmi_sample.main.viewmodel.MainViewModel

class MainActivity : RxActivity<MainViewModel>() {
    fun createIntent(from: Context): Intent {
        return Intent(from, MainActivity::class.java).also {
            val viewModel = MainViewModel(
                MainViewModel.Dependency(
                )
            )
            it.putExtra(EXTRA_VIEWMODEL_HASHCODE, NTViewModelProvider.registerViewModel(viewModel))
            // put extra if needed
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        val viewModelHashCode = intent.getStringExtra(NTViewModelProvider.registerViewModel(viewModel))
        viewModel = MainViewModel(
            MainViewModel.Dependency()
        )
    }

    override fun bindInputs() {
    }

    override fun bindOutputs() {
    }
}