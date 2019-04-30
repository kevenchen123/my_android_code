package com.keven.widget;

import android.annotation.SuppressLint;
import android.content.ClipboardManager;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.Toast;
import java.util.ArrayList;

/*
 * 用于多个EditText的同步粘贴数字
 */
@SuppressLint("AppCompatCustomView")
public class BundleEditText extends EditText {

    ArrayList<BundleEditText> bundleEditTexts;

    public BundleEditText(Context context) {
        super(context);
    }

    public BundleEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BundleEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setBundle(ArrayList<BundleEditText> bundleEditTexts) {
        this.bundleEditTexts = bundleEditTexts;
    }

    @Override
    public boolean onTextContextMenuItem(int id) {
        if (id == android.R.id.paste) {
            try {
                ClipboardManager clip = (ClipboardManager)getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                String text = clip.getPrimaryClip().getItemAt(0).getText().toString();
                Long.parseLong(text);
                if (bundleEditTexts != null) {
                    for (int i = 0; i < text.length() && i < bundleEditTexts.size(); i++) {
                        bundleEditTexts.get(i).setText(text.substring(i, i+1));
                    }
                }
                return true;
            } catch (Exception e) {
                Toast.makeText(getContext(), "只能粘贴数字", Toast.LENGTH_SHORT).show();
            }
        }
        return super.onTextContextMenuItem(id);
    }
}