// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Version1UpgradeTask.java

package com.hindsighttesting.jira.behave.activeobjects.dao;

import com.atlassian.activeobjects.external.*;
import com.hindsighttesting.jira.behave.activeobjects.entities.*;

public class Version1UpgradeTask
    implements ActiveObjectsUpgradeTask
{

    public Version1UpgradeTask()
    {
    }

    public ModelVersion getModelVersion()
    {
        return ModelVersion.valueOf("1");
    }

    public void upgrade(ModelVersion currentVersion, ActiveObjects ao)
    {
        ao.migrate(new Class[] {
            com/hindsighttesting/jira/behave/activeobjects/entities/FeatureEntity, com/hindsighttesting/jira/behave/activeobjects/entities/IssueEntity, com/hindsighttesting/jira/behave/activeobjects/entities/ScenarioEntity, com/hindsighttesting/jira/behave/activeobjects/entities/IssueToScenarioEntity
        });
        FeatureEntity arr$[] = (FeatureEntity[])ao.find(com/hindsighttesting/jira/behave/activeobjects/entities/FeatureEntity);
        int len$ = arr$.length;
        for(int i$ = 0; i$ < len$; i$++)
        {
            FeatureEntity feature = arr$[i$];
            feature.setDescription(feature.getOldDescription());
            feature.save();
        }

        arr$ = (ScenarioEntity[])ao.find(com/hindsighttesting/jira/behave/activeobjects/entities/ScenarioEntity);
        len$ = arr$.length;
        for(int i$ = 0; i$ < len$; i$++)
        {
            ScenarioEntity scenario = arr$[i$];
            scenario.setSteps(scenario.getOldSteps());
            scenario.save();
        }

    }
}
