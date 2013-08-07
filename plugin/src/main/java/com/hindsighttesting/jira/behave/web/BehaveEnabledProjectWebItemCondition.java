// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   BehaveEnabledProjectWebItemCondition.java

package com.hindsighttesting.jira.behave.web;

import com.atlassian.jira.plugin.webfragment.model.JiraHelper;
import com.atlassian.jira.project.Project;
import com.atlassian.plugin.PluginParseException;
import com.atlassian.plugin.web.Condition;
import com.hindsighttesting.jira.behave.service.ProjectSettingsManager;
import java.util.Map;

public class BehaveEnabledProjectWebItemCondition
    implements Condition
{

    public BehaveEnabledProjectWebItemCondition(ProjectSettingsManager settingManager)
    {
        this.settingManager = settingManager;
    }

    public void init(Map map)
        throws PluginParseException
    {
    }

    public boolean shouldDisplay(Map context)
    {
        JiraHelper jiraHelper = (JiraHelper)context.get("helper");
        return settingManager.isProjectEnabled(jiraHelper.getProjectObject().getKey());
    }

    private final ProjectSettingsManager settingManager;
}
