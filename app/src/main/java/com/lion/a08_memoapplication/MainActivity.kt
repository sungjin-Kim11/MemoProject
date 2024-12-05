package com.lion.a08_memoapplication

import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.Menu
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.lion.a08_memoapplication.databinding.ActivityMainBinding
import com.lion.a08_memoapplication.databinding.NavigationHeaderLayoutBinding
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.transition.MaterialSharedAxis
import com.lion.a08_memoapplication.fragment.AddMemoFragment
import com.lion.a08_memoapplication.fragment.CategoryManagementFragment
import com.lion.a08_memoapplication.fragment.ModifyMemoFragment
import com.lion.a08_memoapplication.fragment.ReadMemoFragment
import com.lion.a08_memoapplication.fragment.ShowMemoAllFragment
import com.lion.a08_memoapplication.model.CategoryModel
import com.lion.a08_memoapplication.repository.CategoryRepository
import com.lion.a08_memoapplication.util.FragmentName
import com.lion.a08_memoapplication.util.MemoListName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlin.concurrent.thread


class MainActivity : AppCompatActivity() {

    val activityMainBinding by lazy{
        ActivityMainBinding.inflate(layoutInflater)
    }
    // NavigationView의 메뉴 중 카테고리 데이터를 담을 리스트
    var categoryList = mutableListOf<CategoryModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(activityMainBinding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 네비게이션 뷰를 구성하는 메서드를 호출한다.
        settingNavigationView()
        // 카테고리 메뉴는 추가해는 메서드를 호출한다.
        addCategoryMenu()

        // 첫 화면을 설정해준다.
        replaceFragment(FragmentName.SHOW_MEMO_ALL_FRAGMENT, false, false, null)
    }

    // 네비게이션 뷰를 구성하는 메서드
    fun settingNavigationView(){
        activityMainBinding.apply {
            // 네비게이션 뷰의 해더
            val navigationHeaderLayoutBinding = NavigationHeaderLayoutBinding.inflate(layoutInflater)
            navigationHeaderLayoutBinding.textViewNavigationHeaderTitle1.text = "멋쟁이사자처럼"
            navigationHeaderLayoutBinding.textViewNavigationHeaderTitle2.text = "앱스쿨3기"
            navigationViewMain.addHeaderView(navigationHeaderLayoutBinding.root)

            // 메뉴
            navigationViewMain.inflateMenu(R.menu.navigation_menu)

            // 네비게이션 뷰의 메뉴 중 전체 메모가 선택되어 있는 상태로 설정한다.
            navigationViewMain.setCheckedItem(R.id.navigation_menu_item_all)

//            val menuItem = navigationViewMain.menu.findItem(R.id.navigation_menu_item_memo_category)
//            menuItem.subMenu?.add(Menu.NONE, 100, Menu.NONE, "새로추가한 메뉴")
//            val subMenuItem = menuItem.subMenu?.findItem(100)
//            subMenuItem?.setIcon(R.drawable.lock_24px)

            // 네비게이션의 메뉴를 눌렀을 때
            navigationViewMain.setNavigationItemSelectedListener {
                when(it.itemId){
                    R.id.navigation_menu_item_all -> {
                        val dataBundle = Bundle()
                        // 메모 목록 구분 값
                        dataBundle.putString("MemoName", MemoListName.MEMO_NAME_ALL.str)
                        replaceFragment(FragmentName.SHOW_MEMO_ALL_FRAGMENT, false, false, dataBundle)
                    }
                    R.id.navigation_menu_item_favorite -> {
                        val dataBundle = Bundle()
                        // 메모 목록 구분 값
                        dataBundle.putString("MemoName", MemoListName.MEMO_NAME_FAVORITE.str)
                        replaceFragment(FragmentName.SHOW_MEMO_ALL_FRAGMENT, false, false, dataBundle)
                    }
                    R.id.navigation_menu_item_secret -> {
                        val dataBundle = Bundle()
                        // 메모 목록 구분 값
                        dataBundle.putString("MemoName", MemoListName.MEMO_NAME_SECRET.str)
                        replaceFragment(FragmentName.SHOW_MEMO_ALL_FRAGMENT, false, false, dataBundle)
                    }
                    R.id.navigation_menu_item_management_category -> {
                        replaceFragment(FragmentName.CATEGORY_MANAGEMENT_FRAGMENT, false, false, null)
                    }
                    else -> {
                        val dataBundle = Bundle()
                        // 메모 목록 구분 값
                        dataBundle.putString("MemoName", MemoListName.MEMO_NAME_ADDED.str)
                        // 카테고리 리스트에서 사용자가 누른 번째 객체의 정보를 담아준다
                        dataBundle.putInt("categoryIdx", categoryList[it.itemId - Menu.FIRST - 1].categoryIdx)
                        dataBundle.putString("categoryName", categoryList[it.itemId - Menu.FIRST - 1].categoryName)

                        replaceFragment(FragmentName.SHOW_MEMO_ALL_FRAGMENT, false, false, dataBundle)
                    }
                }

                // 닫아준다.
                drawerLayoutMain.close()
                true
            }
        }
    }

    // 카테고리 데이터를 가져와 카테고리 메뉴를 구성해준다
    fun addCategoryMenu(){

        // 데이터를 가져온다.
        CoroutineScope(Dispatchers.Main).launch {
            val work1 = async(Dispatchers.IO){
                CategoryRepository.selectCategoryAll(this@MainActivity)
            }
            categoryList = work1.await()
            activityMainBinding.apply {
                // 만약 메모 카테고리 메뉴가 있다면 제거한다.
                navigationViewMain.menu.removeItem(Menu.FIRST)
                // 하위 메뉴를 추가해준다.
                val subMenu = navigationViewMain.menu.addSubMenu(Menu.NONE, Menu.FIRST, Menu.NONE, "메모 카테고리")

                // 카테고리의 수 만큼 반복한다
                categoryList.forEachIndexed { index, categoryModel ->
                    val subItem = subMenu.add(Menu.NONE, Menu.FIRST + index + 1, Menu.NONE, categoryModel.categoryName)
                    subItem.setIcon(R.drawable.edit_24px)
                }
            }
        }
    }

    // 프래그먼트를 교체하는 함수
    fun replaceFragment(fragmentName: FragmentName, isAddToBackStack:Boolean, animate:Boolean, dataBundle: Bundle?){
        // 프래그먼트 객체
        val newFragment = when(fragmentName){
            // 전체 메모 화면
            FragmentName.SHOW_MEMO_ALL_FRAGMENT -> ShowMemoAllFragment()
            // 메모 추가 화면
            FragmentName.ADD_MEMO_FRAGMENT -> AddMemoFragment()
            // 메모 읽기 화면
            FragmentName.READ_MEMO_FRAGMENT -> ReadMemoFragment()
            // 메모 수정 화면
            FragmentName.MODIFY_MEMO_FRAGMENT -> ModifyMemoFragment()
            // 카테고리 관리 화면
            FragmentName.CATEGORY_MANAGEMENT_FRAGMENT -> CategoryManagementFragment()
        }

        // bundle 객체가 null이 아니라면
        if(dataBundle != null){
            newFragment.arguments = dataBundle
        }

        // 프래그먼트 교체
        supportFragmentManager.commit {
            if(animate) {
                newFragment.exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, /* forward= */ true)
                newFragment.reenterTransition = MaterialSharedAxis(MaterialSharedAxis.X, /* forward= */ false)
                newFragment.enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, /* forward= */ true)
                newFragment.returnTransition = MaterialSharedAxis(MaterialSharedAxis.X, /* forward= */ false)
            }

            replace(R.id.fragmentContainerView, newFragment)
            if(isAddToBackStack){
                addToBackStack(fragmentName.str)
            }
        }
    }

    // 프래그먼트를 BackStack에서 제거하는 메서드
    fun removeFragment(fragmentName: FragmentName){
        supportFragmentManager.popBackStack(fragmentName.str, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }


    // 키보드 올리는 메서드
    fun showSoftInput(view: View){
        // 입력을 관리하는 매니저
        val inputManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        // 포커스를 준다.
        view.requestFocus()

        thread {
            SystemClock.sleep(500)
            // 키보드를 올린다.
            inputManager.showSoftInput(view, 0)
        }
    }
    // 키보드를 내리는 메서드
    fun hideSoftInput(){
        // 포커스가 있는 뷰가 있다면
        if(currentFocus != null){
            // 입력을 관리하는 매니저
            val inputManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            // 키보드를 내린다.
            inputManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
            // 포커스를 해제한다.
            currentFocus?.clearFocus()
        }
    }

    // 리사이클러 뷰 스크롤에 따라 fab 설정하는 메서드
    fun fabHideAndShow(recyclerView:RecyclerView, fab:FloatingActionButton){
        // 리사이클러뷰가 스크롤 상태가 변경되면 동작하는 리스너
        recyclerView.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if(oldScrollY == 0) {
                fab.show()
            } else {
                // 만약 제일 아래에 있는 상태라면..
                if (recyclerView.canScrollVertically(1) == false) {
                    // FloatingActionButton을 사라지게 된다.
                    fab.hide()
                } else {
                    // 만약 제일 아래에 있는 상태가 아니고 FloatingActionButton이 보이지 않는 상태라면
                    if (fab.isShown == false) {
                        // FloatingActionButton을 나타나게 한다.
                        fab.show()
                    }
                }
            }
        }
    }
}

