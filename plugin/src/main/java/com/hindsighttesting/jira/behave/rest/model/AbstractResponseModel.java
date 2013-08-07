// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   AbstractResponseModel.java

package com.hindsighttesting.jira.behave.rest.model;

import com.hindsighttesting.jira.behave.rest.RestEntityLink;
import java.util.HashMap;
import java.util.Map;

public class AbstractResponseModel
{

    public AbstractResponseModel()
    {
        links = new HashMap();
    }

    public Map getLinks()
    {
        return links;
    }

    public void setLinks(Map links)
    {
        this.links = links;
    }

    public void addRestLink(String name, String href)
    {
        links.put(name, new RestEntityLink(href));
    }

    private Map links;
}
