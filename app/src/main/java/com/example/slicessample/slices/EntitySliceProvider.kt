package com.example.slicessample.slices

import android.app.PendingIntent
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.IconCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.net.toUri
import androidx.slice.Slice
import androidx.slice.SliceProvider
import androidx.slice.builders.ListBuilder
import androidx.slice.builders.SliceAction
import com.example.slicessample.MainActivity
import com.example.slicessample.R
import com.example.slicessample.data.DataFactory
import timber.log.Timber

class EntitySliceProvider: SliceProvider(), EntityManagerDispatcher {

    private var foundEntities: FoundEntities? = null
    private var startBitmap: Bitmap? = null

    private lateinit var manager: EntityManager

    companion object {
        const val AUTHORITY = "content://com.example.slicessample"
    }

    override fun onCreateSliceProvider(): Boolean {
        manager = EntityManager(DataFactory())
        manager.bind(this)
        return true
    }

    override fun shutdown() {
        manager.unbind()
        super.shutdown()
    }

    override fun onMapIntentToUri(intent: Intent): Uri {
        Timber.d("intent is: ${intent.getStringExtra("dat")}")
        return super.onMapIntentToUri(intent)
    }

    override fun onBindSlice(sliceUri: Uri): Slice? {
        return if (sliceUri.toString().contains("test", true)) {
            if (foundEntities == null) {
                manager.loadEntities(AUTHORITY + sliceUri.path)
                ListBuilder(context!!, sliceUri, ListBuilder.INFINITY)
                        .addRow {
                            it.title = "Loading..."
                            it.primaryAction = emptyAction()
                        }
                        .build()
            } else {
                val builder = ListBuilder(context!!, sliceUri, ListBuilder.INFINITY)
                builder.setHeader {
                    it.title = "List click sample app"
                    it.subtitle = "List of entities:"
                }
                foundEntities?.foundEntities?.forEach { entity ->
                    builder.addRow {
                        it.title = entity.name
                        it.subtitle = entity.totalTime
                        it.primaryAction = entityAction(entity.id)
                    }
                }
                return builder.build()
            }
        } else {
            null
        }
    }

    override fun updateFoundEntites(foundEntities: FoundEntities) {
        this.foundEntities = foundEntities
        context?.contentResolver?.notifyChange(foundEntities.slicePath.toUri(), null)
    }

    // Kinda does nothing - to omit Runtime crash when action not set
    private fun emptyAction(): SliceAction {
        val intent = Intent()
        return SliceAction(PendingIntent.getActivity(context, 0, intent, 0),
                IconCompat.createWithResource(context!!, R.drawable.ic_start), "")
    }

    private fun entityAction(entityId: Int): SliceAction {
        Timber.d("Id is: $entityId")
        val intent = MainActivity.createIntent(context!!, entityId)
        if (startBitmap == null) {
            startBitmap = ContextCompat.getDrawable(context!!, R.drawable.ic_start)!!.toBitmap()
        }
        return SliceAction(PendingIntent.getActivity(context, 0, intent, 0),
                IconCompat.createWithBitmap(startBitmap), "Start!")
    }
}