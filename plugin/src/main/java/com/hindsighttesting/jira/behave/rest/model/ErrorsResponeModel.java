// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ErrorsResponeModel.java

package com.hindsighttesting.jira.behave.rest.model;

import com.hindsighttesting.jira.behave.service.exceptions.CucumberServiceException;

public class ErrorsResponeModel
{
    public class ErrorModel
    {

        public String getContext()
        {
            return context;
        }

        public String getMessage()
        {
            return message;
        }

        private final String context;
        private final String message;
        final ErrorsResponeModel this$0;

        private ErrorModel(String context, String message)
        {
            this$0 = ErrorsResponeModel.this;
            super();
            this.context = context;
            this.message = message;
        }

    }


    public ErrorsResponeModel(CucumberServiceException e)
    {
        errors = new ErrorModel[0];
        errors = (new ErrorModel[] {
            new ErrorModel(e.getProperty(), e.getMessage())
        });
    }

    public ErrorModel[] getErrors()
    {
        return errors;
    }

    public void setErrors(ErrorModel errors[])
    {
        this.errors = errors;
    }

    private ErrorModel errors[];
}
