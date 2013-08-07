// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SystemInfoResource.java

package com.hindsighttesting.jira.behave.rest;

import com.atlassian.sal.api.ApplicationProperties;
import com.atlassian.upm.api.license.entity.LicenseType;
import com.atlassian.upm.api.license.entity.PluginLicense;
import com.atlassian.upm.api.util.Option;
import com.atlassian.upm.license.storage.lib.PluginLicenseStoragePluginUnresolvedException;
import com.atlassian.upm.license.storage.lib.ThirdPartyPluginLicenseStorageManager;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.core.Response;

public class SystemInfoResource
{

    public SystemInfoResource(ThirdPartyPluginLicenseStorageManager licenseManager, ApplicationProperties applicationProperties)
    {
        this.licenseManager = licenseManager;
        this.applicationProperties = applicationProperties;
    }

    public Response getFeaturesForProject()
    {
        Map values = new HashMap();
        try
        {
            PluginLicense license = (PluginLicense)licenseManager.getLicense().get();
            values.put("licenseEdition", license.getEdition().toString());
            values.put("licenseType", license.getLicenseType().name());
        }
        catch(PluginLicenseStoragePluginUnresolvedException e) { }
        values.put("platformVersion", applicationProperties.getVersion());
        values.put("behaveVersion", applicationProperties.getVersion());
        return Response.ok(values).build();
    }

    private final ThirdPartyPluginLicenseStorageManager licenseManager;
    private final ApplicationProperties applicationProperties;
}
