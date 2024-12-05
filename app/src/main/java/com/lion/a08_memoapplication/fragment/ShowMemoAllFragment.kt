package com.lion.a08_memoapplication.fragment

import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.google.android.material.snackbar.Snackbar
import com.lion.a08_memoapplication.MainActivity
import com.lion.a08_memoapplication.R
import com.lion.a08_memoapplication.databinding.DialogMemoPasswordBinding
import com.lion.a08_memoapplication.databinding.FragmentShowMemoAllBinding
import com.lion.a08_memoapplication.databinding.RowMemoBinding
import com.lion.a08_memoapplication.model.MemoModel
import com.lion.a08_memoapplication.repository.MemoRepository
import com.lion.a08_memoapplication.util.FragmentName
import com.lion.a08_memoapplication.util.MemoListName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ShowMemoAllFragment : Fragment() {

    lateinit var fragmentShowMemoAllBinding: FragmentShowMemoAllBinding
    lateinit var mainActivity: MainActivity

    // RecyclerView 구성을 위한 임시데이터
//    val tempData1 = Array(100){
//        "메모 제목 $it"
//    }

    // RecyclerView를 구성하기 위한 리스트
    var memoList = mutableListOf<MemoModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        fragmentShowMemoAllBinding = FragmentShowMemoAllBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity

        // Toolbar를 구성하는 메서드를 호출한다.
        settingToolbar()
        // RecyclerView를 구성하는 메서드를 호출한다.
        settingRecyclerView()
        // 버튼을 설정하는 메서드를 호출한다.
        settingButton()
        // 데이터를 읽어와 RecyclerView를 갱신하는 메서드를 호출한다.
        refreshRecyclerView()

        return fragmentShowMemoAllBinding.root
    }

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
        }
    }

    // RecyclerView를 구성하는 메서드
    fun settingRecyclerView(){
        fragmentShowMemoAllBinding.apply {
            recyclerViewShowMemoAll.adapter = RecyclerShowMemoAdapter()
            recyclerViewShowMemoAll.layoutManager = LinearLayoutManager(mainActivity)
            val deco = MaterialDividerItemDecoration(mainActivity, MaterialDividerItemDecoration.VERTICAL)
            recyclerViewShowMemoAll.addItemDecoration(deco)

            mainActivity.fabHideAndShow(recyclerViewShowMemoAll, fabShowMemoAllAdd)
        }
    }

    // 버튼을 설정하는 메서드
    fun settingButton(){
        fragmentShowMemoAllBinding.apply {
            // fab를 누를 때
            fabShowMemoAllAdd.setOnClickListener {
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

                // AddMemoFragment가 나타나게 한다.
                mainActivity.replaceFragment(FragmentName.ADD_MEMO_FRAGMENT, true, true, dataBundle)
            }
        }
    }

    // RecyclerView의 어뎁터
    inner class RecyclerShowMemoAdapter : RecyclerView.Adapter<RecyclerShowMemoAdapter.ViewHolderMemoAdapter>(){
        // ViewHolder
        inner class ViewHolderMemoAdapter(val rowMemoBinding: RowMemoBinding) : RecyclerView.ViewHolder(rowMemoBinding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderMemoAdapter {
            val rowMemoBinding = RowMemoBinding.inflate(layoutInflater, parent, false)
            val viewHolderMemoAdapter = ViewHolderMemoAdapter(rowMemoBinding)

            rowMemoBinding.root.setOnClickListener {
                // mainActivity.replaceFragment(FragmentName.READ_MEMO_FRAGMENT, true, true, null)
                // 항목을 눌러 메모 보는 화면으로 이동하는 처리
                showMemoData(viewHolderMemoAdapter.adapterPosition)
            }

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


            return viewHolderMemoAdapter
        }

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
    }

    // 데이터를 읽어와 RecyclerView를 갱신하는 메서드
    fun refreshRecyclerView(){
        memoList.clear()

        CoroutineScope(Dispatchers.Main).launch {
            val work1 = async(Dispatchers.IO){
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
            }
            memoList = work1.await()
            fragmentShowMemoAllBinding.recyclerViewShowMemoAll.adapter?.notifyDataSetChanged()
        }
    }

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
}