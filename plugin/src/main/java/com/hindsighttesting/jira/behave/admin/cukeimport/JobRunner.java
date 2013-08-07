// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   JobRunner.java

package com.hindsighttesting.jira.behave.admin.cukeimport;

import com.atlassian.modzdetector.IOUtils;
import com.hindsighttesting.jira.behave.service.FeatureManager;
import com.hindsighttesting.jira.behave.service.ScenarioManager;
import gherkin.parser.Parser;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Referenced classes of package com.hindsighttesting.jira.behave.admin.cukeimport:
//            FeatureImport, ImportJob

public class JobRunner
    implements Runnable
{

    public JobRunner(FeatureManager featureManager, ScenarioManager scenarioManager, ImportJob job)
    {
        this.featureManager = featureManager;
        this.scenarioManager = scenarioManager;
        this.job = job;
        job.update(ImportJob.Status.INPROGRESS);
    }

    public void run()
    {
        ZipInputStream zis = null;
        zis = new ZipInputStream(new BufferedInputStream(new FileInputStream(job.getFile())));
        int featureFileCount = 0;
        int sucessfulFileCount = 0;
        do
        {
            ZipEntry entry;
            if((entry = zis.getNextEntry()) == null)
                break;
            if(entry.getName().endsWith(".feature"))
            {
                featureFileCount++;
                String feature = IOUtils.toString(zis);
                FeatureImport formatter = new FeatureImport(job.getProjectKey(), featureManager, scenarioManager);
                Parser gherkin = new Parser(formatter, true);
                gherkin.parse(feature, entry.getName(), Integer.valueOf(0));
                if(formatter.isSucessful())
                    sucessfulFileCount++;
            }
        } while(true);
        if(featureFileCount > 0 && sucessfulFileCount > 0)
            job.update((new StringBuilder()).append("Successful loaded ").append(sucessfulFileCount).append("/").append(featureFileCount).append(" features").toString(), ImportJob.Status.DONE);
        else
            job.update("No valid feature files found", ImportJob.Status.FAILED);
        Exception e;
        if(zis != null)
            try
            {
                zis.close();
            }
            // Misplaced declaration of an exception variable
            catch(Exception e) { }
        break MISSING_BLOCK_LABEL_273;
        e;
        job.update(e.toString(), ImportJob.Status.FAILED);
        log.error("Exception during import", e);
        if(zis != null)
            try
            {
                zis.close();
            }
            // Misplaced declaration of an exception variable
            catch(Exception e) { }
        break MISSING_BLOCK_LABEL_273;
        Exception exception;
        exception;
        if(zis != null)
            try
            {
                zis.close();
            }
            catch(IOException e) { }
        throw exception;
    }

    private static final Logger log = LoggerFactory.getLogger(com/hindsighttesting/jira/behave/admin/cukeimport/JobRunner);
    private final FeatureManager featureManager;
    private final ScenarioManager scenarioManager;
    private final ImportJob job;
    private final List failures = new ArrayList();

}
