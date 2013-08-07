// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ProjectValue.java

package com.hindsighttesting.jira.behave.rest.model;


public class ProjectValue
{

    public ProjectValue(String key, String name)
    {
        this.key = key;
        this.name = name;
    }

    public ProjectValue()
    {
    }

    public String getKey()
    {
        return key;
    }

    public String getName()
    {
        return name;
    }

    public void setKey(String key)
    {
        this.key = key;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public boolean equals(Object o)
    {
        if(this == o)
            return true;
        if(o == null || getClass() != o.getClass())
            return false;
        ProjectValue that = (ProjectValue)o;
        return key.equals(that.key);
    }

    public int hashCode()
    {
        return key.hashCode();
    }

    private String key;
    private String name;
}
