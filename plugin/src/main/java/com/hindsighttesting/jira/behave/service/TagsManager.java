// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)
// Source File Name: TagsManager.java

package com.hindsighttesting.jira.behave.service;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.hindsighttesting.jira.behave.activeobjects.dao.ProjectDao;
import com.hindsighttesting.jira.behave.activeobjects.dao.TagsDao;
import com.hindsighttesting.jira.behave.activeobjects.entities.FeatureEntity;
import com.hindsighttesting.jira.behave.activeobjects.entities.FeatureTagEntity;
import com.hindsighttesting.jira.behave.activeobjects.entities.ProjectEntity;
import com.hindsighttesting.jira.behave.activeobjects.entities.ScenarioEntity;
import com.hindsighttesting.jira.behave.activeobjects.entities.ScenarioTagEntity;
import com.hindsighttesting.jira.behave.activeobjects.entities.TagEntity;

public class TagsManager
{

    public TagsManager(TagsDao tagsDao, ProjectDao projectDao)
    {
        this.tagsDao = tagsDao;
        this.projectDao = projectDao;
    }

    public void tagScenario(ScenarioEntity scenario, Set tags)
    {
        String projectKey = scenario.getFeature().getProject();
        ScenarioTagEntity tagLink;
        for (Iterator i$ = tags.iterator(); i$.hasNext(); tagsDao.saveTaggedScenario(tagLink))
        {
            String tagName = (String) i$.next();
            TagEntity tagEntity = getOrCreateTag(projectKey, tagName);
            tagLink = tagsDao.newTaggedScenario();
            // tagLink.setScenario(scenario);
            // tagLink.setTag(tagEntity);
        }

    }

    private TagEntity getOrCreateTag(String projectkey, String tagName)
    {
        ProjectEntity project = projectDao.getOrCreateProject(projectkey);
        TagEntity tagEntity = tagsDao.getTag(tagName, project.getID());
        if (tagEntity == null)
        {
            tagEntity = tagsDao.newTag();
            // tagEntity.setName(tagName);
            // tagEntity.setProject(project);
            tagsDao.saveTag(tagEntity);
        }
        return tagEntity;
    }

    public void untagScenario(ScenarioEntity scenario, Set tagsToRemoved)
    {
        String projectKey = scenario.getFeature().getProject();
        ProjectEntity project = projectDao.getOrCreateProject(projectKey);
        Iterator i$ = tagsToRemoved.iterator();
        do
        {
            if (!i$.hasNext())
                break;
            String tagName = (String) i$.next();
            ScenarioTagEntity tagLink = null;
            // tagLink = tagsDao.getScenarioTag(project.getID(), scenario.getID(),
            // tagName);
            if (tagLink != null)
                tagsDao.deleteScenarioTag(tagLink);
        } while (true);
    }

    public void tagFeature(FeatureEntity feature, Set tags)
    {
        String projectKey = feature.getProject();
        FeatureTagEntity tagLink;
        for (Iterator i$ = tags.iterator(); i$.hasNext(); tagsDao.saveFeatureTag(tagLink))
        {
            String tagName = (String) i$.next();
            TagEntity tagEntity = getOrCreateTag(projectKey, tagName);
            tagLink = tagsDao.newFeatureTag();
            // tagLink.setFeature(feature);
            // tagLink.setTag(tagEntity);
        }

    }

    public void untagFeature(FeatureEntity feature, Set tags)
    {
        String projectKey = feature.getProject();
        ProjectEntity project = projectDao.getOrCreateProject(projectKey);
        Iterator i$ = tags.iterator();
        do
        {
            if (!i$.hasNext())
                break;
            String tagName = (String) i$.next();
            FeatureTagEntity tagLink = null;
            // tagLink = tagsDao.getFeatureTag(project.getID(), feature.getID(),
            // tagName);
            if (tagLink != null)
                tagsDao.deleteFeatureTag(tagLink);
        } while (true);
    }

    public Set searchForTag(String projectKey, String query)
    {
        Set searchResults = new HashSet();
        ProjectEntity project = projectDao.getOrCreateProject(projectKey);
        TagEntity results[] = tagsDao.searchForTag(project, query);
        TagEntity arr$[] = results;
        int len$ = arr$.length;
        for (int i$ = 0; i$ < len$; i$++)
        {
            TagEntity tag = arr$[i$];
            searchResults.add(tag.getName());
        }

        return searchResults;
    }

    public Set getTags(String projectKey)
    {
        Set searchResults = new HashSet();
        ProjectEntity project = projectDao.getOrCreateProject(projectKey);
        TagEntity results[] = tagsDao.getTags(project);
        TagEntity arr$[] = results;
        int len$ = arr$.length;
        for (int i$ = 0; i$ < len$; i$++)
        {
            TagEntity tag = arr$[i$];
            searchResults.add(tag.getName());
        }

        return searchResults;
    }

    private final TagsDao tagsDao;
    private final ProjectDao projectDao;
}
