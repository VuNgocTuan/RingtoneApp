package ringtone.studio.vuetu.ringtoneapp.repository.sqlite

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import ringtone.studio.vuetu.ringtoneapp.repository.model.Ringtone

/**
 * Created by vungoctuan on 4/21/18.
 */
class RingtoneDatabaseHelper(context: Context?) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        val DATABASE_NAME = "ringtoneDatabase"
        val DATABASE_VERSION = 1

        private val TABLE_NEWEST_RINGTONE = "tbl_ringtone_new"

        private val KEY_RINGTONE_ID = "id"
        val KEY_RINGTONE_NAME = "name"
        val KEY_RINGTONE_AUTHOR = "author"
        val KEY_RINGTONE_URL = "url"

        private val CREATE_RINGTONE_TABLE = "CREATE TABLE $TABLE_NEWEST_RINGTONE ( " +
                KEY_RINGTONE_ID + " INTEGER PRIMARY KEY," +
                KEY_RINGTONE_NAME + " TEXT," +
                KEY_RINGTONE_AUTHOR + " TEXT," +
                KEY_RINGTONE_URL + " TEXT" +
                ")"

        private var sInstance: RingtoneDatabaseHelper? = null
        fun getInstance(context: Context?): RingtoneDatabaseHelper? {
            if (sInstance == null) {
                sInstance = RingtoneDatabaseHelper(context?.applicationContext)
            }
            return sInstance
        }
    }

    override fun onConfigure(db: SQLiteDatabase?) {
        super.onConfigure(db)
        db?.setForeignKeyConstraintsEnabled(true)
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_RINGTONE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (oldVersion != newVersion) {
            db?.execSQL("DROP TABLE IF EXISTS " + TABLE_NEWEST_RINGTONE)
            onCreate(db)
        }
    }

    fun addNewestRingtone(ringtone: Ringtone) {
        val db = writableDatabase
        db.beginTransaction()
        try {
            val values = ContentValues()
//            values.put(KEY_RINGTONE_ID, ringtone.id)
            values.put(RingtoneDatabaseHelper.KEY_RINGTONE_NAME, ringtone.name)
            values.put(RingtoneDatabaseHelper.KEY_RINGTONE_AUTHOR, ringtone.author)
            values.put(RingtoneDatabaseHelper.KEY_RINGTONE_URL, ringtone.url)
            db.insertOrThrow(RingtoneDatabaseHelper.TABLE_NEWEST_RINGTONE, null, values)
            db.setTransactionSuccessful()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            db.endTransaction()
            db.close()
        }
    }

    fun getNewestRingtone(): MutableList<Ringtone> {
        val newestRingtone = mutableListOf<Ringtone>()
        val db = readableDatabase
        val cursor = db.query(
                RingtoneDatabaseHelper.TABLE_NEWEST_RINGTONE,
                null,
                null,
                null,
                null,
                null,
                null)
        try {
            if (cursor.moveToNext()) {
                do {
                    val ringtone = Ringtone(
                            cursor.getInt(cursor.getColumnIndex(KEY_RINGTONE_ID)),
                            cursor.getString(cursor.getColumnIndex(KEY_RINGTONE_NAME)),
                            cursor.getString(cursor.getColumnIndex(KEY_RINGTONE_AUTHOR)),
                            cursor.getString(cursor.getColumnIndex(KEY_RINGTONE_URL))
                    )
                    newestRingtone.add(ringtone)
                } while (cursor.moveToNext())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            db.close()
            if (cursor != null && !cursor.isClosed) {
                cursor.close()
            }
        }
        return newestRingtone
    }

    fun deleteAllRecords() {
        val db = writableDatabase
        db.beginTransaction()
        try {
            db.execSQL("delete from $TABLE_NEWEST_RINGTONE")
            db.setTransactionSuccessful()
        } catch (e: Exception) {
            e.printStackTrace()
        }finally {
            db.endTransaction()
            db.close()
        }
    }
}