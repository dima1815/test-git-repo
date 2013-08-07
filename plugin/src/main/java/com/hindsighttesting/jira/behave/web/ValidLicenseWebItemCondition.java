// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ValidLicenseWebItemCondition.java

package com.hindsighttesting.jira.behave.web;

import com.atlassian.plugin.PluginParseException;
import com.atlassian.plugin.web.Condition;
import com.atlassian.upm.api.license.entity.PluginLicense;
import com.atlassian.upm.api.util.Option;
import com.atlassian.upm.license.storage.lib.PluginLicenseStoragePluginUnresolvedException;
import com.atlassian.upm.license.storage.lib.ThirdPartyPluginLicenseStorageManager;
import java.util.Map;

public class ValidLicenseWebItemCondition
    implements Condition
{

    public ValidLicenseWebItemCondition(ThirdPartyPluginLicenseStorageManager licenseManager)
    {
        this.licenseManager = licenseManager;
    }

    public void init(Map map)
        throws PluginParseException
    {
    }

    public boolean shouldDisplay(Map context)
    {
        try
        {
            if(licenseManager.getLicense().isDefined())
            {
                PluginLicense pluginLicense = (PluginLicense)licenseManager.getLicense().get();
                if(!pluginLicense.getError().isDefined())
                    return true;
            }
        }
        catch(PluginLicenseStoragePluginUnresolvedException e) { }
        return false;
    }

    private final ThirdPartyPluginLicenseStorageManager licenseManager;
}
