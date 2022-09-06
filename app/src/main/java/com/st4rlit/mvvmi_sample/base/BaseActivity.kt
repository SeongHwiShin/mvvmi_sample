package com.st4rlit.mvvmi_sample

import android.app.ActivityManager
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.st4rlit.mvvmi_sample.base.Deinitializable
import com.st4rlit.mvvmi_sample.base.RxViewModel
import com.st4rlit.mvvmi_sample.common.NTViewModelProvider
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.json.JSONException
import org.json.JSONObject


abstract class RxActivity<ViewModel : RxViewModel> : AppCompatActivity(), Deinitializable {

    lateinit var viewModel: ViewModel
    var disposeBag = CompositeDisposable()

    abstract fun bindInputs()
    abstract fun bindOutputs()

    override fun deinitialize() {
        if (!disposeBag.isDisposed)
            disposeBag.dispose()
        viewModel.deinitialize()
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
    }

    // Stop에서 구독중인 Disposable 해제
    override fun onStop() {
        super.onStop()
        NTViewModelProvider.registerViewModel(viewModel)
        disposeBag.clear()

        val activityApp = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        val list = activityApp.runningAppProcesses

        if (list == null || list.isEmpty()) return

    }

    protected open fun getScreenName(): String {
        return javaClass.simpleName
    }

    // Start에서 바인딩
    override fun onStart() {
        super.onStart()
        bindBaseOutput()
        bindInputs()
        bindOutputs()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun startActivity(intent: Intent) {
        super.startActivity(intent)
//        overridePendingTransition(R.anim.start_enter, R.anim.start_exit)
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onDestroy() {
        deinitialize()
        super.onDestroy()
    }

    private fun bindBaseOutput() {


        viewModel.baseOutput.error
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { message ->
//                AlertDialog.Builder(this, R.style.AppBaseThemeDialog)
//                    .setTitle("트로스트")
//                    .setMessage("서버와의 연결에 실패하였습니다. 잠시 후 다시 시도해주세요. $message")
//                    .setPositiveButton("확인", null)
//                    .show()
            }
            .apply { disposeBag.add(this) }
    }


    protected fun showErrorAlert(message: String?) {
        runOnUiThread {
            var errorMessage = message
            if (message.isNullOrEmpty()) {
                errorMessage = "서버와의 연결에 실패하였습니다. 잠시 후 다시 시도해주세요."
            }
            AlertDialog.Builder(this, androidx.appcompat.R.style.AlertDialog_AppCompat)
                .setTitle("트로스트")
                .setMessage(errorMessage)
                .setPositiveButton("확인", null)
                .show()
        }
    }

    open fun hideKeyboard(): Boolean {
        if (currentFocus != null) {
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            return imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
        return false
    }

    fun <T> Single<T>.withCommonErrorHandle(): Single<T> {
        return this.onErrorResumeNext {

//            showErrorAlert(it)
            return@onErrorResumeNext Single.never()
        }
    }

    companion object {
        val EXTRA_VIEWMODEL_HASHCODE = "extra_view_model_hash_code"
        var isBackground = false

    }
}