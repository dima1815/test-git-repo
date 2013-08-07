// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ScenarioResponseModel.java

package com.hindsighttesting.jira.behave.rest.model;

import com.hindsighttesting.jira.behave.service.ScenarioModel;
import java.util.List;

// Referenced classes of package com.hindsighttesting.jira.behave.rest.model:
//            AbstractResponseModel

public class ScenarioResponseModel extends AbstractResponseModel
{

    public ScenarioResponseModel()
    {
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getSteps()
    {
        return steps;
    }

    public void setSteps(String steps)
    {
        this.steps = steps;
    }

    public boolean isOutline()
    {
        return outline;
    }

    public void setOutline(boolean outline)
    {
        this.outline = outline;
    }

    public List getIssues()
    {
        return issues;
    }

    public void setIssues(List issues)
    {
        this.issues = issues;
    }

    public com.hindsighttesting.jira.behave.service.ScenarioModel.ImplementationState getStatus()
    {
        return status;
    }

    public void setStatus(com.hindsighttesting.jira.behave.service.ScenarioModel.ImplementationState status)
    {
        this.status = status;
    }

    public boolean isManual()
    {
        return manual;
    }

    public void setManual(boolean manual)
    {
        this.manual = manual;
    }

    public String[] getTags()
    {
        return tags;
    }

    public void setTags(String tags[])
    {
        this.tags = tags;
    }

    private int id;
    private String name;
    private String steps;
    private boolean outline;
    private List issues;
    private com.hindsighttesting.jira.behave.service.ScenarioModel.ImplementationState status;
    private boolean manual;
    private String tags[];
}
