// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   GlobalSettingsManager.java

package com.hindsighttesting.jira.behave.service;

import com.atlassian.sal.api.pluginsettings.PluginSettings;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;

public class GlobalSettingsManager
{

    public GlobalSettingsManager(PluginSettingsFactory pluginSettingsFactory)
    {
        this.pluginSettingsFactory = pluginSettingsFactory;
    }

    public boolean isAllProjectsEnabled()
    {
        if(allprojectEnabled == null)
        {
            PluginSettings globalSettings = pluginSettingsFactory.createGlobalSettings();
            Object globalEnabledString = globalSettings.get("com.hindsighttesting.behave.global.allprojects");
            allprojectEnabled = Boolean.valueOf(globalEnabledString != null ? Boolean.valueOf((String)globalEnabledString).booleanValue() : true);
        }
        return allprojectEnabled.booleanValue();
    }

    public void saveAllprojectEnabled(Boolean allprojectEnabled)
    {
        PluginSettings globalSettings = pluginSettingsFactory.createGlobalSettings();
        globalSettings.put("com.hindsighttesting.behave.global.allprojects", allprojectEnabled.toString());
        this.allprojectEnabled = allprojectEnabled;
    }

    private static final String GLOBAL_ALLPROJECTS_KEY = "com.hindsighttesting.behave.global.allprojects";
    private final PluginSettingsFactory pluginSettingsFactory;
    private Boolean allprojectEnabled;
}
