// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ModelFactory.java

package com.hindsighttesting.jira.behave.service;

import com.google.common.collect.Sets;
import com.hindsighttesting.jira.behave.activeobjects.entities.*;
import com.hindsighttesting.jira.behave.rest.model.FeatureInputModel;
import com.hindsighttesting.jira.behave.rest.model.ScenarioInputModel;
import java.util.*;

// Referenced classes of package com.hindsighttesting.jira.behave.service:
//            FeatureModel, ScenarioModel

public class ModelFactory
{

    private ModelFactory()
    {
    }

    public static FeatureModel buildFeatureModel(FeatureEntity entity)
    {
        FeatureModel model = new FeatureModel();
        model.setId(entity.getID());
        model.setName(entity.getName());
        model.setDescription(entity.getDescription());
        model.setTags(extractTags(entity.getTags()));
        model.setBackground(entity.getBackground());
        return model;
    }

    public static FeatureModel buildFeatureModel(Integer id, FeatureInputModel feature)
    {
        FeatureModel model = new FeatureModel();
        if(id != null)
            model.setId(id.intValue());
        model.setName(feature.getName());
        model.setDescription(feature.getDescription());
        String tags[] = feature.getTags();
        if(tags != null)
            model.setTags(Sets.newHashSet(tags));
        model.setBackground(feature.getBackground());
        return model;
    }

    public static ScenarioModel buildScenarioModel(ScenarioEntity scenario, ScenarioModel.ImplementationState state)
    {
        ScenarioModel model = new ScenarioModel();
        model.setId(scenario.getID());
        model.setName(scenario.getName());
        model.setSteps(scenario.getSteps());
        model.setOutline(Boolean.valueOf(scenario.getType() == com.hindsighttesting.jira.behave.activeobjects.entities.ScenarioEntity.Type.OUTLINE));
        FeatureEntity feature = scenario.getFeature();
        model.setProjectKey(feature.getProject());
        model.setFeatureId(feature.getID());
        model.setManual(Boolean.valueOf(scenario.getTestNethod() == com.hindsighttesting.jira.behave.activeobjects.entities.ScenarioEntity.TestType.MANUAL));
        List issues = new ArrayList();
        if(scenario.getIssues() != null)
        {
            IssueEntity arr$[] = scenario.getIssues();
            int len$ = arr$.length;
            for(int i$ = 0; i$ < len$; i$++)
            {
                IssueEntity issue = arr$[i$];
                issues.add(issue.getIssueKey());
            }

        }
        model.setIssues(issues);
        model.setStatus(state);
        model.setTags(extractTags(scenario.getTags()));
        return model;
    }

    private static Set extractTags(TagEntity tags[])
    {
        Set tagsAsStrings = null;
        if(tags != null && tags.length > 0)
        {
            tagsAsStrings = new HashSet();
            TagEntity arr$[] = tags;
            int len$ = arr$.length;
            for(int i$ = 0; i$ < len$; i$++)
            {
                TagEntity tag = arr$[i$];
                tagsAsStrings.add(tag.getName());
            }

        }
        return tagsAsStrings;
    }

    public static ScenarioModel buildScenarioModel(ScenarioInputModel scenario)
    {
        ScenarioModel model = new ScenarioModel();
        model.setId(scenario.getId());
        model.setName(scenario.getName());
        model.setOutline(Boolean.valueOf(scenario.isOutline()));
        model.setSteps(scenario.getSteps());
        model.setManual(scenario.isManual());
        if(scenario.getTags() != null)
            model.setTags(Sets.newHashSet(scenario.getTags()));
        return model;
    }

    public static ScenarioModel buildScenarioModel(ScenarioEntity scenario)
    {
        return buildScenarioModel(scenario, null);
    }
}
