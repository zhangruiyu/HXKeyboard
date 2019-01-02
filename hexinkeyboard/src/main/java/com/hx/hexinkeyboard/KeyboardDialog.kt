package com.hx.hexinkeyboard

import android.text.InputType
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.PopupWindow
import kotlinx.android.synthetic.main.keybord_layout.view.*
import java.lang.reflect.Method

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

fun EditText.keyBoardDialog(
    type: NumberStyle,
    showListener: () -> Unit = {},
    defaultPop: Boolean = true
): PopupWindow {
    val maxLength = getMaxLength(this)
    val parentView = this.rootView
    val inflate = LayoutInflater.from(context).inflate(R.layout.keybord_layout, null).apply {
        val tvList = arrayOf(
            tv0_keyboard,
            tv1_keyboard,
            tv2_keyboard,
            tv3_keyboard,
            tv4_keyboard,
            tv5_keyboard,
            tv6_keyboard,
            tv7_keyboard,
            tv8_keyboard,
            tv9_keyboard
        )
        for ((index, value) in tvList.withIndex()) {
            value.setOnClickListener {
                if (type == NumberStyle.PayPasswordNum && this@keyBoardDialog.text.length == 6) {
                    this@keyBoardDialog.setText("")
                }
                val text = this@keyBoardDialog.text.toString()
                val indexInsert = this@keyBoardDialog.selectionEnd
                keyBoardEditInit(
                    this@keyBoardDialog,
                    text,
                    type,
                    indexInsert,
                    index,
                    maxLength
                )
            }
        }
        tv00_keyboard.setOnClickListener {
            for (i in 0..1) {
                val text = this@keyBoardDialog.text.toString()
                val indexInsert = this@keyBoardDialog.selectionEnd
                keyBoardEditInit(
                    this@keyBoardDialog,
                    text,
                    type,
                    indexInsert,
                    0,
                    maxLength
                )
            }
        }
        tvdian_keyboard.setOnClickListener {
            if (type == NumberStyle.MoneyNum) {
                val indexInsert = this@keyBoardDialog.selectionEnd
                if (indexInsert != 0) {
                    this@keyBoardDialog.apply {
                        val textEnd = StringBuilder(text).insert(indexInsert, ".").toString()
                        if (type == NumberStyle.MoneyNum && text.length < maxLength) {
                            if (text.isNotEmpty() && !text.contains(".")) {
                                setText(textEnd)
                                setSelection(indexInsert + 1)
                            }
                        } else {
                            setText(textEnd)
                            if (textEnd == text.toString()) {
                                setSelection(indexInsert + 1)
                            }

                        }
                    }
                }
            }
        }
        lldelete_keyboard.setOnClickListener {
            val indexInsert = this@keyBoardDialog.selectionEnd
            if (indexInsert > 0) this@keyBoardDialog.setText(
                StringBuilder(this@keyBoardDialog.text.toString()).delete(
                    indexInsert - 1,
                    indexInsert
                ).toString()
            )
            if (indexInsert > 1) this@keyBoardDialog.setSelection(indexInsert - 1)
        }
        this@keyBoardDialog.apply {

            if (android.os.Build.VERSION.SDK_INT <= 10) {
                inputType = InputType.TYPE_NULL
            } else {
                val cls = EditText::class.java
                val method: Method
                try {
                    method = cls.getMethod(
                        "setShowSoftInputOnFocus",
                        Boolean::class.javaPrimitiveType
                    )
                    method.isAccessible = true
                    method.invoke(this, false)
                } catch (e: Exception) {//TODO: handle exception

                }
            }
        }

    }
    val popWindow = PopupWindow(
        inflate,
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT,
        true
    ).apply {

        isTouchable = true
        isFocusable = false
        animationStyle = R.style.anim_menu_bottombar
        try {
            //默认是否弹出
            if (defaultPop) {
                showAtLocation(parentView, Gravity.BOTTOM, 0, 0)
            }
        } catch (e: Exception) {

        }

    }
    inflate.tvwancheng_keyboard.setOnClickListener {
        popWindow.dismiss()
    }
    this@keyBoardDialog.setOnClickListener {
        if (!popWindow.isShowing) {
            popWindow.showAtLocation(parentView, Gravity.BOTTOM, 0, 0)
            showListener()
        }
    }
    return popWindow
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
        if (type == NumberStyle.PayPasswordNum && text.length < maxLength) {
            var textEnd = StringBuilder(text).insert(indexInsert, index).toString()
            setText(textEnd)
            setSelection(indexInsert + 1)
        } else if (type == NumberStyle.CodeNum && text.length < maxLength) {
            var textEnd = StringBuilder(text).insert(indexInsert, index).toString()
            setText(textEnd)
            setSelection(indexInsert + 1)
        } else if (type == NumberStyle.PhoneNum && text.length < maxLength) {
            if (indexInsert == 3 || indexInsert == 8) {
                var textEnd = StringBuilder(text).insert(indexInsert, " " + index).toString()
                setText(textEnd)
                setSelection(indexInsert + 2)
            } else {
                var textEnd = StringBuilder(text).insert(indexInsert, index).toString()
                setText(textEnd)
                setSelection(indexInsert + 1)
            }

        } else if (type == NumberStyle.IDNum && text.length < maxLength) {
            if (indexInsert == 6 || indexInsert == 11 || indexInsert == 16) {
                var textEnd = StringBuilder(text).insert(indexInsert, " " + index).toString()
                setText(textEnd)
                setSelection(indexInsert + 2)
            } else {
                var textEnd = StringBuilder(text).insert(indexInsert, index).toString()
                setText(textEnd)
                setSelection(indexInsert + 1)
            }
        } else if (type == NumberStyle.BankNum && text.length < maxLength) {
            if (indexInsert == 4 || indexInsert == 8 || indexInsert == 12 || indexInsert == 16) {
                var textEnd = StringBuilder(text).insert(indexInsert, " " + index).toString()
                setText(textEnd)
                setSelection(indexInsert + 2)
            } else {
                var textEnd = StringBuilder(text).insert(indexInsert, index).toString()
                setText(textEnd)
                setSelection(indexInsert + 1)
            }
        } else if (type == NumberStyle.MoneyNum && text.length < maxLength) {
            var textEnd = StringBuilder(text).insert(indexInsert, index).toString()
            if (text.contains(".") && text.length - text.indexOf(".") > 2) {

            } else {
                setText(textEnd)
                setSelection(indexInsert + 1)
            }
        }
    }
}

fun getMaxLength(et: EditText): Int {
    var lenght: Int = 0
    for (i in et.filters) {
        val cls = i::class.java
        if (cls.name == "android.text.InputFilter\$LengthFilter") {
            for (j in cls.declaredFields) {
                if (j.name == "mMax") {
                    j.isAccessible = true
                    lenght = j.get(i) as Int
                }
            }
        }
    }
    return lenght
}

