// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   CucumberImportService.java

package com.hindsighttesting.jira.behave.admin.cukeimport;

import com.hindsighttesting.jira.behave.service.FeatureManager;
import com.hindsighttesting.jira.behave.service.ScenarioManager;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.DisposableBean;

// Referenced classes of package com.hindsighttesting.jira.behave.admin.cukeimport:
//            JobRunner, ImportJobException, ImportJob

public class CucumberImportService
    implements DisposableBean
{

    public CucumberImportService(FeatureManager featureManager, ScenarioManager scenarioManager)
    {
        jobNumber = 0;
        this.featureManager = featureManager;
        this.scenarioManager = scenarioManager;
    }

    public void startJob(int jobId)
        throws ImportJobException
    {
        ImportJob job = getJob(jobId);
        job.update(ImportJob.Status.READY);
        jobExecutor.execute(new JobRunner(featureManager, scenarioManager, job));
    }

    public ImportJob getJob(int jobId)
        throws ImportJobException
    {
        if(!jobDatabase.containsKey(Integer.valueOf(jobId)))
        {
            throw new ImportJobException("Job record not found");
        } else
        {
            ImportJob job = (ImportJob)jobDatabase.get(Integer.valueOf(jobId));
            return job;
        }
    }

    public ImportJob createJob(String projectKey, InputStream zipFile)
        throws IOException
    {
        ImportJob job;
        File tmpFile;
        FileOutputStream uploadTarget;
        int jobNumber = allocateJobNumber();
        job = new ImportJob(jobNumber, projectKey);
        jobDatabase.put(Integer.valueOf(jobNumber), job);
        tmpFile = File.createTempFile((new StringBuilder()).append(projectKey).append(jobNumber).toString(), "cucumberImport");
        uploadTarget = null;
        uploadTarget = new FileOutputStream(tmpFile);
        IOUtils.copy(zipFile, uploadTarget);
        if(uploadTarget != null)
            uploadTarget.close();
        job.setFile(tmpFile);
        break MISSING_BLOCK_LABEL_160;
        Exception e;
        e;
        job.update(e.getMessage(), ImportJob.Status.FAILED);
        e.printStackTrace();
        if(uploadTarget != null)
            uploadTarget.close();
        job.setFile(tmpFile);
        break MISSING_BLOCK_LABEL_160;
        Exception exception;
        exception;
        if(uploadTarget != null)
            uploadTarget.close();
        job.setFile(tmpFile);
        throw exception;
        return job;
    }

    public synchronized int allocateJobNumber()
    {
        jobNumber++;
        return jobNumber;
    }

    public void destroy()
        throws Exception
    {
        jobExecutor.shutdownNow();
    }

    private int jobNumber;
    private final Map jobDatabase = new HashMap();
    private final ExecutorService jobExecutor = Executors.newSingleThreadExecutor();
    private final FeatureManager featureManager;
    private final ScenarioManager scenarioManager;
}
