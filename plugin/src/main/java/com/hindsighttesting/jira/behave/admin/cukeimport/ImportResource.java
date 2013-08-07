// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ImportResource.java

package com.hindsighttesting.jira.behave.admin.cukeimport;

import javax.ws.rs.core.Response;

// Referenced classes of package com.hindsighttesting.jira.behave.admin.cukeimport:
//            ImportJobException, CucumberImportService

public class ImportResource
{

    public ImportResource(CucumberImportService importService)
    {
        this.importService = importService;
    }

    public Response getJobStatus(int jobId)
    {
        try
        {
            return Response.ok(importService.getJob(jobId)).build();
        }
        catch(ImportJobException e)
        {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    private final CucumberImportService importService;
}
