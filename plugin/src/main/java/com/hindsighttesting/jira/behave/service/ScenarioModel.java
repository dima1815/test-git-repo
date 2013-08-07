// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ScenarioModel.java

package com.hindsighttesting.jira.behave.service;

import java.util.*;

public class ScenarioModel
{
    public static final class ImplementationState extends Enum
    {

        public static ImplementationState[] values()
        {
            return (ImplementationState[])$VALUES.clone();
        }

        public static ImplementationState valueOf(String name)
        {
            return (ImplementationState)Enum.valueOf(com/hindsighttesting/jira/behave/service/ScenarioModel$ImplementationState, name);
        }

        public static final ImplementationState ORPHAN;
        public static final ImplementationState OPEN;
        public static final ImplementationState WIP;
        public static final ImplementationState COMPLETED;
        private static final ImplementationState $VALUES[];

        static 
        {
            ORPHAN = new ImplementationState("ORPHAN", 0);
            OPEN = new ImplementationState("OPEN", 1);
            WIP = new ImplementationState("WIP", 2);
            COMPLETED = new ImplementationState("COMPLETED", 3);
            $VALUES = (new ImplementationState[] {
                ORPHAN, OPEN, WIP, COMPLETED
            });
        }

        private ImplementationState(String s, int i)
        {
            super(s, i);
        }
    }


    public ScenarioModel()
    {
        issues = new ArrayList();
        status = ImplementationState.OPEN;
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

    public Boolean isOutline()
    {
        return outline;
    }

    public void setOutline(Boolean outline)
    {
        this.outline = outline;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getFeatureId()
    {
        return featureId;
    }

    public void setFeatureId(int featureId)
    {
        this.featureId = featureId;
    }

    public String getProjectKey()
    {
        return projectKey;
    }

    public void setProjectKey(String projectKey)
    {
        this.projectKey = projectKey;
    }

    public List getIssues()
    {
        return issues;
    }

    public void setIssues(List issues)
    {
        this.issues = issues;
    }

    public ImplementationState getStatus()
    {
        return status;
    }

    public void setStatus(ImplementationState status)
    {
        this.status = status;
    }

    public Boolean isManual()
    {
        return manual;
    }

    public void setManual(Boolean manual)
    {
        this.manual = manual;
    }

    public Set getTags()
    {
        return tags;
    }

    public void setTags(Set tags)
    {
        this.tags = tags;
    }

    private int id;
    private String name;
    private String steps;
    private Boolean outline;
    private int featureId;
    private String projectKey;
    private Boolean manual;
    private List issues;
    private Set tags;
    private ImplementationState status;
}
