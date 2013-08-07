// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   IssueRelationService.java

package com.hindsighttesting.jira.behave.service;

import com.atlassian.crowd.embedded.api.User;
import com.hindsighttesting.jira.behave.service.exceptions.CucumberServiceException;
import java.util.List;

public interface IssueRelationService
{

    public abstract List getScenarios(String s, User user);

    public abstract void addScenarioToIssue(String s, int i, User user)
        throws CucumberServiceException;

    public abstract void removeScenarioFromIssue(String s, int i, User user)
        throws CucumberServiceException;

    public abstract void deleteIssue(String s);

    public abstract void removeAllIssuesFromScenario(int i, User user);
}
