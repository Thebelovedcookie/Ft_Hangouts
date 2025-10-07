package com.example.myapplication

import android.content.ContentValues
import android.content.Context
import 	android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteDatabase
import android.widget.Toast
import android.util.Log

const val DATABASE_NAME = "MyDB"
const val TABLE_NAME = "Contact"
const val COL_NAME = "name"
const val COL_AGE = "age"
const val COL_PHONE = "phoneNumber"
const val COL_ADDRESS = "address"
const val COL_MAIL = "mail"
const val COL_ID = "id"

const val TABLE_CONVERSATIONS = "Conversations"
const val COL_CONTACT_ID = "contact_id"

const val TABLE_MESSAGE = "Message"
const val COL_MESSAGE = "content"
const val COL_IS_SENT = "isSent"
const val COL_CONV_ID = "convId"
const val COL_TIMESTAMP = "timestamp"

class DataBaseHandler(var context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {
    override fun onCreate(db: SQLiteDatabase) {
        //Table Contact
        val createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_NAME + " VARCHAR(256) NOT NULL," +
                COL_AGE + " INTEGER," +
                COL_PHONE + " VARCHAR(256) NOT NULL," +
                COL_ADDRESS + " VARCHAR(256)," +
                COL_MAIL + " VARCHAR(256))"
        db.execSQL(createTable)
        //Table Conv
        val createTableConv = "CREATE TABLE " + TABLE_CONVERSATIONS + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_CONTACT_ID + " INTEGER UNIQUE," +
                COL_PHONE + " VARCHAR(256) NOT NULL," +
                "FOREIGN KEY(" + COL_CONTACT_ID + ") REFERENCES " + TABLE_NAME + "(" + COL_ID + ") ON DELETE CASCADE" + ")"

        db.execSQL(createTableConv)
        //Table msg
        val createTableMessage = "CREATE TABLE " + TABLE_MESSAGE + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_CONV_ID + " INTEGER NOT NULL," +
                COL_MESSAGE +" TEXT NOT NULL," +
                COL_IS_SENT +" INTEGER," +
                COL_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP," +
                "FOREIGN KEY(" + COL_CONV_ID + ") REFERENCES " + TABLE_CONVERSATIONS + "(" + COL_ID + ") ON DELETE CASCADE" + ")"

        db.execSQL(createTableMessage)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CONVERSATIONS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertMessage(convId: Int, message: String, isSent: Boolean, timestamp: Long): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COL_CONV_ID, convId)
            put(COL_MESSAGE, message)
            put(COL_IS_SENT, if (isSent) 1 else 0)
            put("timestamp", timestamp)
        }
        return db.insert(TABLE_MESSAGE, null, values)
    }

    fun getIdOfConversation(phone: String): Int? {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT id FROM $TABLE_CONVERSATIONS WHERE $COL_PHONE = ?",
            arrayOf(phone)
        )

        val convId = if (cursor.moveToFirst()) cursor.getInt(0) else null
        cursor.close()
        return convId
    }

    fun getContactById(contactId: Int): Contact? {
        val db = this.readableDatabase
        var contact: Contact? = null

        val cursor = db.rawQuery(
            "SELECT id, name, phoneNumber FROM $TABLE_NAME WHERE id = ?",
            arrayOf(contactId.toString())
        )

        if (cursor.moveToFirst()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
            val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
            val phone = cursor.getString(cursor.getColumnIndexOrThrow("phoneNumber"))
            contact = Contact(id, name, 0,phone, "", "")
        }

        cursor.close()
        db.close()
        return contact
    }

    fun insertConversation(phone: String, contactId: Int?): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_CONTACT_ID, contactId)
            put(COL_PHONE, phone)
        }
        return db.insert(TABLE_CONVERSATIONS, null, values).toInt()
    }

    fun updateConversationContactId(convId: Int, contactId: Int) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_CONTACT_ID, contactId)
        }
        db.update(TABLE_CONVERSATIONS, values, "$COL_ID = ?", arrayOf(convId.toString()))
    }

    fun insertData(contact: Contact): Int {
        return try {
            val db = this.writableDatabase
            val cv = ContentValues().apply {
                put(COL_NAME, contact.name)
                put(COL_AGE, contact.age)
                put(COL_PHONE, contact.phoneNumber)
                put(COL_ADDRESS, contact.address)
                put(COL_MAIL, contact.mail)
            }

            val result = db.insert(TABLE_NAME, null, cv)

            if (result == -1L) {
                Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                0
            } else {
                Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
                result.toInt()
            }
        } catch (e: Exception) {
            Toast.makeText(context, "Error1: ${e.message}", Toast.LENGTH_LONG).show()
            e.printStackTrace()
            0
        }
    }

    fun deleteContact(id: Int) {
        try {
            val db = this.writableDatabase
            val deletedRows = db.delete(TABLE_NAME, "id=?", arrayOf(id.toString()))
            Log.d("DB", "Deleted rows: $deletedRows")
            if (deletedRows == 0) {
                Toast.makeText(context, "Nothing to erase, check the id", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(context, "Error2: ${e.message}", Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    fun readData(): List<Contact> {
        val contacts = mutableListOf<Contact>()

        try {
            val db = this.readableDatabase

            // Define a projection that specifies which columns from the database
            // you will actually use after this query.
            val projection = arrayOf(COL_ID, COL_NAME, COL_AGE, COL_PHONE, COL_ADDRESS, COL_MAIL)

            val cursor = db.query(
                TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                "$COL_NAME ASC"
            )

            with(cursor) {
                while (moveToNext()) {
                    val id = getInt(getColumnIndexOrThrow(COL_ID))
                    val name = getString(getColumnIndexOrThrow(COL_NAME))
                    val age = getInt(getColumnIndexOrThrow(COL_AGE))
                    val phone = getString(getColumnIndexOrThrow(COL_PHONE))
                    val address = getString(getColumnIndexOrThrow(COL_ADDRESS))
                    val mail = getString(getColumnIndexOrThrow(COL_MAIL))

                    contacts.add(Contact(id, name, age, phone, address, mail))
                }
            }
            cursor.close()

        } catch (e: Exception)
        {
            Toast.makeText(context, "Error3: ${e.message}", Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
        return contacts
    }

    fun updateData(contact: Contact): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(COL_NAME, contact.name)
            put(COL_AGE, contact.age)
            put(COL_PHONE, contact.phoneNumber)
            put(COL_ADDRESS, contact.address)
            put(COL_MAIL, contact.mail)
        }
        db.update(TABLE_NAME, contentValues, "id = ?", arrayOf(contact.id.toString()))
        db.close()
        return contact.id
    }

    data class ConversationPreview(
        val convId: Int,
        val contact: Contact,
        val lastMessage: String,
        val timestamp: String?
    )

    fun getConversationPreviews(): List<ConversationPreview> {
        val db = this.readableDatabase
        val list = mutableListOf<ConversationPreview>()

        val convCursor = db.rawQuery(
            "SELECT id AS conv_id, $COL_CONTACT_ID, $COL_PHONE FROM $TABLE_CONVERSATIONS",
            null
        )

        while (convCursor.moveToNext()) {
            val convId = convCursor.getInt(convCursor.getColumnIndexOrThrow("conv_id"))
            val contactId = convCursor.getInt(convCursor.getColumnIndexOrThrow(COL_CONTACT_ID))
            val phoneNumber = convCursor.getString(convCursor.getColumnIndexOrThrow(COL_PHONE))

            var contactName = ""
            if (contactId != 0) {
                val contactCursor = db.rawQuery(
                    "SELECT name FROM $TABLE_NAME WHERE id = ?",
                    arrayOf(contactId.toString())
                )
                if (contactCursor.moveToFirst()) {
                    val name = contactCursor.getString(contactCursor.getColumnIndexOrThrow(COL_NAME))
                    if (!name.isNullOrEmpty()) contactName = name
                }
                contactCursor.close()
            }

            val msgCursor = db.rawQuery(
                "SELECT content, timestamp FROM $TABLE_MESSAGE WHERE $COL_CONV_ID = ? ORDER BY id DESC LIMIT 1",
                arrayOf(convId.toString())
            )
            var lastMessage = ""
            var timestamp: String? = null
            if (msgCursor.moveToFirst()) {
                lastMessage = msgCursor.getString(msgCursor.getColumnIndexOrThrow("content"))
                timestamp = msgCursor.getString(msgCursor.getColumnIndexOrThrow("timestamp"))
            }
            msgCursor.close()

            val contact = Contact(contactId, contactName, 0, phoneNumber, "", "")
            list.add(ConversationPreview(convId, contact, lastMessage, timestamp))
        }

        convCursor.close()
        return list.sortedByDescending { it.timestamp }
    }

    fun contactExists(contactId: Int): Boolean {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT id FROM $TABLE_NAME WHERE id = ?", arrayOf(contactId.toString()))
        val exists = cursor.moveToFirst()
        cursor.close()
        return exists
    }

    fun getConversation(convId: Int): List<Message>
    {
        val dbHelper = readableDatabase
        val messages = mutableListOf<Message>()

        val cursor = dbHelper.rawQuery(
            "SELECT * FROM $TABLE_MESSAGE WHERE $COL_CONV_ID = ? ORDER BY $COL_ID ASC",
            arrayOf(convId.toString()))

        while (cursor.moveToNext())
        {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID))
            val content = cursor.getString(cursor.getColumnIndexOrThrow(COL_MESSAGE))
            val isSent = cursor.getInt(cursor.getColumnIndexOrThrow(COL_IS_SENT)) == 1
            val timestamp = cursor.getString(cursor.getColumnIndexOrThrow(COL_TIMESTAMP))

            val message = Message(id, convId, content, isSent, timestamp)
            messages.add(message)
        }

        cursor.close()
        return messages
    }
    fun getContactIdByPhoneNumber(phone: String): Int? {
        val db = this.readableDatabase
        var contactId: Int? = null


        val query = "SELECT $COL_ID FROM $TABLE_NAME WHERE $COL_PHONE = ?"
        val cursor = db.rawQuery(query, arrayOf(phone))

        if (cursor.moveToFirst()) {
            contactId = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
        }

        cursor.close()
        return contactId
    }

    fun insertContact(phoneNumber: String): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COL_PHONE, phoneNumber)
            put(COL_NAME, phoneNumber)
            put(COL_AGE, 0)
            put(COL_MAIL, "")
            put(COL_ADDRESS, "")
        }

        val newId = db.insert(TABLE_NAME, null, values)

        if (newId == -1L) {
            Log.e("DB", "Échec de l'insertion du contact ($phoneNumber)")
            return -1
        } else {
            Log.d("DB", "Contact inséré avec ID = $newId ($phoneNumber)")
            return newId.toInt()
        }
    }


}