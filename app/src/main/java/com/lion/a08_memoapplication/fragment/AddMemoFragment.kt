package com.lion.a08_memoapplication.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.lion.a08_memoapplication.MainActivity
import com.lion.a08_memoapplication.R
import com.lion.a08_memoapplication.databinding.FragmentAddMemoBinding
import com.lion.a08_memoapplication.model.CategoryModel
import com.lion.a08_memoapplication.model.MemoModel
import com.lion.a08_memoapplication.repository.CategoryRepository
import com.lion.a08_memoapplication.repository.MemoRepository
import com.lion.a08_memoapplication.util.FragmentName
import com.lion.a08_memoapplication.util.MemoListName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class AddMemoFragment : Fragment() {

    lateinit var fragmentAddMemoBinding: FragmentAddMemoBinding
    lateinit var mainActivity: MainActivity

    // 카테고리 목록 데이터를 담을 리스트
    val categoryNameList = mutableListOf<String>()
    val categoryIdxList = mutableListOf<Int>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        fragmentAddMemoBinding = FragmentAddMemoBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity

        // 카테고리 선택 메뉴를 구성하는 메서드를 호출한다.
        settingSelectCategoryMenu()
        // 툴바를 구성하는 메서드
        settingToolbar()
        // 체크박스와 비밀번호 입력 요소 설정
        settingSecretTextField()

        return fragmentAddMemoBinding.root
    }

    // 카테고리 선택 메뉴를 구성하는 메서드
    fun settingSelectCategoryMenu(){
        fragmentAddMemoBinding.apply {
            // 특정 카테고리를 선택했을 때
            textFieldAddMemoCategory.editText?.setText("카테고리")

            // 특정 카테고리가 선택이 안되어 있을 때
            // val items = arrayOf("Item 1", "Item 2", "Item 3", "Item 4")
            // (textFieldAddMemoCategory.editText as? MaterialAutoCompleteTextView)?.setSimpleItems(items)

            // 만약 사용자가 추가한 카테고리 라면
            if (arguments?.getString("MemoName") == MemoListName.MEMO_NAME_ADDED.str) {
                categoryIdxList.clear()
                categoryNameList.clear()
                // 전달받은 카테고리 정보를 추출한다.
                val categoryIdx = arguments?.getInt("categoryIdx")!!
                val categoryName = arguments?.getString("categoryName")!!
                // 리스트에 담는다
                categoryIdxList.add(categoryIdx)
                categoryNameList.add(categoryName)

                // 카테고리 목록을 설정해준다.
                val a1 = textFieldAddMemoCategory.editText as? MaterialAutoCompleteTextView
                a1?.setSimpleItems(categoryNameList.toTypedArray())
                // 첫 번째 값을 설정해준다.
                a1?.setText(categoryName, false)
            } else {
                // 데이터 베이스에서 모든 카테고리 정보를 가져온다.
                categoryNameList.clear()
                categoryIdxList.clear()

                CoroutineScope(Dispatchers.Main).launch {
                    val work1 = async(Dispatchers.IO){
                        CategoryRepository.selectCategoryAll(mainActivity)
                    }
                    val tempList = work1.await()

                    tempList.forEach {
                        categoryNameList.add(it.categoryName)
                        categoryIdxList.add(it.categoryIdx)
                    }

                    // 카테고리 목록을 설정해준다.
                    val a1 = textFieldAddMemoCategory.editText as? MaterialAutoCompleteTextView
                    a1?.setSimpleItems(categoryNameList.toTypedArray())
                    // 첫 번째 값을 설정해준다.
                    a1?.setText(categoryNameList[0], false)
                }
            }

        }
    }

    // 툴바를 구성하는 메서드
    fun settingToolbar(){
        fragmentAddMemoBinding.apply {
            toolbarAddMemo.title = "메모 작성"

            toolbarAddMemo.setNavigationIcon(R.drawable.arrow_back_24px)
            toolbarAddMemo.setNavigationOnClickListener {
                mainActivity.removeFragment(FragmentName.ADD_MEMO_FRAGMENT)
            }

            // 툴바의 메뉴를 구셩한다.
            val menuItemDone = toolbarAddMemo.menu.add(Menu.NONE, Menu.FIRST + 1, Menu.NONE, "작성완료")
            // showAsAction 설정
            menuItemDone.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS)
            // 아이콘
            menuItemDone.setIcon(R.drawable.check_24px)

            toolbarAddMemo.setOnMenuItemClickListener {
                when(it.itemId){
                    Menu.FIRST + 1 ->{
                        // mainActivity.removeFragment(FragmentName.ADD_MEMO_FRAGMENT)
                        // 메모 내용을 저장하는 메서드를 호출한다.
                        addNewMemoData()
                    }
                }
                true
            }
        }
    }

    // 체크박스와 비밀번호 입력 요소 설정
    fun settingSecretTextField(){
        fragmentAddMemoBinding.apply {
            // 비밀번호 입력 요소를 비활성화 시킨다.
            textFieldAddMemoPassword.editText?.isEnabled = false

            // 체크박스 이벤트
            checkBoxAddMemoSecret.setOnCheckedChangeListener { buttonView, isChecked ->
                if(isChecked){
                    // 비빌번호 입력 요소를 활성화한다.
                    textFieldAddMemoPassword.editText?.isEnabled = true
                } else {
                    // 비밀번호 입력 요소를 비활성화 한다.
                    textFieldAddMemoPassword.editText?.isEnabled = false
                    textFieldAddMemoPassword.editText?.setText("")
                }
            }
        }
    }

    // 메모 내용을 저장하는 메서드
    fun addNewMemoData(){
        fragmentAddMemoBinding.apply {
            // 입력한 데이터를 가져온다.
            val memoTitle = textFieldAddMemoTitle.editText?.text.toString()
            val memoText = textFieldAddMemoText.editText?.text.toString()
            val memoIsSecret = checkBoxAddMemoSecret.isChecked
            val memoPassword = textFieldAddMemoPassword.editText?.text.toString()

            val temp = categoryNameList.indexOf(textFieldAddMemoCategory.editText?.text.toString())
            val memoCategoryIdx = categoryIdxList[temp]

            val memoModel = MemoModel(
                memoTitle = memoTitle,
                memoText = memoText,
                memoIsSecret = memoIsSecret,
                memoPassword = memoPassword,
                memoCategoryIdx = memoCategoryIdx
            )
            // 데이터를 저장한다.
            CoroutineScope(Dispatchers.Main).launch {
                val work1 = async(Dispatchers.IO){
                    MemoRepository.insertMemoData(mainActivity, memoModel)
                }
                work1.join()
                // 이전 프래그먼트로 이동한다.
                mainActivity.removeFragment(FragmentName.ADD_MEMO_FRAGMENT)
            }
        }
    }
}