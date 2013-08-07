// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ProjectSettingsManager.java

package com.hindsighttesting.jira.behave.service;

import com.atlassian.crowd.embedded.api.User;
import com.atlassian.jira.bc.ServiceOutcome;
import com.atlassian.jira.bc.project.ProjectService;
import com.atlassian.jira.project.Project;
import com.atlassian.jira.project.ProjectManager;
import com.google.common.base.Function;
import com.google.common.collect.MapMaker;
import com.google.common.collect.Sets;
import com.hindsighttesting.jira.behave.activeobjects.dao.ProjectSettingsDao;
import com.hindsighttesting.jira.behave.activeobjects.entities.ProjectConfigEntity;
import com.hindsighttesting.jira.behave.rest.model.BehaveConfig;
import com.hindsighttesting.jira.behave.rest.model.ProjectValue;
import java.util.*;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

// Referenced classes of package com.hindsighttesting.jira.behave.service:
//            BehaveConfigurationException, GlobalSettingsManager

public class ProjectSettingsManager
{

    public ProjectSettingsManager(ProjectManager projectManager, ProjectService projectService, ProjectSettingsDao dao, GlobalSettingsManager globalSettings)
    {
        settingsDao = dao;
        this.projectManager = projectManager;
        this.globalSettings = globalSettings;
        this.projectService = projectService;
        configCache = (new MapMaker()).concurrencyLevel(4).softKeys().expiration(1L, TimeUnit.HOURS).makeComputingMap(new Function() {

            public Boolean apply(String key)
            {
                return settingsDao.isProjectEnabled(key);
            }

            public volatile Object apply(Object x0)
            {
                return apply((String)x0);
            }

            final ProjectSettingsManager this$0;

            
            {
                this$0 = ProjectSettingsManager.this;
                super();
            }
        }
);
    }

    public boolean isProjectEnabled(String key)
    {
        try
        {
            boolean enabled = globalSettings.isAllProjectsEnabled() || ((Boolean)configCache.get(key)).booleanValue();
            return enabled;
        }
        catch(Exception e)
        {
            return false;
        }
    }

    public Set getEnabledProjects()
    {
        Set enabledProjects = new HashSet();
        Set projects = settingsDao.getEnabledProjects();
        Iterator i$ = projectManager.getProjectObjects().iterator();
        do
        {
            if(!i$.hasNext())
                break;
            Project project = (Project)i$.next();
            if(projects.contains(project.getKey()))
                enabledProjects.add(new ProjectValue(project.getKey(), project.getName()));
        } while(true);
        return enabledProjects;
    }

    public Set getEnabledProjects(User user)
    {
        Set enabledProjects = new HashSet();
        Set projects = settingsDao.getEnabledProjects();
        ServiceOutcome userVisibleProjects = projectService.getAllProjects(user);
        if(userVisibleProjects.isValid())
        {
            List visiableProjects = (List)userVisibleProjects.getReturnedValue();
            if(globalSettings.isAllProjectsEnabled())
            {
                Project project;
                for(Iterator i$ = visiableProjects.iterator(); i$.hasNext(); enabledProjects.add(new ProjectValue(project.getKey(), project.getName())))
                    project = (Project)i$.next();

            } else
            {
                Iterator i$ = visiableProjects.iterator();
                do
                {
                    if(!i$.hasNext())
                        break;
                    Project project = (Project)i$.next();
                    if(projects.contains(project.getKey()))
                        enabledProjects.add(new ProjectValue(project.getKey(), project.getName()));
                } while(true);
            }
        }
        return enabledProjects;
    }

    public void enableProject(String projectKey, boolean enabled)
        throws BehaveConfigurationException
    {
        if(projectManager.getProjectObjByKeyIgnoreCase(projectKey) == null)
            throw new BehaveConfigurationException("Invalid project key");
        ProjectConfigEntity configuration = settingsDao.getConfiguration(projectKey);
        if(configuration == null)
            configuration = settingsDao.createProjectConfig(projectKey);
        else
            configCache.remove(projectKey);
        configuration.setEnabled(enabled);
        settingsDao.saveConfig(configuration);
    }

    public void saveConfig(BehaveConfig config)
    {
        if(config.getEnabledProjects() != null)
        {
            HashSet tobeEnabled = Sets.newHashSet(config.getEnabledProjects());
            Set currentlyEnabled = getEnabledProjects();
            com.google.common.collect.Sets.SetView newleyEnabled = Sets.difference(tobeEnabled, currentlyEnabled);
            com.google.common.collect.Sets.SetView newleyDisabled = Sets.difference(currentlyEnabled, tobeEnabled);
            for(Iterator i$ = newleyEnabled.iterator(); i$.hasNext();)
            {
                ProjectValue project = (ProjectValue)i$.next();
                try
                {
                    enableProject(project.getKey(), true);
                }
                catch(BehaveConfigurationException e) { }
            }

            for(Iterator i$ = newleyDisabled.iterator(); i$.hasNext();)
            {
                ProjectValue project = (ProjectValue)i$.next();
                try
                {
                    enableProject(project.getKey(), false);
                }
                catch(BehaveConfigurationException e) { }
            }

        }
        if(config.isEnableAllProjects() != null)
            globalSettings.saveAllprojectEnabled(config.isEnableAllProjects());
    }

    public BehaveConfig getGlobalConfig()
    {
        return new BehaveConfig(getEnabledProjects(), Boolean.valueOf(globalSettings.isAllProjectsEnabled()));
    }

    private final ProjectSettingsDao settingsDao;
    private final ProjectManager projectManager;
    private final ProjectService projectService;
    private final GlobalSettingsManager globalSettings;
    private final ConcurrentMap configCache;

}
