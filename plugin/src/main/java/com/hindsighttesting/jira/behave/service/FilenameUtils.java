// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   FilenameUtils.java

package com.hindsighttesting.jira.behave.service;

import com.hindsighttesting.jira.behave.activeobjects.entities.FeatureEntity;

public class FilenameUtils
{

    public FilenameUtils()
    {
    }

    public static String generateFeatureFileName(FeatureEntity feature)
    {
        String stanitzedName = feature.getName().trim().replaceAll("[\\s\\/\\:\\*\\?\"<>\\|]", "_");
        if(stanitzedName.length() > 200)
            stanitzedName = stanitzedName.substring(0, 199);
        return (new StringBuilder()).append(stanitzedName).append("_").append(feature.getID()).append(".feature").toString();
    }
}
