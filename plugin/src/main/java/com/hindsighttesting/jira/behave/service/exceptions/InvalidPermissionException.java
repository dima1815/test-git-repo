// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   InvalidPermissionException.java

package com.hindsighttesting.jira.behave.service.exceptions;


// Referenced classes of package com.hindsighttesting.jira.behave.service.exceptions:
//            CucumberServiceException

public class InvalidPermissionException extends CucumberServiceException
{

    public InvalidPermissionException(String arg0)
    {
        super(arg0);
    }

    private InvalidPermissionException(String property, String message)
    {
        super(property, message);
    }

    private static final long serialVersionUID = 0x4a4ba2f736f339feL;
}
