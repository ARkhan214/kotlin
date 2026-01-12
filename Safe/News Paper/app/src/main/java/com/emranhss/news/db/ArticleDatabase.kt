package com.emranhss.news.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.emranhss.news.models.Article
import okio.Lock

@Database(
    entities = [Article::class],
    version = 3
)

@TypeConverters(Converters::class)
abstract class ArticleDatabase : RoomDatabase() {

    abstract  fun getArticleDao(): ArticleDao

    companion object{
        @Volatile
        private var intance : ArticleDatabase? = null
        private val Lock = Any()

        operator  fun invoke(context: Context) = intance?: synchronized(Lock){
         intance ?: createDatabase(context).also{intance = it }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                ArticleDatabase::class.java,
                name = "article_db.db"
            ).build()
    }

}