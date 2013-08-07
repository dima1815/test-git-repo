// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ProjectsResource.java

package com.hindsighttesting.jira.behave.rest;

import com.atlassian.jira.security.JiraAuthenticationContext;
import com.hindsighttesting.jira.behave.service.ProjectSettingsManager;
import javax.ws.rs.core.Response;

public class ProjectsResource
{

    public ProjectsResource(ProjectSettingsManager settingsManager, JiraAuthenticationContext authenticationContext)
    {
        this.settingsManager = settingsManager;
        this.authenticationContext = authenticationContext;
    }

    public Response getProjects()
    {
        com.atlassian.crowd.embedded.api.User user = authenticationContext.getLoggedInUser();
        java.util.Set enabledProjects = settingsManager.getEnabledProjects(user);
        return Response.ok(enabledProjects).build();
    }

    private final ProjectSettingsManager settingsManager;
    private final JiraAuthenticationContext authenticationContext;
}
