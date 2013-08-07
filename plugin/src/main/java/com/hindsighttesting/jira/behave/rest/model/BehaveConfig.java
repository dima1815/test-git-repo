// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   BehaveConfig.java

package com.hindsighttesting.jira.behave.rest.model;

import java.util.Set;

// Referenced classes of package com.hindsighttesting.jira.behave.rest.model:
//            ProjectValue

public class BehaveConfig
{

    public BehaveConfig()
    {
    }

    public BehaveConfig(Set enabledProjects, Boolean globalEnabled)
    {
        this.enabledProjects = (ProjectValue[])enabledProjects.toArray(new ProjectValue[enabledProjects.size()]);
        enableAllProjects = globalEnabled;
    }

    public ProjectValue[] getEnabledProjects()
    {
        return enabledProjects;
    }

    public void setEnabledProjects(ProjectValue enabledProjects[])
    {
        this.enabledProjects = enabledProjects;
    }

    public Boolean isEnableAllProjects()
    {
        return enableAllProjects;
    }

    public void setEnableAllProjects(Boolean enableAllProjects)
    {
        this.enableAllProjects = enableAllProjects;
    }

    private ProjectValue enabledProjects[];
    private Boolean enableAllProjects;
}
