package com.stefan.wallpaper.demo.util;

import android.app.Dialog;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.EditText;
import android.widget.TextView;

import com.stefan.wallpaper.demo.BaseActivity;
import com.stefan.wallpaper.demo.MainActivity;
import com.stefan.wallpaper.demo.R;

public class DialogUtils {

    private MainActivity mMainActivity;
    private Dialog mDialog;

    private EditText mDialogText;
    private TextView mDialogPositiveButton;
    private TextView mDialogNegativeButton;

    public DialogUtils(MainActivity mMainActivity) {
        this.mMainActivity = mMainActivity;
    }

    public void showDialog() {
        if (mDialog == null) {
            mDialog = new Dialog(mMainActivity, R.style.CustomDialogTheme);
        }
        mDialog.setContentView(R.layout.dialog);
        mDialog.show();

        mDialogText = (EditText) mDialog.findViewById(R.id.dialog_title);

        mDialogNegativeButton = (TextView) mDialog
                .findViewById(R.id.dialog_negative);
        mDialogPositiveButton = (TextView) mDialog
                .findViewById(R.id.dialog_positive);
        initDialogButtons();
        mDialogText.setOnKeyListener(new OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN)
                        && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    String q = mDialogText.getText().toString();
                    if (!q.equals("")) {
                        mDialog.dismiss();
                        mMainActivity.search(mDialogText.getText().toString());
                    }
                }
                return true;
            }
        });
    }

    private void initDialogButtons() {

        mDialogNegativeButton.setText(mMainActivity.getString(R.string.cancel));
        mDialogNegativeButton.setTypeface(BaseActivity.sRobotoBlack);
        mDialogNegativeButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                mDialog.dismiss();
            }
        });

        mDialogPositiveButton.setText(mMainActivity.getString(R.string.ok));
        mDialogPositiveButton.setTypeface(BaseActivity.sRobotoBlack);
        mDialogPositiveButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                mDialog.dismiss();
                mMainActivity.search(mDialogText.getText().toString());
            }
        });
    }

    public void dismissDialog() {
        mDialog.dismiss();
    }
}
