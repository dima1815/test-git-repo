// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   IssueScenarioInputModel.java

package com.hindsighttesting.jira.behave.rest.model;


public class IssueScenarioInputModel
{

    public IssueScenarioInputModel()
    {
    }

    public int getScenarioId()
    {
        return scenarioId;
    }

    public void setScenarioId(int scenarioId)
    {
        this.scenarioId = scenarioId;
    }

    private int scenarioId;
}
