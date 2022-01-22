package com.sumair.dynamicform.utilities.form;

import com.sumair.dynamicform.auth.form.http.wrapper.form.LayoutWrapper;

import java.util.ArrayList;

public class LayoutModelSingleton {

    private static ArrayList<LayoutWrapper> registrationForm = new ArrayList<>();

    public static ArrayList<LayoutWrapper> getInstance(){
        if(registrationForm == null){
            registrationForm = new ArrayList<>();
        }

        return registrationForm;
    }

    public static void setRegistrationForm(LayoutWrapper wrapper){
        registrationForm.add(wrapper);
        //instance.get(0).getLayout().get(index).getColumns();
    }

    public static ArrayList<LayoutWrapper> getRegistrationForm(){
        return registrationForm;
    }
}
