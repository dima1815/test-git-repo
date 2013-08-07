// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ProjectSettingsDao.java

package com.hindsighttesting.jira.behave.activeobjects.dao;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.hindsighttesting.jira.behave.activeobjects.entities.ProjectConfigEntity;
import java.util.*;
import net.java.ao.*;

public class ProjectSettingsDao
{

    public ProjectSettingsDao(ActiveObjects entityManager)
    {
        this.entityManager = entityManager;
    }

    public Boolean isProjectEnabled(String key)
    {
        ProjectConfigEntity enabled[] = (ProjectConfigEntity[])entityManager.find(com/hindsighttesting/jira/behave/activeobjects/entities/ProjectConfigEntity, "PROJECT_KEY = ? AND ENABLED = ?", new Object[] {
            key, Boolean.valueOf(true)
        });
        return Boolean.valueOf(enabled.length == 1);
    }

    public ProjectConfigEntity createProjectConfig(String key)
    {
        HashMap params = new HashMap();
        params.put("PROJECT_KEY", key);
        return (ProjectConfigEntity)entityManager.create(com/hindsighttesting/jira/behave/activeobjects/entities/ProjectConfigEntity, params);
    }

    public void saveConfig(ProjectConfigEntity config)
    {
        if(config.getProjectKey() == null || config.getProjectKey().isEmpty())
        {
            throw new IllegalArgumentException("Project key must be specified");
        } else
        {
            config.save();
            return;
        }
    }

    public Set getEnabledProjects()
    {
        final Set keys = new HashSet();
        EntityStreamCallback streamCallback = new EntityStreamCallback() {

            public void onRowRead(ProjectConfigEntity t)
            {
                String projectKey = t.getProjectKey();
                keys.add(projectKey);
            }

            public volatile void onRowRead(RawEntity x0)
            {
                onRowRead((ProjectConfigEntity)x0);
            }

            final Set val$keys;
            final ProjectSettingsDao this$0;

            
            {
                this$0 = ProjectSettingsDao.this;
                keys = set;
                super();
            }
        }
;
        entityManager.stream(com/hindsighttesting/jira/behave/activeobjects/entities/ProjectConfigEntity, Query.select("ID, PROJECT_KEY").where("ENABLED = ?", new Object[] {
            Boolean.valueOf(true)
        }), streamCallback);
        return keys;
    }

    public ProjectConfigEntity getConfiguration(String projectKey)
    {
        ProjectConfigEntity configs[] = (ProjectConfigEntity[])entityManager.find(com/hindsighttesting/jira/behave/activeobjects/entities/ProjectConfigEntity, "PROJECT_KEY like ?", new Object[] {
            projectKey
        });
        return configs.length != 1 ? null : configs[0];
    }

    private final ActiveObjects entityManager;
}
