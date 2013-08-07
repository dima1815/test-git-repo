// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ScenarioCollection.java

package com.hindsighttesting.jira.behave.rest.model;

import java.util.List;

// Referenced classes of package com.hindsighttesting.jira.behave.rest.model:
//            AbstractResponseModel

public class ScenarioCollection extends AbstractResponseModel
{

    public ScenarioCollection()
    {
    }

    public List getScenarios()
    {
        return scenarios;
    }

    public void setScenarios(List scenarios)
    {
        this.scenarios = scenarios;
    }

    private List scenarios;
}
