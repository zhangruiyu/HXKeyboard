package com.hx.hexinkeyboard

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.text.InputType
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.PopupWindow
import kotlinx.android.synthetic.main.keybord_layout.view.*
import java.lang.reflect.Method


class HXKeyboardEditText : EditText {
    private var keyboardType: Int = 0

    lateinit var inflate: View
    var showOrDismissListener: ((Boolean) -> Unit)? = null
    lateinit var popWindow: PopupWindow

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        val array = context.obtainStyledAttributes(attrs, R.styleable.HXKeyboardEditText)
        keyboardType = array.getInteger(R.styleable.HXKeyboardEditText_keyboard_type, 0)
        array?.recycle()
        initView()
    }


    private fun initView() {
        val type = getNumberStyleByType(keyboardType)
        val maxLength = getMaxLength()
        inflate = LayoutInflater.from(context).inflate(R.layout.keybord_layout, null).apply {
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
                    if (type == NumberStyle.PayPasswordNum && this@HXKeyboardEditText.text.length == 6) {
                        this@HXKeyboardEditText.setText("")
                    }
                    val text = this@HXKeyboardEditText.text.toString()
                    val indexInsert = this@HXKeyboardEditText.selectionEnd
                    keyBoardEditInit(
                        this@HXKeyboardEditText,
                        text,
                        type,
                        indexInsert,
                        index,
                        maxLength
                    )
                }
            }
            //00
            tv00_keyboard.setOnClickListener {
                for (i in 0..1) {
                    var text = this@HXKeyboardEditText.text.toString()
                    val indexInsert = this@HXKeyboardEditText.selectionEnd
                    keyBoardEditInit(this@HXKeyboardEditText, text, type, indexInsert, 0, maxLength)
                }
            }
            //小数点
            tvdian_keyboard.setOnClickListener {
                if (type == NumberStyle.MoneyNum) {
                    val indexInsert = this@HXKeyboardEditText.selectionEnd
                    if (indexInsert != 0) {
                        this@HXKeyboardEditText.apply {
                            var textEnd = StringBuilder(text).insert(indexInsert, ".").toString()
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
            //删除
            lldelete_keyboard.setOnClickListener {
                val indexInsert = this@HXKeyboardEditText.selectionEnd
                if (indexInsert > 0) this@HXKeyboardEditText.setText(
                    StringBuilder(this@HXKeyboardEditText.text.toString()).delete(
                        indexInsert - 1,
                        indexInsert
                    ).toString()
                )
                if (indexInsert > 1) this@HXKeyboardEditText.setSelection(indexInsert - 1)
            }
            shield()

        }
        popWindow = PopupWindow(
            inflate,
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true
        ).apply {

            isTouchable = true
            isFocusable = false
            animationStyle = R.style.anim_menu_bottombar

        }
        inflate.tvwancheng_keyboard.setOnClickListener {
            dismiss()
        }
        setOnClickListener {
            if (!popWindow.isShowing) {
                popWindow.showAtLocation(rootView, Gravity.BOTTOM, 0, 0)
                showOrDismissListener?.invoke(true)
            }
        }
    }

    //屏蔽掉原生键盘
    @SuppressLint("ObsoleteSdkInt")
    private fun shield() {
        this@HXKeyboardEditText.apply {
            //其实没用 已经不兼容2.3了
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1) {
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
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }


    private fun getMaxLength(): Int {
        var lenght = 0
        for (i in filters) {
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
        if (lenght == 0) {
            lenght = Int.MAX_VALUE
        }
        return lenght
    }

    fun dismiss() {
        if (popWindow.isShowing) {
            popWindow.dismiss()
            showOrDismissListener?.invoke(false)
        }
    }
}
