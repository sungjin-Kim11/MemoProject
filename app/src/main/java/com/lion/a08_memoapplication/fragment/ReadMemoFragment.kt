package com.lion.a08_memoapplication.fragment

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lion.a08_memoapplication.MainActivity
import com.lion.a08_memoapplication.R
import com.lion.a08_memoapplication.databinding.FragmentReadMemoBinding
import com.lion.a08_memoapplication.model.MemoModel
import com.lion.a08_memoapplication.repository.CategoryRepository
import com.lion.a08_memoapplication.repository.MemoRepository
import com.lion.a08_memoapplication.util.FragmentName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ReadMemoFragment : Fragment() {

    lateinit var fragmentReadMemoBinding: FragmentReadMemoBinding
    lateinit var mainActivity:MainActivity

    // 메모 데이터를 담을 변수
    lateinit var readMemoModel: MemoModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentReadMemoBinding = FragmentReadMemoBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity

        // 툴바를 구성하는 메서드
        settingToolbar()
        // 체크 박스를 구성하는 메서드
        settingCheckBox()
        // 데이터를 가져와 보여주는 메서드를 호출한다.
        settingMemoData()

        return fragmentReadMemoBinding.root
    }

    // 툴바를 구성하는 메서드
    fun settingToolbar(){
        fragmentReadMemoBinding.apply {
            toolbarReadMemo.title = "메모 읽기"

            toolbarReadMemo.setNavigationIcon(R.drawable.arrow_back_24px)
            toolbarReadMemo.setNavigationOnClickListener {
                mainActivity.removeFragment(FragmentName.READ_MEMO_FRAGMENT)
            }

            // 메뉴
            toolbarReadMemo.inflateMenu(R.menu.toolbar_read_memo_menu)
            toolbarReadMemo.setOnMenuItemClickListener {
                when(it.itemId){
                    // 수정
                    R.id.toolbar_read_memo_menu_item_modify -> {
                        // 메모 번호를 전달한다.
                        val dataBundle = Bundle()
                        dataBundle.putInt("memoIdx", readMemoModel.memoIdx)
                        mainActivity.replaceFragment(FragmentName.MODIFY_MEMO_FRAGMENT, true, true, dataBundle)
                    }
                    // 삭제
                    R.id.toolbar_read_memo_menu_item_delete -> {
                        // mainActivity.removeFragment(FragmentName.READ_MEMO_FRAGMENT)
                        // 삭제처리하는 메서드를 호출한다.
                        deleteMemoData()
                    }
                }
                true
            }
        }
    }

    // 데이터를 가져와 보여준다.
    fun settingMemoData(){
        // 메모 번호를 받는다.
        val memoIdx = arguments?.getInt("memoIdx")!!
        // 데이터를 가져온다.
        CoroutineScope(Dispatchers.Main).launch {
            val work1 = async(Dispatchers.IO){
                MemoRepository.selectMemoDataByMemoIdx(mainActivity, memoIdx)
            }
            readMemoModel = work1.await()

            val work2 = async(Dispatchers.IO){
                CategoryRepository.selectCategoryDataByCategoryIdx(mainActivity, readMemoModel.memoCategoryIdx)
            }
            val categoryModel = work2.await()

            // 입력 요소에 넣어준다.
            fragmentReadMemoBinding.apply {
                textFieldReadMemoTitle.editText?.setText(readMemoModel.memoTitle)
                textFieldReadMemoText.editText?.setText(readMemoModel.memoText)
                textFieldReadMemoCategory.editText?.setText(categoryModel.categoryName)
                checkBoxReadMemoFavorite.isChecked = readMemoModel.memoIsFavorite
            }
        }
    }

    // 체크 박스를 구성하는 메서드
    fun settingCheckBox(){
        fragmentReadMemoBinding.apply {
            checkBoxReadMemoFavorite.setOnCheckedChangeListener { buttonView, isChecked ->
                // 즐겨찾기 여부 값을 업데이트 한다.
                CoroutineScope(Dispatchers.Main).launch {
                    val work1 = async(Dispatchers.IO){
                        MemoRepository.updateMemoFavorite(mainActivity, readMemoModel.memoIdx, isChecked)
                    }
                    work1.join()
                }
            }
        }
    }

    // 메모 삭제 처리 메서드
    fun deleteMemoData(){
        val builder = MaterialAlertDialogBuilder(mainActivity)
        builder.setTitle("메모 삭제")
        builder.setMessage("삭제한 메모는 복구할 수 없습니다")
        builder.setNegativeButton("취소", null)
        builder.setPositiveButton("확인"){ dialogInterface: DialogInterface, i: Int ->
            // 데이터를 삭제한다.
            CoroutineScope(Dispatchers.Main).launch {
                val work1 = async(Dispatchers.IO){
                    MemoRepository.deleteMemoDataByMemoIdx(mainActivity, readMemoModel.memoIdx)
                }
                work1.join()
                mainActivity.removeFragment(FragmentName.READ_MEMO_FRAGMENT)
            }
        }
        builder.show()
    }
}







