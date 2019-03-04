package com.cloud.dialogs.digitalkeys;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Spannable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.cloud.dialogs.DialogManager;
import com.cloud.dialogs.R;
import com.cloud.dialogs.plugs.DialogPlus;
import com.cloud.objects.ObjectJudge;
import com.cloud.objects.events.Action1;
import com.cloud.objects.events.Action3;
import com.cloud.objects.events.Func0;
import com.cloud.objects.utils.ConvertUtils;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/7/9
 * @Description:o数字键盘工具类
 * @Modifier:
 * @ModifyContent:
 */
public class DigitalKeyboardUtils {

    private EditText[] editTexts = null;
    private OnDigitalKeyBoardListener onDigitalKeyBoardListener = null;
    private int editTagKey = 706211543;
    private String endString = "";

    public static DigitalKeyboardUtils getInstance() {
        return new DigitalKeyboardUtils();
    }

    public DigitalKeyboardUtils setOnDigitalKeyBoardListener(OnDigitalKeyBoardListener listener) {
        this.onDigitalKeyBoardListener = listener;
        return this;
    }

    /**
     * 初始化软键盘(需要在页面初始化时调用)
     *
     * @param editTexts
     */
    @SuppressLint("ClickableViewAccessibility")
    public DigitalKeyboardUtils initializeKeyboard(EditText... editTexts) {
        if (ObjectJudge.isNullOrEmpty(editTexts)) {
            return this;
        }
        this.editTexts = editTexts;
        for (int i = 0; i < editTexts.length; i++) {
            EditText editText = editTexts[i];
            editText.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        if (onDigitalKeyBoardListener != null) {
                            onDigitalKeyBoardListener.onEditTextActionUp(DigitalKeyboardUtils.this, (EditText) v, event);
                        }
                        if (DigitalKeyboardUtils.this.editTexts != null) {
                            for (EditText text : DigitalKeyboardUtils.this.editTexts) {
                                if (text == v) {
                                    text.requestFocus();
                                    text.setTag(editTagKey, "SOFT_INPUT_DIGITAL_EDIT");
                                } else {
                                    text.clearFocus();
                                    text.setTag(editTagKey, "");
                                }
                            }
                        }
                    }
                    return true;
                }
            });
        }
        return this;
    }

    /**
     * 显示数字键盘
     *
     * @param context 当前上下文
     */
    public DialogPlus show(final Context context) {
        DialogManager dialogManager = DialogManager.getInstance();
        dialogManager.setOnOutsideClickListener(new DialogManager.OnOutsideClickListener() {
            @Override
            public void onOutsideClick(DialogPlus dialog) {
                if (onDigitalKeyBoardListener == null) {
                    return;
                }
                EditText editText = null;
                if (focusEditFunction != null) {
                    editText = focusEditFunction.call();
                }
                onDigitalKeyBoardListener.onKeyBoardListener(DigitalKeyboardUtils.this, InputDigitalKeyType.cancel, "", editText);
            }
        });
        DialogManager.DialogManagerBuilder builder = dialogManager.builder(context, R.layout.rx_digital_key_board_view);
        builder.setGravity(Gravity.BOTTOM);
        builder.setCancelable(true);
        DialogPlus dialogPlus = builder.show(new Action3<View, DialogPlus, Object>() {
            @Override
            public void call(View view, DialogPlus dialogPlus, Object object) {
                DigitalKeyboardFragment keyboardFragment = new DigitalKeyboardFragment();
                keyboardFragment.setOnDigitalKeyBoardListener(onDigitalKeyBoardListener);
                keyboardFragment.setCloseAction(closeAction);
                keyboardFragment.setFocusEditFunction(focusEditFunction);
                keyboardFragment.setDeleteAction(deleteAction);
                keyboardFragment.build(view, context, DigitalKeyboardUtils.this, dialogPlus);
            }
        });
        return dialogPlus;
    }

    /**
     * 获取数字键盘视图
     *
     * @param context 上下文
     * @return
     */
    public View getDigitalKeyboardView(Context context) {
        View contentView = View.inflate(context, R.layout.rx_digital_key_board_view, null);
        DigitalKeyboardFragment keyboardFragment = new DigitalKeyboardFragment();
        keyboardFragment.setOnDigitalKeyBoardListener(onDigitalKeyBoardListener);
        keyboardFragment.setCloseAction(closeAction);
        keyboardFragment.setFocusEditFunction(focusEditFunction);
        keyboardFragment.setDeleteAction(deleteAction);
        keyboardFragment.build(contentView, context, this, null);
        return contentView;
    }

    private Action1<InputDigitalKeyType> closeAction = new Action1<InputDigitalKeyType>() {
        @Override
        public void call(InputDigitalKeyType inputDigitalKeyType) {
            if (onDigitalKeyBoardListener == null || inputDigitalKeyType == InputDigitalKeyType.confirm) {
                return;
            }
            EditText editText = null;
            if (focusEditFunction != null) {
                editText = focusEditFunction.call();
            }
            onDigitalKeyBoardListener.onKeyBoardListener(DigitalKeyboardUtils.this, InputDigitalKeyType.cancel, "", editText);
        }
    };

    private Action1<EditText> deleteAction = new Action1<EditText>() {
        @Override
        public void call(EditText editText) {
            Spannable spanText = editText.getText();
            if (spanText.length() > 0) {
                String content = spanText.toString();
                content = removeEndString(content, endString);
                if (content.length() > 0) {
                    content = content.substring(0, content.length() - 1);
                }
                content = appendEndString(content, endString);
                editText.setText((TextUtils.isEmpty(endString) || TextUtils.equals(endString.trim(), content.trim())) ? "" : content);
                editText.setSelection(editText.length());
            }
        }
    };

    private EditText getFocusEditText() {
        if (editTexts == null) {
            return null;
        }
        EditText text = null;
        for (EditText editText : editTexts) {
            if (editText.hasFocus()) {
                text = editText;
                break;
            } else {
                String textTag = String.valueOf(editText.getTag(editTagKey));
                if (TextUtils.equals(textTag, "SOFT_INPUT_DIGITAL_EDIT")) {
                    text = editText;
                    break;
                }
            }
        }
        return text;
    }

    private String appendEndString(String inputText, String endString) {
        this.endString = endString;
        if (inputText == null) {
            return "";
        }
        inputText = inputText.trim();
        if (!TextUtils.isEmpty(endString)) {
            endString = endString.trim();
            if (!inputText.endsWith(endString)) {
                inputText += endString;
            }
        }
        return inputText;
    }

    private String removeEndString(String content, String endString) {
        content = content.trim();
        if (TextUtils.isEmpty(endString)) {
            return content;
        }
        endString = endString.trim();
        int index = content.lastIndexOf(endString);
        if (index < 0) {
            return content;
        } else {
            return content.substring(0, index);
        }
    }

    /**
     * 获取已输入文本
     *
     * @param digitalSymbol 数字或符号
     * @param isHasDecimal  是否包含小数
     * @param endString     添加至未尾字符
     * @return
     */
    public String getInputText(String digitalSymbol, boolean isHasDecimal, String endString) {
        EditText editText = getFocusEditText();
        if (editText == null) {
            return "";
        }
        String content = editText.getText().toString();
        if (TextUtils.equals(digitalSymbol, ".") && content.contains(".")) {
            return content;
        }
        content = removeEndString(content, TextUtils.isEmpty(this.endString) ? endString : this.endString);
        String value = content + digitalSymbol;
        if (isHasDecimal) {
            return appendEndString(value, endString);
        } else {
            int intValue = ConvertUtils.toInt(value);
            return appendEndString(String.valueOf(intValue), endString);
        }
    }

    /**
     * 获取已输入文本
     *
     * @param digitalSymbol 数字或符号
     * @param isHasDecimal  是否包含小数
     * @return
     */
    public String getInputText(String digitalSymbol, boolean isHasDecimal) {
        return getInputText(digitalSymbol, isHasDecimal, "");
    }

    /**
     * 获取已输入文本
     *
     * @param digitalSymbol 数字或符号
     * @return
     */
    public String getInputText(String digitalSymbol) {
        return getInputText(digitalSymbol, true);
    }

    /**
     * 获取已输入文本
     *
     * @param digitalSymbol 数字或符号
     * @param endString     添加至未尾字符
     * @return
     */
    public String getInputText(String digitalSymbol, String endString) {
        return getInputText(digitalSymbol, true, endString);
    }

    private String sizeLimit(String text, int minValue, int maxValue, boolean isHasDecimal) {
        double value = ConvertUtils.toDouble(text);
        if (value < minValue) {
            value = minValue;
        } else if (value > maxValue) {
            value = maxValue;
        }
        if (isHasDecimal) {
            return String.valueOf(value);
        } else {
            return String.valueOf((int) value);
        }
    }

    /**
     * 获取已输入文本
     *
     * @param digitalSymbol 数字或符号
     * @param minValue      最小值
     * @param maxValue      最大值
     * @param isHasDecimal  是否包含小数
     * @param endString     添加至未尾字符
     * @return
     */
    public String getInputText(String digitalSymbol, int minValue, int maxValue, boolean isHasDecimal, String endString) {
        String text = getInputText(digitalSymbol, endString);
        return sizeLimit(text, minValue, maxValue, isHasDecimal);
    }

    /**
     * 获取已输入文本
     *
     * @param digitalSymbol 数字或符号
     * @param minValue      最小值
     * @param maxValue      最大值
     * @param isHasDecimal  是否包含小数
     * @return
     */
    public String getInputText(String digitalSymbol, int minValue, int maxValue, boolean isHasDecimal) {
        return getInputText(digitalSymbol, minValue, maxValue, isHasDecimal, "");
    }

    /**
     * 在原文本的基础上减一
     *
     * @param minVaue 减少的最小值
     * @return
     */
    public String reduceOne(int minVaue) {
        EditText editText = getFocusEditText();
        if (editText == null) {
            return "";
        }
        String trim = editText.getText().toString().trim();
        if (TextUtils.isEmpty(trim)) {
            return "";
        }
        int value = ConvertUtils.toInt(trim);
        if (value > minVaue) {
            value -= 1;
        }
        return String.valueOf(value);
    }

    /**
     * 在原文本的基础上加一
     *
     * @param maxValue 最大值
     * @return
     */
    public String addOne(int maxValue) {
        EditText editText = getFocusEditText();
        if (editText == null) {
            return "";
        }
        String trim = editText.getText().toString().trim();
        int value = ConvertUtils.toInt(trim);
        if (value < maxValue) {
            value += 1;
        }
        return String.valueOf(value);
    }

    private Func0<EditText> focusEditFunction = new Func0<EditText>() {
        @Override
        public EditText call() {
            return getFocusEditText();
        }
    };
}
