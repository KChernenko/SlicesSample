package com.example.slicessample

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private var entityId: Int = -1

    companion object {
        private const val ID_EXTRA = "id_extra"

        fun createIntent(context: Context, entityId: Int): Intent {
            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra(ID_EXTRA, entityId)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        entityId = intent.getIntExtra(ID_EXTRA, -1)

        Timber.d("Selected id is: $entityId")
    }
}
