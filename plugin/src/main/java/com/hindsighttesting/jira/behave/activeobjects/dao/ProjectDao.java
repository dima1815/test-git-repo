// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ProjectDao.java

package com.hindsighttesting.jira.behave.activeobjects.dao;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.hindsighttesting.jira.behave.activeobjects.entities.ProjectEntity;
import net.java.ao.DBParam;

public class ProjectDao
{

    public ProjectDao(ActiveObjects ao)
    {
        entityManager = ao;
    }

    public ProjectEntity getOrCreateProject(String projectKey)
    {
        ProjectEntity projects[] = (ProjectEntity[])entityManager.find(com/hindsighttesting/jira/behave/activeobjects/entities/ProjectEntity, "PROJECT_KEY = ?", new Object[] {
            projectKey
        });
        if(projects.length != 1)
        {
            ProjectEntity create = (ProjectEntity)entityManager.create(com/hindsighttesting/jira/behave/activeobjects/entities/ProjectEntity, new DBParam[0]);
            create.setProjectKey(projectKey);
            create.save();
            return create;
        } else
        {
            return projects[0];
        }
    }

    public ProjectEntity createProject(String string)
    {
        ProjectEntity project = (ProjectEntity)entityManager.create(com/hindsighttesting/jira/behave/activeobjects/entities/ProjectEntity, new DBParam[0]);
        project.setProjectKey("TST");
        project.save();
        return project;
    }

    private final ActiveObjects entityManager;
}
