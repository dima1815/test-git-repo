// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ScenarioTemplate.java

package com.hindsighttesting.jira.behave.service;

import java.util.*;

// Referenced classes of package com.hindsighttesting.jira.behave.service:
//            ScenarioModel

public class ScenarioTemplate
{

    public ScenarioTemplate()
    {
        issues = new ArrayList();
        tags = new HashSet();
        status = ScenarioModel.ImplementationState.OPEN;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String[] getSteps()
    {
        return steps;
    }

    public void setSteps(String steps[])
    {
        this.steps = steps;
    }

    public Boolean getOutline()
    {
        return outline;
    }

    public void setOutline(Boolean outline)
    {
        this.outline = outline;
    }

    public Boolean getManual()
    {
        return manual;
    }

    public void setManual(Boolean manual)
    {
        this.manual = manual;
    }

    public List getIssues()
    {
        return issues;
    }

    public void setIssues(List issues)
    {
        this.issues = issues;
    }

    public Set getTags()
    {
        return tags;
    }

    public void setTags(Set tags)
    {
        this.tags = tags;
    }

    public ScenarioModel.ImplementationState getStatus()
    {
        return status;
    }

    public void setStatus(ScenarioModel.ImplementationState status)
    {
        this.status = status;
    }

    public String getTagList()
    {
        StringBuilder buf = new StringBuilder();
        buf.append('@');
        buf.append(status.toString());
        buf.append(' ');
        for(Iterator i$ = tags.iterator(); i$.hasNext(); buf.append(' '))
        {
            String tag = (String)i$.next();
            buf.append('@');
            buf.append(tag);
        }

        for(Iterator i$ = issues.iterator(); i$.hasNext(); buf.append(' '))
        {
            String tag = (String)i$.next();
            buf.append('@');
            buf.append(tag);
        }

        if(manual.booleanValue())
            buf.append("@MANUAL ");
        return buf.toString();
    }

    public String getType()
    {
        if(outline.booleanValue())
            return "Scenario Outline";
        else
            return "Scenario";
    }

    private String name;
    private String steps[];
    private Boolean outline;
    private Boolean manual;
    private List issues;
    private Set tags;
    private ScenarioModel.ImplementationState status;
}
