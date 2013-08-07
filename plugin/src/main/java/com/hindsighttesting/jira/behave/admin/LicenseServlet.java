// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   LicenseServlet.java

package com.hindsighttesting.jira.behave.admin;

import com.atlassian.sal.api.ApplicationProperties;
import com.atlassian.sal.api.auth.LoginUriProvider;
import com.atlassian.sal.api.message.I18nResolver;
import com.atlassian.sal.api.user.UserManager;
import com.atlassian.templaterenderer.TemplateRenderer;
import com.atlassian.upm.api.license.entity.PluginLicense;
import com.atlassian.upm.api.util.Option;
import com.atlassian.upm.license.storage.lib.*;
import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import org.apache.commons.lang.StringUtils;

public class LicenseServlet extends HttpServlet
{

    public LicenseServlet(ThirdPartyPluginLicenseStorageManager licenseManager, AtlassianMarketplaceUriFactory uriFactory, ApplicationProperties applicationProperties, TemplateRenderer renderer, LoginUriProvider loginUriProvider, UserManager userManager, I18nResolver i18nResolver)
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
        }
        if(!hasAdminPermission())
        {
            handleUnpermittedUser(req, resp);
            return;
        } else
        {
            Map context = initVelocityContext(resp);
            addEligibleMarketplaceButtons(context);
            renderer.render("templates/license-admin.vm", context, resp.getWriter());
            return;
        }
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
        if(userManager.getRemoteUsername() == null)
        {
            redirectToLogin(req, resp);
            return;
        }
        if(!hasAdminPermission())
        {
            handleUnpermittedUser(req, resp);
            return;
        }
        Map context = initVelocityContext(resp);
        if(!context.containsKey("errorMessage"))
            try
            {
                if(!licenseManager.isUpmLicensingAware())
                {
                    String license = req.getParameter("license");
                    Option validatedLicense = licenseManager.validateLicense(license);
                    if(StringUtils.isEmpty(license))
                    {
                        licenseManager.removeRawLicense();
                        context.put("successMessage", i18nResolver.getText("plugin.license.storage.admin.license.remove"));
                        context.put("license", licenseManager.getLicense());
                    } else
                    if(validatedLicense.isDefined())
                    {
                        licenseManager.setRawLicense(license);
                        if(((PluginLicense)validatedLicense.get()).getError().isDefined())
                            context.put("warningMessage", i18nResolver.getText("plugin.license.storage.admin.license.update.invalid"));
                        else
                            context.put("successMessage", i18nResolver.getText("plugin.license.storage.admin.license.update"));
                        context.put("license", licenseManager.getLicense());
                    } else
                    {
                        context.put("errorMessage", i18nResolver.getText("plugin.license.storage.admin.license.invalid"));
                    }
                }
            }
            catch(PluginLicenseStoragePluginUnresolvedException e)
            {
                context.put("errorMessage", i18nResolver.getText("plugin.license.storage.admin.plugin.unavailable"));
                context.put("displayLicenseAdminUi", Boolean.valueOf(false));
            }
        addEligibleMarketplaceButtons(context);
        renderer.render("templates/license-admin.vm", context, resp.getWriter());
    }

    private Map initVelocityContext(HttpServletResponse resp)
    {
        resp.setContentType("text/html;charset=utf-8");
        URI servletUri = URI.create((new StringBuilder()).append(applicationProperties.getBaseUrl()).append("/plugins/servlet/behave/license").toString());
        Map context = new HashMap();
        context.put("servletUri", servletUri);
        context.put("displayLicenseAdminUi", Boolean.valueOf(true));
        try
        {
            context.put("license", licenseManager.getLicense());
            context.put("upmLicensingAware", Boolean.valueOf(licenseManager.isUpmLicensingAware()));
            context.put("pluginKey", licenseManager.getPluginKey());
            if(licenseManager.isUpmLicensingAware())
                context.put("warningMessage", i18nResolver.getText("plugin.license.storage.admin.upm.licensing.aware", new Serializable[] {
                    licenseManager.getPluginManagementUri()
                }));
        }
        catch(PluginLicenseStoragePluginUnresolvedException e)
        {
            context.put("errorMessage", i18nResolver.getText("plugin.license.storage.admin.plugin.unavailable"));
            context.put("displayLicenseAdminUi", Boolean.valueOf(false));
        }
        return context;
    }

    private void addEligibleMarketplaceButtons(Map context)
    {
        URI servletUri = URI.create((new StringBuilder()).append(applicationProperties.getBaseUrl()).append("/plugins/servlet/behave/license").toString());
        try
        {
            boolean eligibleButtons = false;
            if(uriFactory.isPluginBuyable())
            {
                context.put("buyPluginUri", uriFactory.getBuyPluginUri(servletUri));
                eligibleButtons = true;
            }
            if(uriFactory.isPluginTryable())
            {
                context.put("tryPluginUri", uriFactory.getTryPluginUri(servletUri));
                eligibleButtons = true;
            }
            if(uriFactory.isPluginRenewable())
            {
                context.put("renewPluginUri", uriFactory.getRenewPluginUri(servletUri));
                eligibleButtons = true;
            }
            if(uriFactory.isPluginUpgradable())
            {
                context.put("upgradePluginUri", uriFactory.getUpgradePluginUri(servletUri));
                eligibleButtons = true;
            }
            context.put("eligibleButtons", Boolean.valueOf(eligibleButtons));
        }
        catch(PluginLicenseStoragePluginUnresolvedException e)
        {
            context.put("errorMessage", i18nResolver.getText("plugin.license.storage.admin.plugin.unavailable"));
            context.put("displayLicenseAdminUi", Boolean.valueOf(false));
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

    private void redirectToLogin(HttpServletRequest req, HttpServletResponse resp)
        throws IOException
    {
        resp.sendRedirect(loginUriProvider.getLoginUri(URI.create(req.getRequestURL().toString())).toASCIIString());
    }

    private void handleUnpermittedUser(HttpServletRequest req, HttpServletResponse resp)
        throws IOException
    {
        resp.setContentType("text/html;charset=utf-8");
        Map context = new HashMap();
        context.put("errorMessage", i18nResolver.getText("plugin.license.storage.admin.unpermitted"));
        context.put("displayLicenseAdminUi", Boolean.valueOf(false));
        renderer.render("templates/license-admin.vm", context, resp.getWriter());
    }

    private static final String TEMPLATE = "templates/license-admin.vm";
    private final ThirdPartyPluginLicenseStorageManager licenseManager;
    private final AtlassianMarketplaceUriFactory uriFactory;
    private final ApplicationProperties applicationProperties;
    private final TemplateRenderer renderer;
    private final LoginUriProvider loginUriProvider;
    private final UserManager userManager;
    private final I18nResolver i18nResolver;
}
