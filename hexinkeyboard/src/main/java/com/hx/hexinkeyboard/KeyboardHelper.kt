package com.hx.hexinkeyboard

import android.widget.EditText

/**
 * Created by lichao on 2018/6/17.
 *
 */

enum class NumberStyle {
    CodeNum, PhoneNum, BankNum, IDNum, PayPasswordNum, MoneyNum
}

fun getNumberStyleByType(type: Int): NumberStyle {
    return when (type) {
        0 -> NumberStyle.CodeNum
        1 -> NumberStyle.PhoneNum
        2 -> NumberStyle.BankNum
        3 -> NumberStyle.IDNum
        4 -> NumberStyle.PayPasswordNum
        5 -> NumberStyle.MoneyNum
        else -> NumberStyle.MoneyNum
    }
}


fun keyBoardEditInit(
    editText: EditText,
    text: String,
    type: NumberStyle,
    indexInsert: Int,
    index: Int,
    maxLength: Int
) {
    editText.apply {
        var textEnd: String? = null
        var selectionIndex: Int? = null
        if (type == NumberStyle.PayPasswordNum && text.length < maxLength) {
            textEnd = StringBuilder(text).insert(indexInsert, index).toString()
            selectionIndex = indexInsert + 1
        } else if (type == NumberStyle.CodeNum && text.length < maxLength) {
            textEnd = StringBuilder(text).insert(indexInsert, index).toString()
            selectionIndex = indexInsert + 1
        } else if (type == NumberStyle.PhoneNum && text.length < maxLength) {
            if (indexInsert == 3 || indexInsert == 8) {
                textEnd = StringBuilder(text).insert(indexInsert, " $index").toString()
                selectionIndex = indexInsert + 2
            } else {
                textEnd = StringBuilder(text).insert(indexInsert, index).toString()
                selectionIndex = indexInsert + 1
            }

        } else if (type == NumberStyle.IDNum && text.length < maxLength) {
            if (indexInsert == 6 || indexInsert == 11 || indexInsert == 16) {
                textEnd = StringBuilder(text).insert(indexInsert, " $index").toString()
                selectionIndex = indexInsert + 2
            } else {
                textEnd = StringBuilder(text).insert(indexInsert, index).toString()
                selectionIndex = indexInsert + 1
            }
        } else if (type == NumberStyle.BankNum && text.length < maxLength) {
            if (indexInsert == 4 || indexInsert == 8 || indexInsert == 12 || indexInsert == 16) {
                textEnd = StringBuilder(text).insert(indexInsert, " $index").toString()
                selectionIndex = indexInsert + 2
            } else {
                textEnd = StringBuilder(text).insert(indexInsert, index).toString()
                selectionIndex = indexInsert + 1
            }
        } else if (type == NumberStyle.MoneyNum && text.length < maxLength) {
            textEnd = StringBuilder(text).insert(indexInsert, index).toString()
            if (text.contains(".") && text.length - text.indexOf(".") > 2) {

            } else {
                selectionIndex = indexInsert + 1
            }
        }
        if (textEnd.isNullOrEmpty().not()) {
            setText(textEnd)
            if (selectionIndex != null) {
                setSelection(selectionIndex)
            }
        }
    }
}

