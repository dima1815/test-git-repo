// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   FeatureManager.java

package com.hindsighttesting.jira.behave.service;

import com.atlassian.crowd.embedded.api.User;
import com.atlassian.jira.project.ProjectManager;
import com.google.common.collect.Sets;
import com.hindsighttesting.jira.behave.activeobjects.dao.FeatureDao;
import com.hindsighttesting.jira.behave.activeobjects.dao.FeatureStreamCallback;
import com.hindsighttesting.jira.behave.activeobjects.entities.*;
import com.hindsighttesting.jira.behave.service.exceptions.CucumberServiceException;
import com.hindsighttesting.jira.behave.service.exceptions.InvalidFeatureException;
import com.hindsighttesting.jira.behave.service.exceptions.InvalidRequestException;
import java.util.*;

// Referenced classes of package com.hindsighttesting.jira.behave.service:
//            FeatureModel, TagsManager, ModelFactory, ScenarioManager

public class FeatureManager
{

    public FeatureManager(FeatureDao featureDao, ScenarioManager scenarioManager, ProjectManager projectManager, TagsManager tagsManager)
    {
        this.featureDao = featureDao;
        this.projectManager = projectManager;
        this.scenarioManager = scenarioManager;
        this.tagsManager = tagsManager;
    }

    public FeatureModel addFeature(String projectKey, FeatureModel model)
        throws InvalidRequestException
    {
        String name = model.getName();
        String description = model.getDescription();
        if(name == null || "".equals(name.trim()))
            throw new InvalidRequestException("name", "Name is a required property of a feature");
        isValidJiraProject(projectKey);
        FeatureEntity feature = featureDao.newFeature();
        feature.setName(name);
        feature.setProject(projectKey);
        feature.setDescription(description);
        feature.setBackground(model.getBackground());
        featureDao.saveFeature(feature);
        if(model.getTags() != null)
            tagsManager.tagFeature(feature, model.getTags());
        return ModelFactory.buildFeatureModel(feature);
    }

    private void isValidJiraProject(String projectKey)
        throws InvalidRequestException
    {
        if(projectManager.getProjectObjByKey(projectKey) == null)
            throw new InvalidRequestException("project", (new StringBuilder()).append("JIRA Project with the '").append(projectKey).append("' not found").toString());
        else
            return;
    }

    public boolean projectHasFeatures(String projectKey)
        throws InvalidRequestException
    {
        isValidJiraProject(projectKey);
        int count = featureDao.getFeatureCount(projectKey);
        return count > 0;
    }

    public List getAllFeatures(String projectKey)
    {
        final List features = new ArrayList();
        if(projectManager.getProjectObjByKey(projectKey) != null)
            featureDao.getAllFeaturesAsPagedStream(projectKey, new FeatureStreamCallback() {

                public void readValue(FeatureEntity feature)
                {
                    features.add(ModelFactory.buildFeatureModel(feature));
                }

                final List val$features;
                final FeatureManager this$0;

            
            {
                this$0 = FeatureManager.this;
                features = list;
                super();
            }
            }
);
        return features;
    }

    public FeatureModel getFeature(String projectKey, int featureId)
        throws InvalidRequestException
    {
        isValidJiraProject(projectKey);
        FeatureEntity feature = featureDao.getFeature(featureId);
        return feature == null ? null : ModelFactory.buildFeatureModel(feature);
    }

    public FeatureModel updateFeature(String projectKey, int featureId, FeatureModel feature)
        throws InvalidFeatureException, InvalidRequestException
    {
        FeatureEntity entity = getValidatedFeature(featureId);
        isValidJiraProject(projectKey);
        if(feature.getName() != null)
            entity.setName(feature.getName());
        if(feature.getDescription() != null)
            entity.setDescription(feature.getDescription());
        if(feature.getBackground() != null)
            entity.setBackground(feature.getBackground());
        Set tags = feature.getTags();
        if(tags != null)
        {
            Set currentTags = Sets.newHashSet(getTagsAsStrings(entity.getTags()));
            com.google.common.collect.Sets.SetView tagsToRemoved = Sets.difference(currentTags, tags);
            com.google.common.collect.Sets.SetView newTags = Sets.difference(tags, currentTags);
            tagsManager.tagFeature(entity, newTags);
            tagsManager.untagFeature(entity, tagsToRemoved);
        }
        featureDao.saveFeature(entity);
        return ModelFactory.buildFeatureModel(entity);
    }

    private FeatureEntity getValidatedFeature(int featureId)
        throws InvalidFeatureException
    {
        FeatureEntity entity = featureDao.getFeature(featureId);
        if(entity == null)
            throw new InvalidFeatureException("feature", "Unknown feature id");
        else
            return entity;
    }

    public boolean delete(String projectKey, int featureId, User user)
        throws CucumberServiceException
    {
        FeatureEntity feature = getValidatedFeature(featureId);
        if(!projectKey.equalsIgnoreCase(feature.getProject()))
            throw new InvalidRequestException("project", "This feature does not belong to the requested project");
        ScenarioEntity arr$[] = feature.getScenarios();
        int len$ = arr$.length;
        for(int i$ = 0; i$ < len$; i$++)
        {
            ScenarioEntity scenario = arr$[i$];
            scenarioManager.delete(featureId, scenario.getID(), user);
        }

        if(feature.getTags() != null)
        {
            Set tags = Sets.newHashSet(getTagsAsStrings(feature.getTags()));
            tagsManager.untagFeature(feature, tags);
        }
        return featureDao.deleteFeature(feature);
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

    private final FeatureDao featureDao;
    private final ProjectManager projectManager;
    private final ScenarioManager scenarioManager;
    private final TagsManager tagsManager;
}
