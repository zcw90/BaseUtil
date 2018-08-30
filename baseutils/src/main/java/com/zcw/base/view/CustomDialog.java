package com.zcw.base.view;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zcw.base.R;

/**
 * Created by 朱城委 on 2018/7/11.<br><br>
 * 自定义dialog
 */
public class CustomDialog extends Dialog {
	private Context context;
	
	private TextView tvTitle;
	
	private TextView tvMessage;
	
	private Button btnOk;
	
	private Button btnCancel;
	
	/** 包含按钮的LinearLayout */
	private LinearLayout llButton;
	
	public CustomDialog(Context context) {
		this(context, R.style.dialog_transparent);
	}
	
	public CustomDialog(Context context, int themeResId) {
		super(context, themeResId);
		init(context);
	}

	private void init(Context context) {
		this.context = context;
		View dialogView = View.inflate(context, R.layout.dialog_custom, null);
		
		tvTitle = dialogView.findViewById(R.id.tv_dialog_title);
		tvMessage = dialogView.findViewById(R.id.tv_dialog_message);
		llButton = dialogView.findViewById(R.id.ll_button);
		btnOk = dialogView.findViewById(R.id.btn_ok);
		btnCancel = dialogView.findViewById(R.id.btn_cancel);
		
		setContentView(dialogView);
	}

	/**
	 * 设置dialog的标题
	 * @param titleId 标题对应的资源Id，当{@code titleId}小于0时，不显示dialog标题。
	 * @return
	 */
	public CustomDialog withTitle(int titleId) {
		CharSequence title;
		if(titleId < 0) {
			title = null;
		}
		else {
			title = context.getText(titleId);
		}
		return withTitle(title);
	}
	
	/**
	 * 设置dialog的标题
	 * @param title 当{@code title}为null时，不显示dialog标题。
	 * @return
	 */
	public CustomDialog withTitle(CharSequence title) {
		toggleView(tvTitle, title);
		tvTitle.setText(title);
		return this;
	}

    /**
     * 设置dialog message
     * @param resId
     * @return
     */
	public CustomDialog withMessage(int resId) {
		CharSequence message = context.getText(resId);
		return withMessage(message);
	}

    /**
     * 设置dialog message
     * @param message
     * @return
     */
	public CustomDialog withMessage(CharSequence message) {
		tvMessage.setText(message);
		return this;
	}

	/**
	 * 设置左边按钮text和点击函数。
	 * @param textId text 资源id，当{@code textId}小于0时，不显示按钮。
	 * @param listener 点击函数
	 * @return
	 */
	public CustomDialog withButton1(int textId, View.OnClickListener listener) {
		CharSequence text;
		if(textId < 0) {
			text = null;
		}
		else {
			text = context.getText(textId);
		}
		return withButton1(text, listener);
	}
	
	/**
	 * 设置左边按钮text和点击函数。
	 * @param text 当{@code text}为null时，不显示按钮。
	 * @param listener 点击函数
	 * @return
	 */
	public CustomDialog withButton1(CharSequence text, View.OnClickListener listener) {
		toggleView(btnOk, text);
		adjustButton();
		btnOk.setText(text);
		btnOk.setOnClickListener(listener);
		return this;
	}
	
	/**
	 * 设置右边按钮text和点击函数。
	 * @param textId text 资源id，当{@code textId}小于0时，不显示按钮。
	 * @param listener 点击函数
	 * @return
	 */
	public CustomDialog withButton2(int textId, View.OnClickListener listener) {
		CharSequence text;
		if(textId < 0) {
			text = null;
		}
		else {
			text = context.getText(textId);
		}
		return withButton2(text, listener);
	}
	
	/**
	 * 设置左边按钮text和点击函数。
	 * @param text 当{@code text}为null时，不显示按钮。
	 * @param listener 点击函数
	 * @return
	 */
	public CustomDialog withButton2(CharSequence text, View.OnClickListener listener) {
		toggleView(btnCancel, text);
		adjustButton();
		btnCancel.setText(text);
		btnCancel.setOnClickListener(listener);
		return this;
	}
	
	private void toggleView(View view, Object object) {
		if(object == null) {
			view.setVisibility(View.GONE);
		}
		else {
			view.setVisibility(View.VISIBLE);
		}
	}
	
	/**
	 * 调整按钮的显示状态
	 */
	private void adjustButton() {
		// 当2个按钮都不显示时，按钮的父布局也不显示
		if(btnOk.getVisibility() == View.GONE && btnCancel.getVisibility() == View.GONE) {
			llButton.setVisibility(View.GONE);
		}
		else {
			llButton.setVisibility(View.VISIBLE);
		}
		
		if(btnOk.getVisibility() == View.GONE) {
			setMargin(btnCancel, 0);
		}
		else {
			float marginLeft = context.getResources().getDimension(R.dimen.dimen_10);
			setMargin(btnCancel, (int)marginLeft);
		}
	}

	/**
	 * 当左边按钮不显示时，调整右边按钮的marginLeft值
	 * @param view
	 * @param left
	 */
	private void setMargin(View view, int left) {
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)btnCancel.getLayoutParams();
		params.setMargins(left, params.rightMargin, params.topMargin, params.bottomMargin);
		view.setLayoutParams(params);
	}
}
