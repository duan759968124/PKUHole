package cn.edu.pku.treehole.adapters.bindingAdapter

import android.util.TypedValue
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.google.android.material.slider.Slider


/**
 *
 * @Author:         HuidongQin
 * @e-mail:         hdqin@pku.edu.cn
 * @Time:           2023/01/13
 * @Desc:
 * @Version:        1.0
 */


@BindingAdapter("setBindTextSize")
fun bindTextSize(view: TextView, size: Int) {
    view.setTextSize(TypedValue.COMPLEX_UNIT_SP, size.toFloat())
}


@BindingAdapter(value = ["onValueChangeListener"])
fun setOnValueChangeListener(slider: Slider, listener: OnValueChangeListener) {
    slider.addOnChangeListener { _: Slider?, value: Float, _: Boolean ->
        listener.onValueChanged(value)
    }
}

interface OnValueChangeListener {
    fun onValueChanged(value: Float)
}


