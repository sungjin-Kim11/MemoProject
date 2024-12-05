package com.lion.a08_memoapplication.fragment

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.lion.a08_memoapplication.MainActivity
import com.lion.a08_memoapplication.R
import com.lion.a08_memoapplication.databinding.FragmentModifyMemoBinding
import com.lion.a08_memoapplication.model.MemoModel
import com.lion.a08_memoapplication.repository.CategoryRepository
import com.lion.a08_memoapplication.repository.MemoRepository
import com.lion.a08_memoapplication.util.FragmentName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ModifyMemoFragment : Fragment() {

    lateinit var fragmentModifyMemoBinding: FragmentModifyMemoBinding
    lateinit var mainActivity: MainActivity

    // 카테고리의 정보를 담을 리스트
    val categoryNameList = mutableListOf<String>()
    val categoryIdxList = mutableListOf<Int>()
    // 메모 정보를 담을 객체
    lateinit var modifyMemoModel:MemoModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentModifyMemoBinding = FragmentModifyMemoBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity

        // 필요한 데이터를 읽어오는 메서드를 호출한다.
        gettingModifyMemoData()

        // 툴바를 구성하는 메서드
        settingToolbar()
        // 체크박스와 비밀번호 입력 요소 설정
        settingSecretTextField()


        return fragmentModifyMemoBinding.root
    }

    // 카테고리 선택 메뉴를 구성하는 메서드
    fun settingSelectCategoryMenu(){
        fragmentModifyMemoBinding.apply {
            val items = categoryNameList.toTypedArray()
            val a1 = textFieldModifyMemoCategory.editText as MaterialAutoCompleteTextView
            a1.setSimpleItems(items)

            val categoryPosition = categoryIdxList.indexOf(modifyMemoModel.memoCategoryIdx)
            a1.setText(categoryNameList[categoryPosition], false)
        }
    }


    // 툴바를 구성하는 메서드
    fun settingToolbar(){
        fragmentModifyMemoBinding.apply {
            toolbarModifyMemo.title = "메모 수정"

            toolbarModifyMemo.setNavigationIcon(R.drawable.arrow_back_24px)
            toolbarModifyMemo.setNavigationOnClickListener {
                mainActivity.removeFragment(FragmentName.MODIFY_MEMO_FRAGMENT)
            }

            // 툴바의 메뉴를 구셩한다.
            val menuItemDone = toolbarModifyMemo.menu.add(Menu.NONE, Menu.FIRST + 1, Menu.NONE, "수정완료")
            menuItemDone.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS)
            menuItemDone.setIcon(R.drawable.check_24px)

            toolbarModifyMemo.setOnMenuItemClickListener {
                when(it.itemId){
                    Menu.FIRST + 1 ->{
                        // mainActivity.removeFragment(FragmentName.MODIFY_MEMO_FRAGMENT)
                        // 메모 수정처리는 메서드를 호출해준다.
                        processModifyMemoData()
                    }
                }
                true
            }
        }
    }

    // 체크박스와 비밀번호 입력 요소 설정
    fun settingSecretTextField(){
        fragmentModifyMemoBinding.apply {
            // 비밀번호 입력 요소를 비활성화 시킨다.
            textFieldModifyMemoPassword.editText?.isEnabled = false

            // 체크박스 이벤트
            checkBoxModifyMemoSecret.setOnCheckedChangeListener { buttonView, isChecked ->
                if(isChecked){
                    // 비빌번호 입력 요소를 활성화한다.
                    textFieldModifyMemoPassword.editText?.isEnabled = true
                    textFieldModifyMemoPassword.editText?.setText(modifyMemoModel.memoPassword)
                } else {
                    // 비밀번호 입력 요소를 비활성화 한다.
                    textFieldModifyMemoPassword.editText?.isEnabled = false
                    textFieldModifyMemoPassword.editText?.setText("")
                }
            }
        }
    }

    // 필요한 데이터를 읽어오는 메서드
    fun gettingModifyMemoData(){
        CoroutineScope(Dispatchers.Main).launch {
            // 카테고리 정보를 가져온다.
            val work1 = async(Dispatchers.IO){
                CategoryRepository.selectCategoryAll(mainActivity)
            }
            val tempList = work1.await()

            tempList.forEach {
                categoryNameList.add(it.categoryName)
                categoryIdxList.add(it.categoryIdx)
            }
            // 메모 데이터를 가져온다.
            val memoIdx = arguments?.getInt("memoIdx")!!
            val work2 = async(Dispatchers.IO){
                MemoRepository.selectMemoDataByMemoIdx(mainActivity, memoIdx)
            }
            modifyMemoModel = work2.await()

            // 카테고리 선택 메뉴를 구성하는 메서드를 호출한다.
            settingSelectCategoryMenu()

            // 입력 요소들 셋팅
            fragmentModifyMemoBinding.apply {
                textFieldModifyMemoTitle.editText?.setText(modifyMemoModel.memoTitle)
                textFieldModifyMemoText.editText?.setText(modifyMemoModel.memoText)
                checkBoxModifyMemoSecret.isChecked = modifyMemoModel.memoIsSecret
                textFieldModifyMemoPassword.editText?.setText(modifyMemoModel.memoPassword)
            }
        }
    }

    // 수정 완료 처리 메서드
    fun processModifyMemoData(){
        fragmentModifyMemoBinding.apply {
            val builder = MaterialAlertDialogBuilder(mainActivity)
            builder.setTitle("수정 완료")
            builder.setMessage("수정된 메모는 복구할 수 없습니다")
            builder.setNegativeButton("취소", null)
            builder.setPositiveButton("수정"){ dialogInterface: DialogInterface, i: Int ->
                val memoIdx = modifyMemoModel.memoIdx
                val memoTitle = textFieldModifyMemoTitle.editText?.text.toString()
                val memoText = textFieldModifyMemoText.editText?.text.toString()
                val memoIsSecret = checkBoxModifyMemoSecret.isChecked
                val memoIsFavorite = modifyMemoModel.memoIsFavorite

                val memoPassword = if(memoIsSecret){
                    textFieldModifyMemoPassword.editText?.text.toString()
                } else {
                    ""
                }
                val tempStr1 = textFieldModifyMemoCategory.editText?.text.toString()
                val tempIdx = categoryNameList.indexOf(tempStr1)
                val memoCategoryIdx = categoryIdxList[tempIdx]

                // 모델에 담는다.
                val newMemoModel = MemoModel(memoIdx, memoTitle, memoText, memoIsSecret, memoIsFavorite,
                    memoPassword, memoCategoryIdx)

                // 수정한다.
                CoroutineScope(Dispatchers.Main).launch {
                    val work1 = async(Dispatchers.IO){
                        MemoRepository.updateMemoData(mainActivity, newMemoModel)
                    }
                    work1.join()

                    mainActivity.removeFragment(FragmentName.MODIFY_MEMO_FRAGMENT)
                }

            }
            builder.show()
        }
    }
}

