// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ScenarioInputModel.java

package com.hindsighttesting.jira.behave.rest.model;


public class ScenarioInputModel
{

    public ScenarioInputModel()
    {
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

    public String getSteps()
    {
        return steps;
    }

    public void setSteps(String steps)
    {
        this.steps = steps;
    }

    public boolean isOutline()
    {
        return outline;
    }

    public void setOutline(boolean outline)
    {
        this.outline = outline;
    }

    public Boolean isManual()
    {
        return manual;
    }

    public void setManual(Boolean manual)
    {
        this.manual = manual;
    }

    public String[] getTags()
    {
        return tags;
    }

    public void setTags(String tags[])
    {
        this.tags = tags;
    }

    private int id;
    private String name;
    private String steps;
    private boolean outline;
    private Boolean manual;
    private String tags[];
}
