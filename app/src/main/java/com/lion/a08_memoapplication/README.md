# ViewBinding 셋팅

[build.gradle.kts]

```kt
    viewBinding {
        enable = true
    }
```

[MainActivity.kt]

```kt
    val activityMainBinding by lazy{
        ActivityMainBinding.inflate(layoutInflater)
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(activityMainBinding.root)
```

---

# Navigation 구성

### 사용할 아이콘 파일들을 res/drawable 폴더에 넣어준다.

### NavigationView에 사용할 메뉴를 구성해준다.

[res/menu/navigation_menu.xml]
```xml
<?xml version="1.0" encoding="utf-8"?>
<menu xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:android="http://schemas.android.com/apk/res/android">

    <item
        android:id="@+id/navigation_menu_item_all"
        android:icon="@drawable/list_alt_24px"
        android:title="모든 메모 "
        app:showAsAction="ifRoom" />
    <item
        android:id="@+id/navigation_menu_item_favorite"
        android:icon="@drawable/check_24px"
        android:title="즐겨 찾기"
        app:showAsAction="ifRoom" />
    <item
        android:id="@+id/navigation_menu_item_secret"
        android:icon="@drawable/person_24px"
        android:title="비밀 메모"
        app:showAsAction="ifRoom" />
    <item
        android:id="@+id/navigation_menu_item_management_category"
        android:icon="@drawable/edit_24px"
        android:title="카테고리 관리" />
    <item
        android:id="@+id/navigation_menu_item_memo_category"
        android:icon="@drawable/id_card_24px"
        android:title="메모 카테고리"
        app:showAsAction="ifRoom">
        <menu>
            <item
                android:id="@+id/navigation_menu_item_category_basic"
                android:icon="@drawable/storefront_24px"
                android:title="기본 메모"
                app:showAsAction="ifRoom" />
        </menu>
    </item>
</menu>
```

### NavigationView에 Header 설정할 layout을 작성한다.

[res/layout/navigation_header_layout.xml]

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:padding="20dp"
    android:transitionGroup="true">

    <TextView
        android:id="@+id/textViewNavigationHeaderTitle1"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="TextView"
        android:textAppearance="@style/TextAppearance.AppCompat.Large" />

    <TextView
        android:id="@+id/textViewNavigationHeaderTitle2"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="10dp"
        android:text="TextView"
        android:textAppearance="@style/TextAppearance.AppCompat.Large" />
</LinearLayout>
```

### MainActivity의 화면을 구성해준다.

[res/layout/activity_main.xml]
```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/main"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:transitionGroup="true"
    tools:context=".MainActivity">

    <androidx.drawerlayout.widget.DrawerLayout
        android:id="@+id/drawerLayoutMain"
        android:layout_width="0dp"
        android:layout_height="0dp"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" >

        <androidx.coordinatorlayout.widget.CoordinatorLayout
            android:layout_width="match_parent"
            android:layout_height="match_parent">

            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                android:orientation="vertical">

                <androidx.fragment.app.FragmentContainerView
                    android:id="@+id/fragmentContainerView"
                    android:layout_width="match_parent"
                    android:layout_height="match_parent" />
            </LinearLayout>
        </androidx.coordinatorlayout.widget.CoordinatorLayout>

        <com.google.android.material.navigation.NavigationView
            android:id="@+id/navigationViewMain"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:layout_gravity="start" />
    </androidx.drawerlayout.widget.DrawerLayout>
</androidx.constraintlayout.widget.ConstraintLayout>
```
### 네비게이션을 구성하기 위한 메서드를 구현해준다.

```kt
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
        }
    }
```

### 메서드를 호출한다.

[MainActivity.kt - onCreate()]
```kt
        // 네비게이션 뷰를 구성하는 메서드를 호출한다.
        settingNavigationView()
```

---

# 기본 작업

### 라이브러리를 추가한다.

[build.gradle.kts]
```kt
plugins {
    ...
    kotlin("kapt")
}

dependencies {
    ...
    implementation("androidx.fragment:fragment-ktx:1.8.5")
    implementation("androidx.room:room-runtime:2.6.1")
    annotationProcessor("androidx.room:room-runtime:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")
}
```

### 프래그먼트 관련 기본 코드를 작성한다.

[MainActivity.kt]

```kt
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import com.google.android.material.transition.MaterialSharedAxis
```

```kt

    // 프래그먼트를 교체하는 함수
    fun replaceFragment(fragmentName: FragmentName, isAddToBackStack:Boolean, animate:Boolean, dataBundle: Bundle?){
        // 프래그먼트 객체
        val newFragment = when(fragmentName){

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
```


### 패키지를 만들어준다.

- dao : Database와 연동되는 모든 기능들을 구현하는 요소
- model : 값을 담아 관리하기 위한 객체들을 통칭
- vo : 데이터 베이스에 저장할 값이나 저장된 값을 담을 프로퍼티를 가지고 있는 Model의 한 종류
- view model : 화면에 보이고 있는 UI 요소들에 설정할 값들을 담을 프로퍼티를 가지고 있는 Model의 한 종류
- 그 밖에 사용 목적에 따라 다양한 model을 정의해서 사용한다.
- repository : vo 에 담겨진 데이터를 다른 model 객체에 담아주는 역할을 한다.
- service : model에 담겨진 데이터를 화면처리하는 요소로 전달하는 역할을 수행한다. 이 때, 다양한 데이터의 가공처리로 수행해준다.


- database
- dao
- model
- vo
- repository
- util
- fragment

---

# 모든 메모 화면 구성

### ShowMemoAllFragment 를 만들어준다.

[fragment/ShowMemoAllFragment.kt]
```kt
class ShowMemoAllFragment : Fragment() {

    lateinit var fragmentShowMemoAllBinding: FragmentShowMemoAllBinding
    lateinit var mainActivity: MainActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        fragmentShowMemoAllBinding = FragmentShowMemoAllBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity

        return fragmentShowMemoAllBinding.root
    }
}
```

### Fragment의 이름을 정의학 위한 util/Values.kt 파일을 만들어준다.

### Fragment의 이름을 정의해준다.

[util/Values.kt]
```kt
// 프래그먼트들의 이름
enum class FragmentName(val number:Int, var str:String){
    SHOW_MEMO_ALL_FRAGMENT(1, "ShowMemoAllFragment"),
}
```


### 프래그먼트의 객체를 생성한다.
[MainActivity.kt - replaceFragment()]
```kt
            // 전체 메모 화면
            FragmentName.SHOW_MEMO_ALL_FRAGMENT -> ShowMemoAllFragment()
```


### 첫 번째 프래그 먼트를 설정해준다.
[MainActivity.kt - onCreate()]
```kt
        // 첫 화면을 설정해준다.
        replaceFragment(FragmentName.SHOW_MEMO_ALL_FRAGMENT, false, false, null)
```

### Recyclerview의 항목으로 사용할 layout을 만들어준다.

[res/layout/row_text1.xml]
```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:orientation="vertical"
    android:padding="10dp"
    android:transitionGroup="true">

    <TextView
        android:id="@+id/textViewRow"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="TextView"
        android:textAppearance="@style/TextAppearance.AppCompat.Large" />
</LinearLayout>
```

### ShowMemoAllFragment의 화면을 구성한다

[res/layout/fragment_show_memo_all.xml]

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:transitionGroup="true"
    tools:context=".fragment.ShowMemoAllFragment" >

    <com.google.android.material.appbar.MaterialToolbar
        android:id="@+id/toolbarShowMemoAll"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:background="@android:color/transparent"
        android:minHeight="?attr/actionBarSize"
        android:theme="?attr/actionBarTheme"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />

    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/recyclerViewShowMemoAll"
        android:layout_width="0dp"
        android:layout_height="0dp"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/toolbarShowMemoAll" />

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

### RecyclerView 구성을 위한 임시 데이터를 정의한다

[fragment/ShowMemoAllFragment.kt]
```kt
    // RecyclerView 구성을 위한 임시데이터
    val tempData1 = Array(100){
        "메모 제목 $it"
    }
```

### ToolBar를 설정하는 메서드를 구현한다.

[fragment/ShowMemoAllFragment.kt]
```kt
    // Toolbar를 구성하는 메서드
    fun settingToolbar(){
        fragmentShowMemoAllBinding.apply {
            toolbarShowMemoAll.title = "전체 메모"
            // 네비게이션 아이콘을 설정하고 누를 경우 NavigationView가 나타나도록 한다.
            toolbarShowMemoAll.setNavigationIcon(R.drawable.menu_24px)
            toolbarShowMemoAll.setNavigationOnClickListener {
                mainActivity.activityMainBinding.drawerLayoutMain.open()
            }
        }
    }
```

### 메서드를 호출해준다.

[fragment/ShowMemoAllFragment.kt - onCreateView()]
```kt
        // Toolbar를 구성하는 메서드를 호출한다.
        settingToolbar()
```

### Adapter 클래스를 작성한다.
[fragment/ShowMemoAllFragment.kt]
```kt
    // RecyclerView의 어뎁터
    inner class RecyclerShowMemoAdapter : RecyclerView.Adapter<RecyclerShowMemoAdapter.ViewHolderMemoAdapter>(){
        // ViewHolder
        inner class ViewHolderMemoAdapter(val rowText1Binding: RowText1Binding) : RecyclerView.ViewHolder(rowText1Binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderMemoAdapter {
            val rowText1Binding = RowText1Binding.inflate(layoutInflater, parent, false)
            val viewHolderMemoAdapter = ViewHolderMemoAdapter(rowText1Binding)
            return viewHolderMemoAdapter
        }

        override fun getItemCount(): Int {
            return tempData1.size
        }

        override fun onBindViewHolder(holder: ViewHolderMemoAdapter, position: Int) {
            holder.rowText1Binding.textViewRow.text = tempData1[position]
        }
    }
```

### RecyclerView를 구성하는 메서드를 구현한다.

[fragment/ShowMemoAllFragment.kt]
```kt

    // RecyclerView를 구성하는 메서드
    fun settingRecyclerView(){
        fragmentShowMemoAllBinding.apply {
            recyclerViewShowMemoAll.adapter = RecyclerShowMemoAdapter()
            recyclerViewShowMemoAll.layoutManager = LinearLayoutManager(mainActivity)
            val deco = MaterialDividerItemDecoration(mainActivity, MaterialDividerItemDecoration.VERTICAL)
            recyclerViewShowMemoAll.addItemDecoration(deco)
        }
    }

```

### 메서드를 호출한다.

[fragment/ShowMemoAllFragment.kt - onCreateView()]
```kt
        // RecyclerView를 구성하는 메서드를 호출한다.
        settingRecyclerView()

```

---

# 메모 입력 화면

### AddMemoFragment를 만들어준다.

[fragment/AddMemoFragment.kt]
```kt
class AddMemoFragment : Fragment() {

    lateinit var fragmentAddMemoBinding: FragmentAddMemoBinding
    lateinit var mainActivity: MainActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        fragmentAddMemoBinding = FragmentAddMemoBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity

        return fragmentAddMemoBinding.root
    }

}
```

### Fragment 이름을 추가해준다.

[util/Values.kt - FragmentName]
```kt
    // 메모 추가 화면
    ADD_MEMO_FRAGMENT(2, "AddMemoFragment"),
```

### Fragment 객체를 생성한다.

[MainActivity.kt - replaceFragment()]
```kt
            // 메모 추가 화면
            FragmentName.ADD_MEMO_FRAGMENT -> AddMemoFragment()
```

### ShowMemoAllFragment 에서 FAB를 누르면 AddMemoFragment가 보이도록 한다.

[fragment/ShowMemoAllFragment.kt]
```kt
    // 버튼을 설정하는 메서드
    fun settingButton(){
        fragmentShowMemoAllBinding.apply {
            // fab를 누를 때
            fabShowMemoAllAdd.setOnClickListener { 
                // AddMemoFragment가 나타나게 한다.
                mainActivity.replaceFragment(FragmentName.ADD_MEMO_FRAGMENT, true, true, null)
            }
        }
    }
```

### 메서드를 호출한다.

[fragment/ShowMemoAllFragment.kt - onCreateView()]
```kt
        // 버튼을 설정하는 메서드를 호출한다.
        settingButton()
```

### AddMemoFragment의 화면을 구성한다.

[res/layout/fragment_add_memo.xml]
```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    tools:context=".fragment.AddMemoFragment" >

    <com.google.android.material.appbar.MaterialToolbar
        android:id="@+id/toolbarAddMemo"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:background="@android:color/transparent"
        android:minHeight="?attr/actionBarSize"
        android:theme="?attr/actionBarTheme" />

    <ScrollView
        android:layout_width="match_parent"
        android:layout_height="match_parent">

        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:orientation="vertical"
            android:padding="10dp" >

            <com.google.android.material.textfield.TextInputLayout
                android:id="@+id/textFieldAddMemoCategory"
                style="@style/Widget.Material3.TextInputLayout.OutlinedBox.ExposedDropdownMenu"
                android:layout_width="match_parent"
                android:layout_height="wrap_content">

                <AutoCompleteTextView
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:layout_weight="1"
                    android:inputType="none"/>
            </com.google.android.material.textfield.TextInputLayout>

            <com.google.android.material.textfield.TextInputLayout
                android:id="@+id/textFieldAddMemoTitle"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_marginTop="10dp"
                android:hint="메모 제목"
                app:endIconMode="clear_text">

                <com.google.android.material.textfield.TextInputEditText
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:singleLine="true" />
            </com.google.android.material.textfield.TextInputLayout>


            <com.google.android.material.textfield.TextInputLayout
                android:id="@+id/textFieldAddMemoText"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_marginTop="10dp"
                android:hint="메모 내용"
                app:endIconMode="clear_text">

                <com.google.android.material.textfield.TextInputEditText
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"/>
            </com.google.android.material.textfield.TextInputLayout>
        </LinearLayout>
    </ScrollView>
</LinearLayout>
```

### 카테고리 선택 요소 설정 메서드를 만들어준다.

[fragment/AddMemoFragment.kt]
```kt
    // 카테고리 선택 메뉴를 구성하는 메서드
    fun settingSelectCategoryMenu(){
        fragmentAddMemoBinding.apply {
            // 특정 카테고리를 선택했을 때
            textFieldAddMemoCategory.editText?.setText("카테고리")

            // 특정 카테고리가 선택이 안되어 있을 때
            // val items = arrayOf("Item 1", "Item 2", "Item 3", "Item 4")
            // (textFieldAddMemoCategory.editText as? MaterialAutoCompleteTextView)?.setSimpleItems(items)
        }
    }
```

### 메서드를 호출해준다.

[fragment/AddMemoFragment.kt - onCreateView()]
```kt
        // 카테고리 선택 메뉴를 구성하는 메서드를 호출한다.
        settingSelectCategoryMenu()
```

### 툴바를 구성하는 메서드를 만든다.

[fragment/AddMemoFragment.kt]
```kt
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
                        mainActivity.removeFragment(FragmentName.ADD_MEMO_FRAGMENT)
                    }
                }
                true
            }
        }
    }

```

### 메서드를 호출한다.
[fragment/AddMemoFragment.kt - onCreateView()]
```kt
        // 툴바를 구성하는 메서드
        settingToolbar()
```

### 비밀번호 입력 요소에 대한 메서드를 구현해준다.
[fragment/AddMemoFragment.kt]
```kt
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
```

### 메서드를 호출한다.

[fragment/AddMemoFragment.kt - onCreateView()]
```kt
        // 체크박스와 비밀번호 입력 요소 설정
        settingSecretTextField()
```

---

# 메모 내용을 보는 화면

### ReadMemoFragment 를 만들어준다.

[fragment/ReadMemoFragment.kt]
```kt
class ReadMemoFragment : Fragment() {

    lateinit var fragmentReadMemoBinding: FragmentReadMemoBinding
    lateinit var mainActivity:MainActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentReadMemoBinding = FragmentReadMemoBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity

        return fragmentReadMemoBinding.root
    }

}
```

### Fragment의 이름을 정의해준다.

[util/Values.kt - FragmentName]
```kt
    // 메모 읽기 화면
    READ_MEMO_FRAGMENT(3, "ReadMemoFragment"),
```

### Fragment 객체를 생성한다.

[MainActivity.kt - replaceFragment()]
```kt
            // 메모 읽기 화면
            FragmentName.READ_MEMO_FRAGMENT -> ReadMemoFragment()
```

### 항목을 누를 때 ReadMemoFragment가 보이도록 구현해준다.

[fragment/ShowMemoAllFragment.kt - RecyclerShowMemoAdapter()]
```kt
            rowText1Binding.root.setOnClickListener {
                mainActivity.replaceFragment(FragmentName.READ_MEMO_FRAGMENT, true, true, null)
            }
```

### ReadMemoFragment 의 화면을 구성해준다.

[res/layout/fragment_read_memo.xml]
```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:transitionGroup="true"
    tools:context=".fragment.ReadMemoFragment" >

    <com.google.android.material.appbar.MaterialToolbar
        android:id="@+id/toolbarReadMemo"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:background="@android:color/transparent"
        android:minHeight="?attr/actionBarSize"
        android:theme="?attr/actionBarTheme" />

    <ScrollView
        android:layout_width="match_parent"
        android:layout_height="match_parent">

        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:orientation="vertical"
            android:padding="10dp">

            <com.google.android.material.textfield.TextInputLayout
                android:id="@+id/textFieldReadMemoCategory"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:hint="카테고리">

                <com.google.android.material.textfield.TextInputEditText
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:singleLine="true"
                    android:text=" "
                    android:enabled="false"
                    android:textColor="#000000"/>

            </com.google.android.material.textfield.TextInputLayout>

            <com.google.android.material.textfield.TextInputLayout
                android:id="@+id/textFieldReadMemoTitle"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_marginTop="10dp"
                android:hint="메모 제목">

                <com.google.android.material.textfield.TextInputEditText
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:singleLine="true"
                    android:text=" "
                    android:enabled="false"
                    android:textColor="#000000"/>
            </com.google.android.material.textfield.TextInputLayout>

            <com.google.android.material.textfield.TextInputLayout
                android:id="@+id/textFieldReadMemoText"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_marginTop="10dp"
                android:hint="메모 내용">

                <com.google.android.material.textfield.TextInputEditText
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:text=" "
                    android:enabled="false"
                    android:textColor="#000000"/>
            </com.google.android.material.textfield.TextInputLayout>

        </LinearLayout>
    </ScrollView>

</LinearLayout>
```

### 툴바 구성을 위한 메뉴 파일을 만들어준다.

[res/menue/toolbar_read_memo_menu.xml]
```xml
<?xml version="1.0" encoding="utf-8"?>
<menu xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto">

    <item
        android:id="@+id/toolbar_read_memo_menu_item_modify"
        android:icon="@drawable/edit_24px"
        android:title="메모 수정"
        app:showAsAction="ifRoom" />

    <item
        android:id="@+id/toolbar_read_memo_menu_item_delete"
        android:icon="@drawable/delete_24px"
        android:title="메모 삭제"
        app:showAsAction="ifRoom" />
</menu>
```

### 툴바를 구성하는 메서드를 구현해준다.

[fragment/ReadMemoFragment.kt]
```kt
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
        }
    }
```

### 메서드를 호출한다.

[fragment/ReadMemoFragment.kt - onCreateView()]
```kt
        // 툴바를 구성하는 메서드
        settingToolbar()
```

---

# 수정 화면 구성

### ModifyMemoFragment 를 만들어준다.

[fragment/ModifyMemoFragment]
```kt
class ModifyMemoFragment : Fragment() {

    lateinit var fragmentModifyMemoBinding: FragmentModifyMemoBinding
    lateinit var mainActivity: MainActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentModifyMemoBinding = FragmentModifyMemoBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity

        return fragmentModifyMemoBinding.root
    }

}
```

### Fragment의 이름을 설정해준다.

[util/Values.kt - FragmentName]
```kt
    // 메모 수정 화면
    MODIFY_MEMO_FRAGMENT(4, "ModifyMemoFragment"),
```

### Fragment의 객체를 생성한다.

[MainActivity.kt - replaceFragment()]
```kt
            // 메모 수정 화면
            FragmentName.MODIFY_MEMO_FRAGMENT -> ModifyMemoFragment()
```

### 수정 메뉴를 누르면 프래그먼트가 나타나게 한다.

[fragment/ReadMemoFragment.kt - settingToolbar()]
```kt
                    // 수정
                    R.id.toolbar_read_memo_menu_item_modify -> {
                        mainActivity.replaceFragment(FragmentName.MODIFY_MEMO_FRAGMENT, true, true, null)
                    }
```

### ModifyMomoFragment의 화면을 구성해준다.

[res/layout/fragment_modify_memo.xml]

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:orientation="vertical"
    tools:context=".fragment.ModifyMemoFragment">

    <com.google.android.material.appbar.MaterialToolbar
        android:id="@+id/toolbarModifyMemo"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:background="@android:color/transparent"
        android:minHeight="?attr/actionBarSize"
        android:theme="?attr/actionBarTheme" />

    <ScrollView
        android:layout_width="match_parent"
        android:layout_height="match_parent">

        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:orientation="vertical"
            android:padding="10dp">

            <com.google.android.material.textfield.TextInputLayout
                android:id="@+id/textFieldModifyMemoCategory"
                style="@style/Widget.Material3.TextInputLayout.OutlinedBox.ExposedDropdownMenu"
                android:layout_width="match_parent"
                android:layout_height="wrap_content">

                <com.google.android.material.textfield.MaterialAutoCompleteTextView
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:layout_weight="1"
                    android:inputType="none" />
            </com.google.android.material.textfield.TextInputLayout>

            <com.google.android.material.textfield.TextInputLayout
                android:id="@+id/textFieldModifyMemoTitle"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_marginTop="10dp"
                android:hint="메모 제목"
                app:endIconMode="clear_text">

                <com.google.android.material.textfield.TextInputEditText
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:singleLine="true"
                    android:text=" "/>
            </com.google.android.material.textfield.TextInputLayout>

            <com.google.android.material.textfield.TextInputLayout
                android:id="@+id/textFieldModifyMemoText"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_marginTop="10dp"
                android:hint="메모 내용"
                app:endIconMode="clear_text">

                <com.google.android.material.textfield.TextInputEditText
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:text=" "/>
            </com.google.android.material.textfield.TextInputLayout>

            <CheckBox
                android:id="@+id/checkBoxModifyMemoSecret"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_marginTop="10dp"
                android:text="비밀메모" />

            <com.google.android.material.textfield.TextInputLayout
                android:id="@+id/textFieldModifyMemoPassword"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_marginTop="10dp"
                android:hint="비밀번호"
                app:endIconMode="password_toggle">

                <com.google.android.material.textfield.TextInputEditText
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:inputType="number|numberPassword"
                    android:singleLine="true"/>
            </com.google.android.material.textfield.TextInputLayout>

        </LinearLayout>

    </ScrollView>

</LinearLayout>
```

### 카테고리를 선택할 수 있는 입력 요소를 구성해준다.

[fragment/ModifyMemoFragment.kt]
```kt
    // 카테고리 선택 메뉴를 구성하는 메서드
    fun settingSelectCategoryMenu(){
        fragmentModifyMemoBinding.apply {
            val items = arrayOf("Item 1", "Item 2", "Item 3", "Item 4")

            val a1 = textFieldModifyMemoCategory.editText as MaterialAutoCompleteTextView
            a1.setSimpleItems(items)
            a1.setText("Item 1", false)
        }
    }
```

### 툴바를 구성하는 메서드를 구현한다.

[fragment/ModifyMemoFragment.kt]
```kt
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
                        mainActivity.removeFragment(FragmentName.MODIFY_MEMO_FRAGMENT)
                    }
                }
                true
            }
        }
    }
```

### 비밀번호 입력 요소를 구성하는 메서드를 구현한다.

[fragment/ModifyMemoFragment.kt]
```kt
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
                } else {
                    // 비밀번호 입력 요소를 비활성화 한다.
                    textFieldModifyMemoPassword.editText?.isEnabled = false
                    textFieldModifyMemoPassword.editText?.setText("")
                }
            }
        }
    }
```

### 메서드를 호출한다.

[fragment/ModifyMemoFragment.kt - onCreateView]
```kt

        // 카테고리 선택 메뉴를 구성하는 메서드를 호출한다.
        settingSelectCategoryMenu()
        // 툴바를 구성하는 메서드
        settingToolbar()
        // 체크박스와 비밀번호 입력 요소 설정
        settingSecretTextField()

```

---

# 네비게이션 뷰를 통해 보여질 화면들 셋팅

### 네비게이션 메뉴를 눌렀을 때 프래그먼트가 교체되도록 한다

[MainActivity.kt - settingNavigationView()]
```kt
            // 네비게이션의 메뉴를 눌렀을 때
            navigationViewMain.setNavigationItemSelectedListener {
                when(it.itemId){
                    R.id.navigation_menu_item_all -> {
                        replaceFragment(FragmentName.SHOW_MEMO_ALL_FRAGMENT, false, false, null)
                    }
                    R.id.navigation_menu_item_favorite -> {
                        replaceFragment(FragmentName.SHOW_MEMO_ALL_FRAGMENT, false, false, null)
                    }
                    R.id.navigation_menu_item_secret -> {
                        replaceFragment(FragmentName.SHOW_MEMO_ALL_FRAGMENT, false, false, null)
                    }
                    R.id.navigation_menu_item_management_category -> {

                    }
                    R.id.navigation_menu_item_category_basic -> {
                        replaceFragment(FragmentName.SHOW_MEMO_ALL_FRAGMENT, false, false, null)
                    }
                    else -> {
                        replaceFragment(FragmentName.SHOW_MEMO_ALL_FRAGMENT, false, false, null)
                    }
                }

                // 닫아준다.
                drawerLayoutMain.close()
                true
            }
```

---

# 카테고리 관리 화면 구성

### CategoryManagementFragment 를 만들어준다.

[fragment/CategoryManagementFragment.kt]
```kt
class CategoryManagementFragment : Fragment() {

    lateinit var fragmentCategoryManagementBinding: FragmentCategoryManagementBinding
    lateinit var mainActivity: MainActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentCategoryManagementBinding = FragmentCategoryManagementBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity

        return fragmentCategoryManagementBinding.root
    }

}
```

### 프래그먼트 이름을 정의해준다.

[util/Values.kt - FragmentName]
```kt
    // 카테고리 관리 화면
    CATEGORY_MANAGEMENT_FRAGMENT(5, "CategoryManagementFragment"),
```

### 프래그먼트 객체를 생성한다.

[MainActivity.kt - replaceFragment()]
```kt
            // 카테고리 관리 화면
            FragmentName.CATEGORY_MANAGEMENT_FRAGMENT -> CategoryManagementFragment()
```

### NavigationView의 메뉴를 누르면 프래그먼트가 보이도록 한다.

[MainActivity.kt - settingNavigationView()]
```kt
                    R.id.navigation_menu_item_management_category -> {
                        replaceFragment(FragmentName.CATEGORY_MANAGEMENT_FRAGMENT, false, false, null)
                    }
```

### CategoryManagementFragment 의 화면을 구성한다.

[res/layout/fragment_category_management.xml]
```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:transitionGroup="true"
    tools:context=".fragment.CategoryManagementFragment" >

    <com.google.android.material.appbar.MaterialToolbar
        android:id="@+id/toolbarCategoryManagement"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:background="@android:color/transparent"
        android:minHeight="?attr/actionBarSize"
        android:theme="?attr/actionBarTheme"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />

    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/recyclerViewCategoryManagement"
        android:layout_width="0dp"
        android:layout_height="0dp"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/toolbarCategoryManagement" />

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

### drawable 폴더에 more_vert_24px.xml 파일을 넣어준다.

### RecyclerView의 항목 구성을 위한 layout 파일을 만들어준다.

[res/layout/row_category_management.xml]
```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:orientation="horizontal"
    android:padding="10dp">

    <TextView
        android:id="@+id/textViewRowCategoryManagement"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_weight="1"
        android:text="TextView"
        android:textAppearance="@style/TextAppearance.AppCompat.Large" />

    <Button
        android:id="@+id/buttonRowCategoryManagement"
        style="@style/Widget.Material3.Button.IconButton"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        app:icon="@drawable/more_vert_24px" />
</LinearLayout>
```


### RecyclerView 구성을 위한 임시 데이터를 정의해준다.

[fragment/CategoryManagementFragment.kt]
```kt
    // RecyclerView 구성을 위한 임시데이터
    val tempData1 = Array(100){
        "카테고리 $it"
    }
```

### 툴바를 구성하는 메서드를 구현해준다.
[fragment/CategoryManagementFragment.kt]
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
        }
    }
```

### RecyclerView의 어뎁터 클래스를 작성해준다.

[fragment/CategoryManagementFragment.kt]
```kt
    // RecyclerView의 어뎁터
    inner class RecyclerViewCategoryManagementAdapter : RecyclerView.Adapter<RecyclerViewCategoryManagementAdapter.ViewHolderCategoryManagement>(){
        // ViewHolder
        inner class ViewHolderCategoryManagement(val rowCategoryManagementBinding: RowCategoryManagementBinding) : RecyclerView.ViewHolder(rowCategoryManagementBinding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderCategoryManagement {
            val rowCategoryManagementBinding = RowCategoryManagementBinding.inflate(layoutInflater, parent, false)
            val viewHolderCategoryManagement = ViewHolderCategoryManagement(rowCategoryManagementBinding)
            return viewHolderCategoryManagement
        }

        override fun getItemCount(): Int {
            return tempData1.size
        }

        override fun onBindViewHolder(holder: ViewHolderCategoryManagement, position: Int) {
            holder.rowCategoryManagementBinding.textViewRowCategoryManagement.text = tempData1[position]
        }
    }
```

### RecyclerView를 구성하는 메서들를 구현해준다.
[fragment/CategoryManagementFragment.kt]
```kt
    // RecyclerView를 구성하는 메서드
    fun settingRecyclerView(){
        fragmentCategoryManagementBinding.apply {
            recyclerViewCategoryManagement.adapter = RecyclerViewCategoryManagementAdapter()
            recyclerViewCategoryManagement.layoutManager = LinearLayoutManager(mainActivity)
            val deco = MaterialDividerItemDecoration(mainActivity, MaterialDividerItemDecoration.VERTICAL)
            recyclerViewCategoryManagement.addItemDecoration(deco)
        }
    }
```

### 버튼을 구성하는 메서드를 구현해준다.
[fragment/CategoryManagementFragment.kt]
```kt
    // 버튼을 설정하는 메서드
    fun settingButton(){
        fragmentCategoryManagementBinding.apply {
            // fab를 누를 때
            fabCategoryManagementAdd.setOnClickListener {

            }
        }
    }
```

### 메서드를 호출해준다.

[fragment/CategoryManagementFragment.kt - onCreateView()]
```kt
        // Toolbar를 구성하는 메서드를 호출한다.
        settingToolbar()
        // RecyclerView를 구성하는 메서드를 호출한다.
        settingRecyclerView()
        // 버튼을 설정하는 메서드를 호출한다.
        settingButton()
```

### 다이얼로그에서 사용할 layout 파일을 만들어준다.

[res/layout/dialog_category_management.xml]
```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:padding="10dp"
    android:transitionGroup="true">

    <com.google.android.material.textfield.TextInputLayout
        android:id="@+id/textFieldDialogCategoryManagement"
        android:layout_width="match_parent"
        android:layout_height="wrap_content">

        <com.google.android.material.textfield.TextInputEditText
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:singleLine="true" />
    </com.google.android.material.textfield.TextInputLayout>
</LinearLayout>
```


### 키보드 관련 메서드를 구현해준다.

[MainActivity.kt]
```kt

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
```

### fab를 누르면 다이얼로그가 뜨도록 구현해준다.

[fragment/CategoryManagementFragment.kt]
```kt
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

                }
                // 입력요소에 포커스를 준다.
                mainActivity.showSoftInput(dialogCategoryManagementBinding.textFieldDialogCategoryManagement.editText!!)

                builder.show()
            }
        }
    }
```

### BottomSheet를 위한 Fragment를 만든다

[fragment/CategoryManagementBottomSheetFragment.kt]

```kt
class CategoryManagementBottomSheetFragment(val categoryManagementFragment: CategoryManagementFragment) : BottomSheetDialogFragment() {
  
    lateinit var fragmentCategoryManagementBottomSheetBinding: FragmentCategoryManagementBottomSheetBinding
    lateinit var mainActivity: MainActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentCategoryManagementBottomSheetBinding = FragmentCategoryManagementBottomSheetBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity
        
        return fragmentCategoryManagementBottomSheetBinding.root
    }
}
```

### BottomSheet 를 띄워준다.

[fragment/CategoryManagementFragment.kt - RecyclerViewCategoryManagementAdapter]
```kt
                // BottomSheet를 띄운다.
                val categoryManagementBottomSheetFragment = CategoryManagementBottomSheetFragment(this@CategoryManagementFragment)
                categoryManagementBottomSheetFragment.show(mainActivity.supportFragmentManager, "BottomSheet")
```

### BottomSheet의 화면을 구성해준다.

[res/layout/fragment_category_management_bottom_sheet.xml]
```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:paddingStart="10dp"
    android:paddingEnd="10dp"
    android:transitionGroup="true"
    tools:context=".fragment.CategoryManagementBottomSheetFragment" >

    <com.google.android.material.bottomsheet.BottomSheetDragHandleView
        android:id="@+id/view"
        android:layout_width="match_parent"
        android:layout_height="wrap_content" />

    <Button
        android:id="@+id/buttonCategoryManagementBottomSheetModify"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="10dp"
        android:text="수정" />

    <Button
        android:id="@+id/buttonCategoryManagementBottomSheetDeleteOnlyCategory"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="10dp"
        android:text="카테고리만 삭제" />

    <Button
        android:id="@+id/buttonCategoryManagementBottomSheetDeleteCategoryWithMemos"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="10dp"
        android:text="카테고리와 메모 삭제" />

    <Space
        android:layout_width="match_parent"
        android:layout_height="30dp" />
</LinearLayout>
```

### BottomSheet 의 버튼을 구성하는 메서드를 구현한다.

[fragment/CategoryManagementBottomSheetFragment.kt]
```kt
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

                }
                builder.show()

                // BottomSheet를 내려준다.
                dismiss()
            }
        }
    }
```

---

# 데이터베이스 사용 준비

### 저장할 데이터를 정리한다.

```text
메모
메모 번호
메모 제목
메모 내용
비밀 메모 여부
즐겨찾기 여부
비밀번호
카테고리 번호

카테고리
카테고리 번호
카테고리 이름
```

### VO 클래스를 만들어준다.

- vo/MemoVO.kt
- vo/CategoryVO.kt

[vo/MemoVO.kt]

```kt
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "MemoTable")
data class MemoVO(
    // 메모 번호
    @PrimaryKey(autoGenerate = true)
    var memoIdx:Int = 0,
    // 메모 제목
    var memoTitle:String = "",
    // 메모 내용
    var memoText:String = "",
    // 비밀 메모 여부
    var memoIsSecret:Boolean = false,
    // 즐겨찾기 여부
    var memoIsFavorite:Boolean = false,
    // 비밀번호
    var memoPassword:String = "",
    // 카테고리 번호
    var memoCategoryIdx:Int = 0
)
```

[vo/CategoryVO.kt]
```kt
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "CategoryTable")
data class CategoryVO(
    // 카테고리 번호
    @PrimaryKey(autoGenerate = true)
    var categoryIdx:Int = 0,
    // 카테고리 이름
    var categoryName:String = ""
)
```

### dao를 만들어준다.

- dao/MemoDao.kt
- dao/CategoryDao.kt

[dao/MemoDao.kt]

```kt
import androidx.room.Dao

@Dao
interface MemoDao {
}
```

[dao/CategoryDao.kt]

```kt
import androidx.room.Dao

@Dao
interface CategoryDao {
}
```

### 데이터 베이스 클래스를 작성해준다.

[database/MemoDataBase.kt]
```kt

@Database(entities = [MemoVO::class, CategoryVO::class], version = 1, exportSchema = true)
abstract class MemoDataBase : RoomDatabase(){
    // dao
    abstract fun memoDao() : MemoDao
    abstract fun categoryDao() : CategoryDao
    
    companion object{
        // 데이터 베이스 파일 이름
        val dataBaseFileName = "MemoDatabase.db"

        // 데이터 베이스 객체를 담을 변수
        var memoDatabase:MemoDataBase? = null
        @Synchronized
        fun getInstance(context: Context) : MemoDataBase?{
            // 만약 데이터베이스 객체가 null이라면 객체를 생성한다.
            // 데이터베이스 파일 이름 꼭 변경!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            synchronized(MemoDataBase::class){
                memoDatabase = Room.databaseBuilder(
                    context.applicationContext, MemoDataBase::class.java, dataBaseFileName
                ).build()
            }
            return memoDatabase
        }

        // 데이터 베이스 객체가 소멸될 때 호출되는 메서드
        fun destroyInstance(){
            memoDatabase = null
        }
    }
}
```

### model 클래스를 만들어준다.
- model/MemoModel.kt
- model/CategoryModel.kt

[model/MemoModel.kt]

```kt
data class MemoModel(
    // 메모 번호
    var memoIdx:Int = 0,
    // 메모 제목
    var memoTitle:String = "",
    // 메모 내용
    var memoText:String = "",
    // 비밀 메모 여부
    var memoIsSecret:Boolean = false,
    // 즐겨찾기 여부
    var memoIsFavorite:Boolean = false,
    // 비밀번호
    var memoPassword:String = "",
    // 카테고리 번호
    var memoCategoryIdx:Int = 0
)
```

[model/CategoryModel.kt]
```kt
data class CategoryModel(
    // 카테고리 번호
    var categoryIdx:Int = 0,
    // 카테고리 이름
    var categoryName:String = ""
)
```

### Repository를 만들어준다.
- repository/MemoRepository.kt
- repository/CategoryRepository.kt

[repository/MemoRepository.kt]

```kt
class MemoRepository {
    companion object{

    }
}
```

[repository/CategoryRepository.kt]

```kt
class CategoryRepository {
    companion object{

    }
}
```

---

# 처음 설치시 처리
- 카테고리 테이블에 기본 메모 데이터를 저장한다.

### CategoryDao에 모든 데이터를 가져오는 메서드를 정의한다.

[dao/CategoryDao.kt]
```kt
    // 모든 카테고리 정보를 가져오는 메서드
    @Query("""
        select * from CategoryTable
        order by categoryIdx
    """)
    fun selectCategoryAll() : List<CategoryVO>
```
### CategoryRepository에 모든 데이터를 가져오는 메서드를 구현한다.

[repository/CategoryRepository.kt]
```kt
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
```

### 기본 메모 메뉴는 삭제한다.

[res/menu/navigation_menu.xml]

```xml

<item
    android:id="@+id/navigation_menu_item_memo_category"
    android:icon="@drawable/id_card_24px"
    android:title="메모 카테고리"
    app:showAsAction="ifRoom">
    <menu>
        <item
            android:id="@+id/navigation_menu_item_category_basic"
            android:checkable="true"
            android:icon="@drawable/storefront_24px"
            android:title="기본 메모"
            app:showAsAction="ifRoom" />
    </menu>
</item>

```

### 기본 메뉴 메뉴에 대한 코드도 삭제한다.

[MainActivity.kt - settingNavigationView()]
```kt
                    R.id.navigation_menu_item_category_basic -> {
                        replaceFragment(FragmentName.SHOW_MEMO_ALL_FRAGMENT, false, false, null)
                    }
```

### 카테고리 정보를 저장하는 메서드를 정의해준다.
[dao/CategoryDao.kt]
```kt
    
    // 카테고리 정보를 저장하는 메서드
    @Insert
    fun insertCategoryData(categoryVO: CategoryVO)
```

### 카테고리 데이터를 저장하는 메서드를 구현해준다.
[repository/CategoryRepository.kt]
```kt
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
```

### 데이터 베이스 생성시 초기 데이터가 들어갈 수 있도록 데이터 베이스 파일을 수정해준다.

[database/MemoDataBase.kt - getInstance()]
```kt
                // 데이터 베이스 파일에 대한 사건이 발생했을 때 동작하는 콜백
                memoDatabaseBuilder.addCallback(object : Callback(){
                    // 데이터 베이스 파일이 만들어질 때 호출되는 메서드
                    // 생성된 테이블에 초기 데이터를 저장하는 작업을 한다.
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        GlobalScope.launch {
                            withContext(Dispatchers.IO){
                                val categoryModel = CategoryModel(categoryName = "기본 메모")
                                CategoryRepository.insertCategoryData(context, categoryModel)
                            }
                        }
                    }
                })
```

### 카테고리 데이터를 담을 리스트를 정의해준다.
[MainActivity.kt]
```kt
    // NavigationView의 메뉴 중 카테고리 데이터를 담을 리스트
    var categoryList = mutableListOf<CategoryModel>()
```

### 데이터를 가져와 네비게이션 메뉴를 추가해주는 메서드를 구현한다.

[MainActivity.kt]
```kt

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

                // 카테고리의 수 만큼 반복한다\.
                // 카테고리의 수 만큼 반복한다
                categoryList.forEachIndexed { index, categoryModel ->
                    val subItem = subMenu.add(Menu.NONE, Menu.FIRST + index + 1, Menu.NONE, categoryModel.categoryName)
                    subItem.setIcon(R.drawable.edit_24px)
                }
            }
        }
    }
```

### 메서드를 호출한다.

[MainActivity.kt - onCreate()]
```kt
        // 카테고리 메뉴는 추가해는 메서드를 호출한다.
        addCategoryMenu()
```

### RecyclerView와 FAB 연동 처리 메서드를 구현해준다.

[MainActivity.kt]
```kt
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
```

### 메서드를 호출한다.

[fragment/CategoryManagementFragment.kt - settingRecyclerView()]
```kt
            // recyclerView and fab
            mainActivity.fabHideAndShow(recyclerViewCategoryManagement, fabCategoryManagementAdd)
```

### RecyclerView 구성을 위한 리스트를 정의해준다.
[fragment/CategoryManagementFragment.kt - settingRecyclerView()]
```kt
//    val tempData1 = Array(100){
//        "카테고리 $it"
//    }

    // RecyclerView 구성을 위한 리스트
    var categoryList = mutableListOf<CategoryModel>()
```

### Adapter를 수정한다.
[fragment/CategoryManagementFragment.kt - RecyclerViewCategoryManagementAdapter]
```kt
        override fun getItemCount(): Int {
            return categoryList.size
        }

        override fun onBindViewHolder(holder: ViewHolderCategoryManagement, position: Int) {
            holder.rowCategoryManagementBinding.textViewRowCategoryManagement.text = categoryList[position].categoryName
        }
```

### 데이터를 가져와 RecyclerView를 갱신하는 메서드를 구현해준다.
[fragment/CategoryManagementFragment.kt]
```kt
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
```

### 메서드를 호출한다.

[fragment/CategoryManagementFragment.kt - onCreateView()]
```kt
        // 데이터를 가져와 RecyclerView를 갱신하는 메서드를 호출한다.
        refreshRecyclerView()
```

### 카테고리 추가 다이얼로그의 저장기능을 구현한다.
[fragment/CategoryManagementFragment.kt - settingButton()]
```kt
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
                    }
                }
```

---

# 메모 추가 기능

### 메모 목록을 보는 화면에서 어떤 것을 눌렀는지 구분하기 위한 값을 정의해준다.

[util/Values.kt]
```kt
// 메모 목록을 보는 화면에서 어떤 것을 눌렀는지 구분하기 위한 값
enum class MemoListName(val number:Int, val str:String){
    MEMO_NAME_ALL(1, "모든 메모"),
    MEMO_NAME_FAVORITE(2, "즐겨 찾기"),
    MEMO_NAME_SECRET(3, "비밀 메모"),
    MEMO_NAME_ADDED(4, "추가된 메모"),
}
```

### 네비게이션의 메뉴를 눌렀을 때에서 데이터를 전달할 수 있도록 구현해준다.

[MainActivity.kt - settingNavigationView()]
```kt
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
```

### 전달한 문자열을 ShowMemoAllFragment의 툴바의 타이틀로 설정해준다

[fragment/ShowMemoAllFragment.kt - settingToolbar()]

```kt
            if(arguments != null){
                if(arguments?.getString("MemoName") != MemoListName.MEMO_NAME_ADDED.str) {
                    toolbarShowMemoAll.title = arguments?.getString("MemoName")
                } else {
                    toolbarShowMemoAll.title = arguments?.getString("categoryName")
                }
            } else {
                toolbarShowMemoAll.title = "모든 메모"
            }
```

### RecyclerView의 제일 하단으로 오면 FAB가 사라지게 한다.

```kt
            mainActivity.fabHideAndShow(recyclerViewShowMemoAll, fabShowMemoAllAdd)
```

---

# 메모 작성기능을 구현한다.

### 메모 작성 화면으로 이동할 때 사용자가 선택한 카테고리 정보를 전달한다.

[fragment/ShowMemoAllFragment.kt - settingButton()]
```kt
                // 데이터를 담을 번들
                val dataBundle = Bundle()
                if(arguments != null) {
                    dataBundle.putString("MemoName", arguments?.getString("MemoName")!!)
                    // 만약 카테고리를 선택해서 온 것이라면.
                    if (arguments?.getString("MemoName") == MemoListName.MEMO_NAME_ADDED.str) {
                        dataBundle.putInt("categoryIdx", arguments?.getInt("categoryIdx")!!)
                        dataBundle.putString("categoryName", arguments?.getString("categoryName")!!)
                    }
                } else {
                    dataBundle.putString("MemoName", MemoListName.MEMO_NAME_ALL.str)
                }
```

### 카테고리 선택 메뉴를 구성하기 위한 데이터를 담을 리스트를 선언한다.

[fragment/AddMemoFragment.kt]
```kt
    // 카테고리 목록 데이터를 담을 리스트
    val categoryNameList = mutableListOf<String>()
    val categoryIdxList = mutableListOf<Int>()
```

### 카테고리 선택 메뉴를 구성해준다.

[fragment/AddMemoFragment.kt - settingSelectCategoryMenu()]
```kt
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
```

### 메모 정보를 저장하는 메서드를 정의해준다.
[dao/MemoDao.kt]
```kt
    // 메모 내용을 저장하는 메서드
    @Insert
    fun insertMemoData(memoVO: MemoVO)
```

[repository/MemoRepository.kt]

```kt
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
```

### 메모 내뇽을 저장하는 메서드를 구현한다.

[fragment/AddMemoFragment.kt]
```kt
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
```

### 메서드를 호출한다.

[fragment/AddMemoFragment.kt - settingToolbar()]
```kt
                    Menu.FIRST + 1 ->{
                        // mainActivity.removeFragment(FragmentName.ADD_MEMO_FRAGMENT)
                        // 메모 내용을 저장하는 메서드를 호출한다.
                        addNewMemoData()
                    }
```

---

# 메모 목록을 가져오는 기능을 구현한다.

### RecyclerView를 구성하기 위해 사용할 리스트를 정의해준다.

[fragment/ShowMemoAllFragment.kt]
```kt
    // RecyclerView 구성을 위한 임시데이터
//    val tempData1 = Array(100){
//        "메모 제목 $it"
//    }

    // RecyclerView를 구성하기 위한 리스트
    var memoList = mutableListOf<MemoModel>()
```

### Adapter를 수정한다.
[fragment/ShowMemoAllFragment.kt - RecyclerShowMemoAdapter]
```kt
        override fun getItemCount(): Int {
            return memoList.size
        }

        override fun onBindViewHolder(holder: ViewHolderMemoAdapter, position: Int) {
            if(memoList[position].memoIsSecret){
                holder.rowMemoBinding.textViewRowTitle.text = "비밀 메모 입니다"
                holder.rowMemoBinding.textViewRowTitle.setTextColor(Color.LTGRAY)
            } else {
                holder.rowMemoBinding.textViewRowTitle.text = memoList[position].memoTitle
                holder.rowMemoBinding.textViewRowTitle.setTextColor(Color.BLACK)
            }

            val a1 = holder.rowMemoBinding.buttonRowFavorite as MaterialButton
            if(memoList[position].memoIsFavorite){
                a1.setIconResource(R.drawable.star_full_24px)
            } else {
                a1.setIconResource(R.drawable.star_24px)
            }
        }
```

### 모든 메모 데이터를 가져오는 메서드를 구현한다.

[dao/MemoDao.kt]
```kt
    // 모든 메모 데이터를 가져오는 메서드
    @Query("""
        select * from memotable
        order by memoIdx desc
    """)
    fun selectMemoDataAll() : List<MemoVO>
```

[repository/MemoRepository.kt]
```kt
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
```

### 데이터를 가져와 RecyclerView를 갱신하는 메서드를 만들어준다.

[fragment/ShowMemoAllFragment.kt]
```kt
    // 데이터를 읽어와 RecyclerView를 갱신하는 메서드
    fun refreshRecyclerView(){
        memoList.clear()

        CoroutineScope(Dispatchers.Main).launch {
            val work1 = async(Dispatchers.IO){
                MemoRepository.selectMemoDataAll(mainActivity)
            }
            memoList = work1.await()

            fragmentShowMemoAllBinding.recyclerViewShowMemoAll.adapter?.notifyDataSetChanged()
        }
    }
```

### 메서드를 호출한다.
[fragment/ShowMemoAllFragment.kt - onCreateView()]
```kt
        // 데이터를 읽어와 RecyclerView를 갱신하는 메서드를 호출한다.
        refreshRecyclerView()
```

### 즐겨찾기 여부, 비밀번호 설정 여부, 카테고리 번호를 통해 데이터를 가져오는 메서드를 구현해준다.

[dao/MemoDao.kt]
```kt

    // 즐겨찾기가 설정되어 있는 메모 데이터를 가져오는 메서드
    @Query("""
        select * from memotable
        where memoIsFavorite = :memoIsFavorite
        order by memoIdx desc
    """)
    fun selectMemoDataAllByFavorite(memoIsFavorite:Boolean) : List<MemoVO>

    // 비밀메모로 설정되어 있는 메모 데이터를 가져오는 메서드
    @Query("""
        select * from memotable
        where memoIsSecret = :memoIsSecret
        order by memoIdx desc
    """)
    fun selectMemoDataAllBySecret(memoIsSecret:Boolean) : List<MemoVO>

    // 카테고리 번호를 통해 메모 데이터를 가져오는 메서드
    @Query("""
        select * from memotable
        where memoCategoryIdx = :categoryIdx
        order by memoIdx desc
    """)
    fun selectMemoDataAllByCategoryIdx(categoryIdx:Int) : List<MemoVO>
```

[repository/MemoRepository.kt]
```kt

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
```

### 메모 데이터를 가져오는 부분을 수정한다.

[fragment/ShowMemoAllFragment.kt - refreshRecyclerView()]
```kt
if(arguments != null){
                    when(arguments?.getString("MemoName")){
                        // 모든 메모
                        MemoListName.MEMO_NAME_ALL.str -> {
                            MemoRepository.selectMemoDataAll(mainActivity)
                        }
                        // 즐겨 찾기
                        MemoListName.MEMO_NAME_FAVORITE.str -> {
                            MemoRepository.selectMemoDataAllByFavorite(mainActivity, true)
                        }
                        // 비밀 메모
                        MemoListName.MEMO_NAME_SECRET.str -> {
                            MemoRepository.selectMemoDataAllBySecret(mainActivity, true)
                        } 
                        // 사용자가 추가한 카테고리
                        else -> {
                            // 카테고리 번호
                            val categoryIdx = arguments?.getInt("categoryIdx")!!
                            MemoRepository.selectMemoDataAllByCategoryIdx(mainActivity, categoryIdx)
                        }
                    }
                } else {
                    // 전달된 카테고리 관련 데이터가 없다면 모두 가져온다.
                    MemoRepository.selectMemoDataAll(mainActivity)
                }
```

---

# 즐겨찾기 기능을 구현한다.

### 즐겨찾기 값을 수정하는 메서드를 정의해준다.

[dao/MemoDao.kt]
```kt
    // 즐겨찾기 값을 설정하는 메서드
    @Query("""
        update MemoTable
        set memoIsFavorite = :memoIsFavorite
        where memoIdx = :memoIdx
    """)
    fun updateMemoFavorite(memoIdx:Int, memoIsFavorite: Boolean)
```

[repository/MemoRepository.kt]
```kt
        // 즐겨찾기 값을 설정하는 메서드
        fun updateMemoFavorite(context: Context, memoIdx:Int, memoIsFavorite: Boolean){
            val memoDataBase = MemoDataBase.getInstance(context);
            memoDataBase?.memoDao()?.updateMemoFavorite(memoIdx, memoIsFavorite)
        }
```

### 항목의 즐겨찾기 버튼을 누르면 즐겨찾기 관련 처리를 하는 부분을 구현한다.

[repository/MemoRepository.kt - RecyclerShowMemoAdapter]
```kt
// 즐겨찾기 버튼 처리
            rowMemoBinding.buttonRowFavorite.setOnClickListener {
                // 사용자가 선택한 항목 번째 객체를 가져온다.
                val memoModel = memoList[viewHolderMemoAdapter.adapterPosition]
                // 즐겨찾기 값을 반대값으로 넣어준다.
                memoModel.memoIsFavorite = !memoModel.memoIsFavorite
                // 즐겨찾기 값을 수정한다.
                CoroutineScope(Dispatchers.Main).launch {
                    val work1 = async(Dispatchers.IO){
                        MemoRepository.updateMemoFavorite(mainActivity, memoModel.memoIdx, memoModel.memoIsFavorite)
                    }
                    work1.join()

                    // 즐겨찾기 라면...
                    if(arguments?.getString("MemoName") == MemoListName.MEMO_NAME_FAVORITE.str){
                        // 현재 번째 객체를 제거한다.
                        memoList.removeAt(viewHolderMemoAdapter.adapterPosition)
                        fragmentShowMemoAllBinding.recyclerViewShowMemoAll.adapter?.notifyItemRemoved(viewHolderMemoAdapter.adapterPosition)
                    } else {
                        val a1 = rowMemoBinding.buttonRowFavorite as MaterialButton
                        if (memoModel.memoIsFavorite) {
                            a1.setIconResource(R.drawable.star_full_24px)
                        } else {
                            a1.setIconResource(R.drawable.star_24px)
                        }
                    }
                }
            }
```

---

# 메모 읽기 처리

### 비밀번호 입력을 위한 레이아웃을 구성해준다.
[res/layout/dialog_memo_password.xml]
```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:padding="20dp"
    android:transitionGroup="true">

    <com.google.android.material.textfield.TextInputLayout
        android:id="@+id/textFieldDialogMemoPassword"
        android:layout_width="match_parent"
        android:layout_height="wrap_content">

        <com.google.android.material.textfield.TextInputEditText
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:singleLine="true"
            android:inputType="number|numberPassword"/>
    </com.google.android.material.textfield.TextInputLayout>
</LinearLayout>
```
### 항목을 눌렀을 때의 처리하는 메서드를 구현한다.

[fragment/ShowMemoAllFragment.kt]
```kt
    // 항목을 눌러 메모 보는 화면으로 이동하는 처리
    fun showMemoData(position:Int){
        // 비밀 메모인지 확인한다.
        if(memoList[position].memoIsSecret){
            val builder = MaterialAlertDialogBuilder(mainActivity)
            builder.setTitle("비밀번호 입력")

            val dialogMemoPasswordBinding = DialogMemoPasswordBinding.inflate(layoutInflater)
            builder.setView(dialogMemoPasswordBinding.root)

            builder.setNegativeButton("취소", null)
            builder.setPositiveButton("확인"){ dialogInterface: DialogInterface, i: Int ->
                // 사용자가 입력한 비밀번호를 가져온다.
                val inputPassword = dialogMemoPasswordBinding.textFieldDialogMemoPassword.editText?.text.toString()
                // 입력한 비밀번호를 제대로 입력했다면
                if(inputPassword == memoList[position].memoPassword){
                    // 메모 번호를 전달한다.
                    val dataBundle = Bundle()
                    dataBundle.putInt("memoIdx", memoList[position].memoIdx)
                    mainActivity.replaceFragment(FragmentName.READ_MEMO_FRAGMENT, true, true, dataBundle)
                } else {
                    val snackbar = Snackbar.make(mainActivity.activityMainBinding.root, "비밀번호를 잘못 입력하였습니다", Snackbar.LENGTH_SHORT)
                    snackbar.show()
                }
            }
            builder.show()
        } else {
            // 메모 번호를 전달한다.
            val dataBundle = Bundle()
            dataBundle.putInt("memoIdx", memoList[position].memoIdx)
            mainActivity.replaceFragment(FragmentName.READ_MEMO_FRAGMENT, true, true, dataBundle)
        }
    }
```

### 메서드를 호출한다.

[fragment/ShowMemoAllFragment.kt - RecyclerShowMemoAdapter]
```kt
            rowMemoBinding.root.setOnClickListener {
                // mainActivity.replaceFragment(FragmentName.READ_MEMO_FRAGMENT, true, true, null)
                // 항목을 눌러 메모 보는 화면으로 이동하는 처리
                showMemoData(viewHolderMemoAdapter.adapterPosition)
            }
```

### 메모 데이터를 읽어오는 메서드를 정의해준다.

[dao/MemoDao.kt]
```kt
    // 메모 번호를 통해 메모데이터를 가져온다.
    @Query("""
        select * from MemoTable
        where memoIdx = :memoIdx
    """)
    fun selectMemoDataByMemoIdx(memoIdx:Int) : MemoVO
```

[repository/MemoRepository.kt]

```kt
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
```

### 카테고리 정보 하나를 가져오는 메서드를 정의한다.
[dao/categoryDao.kt]
```kt
    // 카테고리 번호를 통해 카테정보를 가져오는 메서드
    @Query("""
        select * from CategoryTable
        where categoryIdx = :categoryIdx
    """)
    fun selectCategoryDataByCategoryIdx(categoryIdx:Int) : CategoryVO
```

[repository/CategoryRepository.kt]

```kt

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
```

### 메모데이터를 가져와 입력 요소에 설정해주는 메서드를 구현한다.
[fragment/ReadMemoFragment.kt]
```kt
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
```

### 메모 데이터를 담을 변수를 선언한다.

[fragment/ReadMemoFragment.kt]
```kt
    // 메모 데이터를 담을 변수
    lateinit var readMemoModel: MemoModel
```

### 메서드를 호출해준다.

[fragment/ReadMemoFragment.kt - onCreateView()]
```kt
        // 데이터를 가져와 보여주는 메서드를 호출한다.
        settingMemoData()
```

### 체크박스의 리스너를 설정한다.

[fragment/ReadMemoFragment.kt]
```kt
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
```

### 메서드를 호출한다
[fragment/ReadMemoFragment.kt - onCreateView()]
```kt
        // 체크 박스를 구성하는 메서드
        settingCheckBox()
```

---

# 메모 삭제 처리

### 메모 삭제 처리 메서드를 구현한다.

[dao/MemoDao.kt]
```kt
    // 메모를 삭제하는 메서드
    @Delete
    fun deleteMemoDataByMemoIdx(memoVO: MemoVO)
```

[repository/MemoRepository.kt]

```kt
        // 메모를 삭제하는 메서드
        fun deleteMemoDataByMemoIdx(context: Context, memoIdx:Int){
            // VO 객체에 객체를 담는다.
            val memoVO = MemoVO(memoIdx = memoIdx)
            val memoDataBase = MemoDataBase.getInstance(context)
            memoDataBase?.memoDao()?.deleteMemoDataByMemoIdx(memoVO)
        }
```

### 메모를 삭제처리하는 메서드를 구현한다.

[fragment/ReadMemoFragment.kt]
```kt
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
```

### 메서드를 호출한다.

[fragment/ReadMemoFragment.kt - settingToolbar()]
```kt
                    // 삭제
                    R.id.toolbar_read_memo_menu_item_delete -> {
                        // mainActivity.removeFragment(FragmentName.READ_MEMO_FRAGMENT)
                        // 삭제처리하는 메서드를 호출한다.
                        deleteMemoData()
                    }
```

---

# 메모 수정

### 수정 메뉴를 누르면 수정 화면으로 이동한다.

[fragment/ReadMemoFragment.kt - settingToolbar()]
```kt
                        // 메모 번호를 전달한다.
                        val dataBundle = Bundle()
                        dataBundle.putInt("memoIdx", readMemoModel.memoIdx)
                        mainActivity.replaceFragment(FragmentName.MODIFY_MEMO_FRAGMENT, true, true, dataBundle)
```

### 카테고리 정보를 담을 리스트와 메모 정보를 담을 객체를 구현해준다.

[fragment/ModifyMemoFragment.kt]
```kt
    // 카테고리의 정보를 담을 리스트
    val categoryNameList = mutableListOf<String>()
    val categoryIdxList = mutableListOf<Int>()
    // 메모 정보를 담을 객체
    lateinit var modifyMemoModel:MemoModel
```

### 데이터를 가져오는 메서드를 구현한다.

[fragment/ModifyMemoFragment.kt]
```kt
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
        }
    }
```

### 메서드를 호출한다.
[fragment/ModifyMemoFragment.kt - onCreateView()]
```kt
        // 필요한 데이터를 읽어오는 메서드를 호출한다.
        gettingModifyMemoData()
```

### 카테고리 정보를 읽어와 구성해주는 메서드를 구현한다.

[fragment/ModifyMemoFragment.kt]

```kt
    // 카테고리 선택 메뉴를 구성하는 메서드
    fun settingSelectCategoryMenu(){
        fragmentModifyMemoBinding.apply {
            // 카테고리 정보를 가져온다.
            CoroutineScope(Dispatchers.Main).launch {
                val work1 = async(Dispatchers.IO){
                    CategoryRepository.selectCategoryAll(mainActivity)
                }
                val tempList = work1.await()

                tempList.forEach {
                    categoryNameList.add(it.categoryName)
                    categoryIdxList.add(it.categoryIdx)
                }

                val items = categoryNameList.toTypedArray()

                val a1 = textFieldModifyMemoCategory.editText as MaterialAutoCompleteTextView
                a1.setSimpleItems(items)
                a1.setText("Item 1", false)
            }
        }
    }
```

### 카테고리 선택 메뉴를 구성하는 메서드를 수정한다.

```kt
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
```

### 메서드를 호출하는 부분을 옮겨준다.

[fragment/ModifyMemoFragment.kt - onCreateView() -> gettingModifyMemoData()]
```kt

            // 카테고리 선택 메뉴를 구성하는 메서드를 호출한다.
            settingSelectCategoryMenu()
```

### 입력요소를 설정하는 부분을 추가해준다.
[fragment/ModifyMemoFragment.kt - gettingModifyMemoData()]
```kt
            // 입력 요소들 셋팅
            fragmentModifyMemoBinding.apply {
                textFieldModifyMemoTitle.editText?.setText(modifyMemoModel.memoTitle)
                textFieldModifyMemoText.editText?.setText(modifyMemoModel.memoText)
                checkBoxModifyMemoSecret.isChecked = modifyMemoModel.memoIsSecret
                textFieldModifyMemoPassword.editText?.setText(modifyMemoModel.memoPassword)
            }
```

### 메모를 수정하는 메서드를 구현해준다.

[dao/MemoDao.kt]
```kt
    // 메모를 수정하는 메서드
    @Update
    fun updateMemoData(memoVO: MemoVO)
```


[repository/MemoRepository.kt]

```kt

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
```

### 수정처리하는 메서드를 구현해준다.
[fragment/ModifyMemoFragment.kt]
```kt
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
```

### 메서드를 호출한다.

[fragment/ModifyMemoFragment.kt - settingToolbar()]
```kt
                    Menu.FIRST + 1 ->{
                        // mainActivity.removeFragment(FragmentName.MODIFY_MEMO_FRAGMENT)
                        // 메모 수정처리는 메서드를 호출해준다.
                        processModifyMemoData()
                    }
```

---

# 카테고리만 삭제

### 메모의 카테고리 번호를 통해 수정 또는 삭제하는 메서드를 정의해준다.

[dao/MemoDao.kt]
```kt
    // 메모의 카테고리 번호를 변경하는 메서드
    @Query("""
        update MemoTable
        set memoCategoryIdx = :newCategoryIdx
        where memoCategoryIdx = :oldCategoryIdx
    """)
    fun updateMemoCategoryIdx(oldCategoryIdx:Int, newCategoryIdx:Int)
    
    // 메모의 카테고리 번호로 메모를 삭제하는 메서드
    @Query("""
        delete from MemoTable
        where memoCategoryIdx = :deleteCategoryIdx
    """)
    fun deleteMemoDataByCategoryIdx(deleteCategoryIdx:Int)
```

[repository/MemoRepository.kt]
```kt
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
```

### 카테고리를 삭제하는 메서드를 정의해준다.

[dao/CategoryDao.kt]
```kt
    // 카테고리 삭제
    @Delete
    fun deleteCategoryDataByCategoryIdx(categoryVO: CategoryVO)
```

[repository/CategoryRepository.kt]
```kt

        // 메모의 카테고리 번호로 메모를 삭제하는 메서드
        fun deleteMemoDataByCategoryIdx(context: Context, deleteCategoryIdx:Int){
            val categoryVO = CategoryVO(categoryIdx = deleteCategoryIdx)
            val memoDataBase = MemoDataBase.getInstance(context)
            memoDataBase?.categoryDao()?.deleteCategoryDataByCategoryIdx(categoryVO)
        }
```

### Recyclerview의 항목을 누르면 나타나는 BottomSheet에 항목 순서 값을 전달하도록 수정한다.

[fragment/CateogryManagmentFragment.kt - RecyclerViewCategoryManagementAdapter]
```kt
val categoryManagementBottomSheetFragment = CategoryManagementBottomSheetFragment(
    this@CategoryManagementFragment,
    viewHolderCategoryManagement.adapterPosition,
    categoryList[viewHolderCategoryManagement.adapterPosition].categoryIdx,
    categoryList[0].categoryIdx
)
```
[fragment/CategoryManagementBottomSheetFragment.kt - RecyclerViewCategoryManagementAdapter]
```kt
class CategoryManagementBottomSheetFragment(
    val categoryManagementFragment: CategoryManagementFragment,
    val selectedPosition:Int,
    val selectedCategoryIdx:Int,
    val basicCategoryIdx:Int) : BottomSheetDialogFragment() {
```

