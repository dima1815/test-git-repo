// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   FeatureModel.java

package com.hindsighttesting.jira.behave.service;

import java.util.Set;

public class FeatureModel
{

    public FeatureModel()
    {
    }

    public FeatureModel(String name)
    {
        this.name = name;
    }

    public FeatureModel(int id, String name)
    {
        this.id = id;
        this.name = name;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
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

    public Set getTags()
    {
        return tags;
    }

    public void setTags(Set tags)
    {
        this.tags = tags;
    }

    public String getBackground()
    {
        return background;
    }

    public void setBackground(String background)
    {
        this.background = background;
    }

    private int id;
    private String name;
    private String description;
    private Set tags;
    private String background;
}
