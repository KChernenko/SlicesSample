package com.example.slicessample.data

import io.reactivex.Single

class DataFactory {

    fun createEntities(): Single<List<Entity>> {
        val entitiesList = mutableListOf<Entity>()

        for (i in 0..10) {
            entitiesList.add(Entity(i, "Entity #$i", "00:0$i"))
        }

        return Single.just(entitiesList)
    }
}