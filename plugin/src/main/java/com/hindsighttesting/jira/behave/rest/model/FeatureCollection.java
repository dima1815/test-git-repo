// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   FeatureCollection.java

package com.hindsighttesting.jira.behave.rest.model;

import java.util.List;

// Referenced classes of package com.hindsighttesting.jira.behave.rest.model:
//            AbstractResponseModel

public class FeatureCollection extends AbstractResponseModel
{

    public FeatureCollection()
    {
    }

    public List getFeatures()
    {
        return features;
    }

    public void setFeatures(List features)
    {
        this.features = features;
    }

    private List features;
}
