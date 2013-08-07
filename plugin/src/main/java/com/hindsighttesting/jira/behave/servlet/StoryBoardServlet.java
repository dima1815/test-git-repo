// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   StoryBoardServlet.java

package com.hindsighttesting.jira.behave.servlet;

import com.atlassian.crowd.embedded.api.User;
import com.atlassian.jira.project.Project;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.user.UserProjectHistoryManager;
import com.atlassian.sal.api.auth.LoginUriProvider;
import com.atlassian.sal.api.user.UserManager;
import com.atlassian.templaterenderer.TemplateRenderer;
import com.atlassian.upm.api.license.entity.PluginLicense;
import com.atlassian.upm.api.util.Option;
import com.atlassian.upm.license.storage.lib.PluginLicenseStoragePluginUnresolvedException;
import com.atlassian.upm.license.storage.lib.ThirdPartyPluginLicenseStorageManager;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.*;

public class StoryBoardServlet extends HttpServlet
{

    public StoryBoardServlet(TemplateRenderer renderer, LoginUriProvider loginUriProvider, UserManager userManager, ThirdPartyPluginLicenseStorageManager licenseManager, UserProjectHistoryManager userProjectHistoryManager, JiraAuthenticationContext jiraAuthenticationContext)
    {
        this.renderer = renderer;
        this.loginUriProvider = loginUriProvider;
        this.userManager = userManager;
        this.licenseManager = licenseManager;
        this.userProjectHistoryManager = userProjectHistoryManager;
        this.jiraAuthenticationContext = jiraAuthenticationContext;
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException
    {
        String username = userManager.getRemoteUsername(request);
        if(username == null)
        {
            redirectToLogin(request, response);
        } else
        {
            String template = "templates/license-failure.vm";
            try
            {
                if(licenseManager.getLicense().isDefined())
                {
                    PluginLicense pluginLicense = (PluginLicense)licenseManager.getLicense().get();
                    if(!pluginLicense.getError().isDefined())
                        template = "templates/feature-panel.vm";
                }
            }
            catch(PluginLicenseStoragePluginUnresolvedException e) { }
            String currentProjectKey = getCurrentProjectKey();
            Map context = new HashMap(1);
            context.put("currentProjectKey", currentProjectKey);
            response.setContentType("text/html;charset=utf-8");
            renderer.render(template, context, response.getWriter());
        }
    }

    private void redirectToLogin(HttpServletRequest request, HttpServletResponse response)
        throws IOException
    {
        response.sendRedirect(loginUriProvider.getLoginUri(URI.create(request.getRequestURL().toString())).toASCIIString());
    }

    private String getCurrentProjectKey()
    {
        User user = jiraAuthenticationContext.getLoggedInUser();
        Project currentProject = userProjectHistoryManager.getCurrentProject(10, user);
        return currentProject != null ? currentProject.getKey() : "";
    }

    private final ThirdPartyPluginLicenseStorageManager licenseManager;
    private final UserManager userManager;
    private final TemplateRenderer renderer;
    private final LoginUriProvider loginUriProvider;
    private final UserProjectHistoryManager userProjectHistoryManager;
    private final JiraAuthenticationContext jiraAuthenticationContext;
}
