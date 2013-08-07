// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   LicenseManagerOutdatedCondition.java

package com.hindsighttesting.jira.behave.web;

import com.atlassian.plugin.PluginParseException;
import com.atlassian.plugin.web.Condition;
import com.atlassian.upm.license.storage.lib.ThirdPartyPluginLicenseStorageManager;
import java.util.Map;

public class LicenseManagerOutdatedCondition
    implements Condition
{

    public LicenseManagerOutdatedCondition(ThirdPartyPluginLicenseStorageManager licenseManager)
    {
        this.licenseManager = licenseManager;
    }

    public void init(Map map)
        throws PluginParseException
    {
    }

    public boolean shouldDisplay(Map context)
    {
        return !licenseManager.isUpmLicensingAware();
    }

    private final ThirdPartyPluginLicenseStorageManager licenseManager;
}
