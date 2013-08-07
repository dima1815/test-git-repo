// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   FeatureInputModel.java

package com.hindsighttesting.jira.behave.rest.model;


public class FeatureInputModel
{

    public FeatureInputModel()
    {
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String[] getTags()
    {
        return tags;
    }

    public void setTags(String tag[])
    {
        tags = tag;
    }

    public String getBackground()
    {
        return background;
    }

    public void setBackground(String background)
    {
        this.background = background;
    }

    private String name;
    private String description;
    private String tags[];
    private String background;
}
