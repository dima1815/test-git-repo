// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   InvalidFeatureException.java

package com.hindsighttesting.jira.behave.service.exceptions;


// Referenced classes of package com.hindsighttesting.jira.behave.service.exceptions:
//            CucumberServiceException

public class InvalidFeatureException extends CucumberServiceException
{

    public InvalidFeatureException(String message)
    {
        super(message);
    }

    public InvalidFeatureException(String property, String message)
    {
        super(property, message);
    }

    private static final long serialVersionUID = 0x6eb74de93e25a471L;
}
