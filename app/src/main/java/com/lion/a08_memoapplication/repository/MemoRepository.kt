package com.lion.a08_memoapplication.repository

import android.content.Context
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Update
import com.lion.a08_memoapplication.database.MemoDataBase
import com.lion.a08_memoapplication.model.MemoModel
import com.lion.a08_memoapplication.vo.MemoVO

class MemoRepository {
    companion object{
        // 메모 내용을 저장하는 메서드
        fun insertMemoData(context:Context, memoModel: MemoModel){
            // Model에 있는 정보를 VO에 담는다.
            val memoVO = MemoVO(
                memoTitle = memoModel.memoTitle,
                memoText = memoModel.memoText,
                memoIsSecret = memoModel.memoIsSecret,
                memoIsFavorite = memoModel.memoIsFavorite,
                memoPassword =  memoModel.memoPassword,
                memoCategoryIdx = memoModel.memoCategoryIdx
            )
            // 데이터를 저장한다.
            val memoDataBase = MemoDataBase.getInstance(context)
            memoDataBase?.memoDao()?.insertMemoData(memoVO)
        }

        // 모든 메모 데이터를 가져오는 메서드
        fun selectMemoDataAll(context: Context) : MutableList<MemoModel>{
            // 데이터를 가져온다.
            val memoDataBase = MemoDataBase.getInstance(context)
            val tempList = memoDataBase?.memoDao()?.selectMemoDataAll()
            // MemoModel에 담아준다.
            val memoList = mutableListOf<MemoModel>()
            tempList?.forEach {
                val memoModel = MemoModel(
                    memoIdx = it.memoIdx,
                    memoTitle = it.memoTitle,
                    memoText =  it.memoText,
                    memoIsSecret = it.memoIsSecret,
                    memoIsFavorite = it.memoIsFavorite,
                    memoPassword = it.memoPassword,
                    memoCategoryIdx = it.memoCategoryIdx
                )
                memoList.add(memoModel)
            }
            return memoList
        }


        // 즐겨찾기가 설정되어 있는 메모 데이터를 가져오는 메서드
        fun selectMemoDataAllByFavorite(context: Context, memoIsFavorite:Boolean) : MutableList<MemoModel>{
            // 데이터를 가져온다.
            val memoDataBase = MemoDataBase.getInstance(context)
            val tempList = memoDataBase?.memoDao()?.selectMemoDataAllByFavorite(memoIsFavorite)
            // MemoModel에 담아준다.
            val memoList = mutableListOf<MemoModel>()
            tempList?.forEach {
                val memoModel = MemoModel(
                    memoIdx = it.memoIdx,
                    memoTitle = it.memoTitle,
                    memoText =  it.memoText,
                    memoIsSecret = it.memoIsSecret,
                    memoIsFavorite = it.memoIsFavorite,
                    memoPassword = it.memoPassword,
                    memoCategoryIdx = it.memoCategoryIdx
                )
                memoList.add(memoModel)
            }
            return memoList
        }

        // 비밀메모로 설정되어 있는 메모 데이터를 가져오는 메서드
        fun selectMemoDataAllBySecret(context: Context, memoIsSecret:Boolean) : MutableList<MemoModel>{
            // 데이터를 가져온다.
            val memoDataBase = MemoDataBase.getInstance(context)
            val tempList = memoDataBase?.memoDao()?.selectMemoDataAllBySecret(memoIsSecret)
            // MemoModel에 담아준다.
            val memoList = mutableListOf<MemoModel>()
            tempList?.forEach {
                val memoModel = MemoModel(
                    memoIdx = it.memoIdx,
                    memoTitle = it.memoTitle,
                    memoText =  it.memoText,
                    memoIsSecret = it.memoIsSecret,
                    memoIsFavorite = it.memoIsFavorite,
                    memoPassword = it.memoPassword,
                    memoCategoryIdx = it.memoCategoryIdx
                )
                memoList.add(memoModel)
            }
            return memoList
        }

        // 카테고리 번호를 통해 메모 데이터를 가져오는 메서드
        fun selectMemoDataAllByCategoryIdx(context: Context, categoryIdx:Int) : MutableList<MemoModel>{
            // 데이터를 가져온다.
            val memoDataBase = MemoDataBase.getInstance(context)
            val tempList = memoDataBase?.memoDao()?.selectMemoDataAllByCategoryIdx(categoryIdx)
            // MemoModel에 담아준다.
            val memoList = mutableListOf<MemoModel>()
            tempList?.forEach {
                val memoModel = MemoModel(
                    memoIdx = it.memoIdx,
                    memoTitle = it.memoTitle,
                    memoText =  it.memoText,
                    memoIsSecret = it.memoIsSecret,
                    memoIsFavorite = it.memoIsFavorite,
                    memoPassword = it.memoPassword,
                    memoCategoryIdx = it.memoCategoryIdx
                )
                memoList.add(memoModel)
            }
            return memoList
        }

        // 즐겨찾기 값을 설정하는 메서드
        fun updateMemoFavorite(context: Context, memoIdx:Int, memoIsFavorite: Boolean){
            val memoDataBase = MemoDataBase.getInstance(context);
            memoDataBase?.memoDao()?.updateMemoFavorite(memoIdx, memoIsFavorite)
        }

        // 메모 번호를 통해 메모데이터를 가져온다.
        fun selectMemoDataByMemoIdx(context:Context, memoIdx:Int) : MemoModel{
            val memoDataBase = MemoDataBase.getInstance(context)
            val memoVO = memoDataBase?.memoDao()?.selectMemoDataByMemoIdx(memoIdx)!!

            val memoModel = MemoModel(
                memoIdx = memoVO.memoIdx,
                memoTitle = memoVO.memoTitle,
                memoText = memoVO.memoText,
                memoIsSecret = memoVO.memoIsSecret,
                memoIsFavorite = memoVO.memoIsFavorite,
                memoPassword = memoVO.memoPassword,
                memoCategoryIdx = memoVO.memoCategoryIdx
            )
            return memoModel
        }

        // 메모를 삭제하는 메서드
        fun deleteMemoDataByMemoIdx(context: Context, memoIdx:Int){
            // VO 객체에 객체를 담는다.
            val memoVO = MemoVO(memoIdx = memoIdx)
            val memoDataBase = MemoDataBase.getInstance(context)
            memoDataBase?.memoDao()?.deleteMemoDataByMemoIdx(memoVO)
        }

        // 메모를 수정하는 메서드
        fun updateMemoData(context: Context, memoModel: MemoModel){
            // VO 객체에 담는다.
            val memoVO = MemoVO(
                memoIdx = memoModel.memoIdx,
                memoTitle = memoModel.memoTitle,
                memoText = memoModel.memoText,
                memoIsSecret = memoModel.memoIsSecret,
                memoIsFavorite = memoModel.memoIsFavorite,
                memoPassword = memoModel.memoPassword,
                memoCategoryIdx = memoModel.memoCategoryIdx
            )
            val memoDataBase = MemoDataBase.getInstance(context)
            memoDataBase?.memoDao()?.updateMemoData(memoVO)
        }

        // 메모의 카테고리 번호를 변경하는 메서드
        fun updateMemoCategoryIdx(context: Context, oldCategoryIdx:Int, newCategoryIdx:Int){
            val memoDataBase = MemoDataBase.getInstance(context)
            memoDataBase?.memoDao()?.updateMemoCategoryIdx(oldCategoryIdx, newCategoryIdx)
        }

        // 메모의 카테고리 번호로 메모를 삭제하는 메서드
        fun deleteMemoDataByCategoryIdx(context: Context, deleteCategoryIdx:Int){
            val memoDataBase = MemoDataBase.getInstance(context)
            memoDataBase?.memoDao()?.deleteMemoDataByCategoryIdx(deleteCategoryIdx)
        }

//        // 메모 이름으로 검색하여 메모 데이터 전체를 가져오는 메서드
//        fun selectMemoDataAllByMemoTitle(context: Context, memoTitle:String) : MutableList<MemoModel>{
//            // 데이터를 가져온다.
//            val memoDatabase = MemoDataBase.getInstance(context)
//            val memoList = memoDatabase?.memoDao()?.selectMemoDataAllByMemoTitle(memoTitle)
//
//            // 메모 데이터를 담을 리스트
//            val tempList = mutableListOf<MemoModel>()
//
//            // 메모의 수 만큼 반복한다.
//            memoList?.forEach {
//                val memoModel = MemoModel(
//                    memoIdx = it.memoIdx,
//                    memoTitle = it.memoTitle,
//                    memoText =  it.memoText,
//                    memoIsSecret = it.memoIsSecret,
//                    memoIsFavorite = it.memoIsFavorite,
//                    memoPassword = it.memoPassword,
//                    memoCategoryIdx = it.memoCategoryIdx
//                )
//                // 리스트에 담는다.
//                tempList.add(memoModel)
//            }
//            return tempList
//        }

        // 키워드로 검색하여 메모 데이터를 가져오는 메서드
        fun selectMemoDataAllByKeyword(context: Context, keyword: String): MutableList<MemoModel> {
            val memoDatabase = MemoDataBase.getInstance(context)

            // 키워드에 맞는 데이터를 가져온다.
            val queryKeyword = "%$keyword%"
            val memoList = memoDatabase?.memoDao()?.selectMemoDataAllByKeyword(queryKeyword)

            // 메모 데이터를 담을 리스트 생성
            val tempList = mutableListOf<MemoModel>()

            // 가져온 메모 데이터를 반복하여 변환
            memoList?.forEach {
                val memoModel = MemoModel(
                    memoIdx = it.memoIdx,
                    memoTitle = it.memoTitle,
                    memoText = it.memoText,
                    memoIsSecret = it.memoIsSecret,
                    memoIsFavorite = it.memoIsFavorite,
                    memoPassword = it.memoPassword,
                    memoCategoryIdx = it.memoCategoryIdx
                )
                // 변환된 데이터를 리스트에 추가
                tempList.add(memoModel)
            }
            return tempList
        }
    }
}