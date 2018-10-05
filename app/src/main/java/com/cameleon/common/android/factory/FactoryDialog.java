package com.cameleon.common.android.factory;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;

public class FactoryDialog {

	private static FactoryDialog instance;

	private FactoryDialog() {
	}

	public static FactoryDialog getInstance() {
		if (instance==null)
			instance = new FactoryDialog();
		return instance;
	}

	public Dialog buildOkCancelDialog(Context context, OnClickListener onClickOkListener, OnClickListener onClickCancelListener, int titleId, int messageId) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(titleId);
		builder.setMessage(messageId);
		builder.setPositiveButton("OK", onClickOkListener);
		builder.setNeutralButton("Cancel", onClickCancelListener);
		return builder.create();
	}
}