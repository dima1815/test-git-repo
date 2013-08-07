// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   FeatureDao.java

package com.hindsighttesting.jira.behave.activeobjects.dao;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.hindsighttesting.jira.behave.activeobjects.entities.FeatureEntity;
import net.java.ao.*;

// Referenced classes of package com.hindsighttesting.jira.behave.activeobjects.dao:
//            FeatureStreamCallback

public class FeatureDao
{

    public FeatureDao(ActiveObjects ao)
    {
        entityManager = ao;
    }

    public void saveFeature(FeatureEntity feature)
    {
        if(feature.getName() == null || "".equals(feature.getName().trim()))
            throw new IllegalArgumentException("Name is required");
        if(feature.getProject() == null || "".equals(feature.getProject().trim()))
        {
            throw new IllegalArgumentException("Project Key is required");
        } else
        {
            feature.save();
            return;
        }
    }

    public FeatureEntity newFeature()
    {
        return (FeatureEntity)entityManager.create(com/hindsighttesting/jira/behave/activeobjects/entities/FeatureEntity, new DBParam[0]);
    }

    public void getAllFeaturesAsStream(String projectKey, final FeatureStreamCallback callback)
    {
        entityManager.stream(com/hindsighttesting/jira/behave/activeobjects/entities/FeatureEntity, Query.select("*").where("JIRA_PROJECT = ?", new Object[] {
            projectKey
        }), new EntityStreamCallback() {

            public void onRowRead(FeatureEntity t)
            {
                callback.readValue(t);
            }

            public volatile void onRowRead(RawEntity x0)
            {
                onRowRead((FeatureEntity)x0);
            }

            final FeatureStreamCallback val$callback;
            final FeatureDao this$0;

            
            {
                this$0 = FeatureDao.this;
                callback = featurestreamcallback;
                super();
            }
        }
);
    }

    public void getAllFeaturesAsPagedStream(String projectKey, FeatureStreamCallback callback)
    {
        int pageSize = 1;
        boolean remaining = true;
        for(int offset = 0; remaining; offset++)
        {
            FeatureEntity features[] = (FeatureEntity[])entityManager.find(com/hindsighttesting/jira/behave/activeobjects/entities/FeatureEntity, Query.select().where("JIRA_PROJECT = ?", new Object[] {
                projectKey
            }).limit(1).offset(offset));
            FeatureEntity arr$[] = features;
            int len$ = arr$.length;
            for(int i$ = 0; i$ < len$; i$++)
            {
                FeatureEntity entity = arr$[i$];
                callback.readValue(entity);
            }

            remaining = 1 <= features.length;
        }

    }

    public FeatureEntity getFeature(int id)
    {
        return (FeatureEntity)entityManager.get(com/hindsighttesting/jira/behave/activeobjects/entities/FeatureEntity, Integer.valueOf(id));
    }

    public boolean deleteFeature(FeatureEntity feature)
    {
        try
        {
            entityManager.delete(new RawEntity[] {
                feature
            });
        }
        catch(Exception e)
        {
            return false;
        }
        return true;
    }

    public int getFeatureCount(String projectKey)
    {
        return entityManager.count(com/hindsighttesting/jira/behave/activeobjects/entities/FeatureEntity, "JIRA_PROJECT = ?", new Object[] {
            projectKey
        });
    }

    private final ActiveObjects entityManager;
}
