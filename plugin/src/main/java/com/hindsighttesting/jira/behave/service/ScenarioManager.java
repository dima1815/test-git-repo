// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ScenarioManager.java

package com.hindsighttesting.jira.behave.service;

import com.atlassian.crowd.embedded.api.User;
import com.google.common.collect.Sets;
import com.hindsighttesting.jira.behave.activeobjects.dao.FeatureDao;
import com.hindsighttesting.jira.behave.activeobjects.dao.ScenarioDao;
import com.hindsighttesting.jira.behave.activeobjects.entities.*;
import com.hindsighttesting.jira.behave.service.exceptions.CucumberServiceException;
import com.hindsighttesting.jira.behave.service.exceptions.InvalidFeatureException;
import com.hindsighttesting.jira.behave.service.exceptions.InvalidRequestException;
import com.hindsighttesting.jira.behave.service.exceptions.InvalidScenarioException;
import java.util.*;

// Referenced classes of package com.hindsighttesting.jira.behave.service:
//            ScenarioModel, TagsManager, ScenarioStateCalculator, ModelFactory, 
//            IssueRelationService

public class ScenarioManager
{

    public ScenarioManager(ScenarioDao scenarioDao, FeatureDao featureDao, IssueRelationService relationManager, TagsManager tagsManager, ScenarioStateCalculator statusCalculator)
    {
        this.scenarioDao = scenarioDao;
        this.featureDao = featureDao;
        this.relationManager = relationManager;
        this.statusCalculator = statusCalculator;
        this.tagsManager = tagsManager;
    }

    public ScenarioModel addScenario(int feature, ScenarioModel scenarioModel)
        throws InvalidRequestException
    {
        if(scenarioModel.getName() == null || scenarioModel.getName().trim().isEmpty())
            throw new InvalidRequestException("name", "Name is a required property of a Scenario");
        FeatureEntity parent = getValidatedFeature(feature);
        ScenarioEntity scenarioEntity = scenarioDao.newScenario();
        scenarioEntity.setFeature(parent);
        scenarioEntity.setName(scenarioModel.getName());
        scenarioEntity.setSteps(scenarioModel.getSteps());
        scenarioEntity.setType(scenarioModel.isOutline().booleanValue() ? com.hindsighttesting.jira.behave.activeobjects.entities.ScenarioEntity.Type.OUTLINE : com.hindsighttesting.jira.behave.activeobjects.entities.ScenarioEntity.Type.SCENARIO);
        scenarioEntity.setTestMethod(scenarioModel.isManual().booleanValue() ? com.hindsighttesting.jira.behave.activeobjects.entities.ScenarioEntity.TestType.MANUAL : com.hindsighttesting.jira.behave.activeobjects.entities.ScenarioEntity.TestType.AUTOMATED);
        scenarioDao.saveScenario(scenarioEntity);
        if(scenarioModel.getTags() != null)
            tagsManager.tagScenario(scenarioEntity, scenarioModel.getTags());
        return ModelFactory.buildScenarioModel(scenarioEntity, statusCalculator.calculateStatus(scenarioEntity));
    }

    private FeatureEntity getValidatedFeature(int feature)
        throws InvalidRequestException
    {
        FeatureEntity parent = featureDao.getFeature(feature);
        if(parent == null)
            throw new InvalidRequestException("feature", (new StringBuilder()).append("Invalid Feature: ").append(feature).toString());
        else
            return parent;
    }

    public List getScenariosFor(int featureId)
    {
        List scenarios = scenarioDao.getScenariosFor(featureId);
        List results = getScenarios(scenarios);
        return results;
    }

    private List getScenarios(List scenarios)
    {
        List results = new ArrayList();
        ScenarioEntity scenario;
        for(Iterator i$ = scenarios.iterator(); i$.hasNext(); results.add(ModelFactory.buildScenarioModel(scenario, statusCalculator.calculateStatus(scenario))))
            scenario = (ScenarioEntity)i$.next();

        return results;
    }

    public ScenarioModel getScenario(int featureId, int scenarioId)
        throws InvalidRequestException, InvalidScenarioException
    {
        ScenarioEntity scenario = scenarioDao.getScenario(scenarioId);
        validateScenarioRequest(featureId, scenario);
        return ModelFactory.buildScenarioModel(scenario, statusCalculator.calculateStatus(scenario));
    }

    public ScenarioModel updateScenario(int featureId, int scenarioId, ScenarioModel scenarioModel)
        throws InvalidRequestException, InvalidScenarioException
    {
        ScenarioEntity scenario = scenarioDao.getScenario(scenarioId);
        validateScenarioRequest(featureId, scenario);
        if(scenarioModel.getName() != null)
            scenario.setName(scenarioModel.getName());
        if(scenarioModel.getSteps() != null)
            scenario.setSteps(scenarioModel.getSteps());
        if(scenarioModel.isOutline() != null)
            scenario.setType(scenarioModel.isOutline().booleanValue() ? com.hindsighttesting.jira.behave.activeobjects.entities.ScenarioEntity.Type.OUTLINE : com.hindsighttesting.jira.behave.activeobjects.entities.ScenarioEntity.Type.SCENARIO);
        if(scenarioModel.isManual() != null)
            scenario.setTestMethod(scenarioModel.isManual().booleanValue() ? com.hindsighttesting.jira.behave.activeobjects.entities.ScenarioEntity.TestType.MANUAL : com.hindsighttesting.jira.behave.activeobjects.entities.ScenarioEntity.TestType.AUTOMATED);
        if(scenarioModel.getTags() != null)
        {
            Set currentTags = Sets.newHashSet(getTagsAsStrings(scenario.getTags()));
            com.google.common.collect.Sets.SetView tagsToRemoved = Sets.difference(currentTags, scenarioModel.getTags());
            com.google.common.collect.Sets.SetView newTags = Sets.difference(scenarioModel.getTags(), currentTags);
            tagsManager.tagScenario(scenario, newTags);
            tagsManager.untagScenario(scenario, tagsToRemoved);
        }
        scenarioDao.saveScenario(scenario);
        return ModelFactory.buildScenarioModel(scenario, statusCalculator.calculateStatus(scenario));
    }

    private String[] getTagsAsStrings(TagEntity tags[])
    {
        if(tags != null)
        {
            String stringTags[] = new String[tags.length];
            for(int i = 0; i < tags.length; i++)
                stringTags[i] = tags[i].getName();

            return stringTags;
        } else
        {
            return new String[0];
        }
    }

    private void validateScenarioRequest(int featureId, ScenarioEntity scenario)
        throws InvalidScenarioException, InvalidRequestException
    {
        if(scenario == null)
            throw new InvalidScenarioException("scenario", "Scenario not found");
        if(scenario.getFeature().getID() != featureId)
            throw new InvalidRequestException("feature", "This scenario does not belong to the requested feature");
        else
            return;
    }

    public boolean delete(int featureId, int scenarioId, User user)
        throws CucumberServiceException
    {
        ScenarioEntity scenario = scenarioDao.getScenario(scenarioId);
        validateScenarioRequest(featureId, scenario);
        relationManager.removeAllIssuesFromScenario(scenarioId, user);
        if(scenario.getTags() != null)
        {
            String tags[] = getTagsAsStrings(scenario.getTags());
            tagsManager.untagScenario(scenario, Sets.newHashSet(tags));
        }
        return scenarioDao.delete(scenario);
    }

    public void moveScenario(int targetFeatureId, int parentFeatureId, int scenarioId)
        throws InvalidScenarioException, InvalidRequestException, InvalidFeatureException
    {
        ScenarioEntity scenario = scenarioDao.getScenario(scenarioId);
        validateScenarioRequest(parentFeatureId, scenario);
        FeatureEntity newParent = featureDao.getFeature(targetFeatureId);
        if(newParent == null)
        {
            throw new InvalidFeatureException("feature", "Requested feature has not been found");
        } else
        {
            scenario.setFeature(newParent);
            scenarioDao.saveScenario(scenario);
            return;
        }
    }

    private final ScenarioDao scenarioDao;
    private final FeatureDao featureDao;
    private final IssueRelationService relationManager;
    private final ScenarioStateCalculator statusCalculator;
    private final TagsManager tagsManager;
}
