// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   RestEntityLink.java

package com.hindsighttesting.jira.behave.rest;


public class RestEntityLink
{

    public RestEntityLink()
    {
    }

    public RestEntityLink(String href)
    {
        this.href = href;
    }

    public String getHref()
    {
        return href;
    }

    public void setHref(String href)
    {
        this.href = href;
    }

    private String href;
}
