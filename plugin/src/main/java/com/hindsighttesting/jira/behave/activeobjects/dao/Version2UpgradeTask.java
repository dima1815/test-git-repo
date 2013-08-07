// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Version2UpgradeTask.java

package com.hindsighttesting.jira.behave.activeobjects.dao;

import com.atlassian.activeobjects.external.*;
import com.hindsighttesting.jira.behave.activeobjects.entities.*;

public class Version2UpgradeTask
    implements ActiveObjectsUpgradeTask
{

    public Version2UpgradeTask()
    {
    }

    public ModelVersion getModelVersion()
    {
        return ModelVersion.valueOf("2");
    }

    public void upgrade(ModelVersion currentVersion, ActiveObjects ao)
    {
        ao.migrate(new Class[] {
            com/hindsighttesting/jira/behave/activeobjects/entities/IssueEntity, com/hindsighttesting/jira/behave/activeobjects/entities/IssueToScenarioEntity, com/hindsighttesting/jira/behave/activeobjects/entities/FeatureEntity, com/hindsighttesting/jira/behave/activeobjects/entities/ScenarioEntity, com/hindsighttesting/jira/behave/activeobjects/entities/TagEntity, com/hindsighttesting/jira/behave/activeobjects/entities/ScenarioTagEntity, com/hindsighttesting/jira/behave/activeobjects/entities/FeatureTagEntity, com/hindsighttesting/jira/behave/activeobjects/entities/ProjectEntity, com/hindsighttesting/jira/behave/activeobjects/entities/ProjectConfigEntity
        });
        FeatureEntity arr$[] = (FeatureEntity[])ao.find(com/hindsighttesting/jira/behave/activeobjects/entities/FeatureEntity);
        int len$ = arr$.length;
        for(int i$ = 0; i$ < len$; i$++)
        {
            FeatureEntity feature = arr$[i$];
            String backgroundOld = feature.getBackgroundOld();
            if(backgroundOld != null && !backgroundOld.isEmpty())
            {
                feature.setBackground(backgroundOld);
                feature.setBackgroundOld(null);
                feature.save();
            }
        }

    }
}
