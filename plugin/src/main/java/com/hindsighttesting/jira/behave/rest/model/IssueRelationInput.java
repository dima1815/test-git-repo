// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   IssueRelationInput.java

package com.hindsighttesting.jira.behave.rest.model;


public class IssueRelationInput
{

    public IssueRelationInput()
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
