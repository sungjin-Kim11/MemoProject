package com.lion.a08_memoapplication.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.lion.a08_memoapplication.vo.MemoVO

@Dao
interface MemoDao {
    // 메모 내용을 저장하는 메서드
    @Insert
    fun insertMemoData(memoVO: MemoVO)

    // 모든 메모 데이터를 가져오는 메서드
    @Query("""
        select * from memotable
        order by memoIdx desc
    """)
    fun selectMemoDataAll() : List<MemoVO>

    // 즐겨찾기가 설정되어 있는 메모 데이터를 가져오는 메서드
    @Query("""
        select * from memotable
        where memoIsFavorite = :memoIsFavorite
        order by memoIdx desc
    """)
    fun selectMemoDataAllByFavorite(memoIsFavorite:Boolean) : List<MemoVO>

    // 비밀메모로 설정되어 있는 메모 데이터를 가져오는 메서드
    @Query("""
        select * from memotable
        where memoIsSecret = :memoIsSecret
        order by memoIdx desc
    """)
    fun selectMemoDataAllBySecret(memoIsSecret:Boolean) : List<MemoVO>

    // 카테고리 번호를 통해 메모 데이터를 가져오는 메서드
    @Query("""
        select * from memotable
        where memoCategoryIdx = :categoryIdx
        order by memoIdx desc
    """)
    fun selectMemoDataAllByCategoryIdx(categoryIdx:Int) : List<MemoVO>

    // 즐겨찾기 값을 설정하는 메서드
    @Query("""
        update MemoTable
        set memoIsFavorite = :memoIsFavorite
        where memoIdx = :memoIdx
    """)
    fun updateMemoFavorite(memoIdx:Int, memoIsFavorite: Boolean)

    // 메모 번호를 통해 메모데이터를 가져온다.
    @Query("""
        select * from MemoTable
        where memoIdx = :memoIdx
    """)
    fun selectMemoDataByMemoIdx(memoIdx:Int) : MemoVO

    // 메모를 삭제하는 메서드
    @Delete
    fun deleteMemoDataByMemoIdx(memoVO: MemoVO)

    // 메모를 수정하는 메서드
    @Update
    fun updateMemoData(memoVO: MemoVO)

    // 메모의 카테고리 번호를 변경하는 메서드
    @Query("""
        update MemoTable
        set memoCategoryIdx = :newCategoryIdx
        where memoCategoryIdx = :oldCategoryIdx
    """)
    fun updateMemoCategoryIdx(oldCategoryIdx:Int, newCategoryIdx:Int)

    // 메모의 카테고리 번호로 메모를 삭제하는 메서드
    @Query("""
        delete from MemoTable
        where memoCategoryIdx = :deleteCategoryIdx
    """)
    fun deleteMemoDataByCategoryIdx(deleteCategoryIdx:Int)
}