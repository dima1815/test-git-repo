// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   RestUrlBuilder.java

package com.hindsighttesting.jira.behave.rest;

import com.atlassian.sal.api.ApplicationProperties;
import com.hindsighttesting.jira.behave.service.FeatureModel;
import com.hindsighttesting.jira.behave.service.ScenarioModel;

public class RestUrlBuilder
{

    public RestUrlBuilder(ApplicationProperties applicationProperties)
    {
        this.applicationProperties = applicationProperties;
    }

    public String featureSet(String projectKey)
    {
        return (new StringBuilder()).append(applicationProperties.getBaseUrl()).append("/rest/cucumber/1.0/project/").append(projectKey).append("/features").toString();
    }

    public String scenario(ScenarioModel model)
    {
        return (new StringBuilder()).append(applicationProperties.getBaseUrl()).append("/rest/cucumber/1.0/project/").append(model.getProjectKey()).append("/feature/").append(model.getFeatureId()).append("/scenario/").append(model.getId()).toString();
    }

    public String issueScenarioLink(String issueKey, ScenarioModel model)
    {
        return (new StringBuilder()).append(applicationProperties.getBaseUrl()).append("/rest/cucumber/1.0/issue/").append(issueKey).append("/scenario/").append(model.getId()).toString();
    }

    public String feature(String projectKey, FeatureModel model)
    {
        return (new StringBuilder()).append(applicationProperties.getBaseUrl()).append("/rest/cucumber/1.0/project/").append(projectKey).append("/feature/").append(model.getId()).toString();
    }

    private final ApplicationProperties applicationProperties;
}
