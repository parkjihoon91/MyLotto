package com.example.mylotto.util.room

import androidx.room.Entity
import androidx.room.PrimaryKey

//@Entity(
//    tableName = "lotto",
//    indices = [
//        Index(value = ["value"], unique = true),
//    ]
//)
//@Entity(tableName = "lotto")
//data class Lotto(
//    var value: String, // 번호
//    var date: String, // 시간
//    var seq: String, // 회차
//    var deleteNumber: Int, // 삭제
//    var check: Boolean = false // 여부
//) {
//    @PrimaryKey(autoGenerate = true)
//    var id: Int = 0
//}

// 바꿀 테이블
@Entity(tableName = "lotto")
data class Lotto(
    var number: String, // 번호
    var time: String, // 시간
    var seq: String, // 회차
    var grade: Int? = null,  // 등수
    var price: Int? = null, // 가격
    var content: String? = null, // 메모 및 비고
    var checked: Boolean = false // 당첨여부
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}