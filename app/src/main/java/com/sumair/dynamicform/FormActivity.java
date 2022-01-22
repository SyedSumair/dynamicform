package com.sumair.dynamicform;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.sumair.dynamicform.auth.form.http.wrapper.form.ColData;
import com.sumair.dynamicform.auth.form.http.wrapper.form.ColumnModel;
import com.sumair.dynamicform.auth.form.http.wrapper.form.Layout;
import com.sumair.dynamicform.auth.form.http.wrapper.form.LayoutWrapper;
import com.sumair.dynamicform.auth.form.http.wrapper.form.MyColumnModelData;
import com.sumair.dynamicform.auth.form.http.wrapper.form.MyLayoutData;
import com.sumair.dynamicform.auth.form.http.wrapper.form.MyLayoutModelWrapper;
import com.sumair.dynamicform.auth.form.http.wrapper.form.WidgetModel;
import com.sumair.dynamicform.utilities.FormUtils;
import com.sumair.dynamicform.utilities.constants.FormConstants;
import com.sumair.dynamicform.utilities.customviews.MyAttachmentView;
import com.sumair.dynamicform.utilities.customviews.MyBottomSheet;
import com.sumair.dynamicform.utilities.customviews.MyDatePicker;
import com.sumair.dynamicform.utilities.customviews.MyProfileImageView;
import com.sumair.dynamicform.utilities.customviews.MyTextArea;
import com.sumair.dynamicform.utilities.customviews.MyTextInputLayout;
import com.sumair.dynamicform.utilities.customviews.StickyScrollView;

import java.util.HashMap;

import static com.sumair.dynamicform.utilities.NumberUtils.getNextNumber;

public class FormActivity extends AppCompatActivity {

    LinearLayout stickyLinear;
    Button btnSave;
    StickyScrollView scrollView;
    boolean isValid;
    private LayoutWrapper wrapper;
    MyLayoutModelWrapper myLayoutModelWrapper;
    HashMap<String, MyAttachmentView.HandleImages> handleImages = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        setListeners();
    }

    private void initViews() {
        stickyLinear = findViewById(R.id.sticky_linear);
        btnSave = findViewById(R.id.btn_save);
        scrollView = findViewById(R.id.scrollView);
        btnSave.setEnabled(false);
    }

    private void setListeners() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData(false);
                if(isValid) {
                    saveData(true);
                    // make parameters and send to server
                }
            }
        });
    }

    public void setUpFormView(LayoutWrapper Wrapper) {
        this.wrapper = Wrapper;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadViews(FormActivity.this);
                btnSave.setEnabled(true);
            }
        }, 300);
    }
    private void loadViews(Context context){
        for (int i = 0; i<wrapper.getLayout().size() ; i++) {
            View header = LayoutInflater.from(context).inflate(R.layout.item_header, stickyLinear, false);
            header.setTag("sticky");
            String header_title = wrapper.getLayout().get(i).getTitle();
            ((TextView) header.findViewById(R.id.txt_header)).setText(header_title);
            if(wrapper.getLayout().get(i).isDynamic()){
                final int finalI1 = i;
                ((ImageView) header.findViewById(R.id.img_deleteHeader)).setVisibility(View.VISIBLE);
                ((ImageView) header.findViewById(R.id.img_deleteHeader)).setImageResource(android.R.drawable.ic_menu_close_clear_cancel);
                header.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Second Form with child data
                    }
                });
            }
            addMyView(stickyLinear, header);

            if(!wrapper.getLayout().get(i).isDynamic()){
                for (int j = 0; j < wrapper.getLayout().get(i).getColumns().size(); j++) {
                    LinearLayout childDynamic = new LinearLayout(context);
                    childDynamic.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    childDynamic.setOrientation(LinearLayout.VERTICAL);

                    final ColumnModel column = wrapper.getLayout().get(i).getColumns().get(j);

                    if(!column.isDynamic()){
                        final ColData columndata = myLayoutModelWrapper.getLayout().get(i).getLayoutData().getColumnModelData().get(j).getColumnData();

                        for (int k = 0; k < columndata.getWidgets().size(); k++) {
                            WidgetModel widget = columndata.getWidgets().get(k);
                            ColData.WidgetValue widgetValue;
                            int id ;
                            if(columndata.getWidgetsValue().size() < columndata.getWidgets().size()){
                                widgetValue = new ColData.WidgetValue();
                                id = getNextNumber();
                                widgetValue.setView_id(id);
                                columndata.getWidgetsValue().add(widgetValue);
                            }
                            else {
                                widgetValue = columndata.getWidgetsValue().get(k);
                                //id = widgetValue.getView_id();
                            }

                            switch (widget.getName()){
                                case FormConstants.TEXT_FIELD:
                                    LinearLayout textInputLayout = new MyTextInputLayout.Builder(context).init(childDynamic, widget, widgetValue).build();
                                    //textInputLayout.setId(id);
                                    if(widget.getProps().getField().equals("email")){
                                        //get from user data
                                        ((EditText) textInputLayout.findViewById(R.id.et_textField)).setText("");
                                    }else if(widget.getProps().getName().equals("first_name")){
                                        //get from user data
                                        ((EditText) textInputLayout.findViewById(R.id.et_textField)).setText("");
                                    }
                                    addMyView(childDynamic,textInputLayout);
                                    break;
                                case FormConstants.TEXT_AREA:
                                    ConstraintLayout text_multiline = new MyTextArea.Builder(context).init(childDynamic, widget, widgetValue).build();
                                    //textInputLayout.setId(id);
                                    addMyView(childDynamic,text_multiline);
                                    break;
                                case FormConstants.DATE_PICKER:
                                    ConstraintLayout datePicker = new MyDatePicker.Builder(context).init(childDynamic, widget, widgetValue).build();
                                    //textInputLayout.setId(id);
                                    addMyView(childDynamic,datePicker);
                                    break;
                                case FormConstants.DROP_DOWN:
                                    ConstraintLayout textView = new MyBottomSheet.Builder(context).init(childDynamic, widget, widgetValue).build();
                                    //textView.setId(id);
                                    addMyView(childDynamic,textView);
                                    break;
                                case FormConstants.DROP_ZONE:
                                    if(widget.getProps().getName().contains(FormConstants.PROFILE_IMAGE)) {
                                        final LinearLayout imageLayout = new MyProfileImageView.Builder(context).init(childDynamic, widget, widgetValue).build();
                                        AppCompatImageView image = imageLayout.findViewById(R.id.img_profileImage);
                                        image.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                //open image picker
                                            }
                                        });
                                        addMyView(childDynamic,imageLayout);
                                    }
                                    else if(widget.getProps().getName().contains(FormConstants.ATTACHMENTS)){
                                        MyAttachmentView.Builder recyclerViewBuilder = new MyAttachmentView.Builder(context).init(childDynamic, widget, widgetValue, ""+i+""+j+""+k);
                                        handleImages.put(""+i+""+j+""+k, recyclerViewBuilder.getHandleImagesListener());
                                        LinearLayout recyclerView = recyclerViewBuilder.build();
                                        //recyclerView.setId(id);
                                        addMyView(childDynamic,recyclerView);
                                    }
                                    break;
                            }
                        }
                        addMyView(stickyLinear,childDynamic);
                    }
                    else {
                        View child = LayoutInflater.from(context).inflate(R.layout.layout_dynamic_column, stickyLinear, false);
                        //header.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 100));
                        //child.setId(column.getColumnDataList().get(0).getColumnId());
                        ((TextView) child.findViewById(R.id.txt_label)).setText(column.getTitle());
                        ((TextView) child.findViewById(R.id.txt_selection)).setText(getString(R.string.add_s,  column.getTitle()));
                        child.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Third Form with child data
                            }
                        });
                        addMyView(stickyLinear,child);
                    }
                }
            }
            else {

            }
        }
    }

    private void addMyView(final LinearLayout parent, final View child){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                parent.addView(child);
            }
        });
    }

    private void saveData(boolean actionSave) {
        for (int z = 0; z < wrapper.getLayout().size(); z++) {
            Layout layout = wrapper.getLayout().get(z);
            if (!layout.isDynamic()) {
                MyLayoutData myLayoutData = myLayoutModelWrapper.getLayout().get(z).getLayoutData();
                for (int i = 0; i < layout.getColumns().size(); i++) {
                    ColumnModel column = layout.getColumns().get(i);
                    MyColumnModelData columnData = myLayoutData.getColumnModelData().get(i);

                    if (!column.isDynamic()) {
                        ColData colData = columnData.getColumnData();
                        for (int j = 0; j < colData.getWidgets().size(); j++) {
                            WidgetModel widget = colData.getWidgets().get(j);
                            if(actionSave){
                                FormUtils.saveWidgetValue(this, widget, colData.getWidgetsValue().get(j));
                            }
                            else {
                                isValid = FormUtils.validateWidget(this, widget, colData.getWidgetsValue().get(j), scrollView, stickyLinear);
                                if (!isValid) return;
                            }
                        }
                    }
                    else {
                        ColData colData = columnData.getColumnDataList().get(0);
                        for (int l = 0; l < colData.getWidgets().size(); l++) {
                            WidgetModel widget = colData.getWidgets().get(l);
                            if (colData.getWidgetsValue().size() != 0)
                                isValid = FormUtils.validateColumn(this, widget, colData.getWidgetsValue().get(l));
                            else
                                isValid = false;

                            if (!isValid) {
                                // Third Form with child data

                                return;
                            }
                        }

                    }
                }
            }
            else {
                MyLayoutData layoutData =  myLayoutModelWrapper.getLayout().get(z).getLayoutDataList().get(0);
                for (int i = 0; i < layout.getColumns().size(); i++) {
                    ColumnModel column = layout.getColumns().get(i);
                    MyColumnModelData columnData = layoutData.getColumnModelData().get(i);

                    isValid = FormUtils.validateLayout(this, column, columnData);
                    if (!isValid) {
                        // Second Form with child data
                        return;
                    }
                }
            }
        }
    }
}