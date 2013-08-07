// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   FeatureScenarioStore.java

package com.hindsighttesting.jira.behave.rest;

import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.sal.api.ApplicationProperties;
import com.hindsighttesting.jira.behave.rest.model.FeatureCollection;
import com.hindsighttesting.jira.behave.rest.model.FeatureInputModel;
import com.hindsighttesting.jira.behave.rest.model.ScenarioCollection;
import com.hindsighttesting.jira.behave.rest.model.ScenarioInputModel;
import com.hindsighttesting.jira.behave.service.*;
import com.hindsighttesting.jira.behave.service.exceptions.CucumberServiceException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.zip.ZipOutputStream;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import org.apache.log4j.Logger;

// Referenced classes of package com.hindsighttesting.jira.behave.rest:
//            HindsightRestResource, RestEntityLink, RestUrlBuilder

public class FeatureScenarioStore extends HindsightRestResource
{

    public FeatureScenarioStore(ApplicationProperties applicationProperties, FeatureManager featureManager, ScenarioManager scenarioService, TagsManager tagsManager, JiraAuthenticationContext authenticationContext, ExportService exportService)
    {
        super(applicationProperties, authenticationContext);
        featureService = featureManager;
        this.scenarioService = scenarioService;
        this.tagsManager = tagsManager;
        this.exportService = exportService;
    }

    public Response getFeaturesForProject(String projectKey)
    {
        FeatureCollection features = new FeatureCollection();
        List featureList = new ArrayList();
        FeatureModel featureModel;
        for(Iterator i$ = featureService.getAllFeatures(projectKey).iterator(); i$.hasNext(); featureList.add(createFeatureResponse(projectKey, featureModel)))
            featureModel = (FeatureModel)i$.next();

        features.setFeatures(featureList);
        features.getLinks().put("self", new RestEntityLink((new StringBuilder()).append(applicationProperties.getBaseUrl()).append("/rest/cucumber/1.0/project/").append(projectKey).append("/features").toString()));
        return Response.ok(features).build();
    }

    public Response createFeatureForProject(String projectKey, FeatureInputModel feature)
        throws URISyntaxException
    {
        try
        {
            FeatureModel input = ModelFactory.buildFeatureModel(null, feature);
            FeatureModel created = featureService.addFeature(projectKey, input);
            return Response.created(new URI((new StringBuilder()).append(applicationProperties.getBaseUrl()).append("/rest/cucumber/1.0/project/").append(projectKey).append("/feature/").append(created.getId()).toString())).build();
        }
        catch(CucumberServiceException e)
        {
            return exceptionHandler(e);
        }
    }

    public Response getFeature(String projectKey, int featureId)
        throws URISyntaxException
    {
        Response response;
        try
        {
            FeatureModel feature = featureService.getFeature(projectKey, featureId);
            response = feature == null ? Response.status(404).build() : Response.ok(createFeatureResponse(projectKey, feature)).build();
        }
        catch(CucumberServiceException e)
        {
            response = exceptionHandler(e);
        }
        return response;
    }

    public Response updateFeature(String projectKey, int featureId, FeatureInputModel feature)
        throws URISyntaxException
    {
        Response response;
        try
        {
            FeatureModel model = featureService.updateFeature(projectKey, featureId, ModelFactory.buildFeatureModel(Integer.valueOf(featureId), feature));
            response = Response.ok(createFeatureResponse(projectKey, model)).build();
        }
        catch(CucumberServiceException e)
        {
            response = exceptionHandler(e);
        }
        return response;
    }

    public Response deleteFeature(String projectKey, int featureId)
        throws URISyntaxException
    {
        Response response;
        try
        {
            boolean isDeleted = featureService.delete(projectKey, featureId, authenticationContext.getLoggedInUser());
            response = Response.status(isDeleted ? 204 : 503).build();
        }
        catch(CucumberServiceException e)
        {
            response = exceptionHandler(e);
        }
        return response;
    }

    public Response getScenarios(String projectKey, int featureId)
    {
        ScenarioCollection scenarios = new ScenarioCollection();
        List scenarioList = new ArrayList();
        ScenarioModel model;
        for(Iterator i$ = scenarioService.getScenariosFor(featureId).iterator(); i$.hasNext(); scenarioList.add(createScenarioReponse(model, restUrlBuilder.scenario(model))))
            model = (ScenarioModel)i$.next();

        scenarios.setScenarios(scenarioList);
        scenarios.addRestLink("self", (new StringBuilder()).append(applicationProperties.getBaseUrl()).append("/rest/cucumber/1.0/project/").append(projectKey).append("/feature/").append(featureId).append("/scenarios").toString());
        return Response.ok(scenarios).build();
    }

    public Response createScenarioForFeature(String projectKey, int featureId, ScenarioInputModel scenario)
        throws URISyntaxException
    {
        try
        {
            if(scenario.isManual() == null)
                scenario.setManual(Boolean.valueOf(false));
            ScenarioModel scenarioModel = ModelFactory.buildScenarioModel(scenario);
            ScenarioModel created = scenarioService.addScenario(featureId, scenarioModel);
            return Response.created(new URI((new StringBuilder()).append(applicationProperties.getBaseUrl()).append("/rest/cucumber/1.0/project/").append(projectKey).append("/feature/").append(featureId).append("/scenario/").append(created.getId()).toString())).build();
        }
        catch(CucumberServiceException e)
        {
            return exceptionHandler(e);
        }
    }

    public Response getScenario(String projectKey, int featureId, int scenarioId)
        throws URISyntaxException
    {
        Response response;
        try
        {
            ScenarioModel scenario = scenarioService.getScenario(featureId, scenarioId);
            response = scenario == null ? Response.status(404).build() : Response.ok(createScenarioReponse(scenario, restUrlBuilder.scenario(scenario))).build();
        }
        catch(CucumberServiceException e)
        {
            response = exceptionHandler(e);
        }
        return response;
    }

    public Response updateScenario(String projectKey, int featureId, int scenarioId, ScenarioInputModel scenario)
        throws URISyntaxException
    {
        Response response;
        try
        {
            ScenarioModel model = scenarioService.updateScenario(featureId, scenarioId, ModelFactory.buildScenarioModel(scenario));
            response = Response.ok(createScenarioReponse(model, restUrlBuilder.scenario(model))).build();
        }
        catch(CucumberServiceException e)
        {
            response = exceptionHandler(e);
        }
        return response;
    }

    public Response deleteScenario(int featureId, int scenarioId)
        throws URISyntaxException
    {
        Response response;
        try
        {
            boolean isDeleted = scenarioService.delete(featureId, scenarioId, authenticationContext.getLoggedInUser());
            response = Response.status(isDeleted ? 204 : 503).build();
        }
        catch(CucumberServiceException e)
        {
            response = exceptionHandler(e);
        }
        return response;
    }

    public Response moveScenario(String projectKey, int featureId, int currentFeatureId, int scenarioId)
        throws URISyntaxException
    {
        Response response;
        try
        {
            scenarioService.moveScenario(featureId, currentFeatureId, scenarioId);
            response = Response.ok().build();
        }
        catch(CucumberServiceException e)
        {
            response = exceptionHandler(e);
        }
        return response;
    }

    public Response getTags(String projectKey, String query)
        throws URISyntaxException
    {
        Response response;
        if(query != null)
        {
            Set tags = tagsManager.searchForTag(projectKey, query);
            Map result = new HashMap();
            result.put("tags", ((Object) (tags.toArray())));
            response = Response.ok(result).build();
        } else
        {
            Set tags = tagsManager.getTags(projectKey);
            Map result = new HashMap();
            result.put("tags", ((Object) (tags.toArray())));
            response = Response.ok(result).build();
        }
        return response;
    }

    public Response getFeaturesAsZip(final String projectKey, final boolean includeManual)
    {
        try
        {
            if(!featureService.projectHasFeatures(projectKey))
                return Response.status(404).entity("No features in this project").build();
        }
        catch(CucumberServiceException e)
        {
            return exceptionHandler(e);
        }
        StreamingOutput output = new StreamingOutput() {

            public void write(OutputStream output)
                throws IOException, WebApplicationException
            {
                ZipOutputStream zipOutput = new ZipOutputStream(output);
                try
                {
                    exportService.getFeatures(projectKey, zipOutput, includeManual);
                }
                catch(Exception e)
                {
                    FeatureScenarioStore.LOG.error("Error while exporting features files", e);
                }
                zipOutput.close();
            }

            final String val$projectKey;
            final boolean val$includeManual;
            final FeatureScenarioStore this$0;

            
            {
                this$0 = FeatureScenarioStore.this;
                projectKey = s;
                includeManual = flag;
                super();
            }
        }
;
        return Response.ok(output).header("Content-Disposition", (new StringBuilder()).append("attachment; filename=").append(projectKey).append(".zip").toString()).build();
    }

    private static final Logger LOG = Logger.getLogger(com/hindsighttesting/jira/behave/rest/FeatureScenarioStore);
    private static final String SCENARIO_ID_PARAM = "scenarioId";
    private static final String FEATURE_ID_PARAM = "featureId";
    private static final String PROJECT_PATH_PARAM = "prjkey";
    private static final int NOT_FOUND = 404;
    private final FeatureManager featureService;
    private final ScenarioManager scenarioService;
    private final TagsManager tagsManager;
    private final ExportService exportService;



}
