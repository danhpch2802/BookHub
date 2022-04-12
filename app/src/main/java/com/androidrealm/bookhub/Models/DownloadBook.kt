package com.androidrealm.bookhub.Models
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.androidrealm.bookhub.Controllers.DAO.DownloadBookDAO

@Entity(tableName = "downloadbook")
data class DownloadBook (
    @ColumnInfo(name = "imagePath" )
    var imagePath:String,
    @ColumnInfo(name = "chapterName" )
    var chapterName:String,
    @ColumnInfo(name = "chapterPath" )
    var chapterPath:String,
    @ColumnInfo(name = "name" )
    var name:String,
    @ColumnInfo(name = "summary")
    var summary:String, //
    @ColumnInfo(name = "author" )
    var author:String,

    ){
    @PrimaryKey(autoGenerate = true)
    var id:Int=0
}



