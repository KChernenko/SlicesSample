package com.example.slicessample.slices

import com.example.slicessample.data.DataFactory
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.util.concurrent.TimeUnit

class EntityManager constructor(private val dataFactory: DataFactory) {

    private var disposable: Disposable? = null
    private var dispatcher: EntityManagerDispatcher? = null

    fun bind(dispatcher: EntityManagerDispatcher) {
        this.dispatcher = dispatcher
    }

    fun unbind() {
        dispatcher = null
        disposable?.dispose()
    }

    fun loadEntities(slicePath: String) {
        Timber.d("Start loading for the path $slicePath")
        disposable = dataFactory.createEntities()
                .delay(5, TimeUnit.SECONDS, Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ entities ->
                    dispatcher?.updateFoundEntites(FoundEntities(foundEntities = entities, slicePath = slicePath))
                }, { throwable ->
                    Timber.e(throwable, "Failed to load entities!")
                })
    }

}