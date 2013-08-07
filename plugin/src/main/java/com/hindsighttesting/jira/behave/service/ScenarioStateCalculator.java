// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ScenarioStateCalculator.java

package com.hindsighttesting.jira.behave.service;

import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.issue.status.Status;
import com.hindsighttesting.jira.behave.activeobjects.entities.IssueEntity;
import com.hindsighttesting.jira.behave.activeobjects.entities.ScenarioEntity;

// Referenced classes of package com.hindsighttesting.jira.behave.service:
//            ScenarioModel

public class ScenarioStateCalculator
{

    public ScenarioStateCalculator(IssueManager issueManager)
    {
        this.issueManager = issueManager;
    }

    public ScenarioModel.ImplementationState calculateStatus(ScenarioEntity scenarioEntity)
    {
        ScenarioModel.ImplementationState result = ScenarioModel.ImplementationState.ORPHAN;
        IssueEntity arr$[] = scenarioEntity.getIssues();
        int len$ = arr$.length;
        for(int i$ = 0; i$ < len$; i$++)
        {
            IssueEntity issue = arr$[i$];
            MutableIssue issueObject = issueManager.getIssueObject(issue.getIssueKey().toUpperCase());
            if(issueObject == null)
                continue;
            ScenarioModel.ImplementationState status = interpretStatus(issueObject.getStatusObject());
            if(status.ordinal() > result.ordinal())
                result = status;
        }

        return result;
    }

    private ScenarioModel.ImplementationState interpretStatus(Status statusObject)
    {
        String statusName = statusObject.getName();
        ScenarioModel.ImplementationState result = ScenarioModel.ImplementationState.OPEN;
        if("Closed".equalsIgnoreCase(statusName) || "Done".equalsIgnoreCase(statusName))
            result = ScenarioModel.ImplementationState.COMPLETED;
        else
        if("In Progress".equalsIgnoreCase(statusName) || "Ready for Integration".equalsIgnoreCase(statusName))
            result = ScenarioModel.ImplementationState.WIP;
        return result;
    }

    private final IssueManager issueManager;
}
