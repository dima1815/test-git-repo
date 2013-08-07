// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   HindsightRestResource.java

package com.hindsighttesting.jira.behave.rest;

import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.sal.api.ApplicationProperties;
import com.hindsighttesting.jira.behave.rest.model.ErrorsResponeModel;
import com.hindsighttesting.jira.behave.rest.model.FeatureResponeModel;
import com.hindsighttesting.jira.behave.rest.model.ScenarioResponseModel;
import com.hindsighttesting.jira.behave.service.FeatureModel;
import com.hindsighttesting.jira.behave.service.ScenarioModel;
import com.hindsighttesting.jira.behave.service.exceptions.*;
import java.util.ArrayList;
import java.util.Set;
import javax.ws.rs.core.Response;

// Referenced classes of package com.hindsighttesting.jira.behave.rest:
//            RestUrlBuilder

public class HindsightRestResource
{

    public HindsightRestResource(ApplicationProperties applicationProperties, JiraAuthenticationContext authenticationContext)
    {
        this.applicationProperties = applicationProperties;
        this.authenticationContext = authenticationContext;
        restUrlBuilder = new RestUrlBuilder(applicationProperties);
    }

    protected ScenarioResponseModel createScenarioReponse(ScenarioModel model, String link)
    {
        ScenarioResponseModel response = new ScenarioResponseModel();
        response.setId(model.getId());
        response.setName(model.getName());
        response.setSteps(model.getSteps());
        response.setOutline(model.isOutline().booleanValue());
        response.setManual(model.isManual().booleanValue());
        response.setIssues(new ArrayList(model.getIssues()));
        response.addRestLink("self", link);
        response.addRestLink("view", restUrlBuilder.scenario(model));
        response.setStatus(model.getStatus());
        Set tags = model.getTags();
        if(tags != null)
            response.setTags((String[])tags.toArray(new String[tags.size()]));
        return response;
    }

    protected FeatureResponeModel createFeatureResponse(String projectKey, FeatureModel model)
    {
        FeatureResponeModel featureEntity = new FeatureResponeModel();
        featureEntity.setId(model.getId());
        featureEntity.setName(model.getName());
        featureEntity.setDescription(model.getDescription());
        featureEntity.addRestLink("self", restUrlBuilder.feature(projectKey, model));
        Set tags = model.getTags();
        if(tags != null)
            featureEntity.setTags((String[])tags.toArray(new String[tags.size()]));
        featureEntity.setBackground(model.getBackground());
        return featureEntity;
    }

    protected Response exceptionHandler(CucumberServiceException e)
    {
        Response.ResponseBuilder response = Response.serverError();
        if(e instanceof InvalidPermissionException)
            response = Response.status(403);
        else
        if((e instanceof InvalidFeatureException) || (e instanceof InvalidScenarioException))
            response = Response.status(404);
        else
        if(e instanceof InvalidRequestException)
            response = Response.status(400);
        return response.entity(new ErrorsResponeModel(e)).build();
    }

    protected final ApplicationProperties applicationProperties;
    protected final JiraAuthenticationContext authenticationContext;
    protected final RestUrlBuilder restUrlBuilder;
}
