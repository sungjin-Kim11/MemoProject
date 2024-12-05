# 검색 기능 추가
* memoTitle 검색 기능
* categoryName 검색 기능
  * 키워드를 입력하면 해당 키워드를 포함하는 모든 정보가 출력
  * 아무것도 입력하지 않으면 전체 정보 출력

---
## memoTitle 검색 기능

### fragment_show_memo_all 변경 및 코드 추가
* LinearLayout을 추가하여 그 안에 toolbarShowMemoAll, textFieldSearchMemoName, RecyclerView를 배치하였다.

[res/layout/fragment_show_memo_all]

```kt
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:transitionGroup="true"
    tools:context=".fragment.ShowMemoAllFragment" >

    <LinearLayout
        android:layout_width="0dp"
        android:layout_height="0dp"
        android:orientation="vertical"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent">

        <com.google.android.material.appbar.MaterialToolbar
            android:id="@+id/toolbarShowMemoAll"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:background="@android:color/transparent"
            android:minHeight="?attr/actionBarSize"
            android:theme="?attr/actionBarTheme" />

        <com.google.android.material.textfield.TextInputLayout
            android:id="@+id/textFieldSearchMemoName"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_margin="10dp"
            android:hint="검색어"
            android:visibility="gone"
            app:endIconMode="clear_text"
            app:startIconDrawable="@drawable/search_24px">

            <com.google.android.material.textfield.TextInputEditText
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:imeOptions="actionSearch"
                android:singleLine="true" />
        </com.google.android.material.textfield.TextInputLayout>

        <androidx.recyclerview.widget.RecyclerView
            android:id="@+id/recyclerViewShowMemoAll"
            android:layout_width="match_parent"
            android:layout_height="match_parent" />
    </LinearLayout>

    <com.google.android.material.floatingactionbutton.FloatingActionButton
        android:id="@+id/fabShowMemoAllAdd"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginEnd="20dp"
        android:layout_marginBottom="20dp"
        android:clickable="true"
        android:src="@drawable/add_24px"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent" />
</androidx.constraintlayout.widget.ConstraintLayout>
```

### toolbar_show_memo_all를 생성하여 툴바에 돋보기 icon 추가
[res/menu/toolbar_show_memo_all.xml]

```kt
<?xml version="1.0" encoding="utf-8"?>
<menu xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:android="http://schemas.android.com/apk/res/android">

    <item
        android:id="@+id/search_memo"
        android:icon="@drawable/search_24px"
        android:title="Item"
        app:showAsAction="always" />
</menu>
```

### ShowMemoAllFragment 툴바 설정
* textFieldSearchMemoName의 visibility를 처음에 Gone으로 설정해 놨었는데, 만약 돋보기 아이콘을 클릭하면
  textFieldSearchMemoName가 나오고 한번더 누르면 사라지게 만들었다.

[fragment/ShowMemoAllFragment]

```kt
    // Toolbar를 구성하는 메서드
    fun settingToolbar(){
        fragmentShowMemoAllBinding.apply {
            if(arguments != null){
                if(arguments?.getString("MemoName") != MemoListName.MEMO_NAME_ADDED.str) {
                    toolbarShowMemoAll.title = arguments?.getString("MemoName")
                } else {
                    toolbarShowMemoAll.title = arguments?.getString("categoryName")
                }
            } else {
                toolbarShowMemoAll.title = MemoListName.MEMO_NAME_ALL.str
            }

            // 네비게이션 아이콘을 설정하고 누를 경우 NavigationView가 나타나도록 한다.
            toolbarShowMemoAll.setNavigationIcon(R.drawable.menu_24px)
            toolbarShowMemoAll.setNavigationOnClickListener {
                mainActivity.activityMainBinding.drawerLayoutMain.open()
            }

            // 메뉴
            toolbarShowMemoAll.inflateMenu(R.menu.toolbar_show_memo_all)
            toolbarShowMemoAll.setOnMenuItemClickListener {
                when(it.itemId){
                    // 검색
                    R.id.search_memo -> {
                        if(fragmentShowMemoAllBinding.textFieldSearchMemoName.visibility == View.VISIBLE){
                            fragmentShowMemoAllBinding.textFieldSearchMemoName.visibility = View.GONE
                        } else {
                            fragmentShowMemoAllBinding.textFieldSearchMemoName.visibility = View.VISIBLE
                        }
                    }
                }
                true
            }
        }
    }
```

### 메모 이름의 요소를 입력하면 해당 요소를 가지고 있는 모든 정보를 가져오는 메서드
[dao/MemoDao.kt]

```kt
    @Query("""
    SELECT * FROM MemoTable 
    WHERE memoTitle LIKE :keyword
    """)
    fun selectMemoDataAllByKeyword(keyword: String): List<MemoVO>
```

### 키워드로 검색하여 메모 데이터를 가져오는 메서드
* 검색하고자 하는 memoTitle 전부를 입력하지 않아도 해당 키워드가 포함된 모든 memoTitle을 가져온다.

[repository/MemoRepository.kt]

```kt
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
```

### 검색 키워드 입력 설정
* 입력된 값이 없으면 전체 정보가 출력이 되고, 입력한 값이 있으면 해당 키워드가 포함된 모든 memoTitle을 출력한다.
* 검색 버튼을 누르지 않아도 바로바로 갱신이 된다.

[fragment/ShowMemoAllFragment]

```kt
    // 입력 요소 설정
    fun settingTextField() {
        fragmentShowMemoAllBinding.apply {

            // EditText에 TextWatcher 추가
            textFieldSearchMemoName.editText?.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

                override fun afterTextChanged(s: Editable?) {
                    CoroutineScope(Dispatchers.Main).launch {
                        val keyword = s?.toString() ?: ""

                        val work1 = async(Dispatchers.IO) {
                            if (keyword.isBlank()) {
                                // 입력값이 없으면 전체 데이터를 가져옴
                                MemoRepository.selectMemoDataAll(mainActivity)
                            } else {
                                // 입력값이 있으면 해당 키워드를 포함하는 데이터를 가져옴
                                MemoRepository.selectMemoDataAllByKeyword(mainActivity, keyword)
                            }
                        }
                        // 결과를 갱신
                        memoList = work1.await()
                        recyclerViewShowMemoAll.adapter?.notifyDataSetChanged()
                    }
                }
            })
        }
    }
```

### 메서드 호출
[fragment/ShowMemoAllFragment]

```kt
        // 입력 요소 설정
        settingTextField()
```

---

## categoryName 검색 기능

### fragment_fragment_category_management.xml 변경 및 코드 추가
[res/layout/fragment_fragment_category_management.xml]

```kt
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:transitionGroup="true"
    tools:context=".fragment.CategoryManagementFragment" >

    <LinearLayout
        android:layout_width="0dp"
        android:layout_height="0dp"
        android:orientation="vertical"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent">

        <com.google.android.material.appbar.MaterialToolbar
            android:id="@+id/toolbarCategoryManagement"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:background="@android:color/transparent"
            android:minHeight="?attr/actionBarSize"
            android:theme="?attr/actionBarTheme" />

        <com.google.android.material.textfield.TextInputLayout
            android:id="@+id/textFieldSearchCategoryName"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_margin="10dp"
            android:hint="검색어"
            android:visibility="gone"
            app:endIconMode="clear_text"
            app:startIconDrawable="@drawable/search_24px">

            <com.google.android.material.textfield.TextInputEditText
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:imeOptions="actionSearch"
                android:singleLine="true" />
        </com.google.android.material.textfield.TextInputLayout>

        <androidx.recyclerview.widget.RecyclerView
            android:id="@+id/recyclerViewCategoryManagement"
            android:layout_width="match_parent"
            android:layout_height="match_parent" />
    </LinearLayout>

    <com.google.android.material.floatingactionbutton.FloatingActionButton
        android:id="@+id/fabCategoryManagementAdd"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginEnd="20dp"
        android:layout_marginBottom="20dp"
        android:clickable="true"
        android:src="@drawable/add_24px"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent" />
</androidx.constraintlayout.widget.ConstraintLayout>
```

### toolbar_show_category_all.xml를 생성하여 툴바에 돋보기 icon 추가
[res/menu/toolbar_show_category_all.xml]

```kt
<?xml version="1.0" encoding="utf-8"?>
<menu xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:android="http://schemas.android.com/apk/res/android">

    <item
        android:id="@+id/search_category"
        android:icon="@drawable/search_24px"
        android:title="Item"
        app:showAsAction="always" />
</menu>
```

### CategoryManagementFragment 툴바 설정
[fragment/CategoryManagementFragment]

```kt
    // Toolbar를 구성하는 메서드
    fun settingToolbar(){
        fragmentCategoryManagementBinding.apply {
            toolbarCategoryManagement.title = "카테고리 관리"
            // 네비게이션 아이콘을 설정하고 누를 경우 NavigationView가 나타나도록 한다.
            toolbarCategoryManagement.setNavigationIcon(R.drawable.menu_24px)
            toolbarCategoryManagement.setNavigationOnClickListener {
                mainActivity.activityMainBinding.drawerLayoutMain.open()
            }

            // 메뉴
            toolbarCategoryManagement.inflateMenu(R.menu.toolbar_show_memo_all)
            toolbarCategoryManagement.setOnMenuItemClickListener {
                when(it.itemId){
                    // 검색
                    R.id.search_memo -> {
                        if(fragmentCategoryManagementBinding.textFieldSearchCategoryName.visibility == View.VISIBLE){
                            fragmentCategoryManagementBinding.textFieldSearchCategoryName.visibility = View.GONE
                        } else {
                            fragmentCategoryManagementBinding.textFieldSearchCategoryName.visibility = View.VISIBLE
                        }
                    }
                }
                true
            }
        }
    }
```

### 카테고리 이름의 요소를 입력하면 해당 요소를 가지고 있는 모든 정보를 가져오는 메서드
[dao/CategoryDao.kt]

```kt
    @Query("""
    SELECT * FROM CategoryTable 
    WHERE categoryName LIKE :keyword
    """)
    fun selectCategoryDataAllByKeyword(keyword: String): List<CategoryVO>
```

### 키워드로 검색하여 카테고리 데이터를 가져오는 메서드
[repository/CategoryRepository.kt]

```kt
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
```

### 검색 키워드 입력 설정
[fragment/CategoryManagementFragment]

```kt
    // 입력 요소 설정
    fun settingTextField() {
        fragmentCategoryManagementBinding.apply {

            // EditText에 TextWatcher 추가
            textFieldSearchCategoryName.editText?.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

                override fun afterTextChanged(s: Editable?) {
                    CoroutineScope(Dispatchers.Main).launch {
                        val keyword = s?.toString() ?: ""

                        val work1 = async(Dispatchers.IO) {
                            if (keyword.isBlank()) {
                                // 입력값이 없으면 전체 데이터를 가져옴
                                CategoryRepository.selectCategoryAll(mainActivity)
                            } else {
                                // 입력값이 있으면 해당 키워드를 포함하는 데이터를 가져옴
                                CategoryRepository.selectCategoryDataAllByKeyword(mainActivity, keyword)
                            }
                        }
                        // 결과를 갱신
                        categoryList = work1.await()
                        recyclerViewCategoryManagement.adapter?.notifyDataSetChanged()
                    }
                }
            })
        }
    }
```

### 메서드 호출
[fragment/CategoryManagementFragment]

```kt
        // 입력 요소 설정
        settingTextField()
```
