// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   FeatureResponeModel.java

package com.hindsighttesting.jira.behave.rest.model;


// Referenced classes of package com.hindsighttesting.jira.behave.rest.model:
//            AbstractResponseModel

public class FeatureResponeModel extends AbstractResponseModel
{

    public FeatureResponeModel()
    {
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
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

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String[] getTags()
    {
        return tags;
    }

    public void setTags(String tag[])
    {
        tags = tag;
    }

    public String getBackground()
    {
        return background;
    }

    public void setBackground(String background)
    {
        this.background = background;
    }

    private long id;
    private String name;
    private String description;
    private String tags[];
    private String background;
}
