package com.st4rlit.mvvmi_sample.main.viewmodel

import com.st4rlit.mvvmi_sample.base.RxViewModel
import com.st4rlit.mvvmi_sample.base.RxViewModelInterface
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.subjects.PublishSubject

class MainViewModel() : RxViewModel() {
    class Input(
        var init: Observer<Unit>
    ) : RxViewModelInterface.Input()

    class Output(
        var error: Observable<String>
    ) : RxViewModelInterface.Output()

    class Dependency(

    ) : RxViewModelInterface.Dependency()

    override lateinit var input: Input
    override lateinit var output: Output
    override lateinit var dependency: Dependency

    private var initSubject = PublishSubject.create<Unit>()

    private var errorSubject = PublishSubject.create<String>()

    constructor(dependency: Dependency) : this() {
        this.dependency = dependency
        this.input =
            Input(
                init = initSubject
            )
        this.output =
            Output(
                error = errorSubject
            )

        bindInputs()
        bindOutputs()
    }

    private fun bindInputs() {

    }

    private fun bindOutputs() {

    }
}