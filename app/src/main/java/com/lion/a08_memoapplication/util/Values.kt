package com.lion.a08_memoapplication.util

// 프래그먼트들의 이름
enum class FragmentName(val number:Int, var str:String){
    // 전메 메모 화면
    SHOW_MEMO_ALL_FRAGMENT(1, "ShowMemoAllFragment"),
    // 메모 추가 화면
    ADD_MEMO_FRAGMENT(2, "AddMemoFragment"),
    // 메모 읽기 화면
    READ_MEMO_FRAGMENT(3, "ReadMemoFragment"),
    // 메모 수정 화면
    MODIFY_MEMO_FRAGMENT(4, "ModifyMemoFragment"),
    // 카테고리 관리 화면
    CATEGORY_MANAGEMENT_FRAGMENT(5, "CategoryManagementFragment"),
}

// 메모 목록을 보는 화면에서 어떤 것을 눌렀는지 구분하기 위한 값
enum class MemoListName(val number:Int, val str:String){
    MEMO_NAME_ALL(1, "모든 메모"),
    MEMO_NAME_FAVORITE(2, "즐겨 찾기"),
    MEMO_NAME_SECRET(3, "비밀 메모"),
    MEMO_NAME_ADDED(4, "추가된 메모"),
}