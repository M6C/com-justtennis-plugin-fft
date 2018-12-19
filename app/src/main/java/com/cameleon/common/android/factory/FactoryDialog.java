package com.cameleon.common.android.factory;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import com.cameleon.common.android.factory.listener.OnClickViewListener;
import com.justtennis.plugin.fft.R;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

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

	public Dialog buildDatePickerDialog(Context context, final OnClickViewListener onClickOkListener, int titleId, Date date) {
		LayoutInflater service = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = service.inflate(R.layout.cameleon_common_date_picker, null, true);
		DatePicker datePicker = layout.findViewById(R.id.datePicker);

		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime(date);
		datePicker.init(
				calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
				null
		);

		return buildViewDialog(context, onClickOkListener, null, layout, datePicker, titleId);
	}

	private Dialog buildViewDialog(Context context, final OnClickViewListener onClickOkListener, final OnClickViewListener onClickCancelListener, View layout, final View view, int titleId) {
		Context ctx = context;
		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		if (titleId>0) {
			builder.setTitle(titleId);
		}
		String txtNegative;
		String txtPositive;
		if (onClickCancelListener==null) {
			txtNegative = "Cancel";
			txtPositive = "Ok";
		} else {
			txtNegative = "No";
			txtPositive = "Yes";
		}
		builder.setNegativeButton(txtNegative, (onClickCancelListener==null ? null : (OnClickListener) (dialog, which) -> onClickCancelListener.onClick(dialog, view, which)));
		builder.setPositiveButton(txtPositive, (dialog, which) -> onClickOkListener.onClick(dialog, view, which));
		builder.setView(layout);
		return builder.create();
	}
}