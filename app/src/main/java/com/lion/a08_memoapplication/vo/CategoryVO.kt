package com.lion.a08_memoapplication.vo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "CategoryTable")
data class CategoryVO(
    // 카테고리 번호
    @PrimaryKey(autoGenerate = true)
    var categoryIdx:Int = 0,
    // 카테고리 이름
    var categoryName:String = ""
)