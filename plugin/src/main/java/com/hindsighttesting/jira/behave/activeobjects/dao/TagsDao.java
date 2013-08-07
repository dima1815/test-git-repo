// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   TagsDao.java

package com.hindsighttesting.jira.behave.activeobjects.dao;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.hindsighttesting.jira.behave.activeobjects.entities.*;
import net.java.ao.DBParam;
import net.java.ao.RawEntity;

public class TagsDao
{

    public TagsDao(ActiveObjects ao)
    {
        entityManager = ao;
    }

    public TagEntity newTag()
    {
        return (TagEntity)entityManager.create(com/hindsighttesting/jira/behave/activeobjects/entities/TagEntity, new DBParam[0]);
    }

    public void saveTaggedScenario(ScenarioTagEntity linked)
    {
        linked.save();
    }

    public TagEntity getTag(String tagName, int projectId)
    {
        TagEntity tags[] = (TagEntity[])entityManager.find(com/hindsighttesting/jira/behave/activeobjects/entities/TagEntity, "TAG_NAME = ? AND PROJECT_ID = ?", new Object[] {
            tagName, Integer.valueOf(projectId)
        });
        return tags.length != 1 ? null : tags[0];
    }

    public void saveTag(TagEntity tagEntity)
    {
        tagEntity.save();
    }

    public ScenarioTagEntity newTaggedScenario()
    {
        return (ScenarioTagEntity)entityManager.create(com/hindsighttesting/jira/behave/activeobjects/entities/ScenarioTagEntity, new DBParam[0]);
    }

    public void deleteScenarioTag(ScenarioTagEntity tagLink)
    {
        entityManager.delete(new RawEntity[] {
            tagLink
        });
    }

    public ScenarioTagEntity getScenarioTag(int projectId, int scenarioId, String tagName)
    {
        TagEntity tag = getTag(tagName, projectId);
        if(tag == null)
        {
            return null;
        } else
        {
            ScenarioTagEntity links[] = (ScenarioTagEntity[])entityManager.find(com/hindsighttesting/jira/behave/activeobjects/entities/ScenarioTagEntity, "SCENARIO_ID = ? AND TAG_ID = ?", new Object[] {
                Integer.valueOf(scenarioId), Integer.valueOf(tag.getID())
            });
            return links.length != 1 ? null : links[0];
        }
    }

    public FeatureTagEntity newFeatureTag()
    {
        return (FeatureTagEntity)entityManager.create(com/hindsighttesting/jira/behave/activeobjects/entities/FeatureTagEntity, new DBParam[0]);
    }

    public void saveFeatureTag(FeatureTagEntity tagLink)
    {
        tagLink.save();
    }

    public void deleteFeatureTag(FeatureTagEntity featureTag)
    {
        entityManager.delete(new RawEntity[] {
            featureTag
        });
    }

    public FeatureTagEntity getFeatureTag(int projectId, int featureId, String tagName)
    {
        TagEntity tag = getTag(tagName, projectId);
        if(tag == null)
        {
            return null;
        } else
        {
            FeatureTagEntity links[] = (FeatureTagEntity[])entityManager.find(com/hindsighttesting/jira/behave/activeobjects/entities/FeatureTagEntity, "FEATURE_ID = ? AND TAG_ID = ?", new Object[] {
                Integer.valueOf(featureId), Integer.valueOf(tag.getID())
            });
            return links.length != 1 ? null : links[0];
        }
    }

    public TagEntity[] searchForTag(ProjectEntity project, String tagRequest)
    {
        String tagQuery = (new StringBuilder()).append(tagRequest).append("%").toString();
        TagEntity tags[] = (TagEntity[])entityManager.find(com/hindsighttesting/jira/behave/activeobjects/entities/TagEntity, "PROJECT_ID = ? and TAG_NAME like ?", new Object[] {
            Integer.valueOf(project.getID()), tagQuery
        });
        return tags;
    }

    public TagEntity[] getTags(ProjectEntity project)
    {
        TagEntity tags[] = (TagEntity[])entityManager.find(com/hindsighttesting/jira/behave/activeobjects/entities/TagEntity, "PROJECT_ID = ?", new Object[] {
            Integer.valueOf(project.getID())
        });
        return tags;
    }

    private final ActiveObjects entityManager;
}
