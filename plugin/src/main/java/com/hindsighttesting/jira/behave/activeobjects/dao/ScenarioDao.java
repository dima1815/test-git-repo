// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ScenarioDao.java

package com.hindsighttesting.jira.behave.activeobjects.dao;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.hindsighttesting.jira.behave.activeobjects.entities.ScenarioEntity;
import java.util.Arrays;
import java.util.List;
import net.java.ao.DBParam;
import net.java.ao.RawEntity;

public class ScenarioDao
{

    public ScenarioDao(ActiveObjects ao)
    {
        entityManager = ao;
    }

    public ScenarioEntity newScenario()
    {
        return (ScenarioEntity)entityManager.create(com/hindsighttesting/jira/behave/activeobjects/entities/ScenarioEntity, new DBParam[0]);
    }

    public void saveScenario(ScenarioEntity scenarioEntity)
    {
        if(scenarioEntity.getType() == null)
            throw new IllegalArgumentException("Scenario type is required");
        if(scenarioEntity.getName() == null || scenarioEntity.getName().trim().isEmpty())
            throw new IllegalArgumentException("Scenario name is required");
        if(scenarioEntity.getFeature() == null)
        {
            throw new IllegalArgumentException("A Scenario must have a Feature");
        } else
        {
            scenarioEntity.save();
            return;
        }
    }

    public List getScenariosFor(int featureId)
    {
        List results = Arrays.asList(entityManager.find(com/hindsighttesting/jira/behave/activeobjects/entities/ScenarioEntity, "FEATURE_ID = ?", new Object[] {
            Integer.valueOf(featureId)
        }));
        return results;
    }

    public ScenarioEntity getScenario(int id)
    {
        return (ScenarioEntity)entityManager.get(com/hindsighttesting/jira/behave/activeobjects/entities/ScenarioEntity, Integer.valueOf(id));
    }

    public boolean delete(ScenarioEntity scenario)
    {
        boolean result = true;
        try
        {
            entityManager.delete(new RawEntity[] {
                scenario
            });
        }
        catch(Exception e)
        {
            result = false;
        }
        return result;
    }

    private final ActiveObjects entityManager;
}
