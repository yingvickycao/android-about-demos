package com.hades.example.android.qa;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import android.app.Activity;

import com.hades.example.android.R;

import org.junit.Assert;
import org.junit.Test;

public class AlertParamGetterTest3 {

    private final String defaultTitle = "Default title";
    private final String customTitle = "Custom title";

    @Test
    public void getAlertParam_1() { // ok
        DefaultUIConfigure configure = null;
        AlertParamGetter3 getter = new AlertParamGetter3(mock(Activity.class), configure) {
            public String getDefaultTitle() {
                return defaultTitle;
            }
        };
        AlertParam param = getter.getAlertParam();
        Assert.assertEquals(defaultTitle, param.getTitle());
    }

    @Test
    public void getAlertParam_2() { // ok
        DefaultUIConfigure configure = new DefaultUIConfigure();
        configure.setDefaultAlertConfigure(new DefaultAlertConfigure());
        configure.getDefaultAlertConfigure().setTitle(customTitle);

        AlertParamGetter3 getter = new AlertParamGetter3(mock(Activity.class), configure) {
            public String getDefaultTitle() {
                return defaultTitle;
            }
        };
        AlertParam param = getter.getAlertParam();
        Assert.assertEquals(customTitle, param.getTitle());
    }
}