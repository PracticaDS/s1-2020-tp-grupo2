package ar.edu.unq.pdes.myprivateblog.rx

import io.reactivex.CompletableTransformer
import io.reactivex.FlowableTransformer
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

object RxSchedulers {
    fun <T> observableAsync(): ObservableTransformer<T, T> {
        return ObservableTransformer { observable ->
            observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }
    }

    fun completableAsync(): CompletableTransformer {
        return CompletableTransformer { observable ->
            observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }
    }

    fun <T> observableCompute(): ObservableTransformer<T, T> {
        return ObservableTransformer { observable ->
            observable.subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
        }
    }

    fun <T> observableMainThread(): ObservableTransformer<T, T> {
        return ObservableTransformer { observable -> observable.observeOn(AndroidSchedulers.mainThread()) }
    }

    fun <T> flowableMainThread(): FlowableTransformer<T, T> {
        return FlowableTransformer { flowable -> flowable.observeOn(AndroidSchedulers.mainThread()) }
    }

    fun <T> flowableAsync(): FlowableTransformer<T, T> {
        return FlowableTransformer { flowable ->
            flowable.subscribeOn(Schedulers.io()).observeOn(
                AndroidSchedulers.mainThread()
            )
        }
    }

    fun <T> flowableCompute(): FlowableTransformer<T, T> {
        return FlowableTransformer { flowable ->
            flowable.subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())
        }
    }
}