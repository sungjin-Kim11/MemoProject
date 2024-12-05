package com.lion.a08_memoapplication.vo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "MemoTable")
data class MemoVO(
    // 메모 번호
    @PrimaryKey(autoGenerate = true)
    var memoIdx:Int = 0,
    // 메모 제목
    var memoTitle:String = "",
    // 메모 내용
    var memoText:String = "",
    // 비밀 메모 여부
    var memoIsSecret:Boolean = false,
    // 즐겨찾기 여부
    var memoIsFavorite:Boolean = false,
    // 비밀번호
    var memoPassword:String = "",
    // 카테고리 번호
    var memoCategoryIdx:Int = 0
)