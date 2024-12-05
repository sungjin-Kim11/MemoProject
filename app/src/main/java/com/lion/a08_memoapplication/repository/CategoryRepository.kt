package com.lion.a08_memoapplication.repository

import android.content.Context
import androidx.room.Query
import com.lion.a08_memoapplication.database.MemoDataBase
import com.lion.a08_memoapplication.database.MemoDataBase.Companion.memoDatabase
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

//        // 카테고리 이름으로 검색하여 메모 데이터 전체를 가져오는 메서드
//        fun selectCategoryDataAllByCategoryName(context: Context, categoryname:String) : MutableList<CategoryModel>{
//            // 데이터를 가져온다.
//            val categoryDatabase = MemoDataBase.getInstance(context)
//            val categoryList = memoDatabase?.categoryDao()?.selectCategoryDataAllByCategoryName(categoryname)
//
//            // 카테고리 데이터를 담을 리스트
//            val tempList = mutableListOf<CategoryModel>()
//
//            // 카테고리의 수 만큼 반복한다.
//            categoryList?.forEach {
//                val categoryModel = CategoryModel(
//                    categoryIdx = it.categoryIdx,
//                    categoryName = it.categoryName
//                )
//                // 리스트에 담는다.
//                tempList.add(categoryModel)
//            }
//            return tempList
//        }

        // 키워드로 검색하여 카테고리 데이터를 가져오는 메서드
        fun selectCategoryDataAllByKeyword(context: Context, keyword: String): MutableList<CategoryModel> {
            val memoDatabase = MemoDataBase.getInstance(context)

            // 키워드에 맞는 데이터를 가져온다.
            val queryKeyword = "%$keyword%"
            val categoryList = memoDatabase?.categoryDao()?.selectCategoryDataAllByKeyword(queryKeyword)

            // 카테고리 데이터를 담을 리스트 생성
            val tempList = mutableListOf<CategoryModel>()

            // 가져온 카테고리 데이터를 반복하여 변환
            categoryList?.forEach {
                val categoryModel = CategoryModel(
                    categoryIdx = it.categoryIdx,
                    categoryName = it.categoryName
                )
                // 변환된 데이터를 리스트에 추가
                tempList.add(categoryModel)
            }
            return tempList
        }
    }
}