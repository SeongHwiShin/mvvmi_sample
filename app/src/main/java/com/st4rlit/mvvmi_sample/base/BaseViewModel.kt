package com.st4rlit.mvvmi_sample.base

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.PublishSubject
interface RxViewModelInterface {
    abstract class Input
    abstract class Output
    abstract class Dependency

    val input : Input
    val output: Output
    val dependency: Dependency
}

abstract class RxViewModel: RxViewModelInterface, Deinitializable {
    var disposeBag = CompositeDisposable()
    protected var showIndicatorSubject: PublishSubject<Boolean> = PublishSubject.create()
    protected var commonErrorSubject: PublishSubject<String> = PublishSubject.create()
    var baseOutput: BaseOutput = BaseOutput(showIndicatorSubject, commonErrorSubject)

    override fun deinitialize() {
        //TODO: 재사용을 위해서 dispose는 일단 막음, Activity 생명주기 상의 clear로 대체
//        if (!disposeBag.isDisposed)
//            disposeBag.dispose()
    }

    data class BaseOutput (
        var showIndicator: Observable<Boolean>,
        var error: Observable<String>
    )


    fun <T> Observable<T>.showLoadingIndicator() : Observable<T> {
        return this.map {
            showIndicatorSubject.onNext(true)
            return@map it
        }
    }

    fun <T> Observable<T>.hideLoadingIndicator() : Observable<T> {
        return this.map {
            showIndicatorSubject.onNext(false)
            return@map it
        }
    }

    fun <T> Observable<T>.withCommonErrorHandle() : Observable<T> {
        return this.onErrorResumeNext {
            showIndicatorSubject.onNext(false)
            commonErrorSubject.onNext(it.localizedMessage ?: "")
            return@onErrorResumeNext Observable.empty()
        }
    }

    fun <T> Single<T>.withLoadingIndicator(): Single<T> {
        return doOnSubscribe { showIndicatorSubject.onNext(true) }
            .doOnSuccess { showIndicatorSubject.onNext(false) }
    }

    fun <T> Single<T>.withCommonErrorHandle() : Single<T> {
        return this.onErrorResumeNext {
            showIndicatorSubject.onNext(false)
            commonErrorSubject.onNext(it.localizedMessage ?: "")
            return@onErrorResumeNext Single.never()
        }
    }

}
