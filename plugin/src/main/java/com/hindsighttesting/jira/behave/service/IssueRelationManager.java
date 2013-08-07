// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   IssueRelationManager.java

package com.hindsighttesting.jira.behave.service;

import com.atlassian.crowd.embedded.api.User;
import com.atlassian.jira.bc.issue.IssueService;
import com.atlassian.jira.util.ErrorCollection;
import com.hindsighttesting.jira.behave.activeobjects.dao.IssueRelationDao;
import com.hindsighttesting.jira.behave.activeobjects.dao.ScenarioDao;
import com.hindsighttesting.jira.behave.activeobjects.entities.*;
import com.hindsighttesting.jira.behave.service.exceptions.CucumberServiceException;
import com.hindsighttesting.jira.behave.service.exceptions.InvalidPermissionException;
import com.hindsighttesting.jira.behave.service.exceptions.InvalidRequestException;
import java.util.*;

// Referenced classes of package com.hindsighttesting.jira.behave.service:
//            IssueRelationService, ModelFactory

public class IssueRelationManager
    implements IssueRelationService
{

    public IssueRelationManager(IssueRelationDao relationDao, ScenarioDao scenarioDao, IssueService issueService)
    {
        this.relationDao = relationDao;
        this.scenarioDao = scenarioDao;
        this.issueService = issueService;
    }

    public List getScenarios(String issueKey, User user)
    {
        List scenarios = new ArrayList();
        IssueEntity issue = relationDao.getIssue(issueKey);
        if(issue != null)
        {
            ScenarioEntity arr$[] = issue.getScenarios();
            int len$ = arr$.length;
            for(int i$ = 0; i$ < len$; i$++)
            {
                ScenarioEntity scenario = arr$[i$];
                scenarios.add(ModelFactory.buildScenarioModel(scenario));
            }

        }
        return scenarios;
    }

    public void addScenarioToIssue(String issueKey, int scenarioId, User user)
        throws CucumberServiceException
    {
        ScenarioEntity scenario = getValidatedScenario(scenarioId);
        validatedJiraIssueKey(issueKey, user, true);
        IssueEntity issue = relationDao.getIssue(issueKey);
        if(issue == null)
        {
            issue = relationDao.createIssue(issueKey.toUpperCase());
            relationDao.addRelationship(issue, scenario);
        } else
        if(!relationDao.hasIssueRelation(issue.getID(), scenarioId))
            relationDao.addRelationship(issue, scenario);
    }

    private ScenarioEntity getValidatedScenario(int scenarioId)
        throws InvalidRequestException
    {
        ScenarioEntity scenario = scenarioDao.getScenario(scenarioId);
        if(scenario == null)
            throw new InvalidRequestException("scenario", String.format("Unknown scenario with ID of {0}", new Object[] {
                Integer.valueOf(scenarioId)
            }));
        else
            return scenario;
    }

    private void validatedJiraIssueKey(String issueKey, User user, boolean writePermission)
        throws CucumberServiceException
    {
        com.atlassian.jira.bc.issue.IssueService.IssueResult issue = issueService.getIssue(user, issueKey);
        if(!issue.isValid())
        {
            Iterator i$ = issue.getErrorCollection().getErrorMessages().iterator();
            if(i$.hasNext())
            {
                String errorMessage = (String)i$.next();
                throw new InvalidRequestException("issueKey", errorMessage);
            } else
            {
                throw new InvalidRequestException("issueKey", "Not a valid JIRA Issue key");
            }
        }
        if(writePermission && !issueService.isEditable(issue.getIssue(), user))
            throw new InvalidPermissionException(String.format("You do not have permission to edit Issue {0}", new Object[] {
                issueKey
            }));
        else
            return;
    }

    public void removeScenarioFromIssue(String issueKey, int scenarioId, User user)
        throws CucumberServiceException
    {
        ScenarioEntity scenario = getValidatedScenario(scenarioId);
        validatedJiraIssueKey(issueKey, user, true);
        IssueEntity issue = relationDao.getIssue(issueKey);
        relationDao.deleteRelationship(issue, scenario);
    }

    public void deleteIssue(String key)
    {
        IssueEntity issue = relationDao.getIssue(key);
        if(issue != null)
        {
            ScenarioEntity arr$[] = issue.getScenarios();
            int len$ = arr$.length;
            for(int i$ = 0; i$ < len$; i$++)
            {
                ScenarioEntity scenario = arr$[i$];
                relationDao.deleteRelationship(issue, scenario);
            }

        }
        relationDao.deleteIssue(issue);
    }

    public void removeAllIssuesFromScenario(int scenarioId, User user)
    {
        IssueToScenarioEntity arr$[] = relationDao.getAllIssuesForScenario(scenarioId);
        int len$ = arr$.length;
        for(int i$ = 0; i$ < len$; i$++)
        {
            IssueToScenarioEntity issueRel = arr$[i$];
            relationDao.deleteRelationship(issueRel.getIssue(), issueRel.getScenario());
        }

    }

    private final IssueRelationDao relationDao;
    private final ScenarioDao scenarioDao;
    private final IssueService issueService;
}
