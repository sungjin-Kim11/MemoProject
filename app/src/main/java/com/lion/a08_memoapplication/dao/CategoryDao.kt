package com.lion.a08_memoapplication.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.lion.a08_memoapplication.vo.CategoryVO

@Dao
interface CategoryDao {

    // 모든 카테고리 정보를 가져오는 메서드
    @Query("""
        select * from CategoryTable
        order by categoryIdx
    """)
    fun selectCategoryAll() : List<CategoryVO>

    // 카테고리 정보를 저장하는 메서드
    @Insert
    fun insertCategoryData(categoryVO: CategoryVO)

    // 카테고리 번호를 통해 카테정보를 가져오는 메서드
    @Query("""
        select * from CategoryTable
        where categoryIdx = :categoryIdx
    """)
    fun selectCategoryDataByCategoryIdx(categoryIdx:Int) : CategoryVO

    // 카테고리 삭제
    @Delete
    fun deleteCategoryDataByCategoryIdx(categoryVO: CategoryVO)

//    // 카테고리 이름으로 카테고리 정보를 가져오는 메서드
//    @Query("""
//        select * from CategoryTable
//        where categoryName = :categoryName
//        order by categoryIdx desc
//    """)
//    fun selectCategoryDataAllByCategoryName(categoryName:String):List<CategoryVO>

    // 카테고리 이름의 요소를 입력하면 해당 요소를 가지고 있는 모든 정보를 가져오는 메서드
    @Query("""
    SELECT * FROM CategoryTable 
    WHERE categoryName LIKE :keyword
    """)
    fun selectCategoryDataAllByKeyword(keyword: String): List<CategoryVO>
}