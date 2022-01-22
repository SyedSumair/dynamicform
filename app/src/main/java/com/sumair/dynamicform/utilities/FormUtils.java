package com.sumair.dynamicform.utilities;

import android.app.Activity;
import android.graphics.Point;
import android.graphics.Rect;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;
import com.sumair.dynamicform.R;
import com.sumair.dynamicform.auth.form.http.wrapper.form.ColData;
import com.sumair.dynamicform.auth.form.http.wrapper.form.ColumnModel;
import com.sumair.dynamicform.auth.form.http.wrapper.form.MyColumnModelData;
import com.sumair.dynamicform.auth.form.http.wrapper.form.WidgetModel;
import com.sumair.dynamicform.utilities.constants.FormConstants;
import com.sumair.dynamicform.utilities.customviews.MyAttachmentView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.MultipartBody;

public class FormUtils {

    public static void saveWidgetValue(Activity activity, WidgetModel widget, ColData.WidgetValue widgetValue) {
        switch (widget.getName()) {
            case FormConstants.TEXT_FIELD:
                LinearLayout linearLayout = activity.findViewById(widgetValue.getView_id());
                EditText editText = linearLayout.findViewById(R.id.et_textField);
                TextInputLayout textInputLayout = linearLayout.findViewById(R.id.textinputLayout);
                String str = editText.getText().toString();
                widgetValue.setValue(str);
                /*if(widget.getProps().getName().contains("email") && widgetValue.isPrefilled()){

                }
                else {
                    widgetValue.setPrefilled(false);
                }*/
                textInputLayout.setError(null);

                break;
            case FormConstants.TEXT_AREA:
                ConstraintLayout textArea = activity.findViewById(widgetValue.getView_id());
                EditText editTextMultiline = textArea.findViewById(R.id.et_multiline);
                String str2 = editTextMultiline.getText().toString();
                widgetValue.setValue(str2);
                editTextMultiline.setError(null);

                break;
            case FormConstants.DATE_PICKER:
                ConstraintLayout datePicker = activity.findViewById(widgetValue.getView_id());
                String date = ((TextView) datePicker.findViewById(R.id.txt_selection)).getText().toString();
                if (date.equals(activity.getString(R.string.dob))) {
                    widgetValue.setValue("");
                } else {
                    widgetValue.setValue(date);
                }
                break;
            case FormConstants.DROP_DOWN:
                ConstraintLayout textView = activity.findViewById(widgetValue.getView_id());
                ArrayList<Integer> indices = (ArrayList<Integer>) textView.getTag();
                if (indices != null && indices.size() != 0) {
                    widgetValue.setSelectedIndex(indices);
                }
                break;
            case FormConstants.DROP_ZONE:
                if (widget.getProps().getName().contains(FormConstants.PROFILE_IMAGE)) {
                    final LinearLayout imageLayout = activity.findViewById(widgetValue.getView_id());
                    AppCompatImageView image = imageLayout.findViewById(R.id.img_profileImage);
                    if (image.getTag(R.string.imageview_tagid) != null) {
                        widgetValue.setProfileImage((String) image.getTag(R.string.imageview_tagid));
                    }
                } else if (widget.getProps().getName().contains(FormConstants.ATTACHMENTS)) {
                    RecyclerView recyclerView = activity.findViewById(widgetValue.getView_id());
                    ArrayList<String> images = ((MyAttachmentView.AttachmentAdapter) recyclerView.getAdapter()).getDataSet();
                    if (images.size() > 1) {
                        if (TextUtils.isEmpty(images.get(0))) {
                            images.remove(0);
                        }
                        widgetValue.setAttachments(images);
                    } else {
                        widgetValue.setAttachments(new ArrayList<>());
                    }
                }
                break;
        }
    }


    public static boolean validateWidget(Activity activity, WidgetModel widget, ColData.WidgetValue widgetValue, ScrollView scrollView, LinearLayout linear) {
        boolean isValidated = false;
        switch (widget.getName()) {
            case FormConstants.TEXT_FIELD:
                LinearLayout linearLayout = activity.findViewById(widgetValue.getView_id());
                EditText editText = linearLayout.findViewById(R.id.et_textField);
                TextInputLayout textInputLayout = linearLayout.findViewById(R.id.textinputLayout);
                String str = editText.getText().toString();
                //widgetValue.setValue(str);
                textInputLayout.setError(null);
                isValidated = true;
                if (widget.getProps().getName().contains("name") && str.trim().isEmpty()) {
                    textInputLayout.setError(activity.getString(R.string.email_is_required));
                    linearLayout.clearFocus();
                    textInputLayout.requestFocus();
                    //scrollToView(scrollView, editText);
                    //scrollToRow(scrollView, (LinearLayout) linearLayout.getParent(), linearLayout);
                    //ObjectAnimator.ofInt(scrollView, "scrollY", linearLayout.getTop()).start();
//                    scrollView.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            scrollView.scrollTo(0, (int)textInputLayout.getY());
//                        }
//                    });
                    isValidated = false;
                }else if (widget.getProps().getName().contains("email")) {
                    textInputLayout.setError(activity.getString(R.string.err_invalid_email));
                    linearLayout.clearFocus();
                    textInputLayout.requestFocus();
                    //scrollToView(scrollView, editText);
                    //scrollToRow(scrollView, (LinearLayout) linearLayout.getParent(), linearLayout);
                    //ObjectAnimator.ofInt(scrollView, "scrollY", linearLayout.getTop()).start();
//                    scrollView.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            scrollView.scrollTo(0, (int)textInputLayout.getY());
//                        }
//                    });
                    isValidated = false;
                } else {
                    isValidated = true;
                }

                break;
            case FormConstants.TEXT_AREA:
                ConstraintLayout textArea = activity.findViewById(widgetValue.getView_id());
                EditText editTextMultiline = textArea.findViewById(R.id.et_multiline);
                String str2 = editTextMultiline.getText().toString();
                editTextMultiline.setError(null);
                isValidated = true;
                if (widget.getProps().isRequired() && TextUtils.isEmpty(str2) || (widget.getProps().isRequired() && str2.trim().length() == 0)) {
                    editTextMultiline.setError(activity.getString(R.string.error_textfield,widget.getProps().getLabelText()));
                    editTextMultiline.clearFocus();
                    editTextMultiline.requestFocus();
                    isValidated = false;
                } else {
                    isValidated = true;
                }
                break;
            case FormConstants.DATE_PICKER:
                ConstraintLayout datePicker = activity.findViewById(widgetValue.getView_id());
                String date = ((TextView) datePicker.findViewById(R.id.txt_selection)).getText().toString();
                isValidated = true;
                if (widget.getProps().isRequired() && (date.equals(activity.getString(R.string.dob)) || TextUtils.isEmpty(date))) {
                    Toast.makeText(activity, activity.getString(R.string.dob), Toast.LENGTH_SHORT).show();
                    isValidated = false;
                } else {
                    isValidated = true;
                }
                break;
            case FormConstants.DROP_DOWN:
                ConstraintLayout textView = activity.findViewById(widgetValue.getView_id());
                ArrayList<Integer> indices = (ArrayList<Integer>) textView.getTag();
                if (indices != null && indices.size() != 0) {
                    //widgetValue.setSelectedIndex(indices);
                    isValidated = true;
                }
                if (widget.getProps().isRequired() && (indices == null || indices.size() == 0)) {
                    Toast.makeText(activity, activity.getString(R.string.select, widget.getProps().getLabelText()), Toast.LENGTH_SHORT).show();
                    //ObjectAnimator.ofInt(scrollView, "scrollY", textView.getTop()).start();
                    scrollView.post(new Runnable() {
                        @Override
                        public void run() {
                            scrollView.scrollTo(0, getRelativeTop((View) textView.getParent(), textView));
                        }
                    });
                    scrollView.clearFocus();
                    textView.findViewById(R.id.txt_selection).requestFocus();
                    isValidated = false;
                } else {
                    isValidated = true;
                }
                break;
            case FormConstants.DROP_ZONE:
                if (widget.getProps().getName().contains(FormConstants.PROFILE_IMAGE)) {
                    final LinearLayout imageLayout = activity.findViewById(widgetValue.getView_id());
                    AppCompatImageView image = imageLayout.findViewById(R.id.img_profileImage);
                    if (image.getTag(R.string.imageview_tagid) != null) {
                        isValidated = true;
                    }
                    if (widget.getProps().isRequired() && image.getTag(R.string.imageview_tagid) == null) {
                        Toast.makeText(activity, activity.getString(R.string.error_profile_image), Toast.LENGTH_SHORT).show();
                        //ObjectAnimator.ofInt(scrollView, "scrollY", imageLayout.getTop()).start();
                        imageLayout.clearFocus();
                        imageLayout.requestFocus();
                        isValidated = false;
                    } else {
                        isValidated = true;
                    }
                } else if (widget.getProps().getName().contains(FormConstants.ATTACHMENTS)) {
                    RecyclerView recyclerView = activity.findViewById(widgetValue.getView_id());
                    ArrayList<String> images = ((MyAttachmentView.AttachmentAdapter) recyclerView.getAdapter()).getDataSet();
                    if (images.size() > 1) {
                        isValidated = true;
                    }
                    if (widget.getProps().isRequired() && images.size() <= 1) {
                        Toast.makeText(activity, activity.getString(R.string.error_attachment_images), Toast.LENGTH_SHORT).show();
                        //ObjectAnimator.ofInt(scrollView, "scrollY", recyclerView.getTop()).start();
                        recyclerView.clearFocus();
                        recyclerView.requestFocus();
                        isValidated = false;
                    } else {
                        isValidated = true;
                    }
                }
                break;
        }
        return isValidated;
    }

    public static boolean validateColumn(Activity activity, WidgetModel widget, ColData.WidgetValue widgetValue) {
        boolean isValidated = false;
        switch (widget.getName()) {
            case FormConstants.TEXT_FIELD:
                if (widget.getProps().isRequired() && TextUtils.isEmpty(widgetValue.getValue())) {
                    isValidated = false;
                } else {
                    isValidated = true;
                }

                break;
            case FormConstants.TEXT_AREA:
                if (widget.getProps().isRequired() && TextUtils.isEmpty(widgetValue.getValue())) {
                    isValidated = false;
                } else {
                    isValidated = true;
                }
                break;
            case FormConstants.DATE_PICKER:
                if (widget.getProps().isRequired() && (widgetValue.getValue().equals(activity.getString(R.string.dob)) || TextUtils.isEmpty(widgetValue.getValue()))) {
                    isValidated = false;
                } else {
                    isValidated = true;
                }
                break;
            case FormConstants.DROP_DOWN:
                if (widget.getProps().isRequired() && widgetValue.getSelectedIndex().size() == 0) {
                    //Toast.makeText(activity, activity.getString(R.string.error_dropdown), Toast.LENGTH_SHORT).show();
                    isValidated = false;
                } else {
                    isValidated = true;
                }
                break;
            case FormConstants.DROP_ZONE:
                if (widget.getProps().getName().contains(FormConstants.PROFILE_IMAGE)) {
                    if (widget.getProps().isRequired() && TextUtils.isEmpty(widgetValue.getProfileImage())) {
                        //Toast.makeText(activity, activity.getString(R.string.error_profile_image), Toast.LENGTH_SHORT).show();
                        isValidated = false;
                    } else {
                        isValidated = true;
                    }
                } else if (widget.getProps().getName().contains(FormConstants.ATTACHMENTS)) {
                    if (widget.getProps().isRequired() && widgetValue.getAttachments().size() == 0) {
                        //Toast.makeText(activity, activity.getString(R.string.error_attachment_images), Toast.LENGTH_SHORT).show();
                        isValidated = false;
                    } else {
                        isValidated = true;
                    }
                }
                break;
        }
        return isValidated;
    }


    public static boolean validateLayout(Activity activity, ColumnModel columnModel, MyColumnModelData columnData) {
        boolean isValidated = false;

        if (!columnModel.isDynamic()) {
            ColData colData = columnData.getColumnData();
            for (int j = 0; j < colData.getWidgets().size(); j++) {
                WidgetModel widget = colData.getWidgets().get(j);
                isValidated = FormUtils.validateColumn(activity, widget, colData.getWidgetsValue().get(j));
                if (!isValidated) {
                    return isValidated;
                }
            }
        } else {
            ColData colData = columnData.getColumnDataList().get(0);
            for (int l = 0; l < colData.getWidgets().size(); l++) {
                WidgetModel widget = colData.getWidgets().get(l);
                if (colData.getWidgetsValue().size() != 0)
                    isValidated = FormUtils.validateColumn(activity, widget, colData.getWidgetsValue().get(l));
                else
                    isValidated = false;

                if (!isValidated) {
                    return isValidated;
                }
            }
        }

        return isValidated;
    }

    public static void preFillColData(ColData colData, int indexRow, int indexColumn) {
        for (int i = 0; i < colData.getWidgets().size(); i++) {
            WidgetModel widgetModel = colData.getWidgets().get(i);
            ColData.WidgetValue widgetValue = colData.getWidgetsValue().get(i);
            switch (widgetModel.getName()) {
                case FormConstants.TEXT_FIELD:
                    //setPrefillTextField(widgetModel, widgetValue, userData, indexRow, indexColumn);
                    break;
                case FormConstants.TEXT_AREA:
                    //setPrefillTextField(widgetModel, widgetValue, indexRow, indexColumn);
                    break;
                case FormConstants.DATE_PICKER:
                    //setPrefillDatePicker(widgetModel, widgetValue, indexRow, indexColumn);
                    break;
                case FormConstants.DROP_DOWN:
                    //setPrefillDropDown(widgetModel, widgetValue, indexRow, indexColumn);
                    break;
                case FormConstants.DROP_ZONE:
                    //setPrefillDropZone(widgetModel, widgetValue, indexRow, indexColumn);
                    break;
            }
        }
    }

    /**
     * Used to get deep child offset.
     * <p/>
     * 1. We need to scroll to child in scrollview, but the child may not the direct child to scrollview.
     * 2. So to get correct child position to scroll, we need to iterate through all of its parent views till the main parent.
     *
     * @param mainParent        Main Top parent.
     * @param parent            Parent.
     * @param child             Child.
     * @param accumulatedOffset Accumulated Offset.
     */
    private static void getDeepChildOffset(final ViewGroup mainParent, final ViewParent parent, final View child, final Point accumulatedOffset) {
        ViewGroup parentGroup = (ViewGroup) parent;
        accumulatedOffset.x += child.getLeft();
        accumulatedOffset.y += child.getBottom();
        if (parentGroup.equals(mainParent)) {
            return;
        }
        getDeepChildOffset(mainParent, parentGroup.getParent(), parentGroup, accumulatedOffset);
    }

    private static void scrollToRow(final ScrollView scrollView, final LinearLayout linearLayout, final View view) {
        long delay = 100; //delay to let finish with possible modifications to ScrollView
        scrollView.postDelayed(new Runnable() {
            public void run() {
                Rect textRect = new Rect(); //coordinates to scroll to
                view.getHitRect(textRect); //fills textRect with coordinates of TextView relative to its parent (LinearLayout)
                scrollView.requestChildRectangleOnScreen(linearLayout, textRect, false); //ScrollView will make sure, the given textRect is visible
            }
        }, delay);
    }

    public static int getRelativeTop(View rootView, View childView) {
        if (childView.getParent() == rootView) return childView.getTop();
        else return childView.getBottom() + getRelativeTop(rootView, (View) childView.getParent());
    }
}
