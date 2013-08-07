// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   InvalidRequestException.java

package com.hindsighttesting.jira.behave.service.exceptions;


// Referenced classes of package com.hindsighttesting.jira.behave.service.exceptions:
//            CucumberServiceException

public class InvalidRequestException extends CucumberServiceException
{

    public InvalidRequestException(String message)
    {
        super(message);
    }

    public InvalidRequestException(String property, String message)
    {
        super(property, message);
    }

    private static final long serialVersionUID = 0xc16dcf390d5a3598L;
}
