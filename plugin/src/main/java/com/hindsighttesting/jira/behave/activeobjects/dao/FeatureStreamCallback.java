// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   FeatureStreamCallback.java

package com.hindsighttesting.jira.behave.activeobjects.dao;

import com.hindsighttesting.jira.behave.activeobjects.entities.FeatureEntity;

public interface FeatureStreamCallback
{

    public abstract void readValue(FeatureEntity featureentity);
}
