package com.example.myapplication

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri

class SmsProvider : ContentProvider() {

    override fun onCreate(): Boolean = true

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        val cursor = MatrixCursor(
            arrayOf(
                "_id", "thread_id", "address", "person", "date",
                "body", "type", "read"
            )
        )
        return cursor
    }

    override fun getType(uri: Uri): String? {
        return when (uri.lastPathSegment) {
            "inbox" -> "vnd.android.cursor.dir/sms"
            "sent" -> "vnd.android.cursor.dir/sms"
            else -> "vnd.android.cursor.dir/sms"
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return null
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        return 0
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        return 0
    }
}
