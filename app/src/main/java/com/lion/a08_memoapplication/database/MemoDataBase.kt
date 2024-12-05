package com.lion.a08_memoapplication.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.lion.a08_memoapplication.dao.CategoryDao
import com.lion.a08_memoapplication.dao.MemoDao
import com.lion.a08_memoapplication.model.CategoryModel
import com.lion.a08_memoapplication.repository.CategoryRepository
import com.lion.a08_memoapplication.vo.CategoryVO
import com.lion.a08_memoapplication.vo.MemoVO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Database(entities = [MemoVO::class, CategoryVO::class], version = 1, exportSchema = true)
abstract class MemoDataBase : RoomDatabase(){
    // dao
    abstract fun memoDao() : MemoDao
    abstract fun categoryDao() : CategoryDao

    companion object{
        // 데이터 베이스 파일 이름
        val dataBaseFileName = "MemoDatabase.db"

        // 데이터 베이스 객체를 담을 변수
        var memoDatabase:MemoDataBase? = null
        @Synchronized
        fun getInstance(context: Context) : MemoDataBase?{
            // 만약 데이터베이스 객체가 null이라면 객체를 생성한다.
            // 데이터베이스 파일 이름 꼭 변경!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            synchronized(MemoDataBase::class){
                val memoDatabaseBuilder = Room.databaseBuilder(
                    context.applicationContext, MemoDataBase::class.java, dataBaseFileName
                )

                // 데이터 베이스 파일에 대한 사건이 발생했을 때 동작하는 콜백
                memoDatabaseBuilder.addCallback(object : Callback(){
                    // 데이터 베이스 파일이 만들어질 때 호출되는 메서드
                    // 생성된 테이블에 초기 데이터를 저장하는 작업을 한다.
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        GlobalScope.launch {
                            withContext(Dispatchers.IO){
                                val categoryModel = CategoryModel(categoryName = "기본 메모")
                                CategoryRepository.insertCategoryData(context, categoryModel)
                            }
                        }
                    }
                })

                memoDatabase = memoDatabaseBuilder.build()
            }
            return memoDatabase
        }

        // 데이터 베이스 객체가 소멸될 때 호출되는 메서드
        fun destroyInstance(){
            memoDatabase = null
        }
    }
}