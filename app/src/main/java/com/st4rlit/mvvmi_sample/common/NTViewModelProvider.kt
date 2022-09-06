package com.st4rlit.mvvmi_sample.common

import com.st4rlit.mvvmi_sample.base.RxViewModel

object NTViewModelProvider {
    var viewModelMap: HashMap<String, RxViewModel> = HashMap()

    fun registerViewModel(viewModel: RxViewModel): String {
        val viewModelHashCode = viewModel.hashCode().toString()
        viewModelMap[viewModelHashCode] = viewModel
        return viewModelHashCode
    }

    inline fun <reified T : RxViewModel> provideViewModel(viewModelHashCode: String, from: Any? = null) : T {
        if ( viewModelMap.containsKey(viewModelHashCode)) {
            val viewModel = viewModelMap[viewModelHashCode] as T
            viewModelMap.remove(viewModelHashCode)
            return viewModel
        }
        else if ( from as? ViewModelRevivable != null) {
            return from.reviveViewModel() as T
        }
        throw IllegalStateException("뷰모델을 찾을 수 없습니다. ${T::class.java.name}")
    }
}

/**
 * 앱 강제종료 등이 일어나서 주입받을 수 없을 때 뷰모델을 필요로 하는 객체가 이 인터페이스를 구현했다면 직접 뷰모델을 생성하여 제공할 수 있도록 함
 * (팩토리를 반드시 구현하지 않아도 되게 하기 위함)
 */
interface ViewModelRevivable {
    fun reviveViewModel(): RxViewModel
}