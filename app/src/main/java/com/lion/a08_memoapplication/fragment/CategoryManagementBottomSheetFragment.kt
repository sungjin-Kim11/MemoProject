package com.lion.a08_memoapplication.fragment

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lion.a08_memoapplication.MainActivity
import com.lion.a08_memoapplication.R
import com.lion.a08_memoapplication.databinding.DialogCategoryManagementBinding
import com.lion.a08_memoapplication.databinding.FragmentCategoryManagementBottomSheetBinding
import com.lion.a08_memoapplication.repository.CategoryRepository
import com.lion.a08_memoapplication.repository.MemoRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class CategoryManagementBottomSheetFragment(
    val categoryManagementFragment: CategoryManagementFragment,
    val selectedPosition:Int,
    val selectedCategoryIdx:Int,
    val basicCategoryIdx:Int) : BottomSheetDialogFragment() {

    lateinit var fragmentCategoryManagementBottomSheetBinding: FragmentCategoryManagementBottomSheetBinding
    lateinit var mainActivity: MainActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentCategoryManagementBottomSheetBinding = FragmentCategoryManagementBottomSheetBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity

        settingButton()

        return fragmentCategoryManagementBottomSheetBinding.root
    }

    // 각 버튼을 구성하는 메서드
    fun settingButton(){
        fragmentCategoryManagementBottomSheetBinding.apply {

            buttonCategoryManagementBottomSheetModify.setOnClickListener {

                val builder = MaterialAlertDialogBuilder(mainActivity)
                builder.setTitle("카테고리 수정")

                val dialogCategoryManagementBinding = DialogCategoryManagementBinding.inflate(layoutInflater)
                builder.setView(dialogCategoryManagementBinding.root)

                builder.setNegativeButton("취소", null)
                builder.setPositiveButton("수정"){ dialogInterface: DialogInterface, i: Int ->

                }
                // 입력요소에 포커스를 준다.
                mainActivity.showSoftInput(dialogCategoryManagementBinding.textFieldDialogCategoryManagement.editText!!)

                builder.show()

                // BottomSheet를 내려준다.
                dismiss()
            }

            buttonCategoryManagementBottomSheetDeleteOnlyCategory.setOnClickListener {

                val builder = MaterialAlertDialogBuilder(mainActivity)
                builder.setTitle("카테고리 삭제")
                builder.setMessage("""
                    카테고리를 삭제합니다
                    카테고리에 있던 메모는
                    기본 메모로 옮겨집니다
                    삭제된 데이터는 복원할 수 없습니다
                """.trimIndent())

                builder.setNegativeButton("취소", null)
                builder.setPositiveButton("삭제"){ dialogInterface: DialogInterface, i: Int ->
                    CoroutineScope(Dispatchers.Main).launch {
                        // 메모들의 카테고리 번호를 변경한다.
                        val work1 = async(Dispatchers.IO){
                            MemoRepository.updateMemoCategoryIdx(mainActivity, selectedCategoryIdx, basicCategoryIdx)
                        }
                        work1.join()
                        // 카테고리를 삭제한다.
                        val work2 = async(Dispatchers.IO){
                            CategoryRepository.deleteMemoDataByCategoryIdx(mainActivity, selectedCategoryIdx)
                        }
                        work2.join()

                        mainActivity.addCategoryMenu()
                        categoryManagementFragment.categoryList.removeAt(selectedPosition)
                        categoryManagementFragment.fragmentCategoryManagementBinding
                            .recyclerViewCategoryManagement.adapter?.notifyItemRemoved(selectedPosition)
                    }
                }
                builder.show()

                // BottomSheet를 내려준다.
                dismiss()
            }

            buttonCategoryManagementBottomSheetDeleteCategoryWithMemos.setOnClickListener {
                val builder = MaterialAlertDialogBuilder(mainActivity)
                builder.setTitle("카테고리 삭제")
                builder.setMessage("""
                    카테고리와 메모를 삭제합니다
                    삭제된 데이터는 복원할 수 없습니다
                """.trimIndent())

                builder.setNegativeButton("취소", null)
                builder.setPositiveButton("삭제"){ dialogInterface: DialogInterface, i: Int ->
                    CoroutineScope(Dispatchers.Main).launch {
                        // 카테고리 내의 모든 메모를 삭제한다.
                        val work1 = async(Dispatchers.IO){
                            MemoRepository.deleteMemoDataByCategoryIdx(mainActivity, selectedCategoryIdx)
                        }
                        work1.join()
                        // 카테고리를 삭제한다.
                        val work2 = async(Dispatchers.IO){
                            CategoryRepository.deleteMemoDataByCategoryIdx(mainActivity, selectedCategoryIdx)
                        }
                        work2.join()

                        mainActivity.addCategoryMenu()
                        categoryManagementFragment.categoryList.removeAt(selectedPosition)
                        categoryManagementFragment.fragmentCategoryManagementBinding
                            .recyclerViewCategoryManagement.adapter?.notifyItemRemoved(selectedPosition)
                    }

                }
                builder.show()

                // BottomSheet를 내려준다.
                dismiss()
            }
        }
    }
}