// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   IssueRelationStore.java

package com.hindsighttesting.jira.behave.rest;

import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.sal.api.ApplicationProperties;
import com.hindsighttesting.jira.behave.rest.model.IssueScenarioInputModel;
import com.hindsighttesting.jira.behave.rest.model.ScenarioCollection;
import com.hindsighttesting.jira.behave.service.IssueRelationService;
import com.hindsighttesting.jira.behave.service.ScenarioModel;
import com.hindsighttesting.jira.behave.service.exceptions.CucumberServiceException;
import java.util.*;
import javax.ws.rs.core.Response;

// Referenced classes of package com.hindsighttesting.jira.behave.rest:
//            HindsightRestResource, RestEntityLink, RestUrlBuilder

public class IssueRelationStore extends HindsightRestResource
{

    public IssueRelationStore(IssueRelationService relationService, ApplicationProperties applicationProperties, JiraAuthenticationContext authenticationContext)
    {
        super(applicationProperties, authenticationContext);
        this.relationService = relationService;
    }

    public Response getScenariosForIssue(String issueKey)
    {
        ScenarioCollection scenarios = new ScenarioCollection();
        List scenarioList = new ArrayList();
        ScenarioModel related;
        for(Iterator i$ = relationService.getScenarios(issueKey, authenticationContext.getLoggedInUser()).iterator(); i$.hasNext(); scenarioList.add(createScenarioReponse(related, restUrlBuilder.issueScenarioLink(issueKey, related))))
            related = (ScenarioModel)i$.next();

        scenarios.setScenarios(scenarioList);
        scenarios.getLinks().put("self", new RestEntityLink((new StringBuilder()).append(applicationProperties.getBaseUrl()).append("/rest/cucumber/1.0/issue/").append(issueKey).append("/scenarios").toString()));
        return Response.ok(scenarios).build();
    }

    public Response addScenarioToIssue(String issueKey, IssueScenarioInputModel scenario)
    {
        try
        {
            relationService.addScenarioToIssue(issueKey, scenario.getScenarioId(), authenticationContext.getLoggedInUser());
        }
        catch(CucumberServiceException e)
        {
            return exceptionHandler(e);
        }
        return Response.ok().build();
    }

    public Response deleteIssueScenario(String issueKey, int scenarioId)
    {
        try
        {
            relationService.removeScenarioFromIssue(issueKey, scenarioId, authenticationContext.getLoggedInUser());
        }
        catch(CucumberServiceException e)
        {
            return exceptionHandler(e);
        }
        return Response.ok().build();
    }

    private final IssueRelationService relationService;
}
