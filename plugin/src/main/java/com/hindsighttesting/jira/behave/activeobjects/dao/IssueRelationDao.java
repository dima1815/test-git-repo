// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   IssueRelationDao.java

package com.hindsighttesting.jira.behave.activeobjects.dao;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.hindsighttesting.jira.behave.activeobjects.entities.*;
import net.java.ao.DBParam;
import net.java.ao.RawEntity;

public class IssueRelationDao
{

    public IssueRelationDao(ActiveObjects ao)
    {
        entityManager = ao;
    }

    public IssueEntity createIssue(String issueKey)
    {
        return (IssueEntity)entityManager.create(com/hindsighttesting/jira/behave/activeobjects/entities/IssueEntity, new DBParam[] {
            new DBParam("ISSUE_KEY", issueKey)
        });
    }

    public void saveIssue(IssueEntity issue)
    {
        issue.save();
    }

    public void addRelationship(IssueEntity issue, ScenarioEntity scenario)
    {
        IssueToScenarioEntity relation = (IssueToScenarioEntity)entityManager.create(com/hindsighttesting/jira/behave/activeobjects/entities/IssueToScenarioEntity, new DBParam[0]);
        relation.setIssue(issue);
        relation.setScenario(scenario);
        relation.save();
    }

    public IssueEntity getIssue(String key)
    {
        IssueEntity results[] = (IssueEntity[])entityManager.find(com/hindsighttesting/jira/behave/activeobjects/entities/IssueEntity, "ISSUE_KEY = ?", new Object[] {
            key.toUpperCase()
        });
        return results.length != 0 ? results[0] : null;
    }

    public void deleteRelationship(IssueEntity issue, ScenarioEntity scenario)
    {
        IssueToScenarioEntity results[] = (IssueToScenarioEntity[])entityManager.find(com/hindsighttesting/jira/behave/activeobjects/entities/IssueToScenarioEntity, "ISSUE_ID = ? and SCENARIO_ID = ?", new Object[] {
            issue, scenario
        });
        if(results.length == 1)
            entityManager.delete(new RawEntity[] {
                results[0]
            });
    }

    public void deleteIssue(IssueEntity issue)
    {
        entityManager.delete(new RawEntity[] {
            issue
        });
    }

    public boolean hasIssueRelation(int issueId, int scenarioId)
    {
        return entityManager.count(com/hindsighttesting/jira/behave/activeobjects/entities/IssueToScenarioEntity, "ISSUE_ID = ? and SCENARIO_ID = ?", new Object[] {
            Integer.valueOf(issueId), Integer.valueOf(scenarioId)
        }) > 0;
    }

    public IssueToScenarioEntity[] getAllIssuesForScenario(int scenarioId)
    {
        return (IssueToScenarioEntity[])entityManager.find(com/hindsighttesting/jira/behave/activeobjects/entities/IssueToScenarioEntity, "SCENARIO_ID = ?", new Object[] {
            Integer.valueOf(scenarioId)
        });
    }

    private final ActiveObjects entityManager;
}
