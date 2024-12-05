package com.lion.a08_memoapplication.fragment

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.lion.a08_memoapplication.MainActivity
import com.lion.a08_memoapplication.R
import com.lion.a08_memoapplication.databinding.DialogCategoryManagementBinding
import com.lion.a08_memoapplication.databinding.FragmentCategoryManagementBinding
import com.lion.a08_memoapplication.databinding.RowCategoryManagementBinding
import com.lion.a08_memoapplication.model.CategoryModel
import com.lion.a08_memoapplication.repository.CategoryRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class CategoryManagementFragment : Fragment() {

    lateinit var fragmentCategoryManagementBinding: FragmentCategoryManagementBinding
    lateinit var mainActivity: MainActivity

    // RecyclerView 구성을 위한 임시데이터
//    val tempData1 = Array(100){
//        "카테고리 $it"
//    }

    // RecyclerView 구성을 위한 리스트
    var categoryList = mutableListOf<CategoryModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentCategoryManagementBinding = FragmentCategoryManagementBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity

        // Toolbar를 구성하는 메서드를 호출한다.
        settingToolbar()
        // RecyclerView를 구성하는 메서드를 호출한다.
        settingRecyclerView()
        // 버튼을 설정하는 메서드를 호출한다.
        settingButton()
        // 데이터를 가져와 RecyclerView를 갱신하는 메서드를 호출한다.
        refreshRecyclerView()

        return fragmentCategoryManagementBinding.root
    }

    // Toolbar를 구성하는 메서드
    fun settingToolbar(){
        fragmentCategoryManagementBinding.apply {
            toolbarCategoryManagement.title = "카테고리 관리"
            // 네비게이션 아이콘을 설정하고 누를 경우 NavigationView가 나타나도록 한다.
            toolbarCategoryManagement.setNavigationIcon(R.drawable.menu_24px)
            toolbarCategoryManagement.setNavigationOnClickListener {
                mainActivity.activityMainBinding.drawerLayoutMain.open()
            }
        }
    }

    // RecyclerView를 구성하는 메서드
    fun settingRecyclerView(){
        fragmentCategoryManagementBinding.apply {
            recyclerViewCategoryManagement.adapter = RecyclerViewCategoryManagementAdapter()
            recyclerViewCategoryManagement.layoutManager = LinearLayoutManager(mainActivity)
            val deco = MaterialDividerItemDecoration(mainActivity, MaterialDividerItemDecoration.VERTICAL)
            recyclerViewCategoryManagement.addItemDecoration(deco)
            // recyclerView and fab
            mainActivity.fabHideAndShow(recyclerViewCategoryManagement, fabCategoryManagementAdd)
        }
    }

    // 버튼을 설정하는 메서드
    fun settingButton(){
        fragmentCategoryManagementBinding.apply {
            // fab를 누를 때
            fabCategoryManagementAdd.setOnClickListener {
                val builder = MaterialAlertDialogBuilder(mainActivity)
                builder.setTitle("카테고리 등록")

                val dialogCategoryManagementBinding = DialogCategoryManagementBinding.inflate(layoutInflater)
                builder.setView(dialogCategoryManagementBinding.root)

                builder.setNegativeButton("취소", null)
                builder.setPositiveButton("등록"){ dialogInterface: DialogInterface, i: Int ->
                    // 카테고리를 저장한다.
                    CoroutineScope(Dispatchers.Main).launch {
                        val work1 = async(Dispatchers.IO){
                            // 입력한 카테고리 이름을 가져온다.
                            val categoryName = dialogCategoryManagementBinding.textFieldDialogCategoryManagement.editText?.text.toString()
                            // 저장한다.
                            val categoryModel = CategoryModel(categoryName = categoryName)
                            CategoryRepository.insertCategoryData(mainActivity, categoryModel)
                        }
                        work1.join()
                        refreshRecyclerView()
                        mainActivity.addCategoryMenu()
                    }
                }
                // 입력요소에 포커스를 준다.
                mainActivity.showSoftInput(dialogCategoryManagementBinding.textFieldDialogCategoryManagement.editText!!)

                builder.show()
            }
        }
    }

    // 데이터를 가져와 RecyclerView를 갱신하는 메서드
    fun refreshRecyclerView(){
        fragmentCategoryManagementBinding.apply {
            // 데이터를 가져온다.
            CoroutineScope(Dispatchers.Main).launch {
                val work1 = async(Dispatchers.IO){
                    CategoryRepository.selectCategoryAll(mainActivity)
                }
                categoryList = work1.await()
                // RecyclerView 갱신
                recyclerViewCategoryManagement.adapter?.notifyDataSetChanged()
            }
        }
    }

    // RecyclerView의 어뎁터
    inner class RecyclerViewCategoryManagementAdapter : RecyclerView.Adapter<RecyclerViewCategoryManagementAdapter.ViewHolderCategoryManagement>(){
        // ViewHolder
        inner class ViewHolderCategoryManagement(val rowCategoryManagementBinding: RowCategoryManagementBinding) : RecyclerView.ViewHolder(rowCategoryManagementBinding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderCategoryManagement {
            val rowCategoryManagementBinding = RowCategoryManagementBinding.inflate(layoutInflater, parent, false)
            val viewHolderCategoryManagement = ViewHolderCategoryManagement(rowCategoryManagementBinding)

            // 쩜쩜쩜 버튼을 누르면 동작하는 리스너
            rowCategoryManagementBinding.buttonRowCategoryManagement.setOnClickListener {
                // BottomSheet를 띄운다.
                val categoryManagementBottomSheetFragment = CategoryManagementBottomSheetFragment(
                    this@CategoryManagementFragment,
                    viewHolderCategoryManagement.adapterPosition,
                    categoryList[viewHolderCategoryManagement.adapterPosition].categoryIdx,
                    categoryList[0].categoryIdx
                )
                categoryManagementBottomSheetFragment.show(mainActivity.supportFragmentManager, "BottomSheet")
            }

            return viewHolderCategoryManagement
        }

        override fun getItemCount(): Int {
            return categoryList.size
        }

        override fun onBindViewHolder(holder: ViewHolderCategoryManagement, position: Int) {
            holder.rowCategoryManagementBinding.textViewRowCategoryManagement.text = categoryList[position].categoryName
        }
    }

}