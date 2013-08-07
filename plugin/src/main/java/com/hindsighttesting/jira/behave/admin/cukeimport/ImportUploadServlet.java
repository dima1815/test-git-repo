// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ImportUploadServlet.java

package com.hindsighttesting.jira.behave.admin.cukeimport;

import com.atlassian.sal.api.ApplicationProperties;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import org.apache.commons.fileupload.*;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

// Referenced classes of package com.hindsighttesting.jira.behave.admin.cukeimport:
//            ImportJobException, CucumberImportService, ImportJob

public class ImportUploadServlet extends HttpServlet
{

    public ImportUploadServlet(CucumberImportService importService, ApplicationProperties applicationProperties)
    {
        this.importService = importService;
        this.applicationProperties = applicationProperties;
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
        String projectKey = req.getPathInfo().substring(1);
        boolean isMultipart = ServletFileUpload.isMultipartContent(req);
        if(!isMultipart)
            resp.sendError(415);
        FileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);
        try
        {
            List items = upload.parseRequest(req);
            System.out.println("Uploaded");
            ImportJob job;
            for(Iterator i$ = items.iterator(); i$.hasNext(); resp.setHeader("Content-Location", (new StringBuilder()).append(applicationProperties.getBaseUrl()).append("/rest/cucumber/1.0/import/").append(job.getProjectKey()).append("/job/").append(job.getId()).toString()))
            {
                FileItem item = (FileItem)i$.next();
                System.out.println("Creating job");
                job = importService.createJob(projectKey, item.getInputStream());
                System.out.println("Starting job");
                importService.startJob(job.getId());
            }

        }
        catch(FileUploadException e)
        {
            resp.setStatus(500);
            throw new ServletException("Upload of the ZIP archive failed", e);
        }
        catch(ImportJobException e)
        {
            resp.setStatus(404);
        }
        System.out.println("done");
    }

    private final ApplicationProperties applicationProperties;
    private final CucumberImportService importService;
}
