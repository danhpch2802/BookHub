package com.androidrealm.bookhub.Controllers.DAO
import android.content.Context
import androidx.room.*
import com.androidrealm.bookhub.Models.DownloadBook

@Dao
public interface DownloadBookDAO {
    @Query("Select * from downloadbook")
    fun getDownloadBookList() : List<DownloadBook>
    @Query("SELECT * FROM downloadbook WHERE name LIKE :name and chapterName like:chapterName")
    fun findByNameAndChapter(name: String,chapterName:String): DownloadBook
    @Insert
    fun insertDownloadBook(downloadBook: DownloadBook )
    @Update
    fun updateDownloadBook(downloadBook: DownloadBook)
    @Delete
    fun deteleDownloadBook(downloadBook: DownloadBook)
}

@Database(entities = arrayOf(DownloadBook::class), version = 1)
abstract class DownloadBookDatabase : RoomDatabase() {
    abstract fun downloadBookDAO() : DownloadBookDAO
    companion object {
        val DB_NAME = "downloadbook_db"
        private var instance: DownloadBookDatabase? = null
        fun getInstance(context: Context): DownloadBookDatabase {
            return instance ?: buildDatabase(context).also { instance = it}
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context, DownloadBookDatabase::class.java,
                DB_NAME).allowMainThreadQueries().build()

    }
}