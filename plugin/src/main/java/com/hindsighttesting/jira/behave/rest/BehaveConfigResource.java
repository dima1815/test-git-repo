// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   BehaveConfigResource.java

package com.hindsighttesting.jira.behave.rest;

import com.hindsighttesting.jira.behave.rest.model.BehaveConfig;
import com.hindsighttesting.jira.behave.service.ProjectSettingsManager;
import javax.ws.rs.core.Response;

public class BehaveConfigResource
{

    public BehaveConfigResource(ProjectSettingsManager settingsManager)
    {
        this.settingsManager = settingsManager;
    }

    public Response getGlobalConfig()
    {
        return Response.ok(settingsManager.getGlobalConfig()).build();
    }

    public Response saveGlobalConfig(BehaveConfig config)
    {
        settingsManager.saveConfig(config);
        return Response.ok().build();
    }

    private final ProjectSettingsManager settingsManager;
}
