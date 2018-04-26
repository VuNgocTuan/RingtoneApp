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
        val DATABASE_VERSION = 2

        val TABLE_NEWEST_RINGTONE = "tbl_ringtone_new"
        val TABLE_HOT_RINGTONE = "tbl_ringtone_hot"
        val TABLE_BEST_RINGTONE = "tbl_ringtone_best"

        private val KEY_RINGTONE_ID = "id"
        val KEY_RINGTONE_NAME = "name"
        val KEY_RINGTONE_AUTHOR = "author"
        val KEY_RINGTONE_URL = "url"

        private val CREATE_NEWEST_RINGTONE_TABLE = "CREATE TABLE $TABLE_NEWEST_RINGTONE ( " +
                KEY_RINGTONE_ID + " INTEGER PRIMARY KEY," +
                KEY_RINGTONE_NAME + " TEXT," +
                KEY_RINGTONE_AUTHOR + " TEXT," +
                KEY_RINGTONE_URL + " TEXT" +
                ")"

        private val CREATE_HOT_RINGTONE_TABLE = "CREATE TABLE $TABLE_HOT_RINGTONE ( " +
                KEY_RINGTONE_ID + " INTEGER PRIMARY KEY," +
                KEY_RINGTONE_NAME + " TEXT," +
                KEY_RINGTONE_AUTHOR + " TEXT," +
                KEY_RINGTONE_URL + " TEXT" +
                ")"

        private val CREATE_BEST_RINGTONE_TABLE = "CREATE TABLE $TABLE_BEST_RINGTONE ( " +
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
        db?.execSQL(CREATE_NEWEST_RINGTONE_TABLE)
        db?.execSQL(CREATE_HOT_RINGTONE_TABLE)
        db?.execSQL(CREATE_BEST_RINGTONE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (oldVersion != newVersion) {
            db?.execSQL("DROP TABLE IF EXISTS " + TABLE_NEWEST_RINGTONE)
            db?.execSQL("DROP TABLE IF EXISTS " + TABLE_HOT_RINGTONE)
            db?.execSQL("DROP TABLE IF EXISTS " + TABLE_BEST_RINGTONE)
            onCreate(db)
        }
    }

    fun addNewestRingtone(ringtone: Ringtone, table: String) {
        val db = writableDatabase
        db.beginTransaction()
        try {
            val values = ContentValues()
            values.put(RingtoneDatabaseHelper.KEY_RINGTONE_NAME, ringtone.name)
            values.put(RingtoneDatabaseHelper.KEY_RINGTONE_AUTHOR, ringtone.author)
            values.put(RingtoneDatabaseHelper.KEY_RINGTONE_URL, ringtone.url)
            db.insertOrThrow(table, null, values)
            db.setTransactionSuccessful()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            db.endTransaction()
            db.close()
        }
    }

    fun getRingtoneList(table: String): MutableList<Ringtone> {
        val newestRingtone = mutableListOf<Ringtone>()
        val db = readableDatabase
        val cursor = db.query(
                table,
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

    fun deleteAllRecords(table: String) {
        val db = writableDatabase
        db.beginTransaction()
        try {
            db.execSQL("delete from $table")
            db.setTransactionSuccessful()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            db.endTransaction()
            db.close()
        }
    }
}