// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ProjectsServlet.java

package com.hindsighttesting.jira.behave.admin;

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

public class ProjectsServlet extends HttpServlet
{

    public ProjectsServlet(UserManager userManager, ThirdPartyPluginLicenseStorageManager licenseManager, LoginUriProvider loginUriProvider, TemplateRenderer renderer)
    {
        this.userManager = userManager;
        this.licenseManager = licenseManager;
        this.loginUriProvider = loginUriProvider;
        this.renderer = renderer;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException
    {
        String username = userManager.getRemoteUsername(request);
        if(username == null)
        {
            redirectToLogin(request, response);
            return;
        }
        if(!hasAdminPermission())
        {
            handleUnpermittedUser(request, response);
            return;
        }
        String template = "templates/license-failure.vm";
        try
        {
            if(licenseManager.getLicense().isDefined())
            {
                PluginLicense pluginLicense = (PluginLicense)licenseManager.getLicense().get();
                if(!pluginLicense.getError().isDefined())
                    template = "templates/projects-enabled-panel.vm";
            }
        }
        catch(PluginLicenseStoragePluginUnresolvedException e) { }
        Map context = new HashMap(1);
        context.put("displayEnabledProjectsUi", Boolean.valueOf(true));
        response.setContentType("text/html;charset=utf-8");
        renderer.render(template, context, response.getWriter());
    }

    private void redirectToLogin(HttpServletRequest request, HttpServletResponse response)
        throws IOException
    {
        response.sendRedirect(loginUriProvider.getLoginUri(URI.create(request.getRequestURL().toString())).toASCIIString());
    }

    private boolean hasAdminPermission()
    {
        String user = userManager.getRemoteUsername();
        try
        {
            return user != null && userManager.isSystemAdmin(user);
        }
        catch(NoSuchMethodError e)
        {
            return user != null && userManager.isSystemAdmin(user);
        }
    }

    private void handleUnpermittedUser(HttpServletRequest req, HttpServletResponse resp)
        throws IOException
    {
        resp.setContentType("text/html;charset=utf-8");
        Map context = new HashMap();
        context.put("errorMessage", "You do not have permissions to enable or disable Behave for JIRA on projects");
        context.put("displayEnabledProjectsUi", Boolean.valueOf(false));
        renderer.render("templates/projects-enabled-panel.vm", context, resp.getWriter());
    }

    private static final String TEMPLATE = "templates/projects-enabled-panel.vm";
    private final UserManager userManager;
    private final ThirdPartyPluginLicenseStorageManager licenseManager;
    private final LoginUriProvider loginUriProvider;
    private final TemplateRenderer renderer;
}
