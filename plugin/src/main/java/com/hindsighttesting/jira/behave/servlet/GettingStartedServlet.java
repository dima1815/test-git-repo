// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   GettingStartedServlet.java

package com.hindsighttesting.jira.behave.servlet;

import com.atlassian.sal.api.ApplicationProperties;
import com.atlassian.sal.api.auth.LoginUriProvider;
import com.atlassian.sal.api.message.I18nResolver;
import com.atlassian.sal.api.user.UserManager;
import com.atlassian.templaterenderer.TemplateRenderer;
import com.atlassian.upm.api.license.entity.LicenseType;
import com.atlassian.upm.api.license.entity.PluginLicense;
import com.atlassian.upm.api.util.Option;
import com.atlassian.upm.license.storage.lib.*;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.*;

public class GettingStartedServlet extends HttpServlet
{

    public GettingStartedServlet(ThirdPartyPluginLicenseStorageManager licenseManager, AtlassianMarketplaceUriFactory uriFactory, ApplicationProperties applicationProperties, TemplateRenderer renderer, LoginUriProvider loginUriProvider, UserManager userManager, I18nResolver i18nResolver)
    {
        this.licenseManager = licenseManager;
        this.uriFactory = uriFactory;
        this.applicationProperties = applicationProperties;
        this.renderer = renderer;
        this.loginUriProvider = loginUriProvider;
        this.userManager = userManager;
        this.i18nResolver = i18nResolver;
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
        if(userManager.getRemoteUsername() == null)
        {
            redirectToLogin(req, resp);
            return;
        } else
        {
            Map context = initVelocityContext(resp);
            renderer.render("templates/gettingstarted-panel.vm", context, resp.getWriter());
            return;
        }
    }

    private boolean hasAdminPermission()
    {
        String user = userManager.getRemoteUsername();
        try
        {
            return user != null && (userManager.isAdmin(user) || userManager.isSystemAdmin(user));
        }
        catch(NoSuchMethodError e)
        {
            return user != null && userManager.isSystemAdmin(user);
        }
    }

    private Map initVelocityContext(HttpServletResponse resp)
    {
        Map context;
        boolean isLicensed;
        resp.setContentType("text/html;charset=utf-8");
        URI licensingUrl = URI.create((new StringBuilder()).append(applicationProperties.getBaseUrl()).append("/plugins/servlet/behave/license").toString());
        context = new HashMap();
        context.put("licensingUrl", licensingUrl);
        context.put("admin", Boolean.valueOf(hasAdminPermission()));
        isLicensed = false;
        try
        {
            isLicensed = licenseManager.getLicense().isDefined();
            PluginLicense pluginLicense = (PluginLicense)licenseManager.getLicense().get();
            isLicensed = isLicensed && !pluginLicense.getError().isDefined();
            context.put("licenseType", pluginLicense.getLicenseType().name());
        }
        catch(PluginLicenseStoragePluginUnresolvedException e)
        {
            context.put("licensed", Boolean.valueOf(isLicensed));
            break MISSING_BLOCK_LABEL_206;
        }
        context.put("licensed", Boolean.valueOf(isLicensed));
        break MISSING_BLOCK_LABEL_206;
        Exception exception;
        exception;
        context.put("licensed", Boolean.valueOf(isLicensed));
        throw exception;
        return context;
    }

    private void redirectToLogin(HttpServletRequest req, HttpServletResponse resp)
        throws IOException
    {
        resp.sendRedirect(loginUriProvider.getLoginUri(URI.create(req.getRequestURL().toString())).toASCIIString());
    }

    private static final String TEMPLATE = "templates/gettingstarted-panel.vm";
    private final ThirdPartyPluginLicenseStorageManager licenseManager;
    private final AtlassianMarketplaceUriFactory uriFactory;
    private final ApplicationProperties applicationProperties;
    private final TemplateRenderer renderer;
    private final LoginUriProvider loginUriProvider;
    private final UserManager userManager;
    private final I18nResolver i18nResolver;
}
