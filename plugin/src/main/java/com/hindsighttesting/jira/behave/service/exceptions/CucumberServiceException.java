// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   CucumberServiceException.java

package com.hindsighttesting.jira.behave.service.exceptions;


public abstract class CucumberServiceException extends Exception
{

    protected CucumberServiceException(String message)
    {
        this(null, message);
    }

    public CucumberServiceException(String property, String message)
    {
        super(message);
        this.property = property;
    }

    public String getProperty()
    {
        return property;
    }

    private static final long serialVersionUID = 0xdad438cb9b7099fL;
    private final String property;
}
