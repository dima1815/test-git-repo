// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   InvalidScenarioException.java

package com.hindsighttesting.jira.behave.service.exceptions;


// Referenced classes of package com.hindsighttesting.jira.behave.service.exceptions:
//            CucumberServiceException

public class InvalidScenarioException extends CucumberServiceException
{

    public InvalidScenarioException(String message)
    {
        super(message);
    }

    public InvalidScenarioException(String property, String message)
    {
        super(property, message);
    }

    private static final long serialVersionUID = 0x6ce0f6315174f31eL;
}
