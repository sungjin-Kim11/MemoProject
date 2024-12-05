package com.lion.a08_memoapplication.repository

import android.content.Context
import androidx.room.Query
import com.lion.a08_memoapplication.database.MemoDataBase
import com.lion.a08_memoapplication.model.CategoryModel
import com.lion.a08_memoapplication.vo.CategoryVO

class CategoryRepository {
    companion object{

        // 모든 카테고리 정보를 가져온다.
        fun selectCategoryAll(context:Context) : MutableList<CategoryModel>{
            val memoDataBase = MemoDataBase.getInstance(context)
            val dataList = memoDataBase?.categoryDao()?.selectCategoryAll()

            val categoryList = mutableListOf<CategoryModel>()
            dataList?.forEach{
                val categoryModel = CategoryModel(
                    categoryIdx = it.categoryIdx,
                    categoryName = it.categoryName
                )
                categoryList.add(categoryModel)
            }
            return categoryList
        }

        // 카테고리 정보를 저장한다.
        fun insertCategoryData(context: Context, categoryModel: CategoryModel){
            // VO에 데이터를 담아준다.
            val categoryVO = CategoryVO(
                categoryName = categoryModel.categoryName
            )
            // 저장한다.
            val memoDataBase = MemoDataBase.getInstance(context)
            memoDataBase?.categoryDao()?.insertCategoryData(categoryVO)
        }

        // 카테고리 번호를 통해 카테정보를 가져오는 메서드
        fun selectCategoryDataByCategoryIdx(context: Context, categoryIdx:Int) : CategoryModel{
            val memoDataBase = MemoDataBase.getInstance(context)
            val categoryVO = memoDataBase?.categoryDao()?.selectCategoryDataByCategoryIdx(categoryIdx)!!
            val categoryModel = CategoryModel(
                categoryIdx = categoryVO.categoryIdx,
                categoryName = categoryVO.categoryName
            )
            return categoryModel
        }

        // 메모의 카테고리 번호로 메모를 삭제하는 메서드
        fun deleteMemoDataByCategoryIdx(context: Context, deleteCategoryIdx:Int){
            val categoryVO = CategoryVO(categoryIdx = deleteCategoryIdx)
            val memoDataBase = MemoDataBase.getInstance(context)
            memoDataBase?.categoryDao()?.deleteCategoryDataByCategoryIdx(categoryVO)
        }
    }
}