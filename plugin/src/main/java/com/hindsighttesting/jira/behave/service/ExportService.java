// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ExportService.java

package com.hindsighttesting.jira.behave.service;

import com.atlassian.jira.project.ProjectManager;
import com.hindsighttesting.jira.behave.activeobjects.dao.FeatureDao;
import com.hindsighttesting.jira.behave.activeobjects.dao.FeatureStreamCallback;
import com.hindsighttesting.jira.behave.activeobjects.entities.*;
import java.io.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.apache.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

// Referenced classes of package com.hindsighttesting.jira.behave.service:
//            ScenarioManager, ScenarioStateCalculator, ScenarioTemplate, FilenameUtils, 
//            ModelFactory, ScenarioModel

public class ExportService
{

    public ExportService(FeatureDao featureDao, ProjectManager projectManager, ScenarioManager scenarioManager, ScenarioStateCalculator statusCalculator)
    {
        this.featureDao = featureDao;
        this.projectManager = projectManager;
        this.scenarioManager = scenarioManager;
        this.statusCalculator = statusCalculator;
    }

    public void getFeatures(String projectKey, final ZipOutputStream zipOutput, final boolean includeManual)
        throws Exception
    {
        if(projectManager.getProjectObjByKey(projectKey) != null)
        {
            Properties p = new Properties();
            p.setProperty("resource.loader", "class");
            p.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
            p.setProperty("runtime.log.logsystem.class", "org.apache.velocity.runtime.log.NullLogSystem");
            VelocityEngine velocity = new VelocityEngine();
            velocity.init(p);
            final Template template = velocity.getTemplate("com/hindsighttesting/maven/featurefile.vm");
            featureDao.getAllFeaturesAsPagedStream(projectKey, new FeatureStreamCallback() {

                public void readValue(FeatureEntity feature)
                {
                    String featureName = FilenameUtils.generateFeatureFileName(feature);
                    try
                    {
                        zipOutput.putNextEntry(new ZipEntry(featureName));
                        VelocityContext context = new VelocityContext();
                        context.put("feature", ModelFactory.buildFeatureModel(feature));
                        List scenarios = new ArrayList();
                        ScenarioEntity arr$[] = feature.getScenarios();
                        int len$ = arr$.length;
                        for(int i$ = 0; i$ < len$; i$++)
                        {
                            ScenarioEntity scenario = arr$[i$];
                            if(scenario.getTestNethod().equals(com.hindsighttesting.jira.behave.activeobjects.entities.ScenarioEntity.TestType.AUTOMATED) || includeManual)
                                scenarios.add(createTemplate(scenario, statusCalculator.calculateStatus(scenario)));
                        }

                        context.put("scenarios", scenarios);
                        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(zipOutput));
                        template.merge(context, writer);
                        writer.flush();
                        zipOutput.closeEntry();
                    }
                    catch(Exception e)
                    {
                        try
                        {
                            zipOutput.closeEntry();
                        }
                        catch(IOException e1)
                        {
                            ExportService.LOG.info("Failed to close ZIP entry", e1);
                        }
                        ExportService.LOG.error("Error exporting feature feature.getID()", e);
                    }
                }

                private ScenarioTemplate createTemplate(ScenarioEntity scenario, ScenarioModel.ImplementationState status)
                {
                    ScenarioTemplate model = new ScenarioTemplate();
                    model.setName(scenario.getName());
                    model.setSteps(scenario.getSteps() != null ? scenario.getSteps().split("\r\n|\r|\n") : new String[0]);
                    model.setOutline(Boolean.valueOf(scenario.getType() == com.hindsighttesting.jira.behave.activeobjects.entities.ScenarioEntity.Type.OUTLINE));
                    model.setManual(Boolean.valueOf(scenario.getTestNethod() == com.hindsighttesting.jira.behave.activeobjects.entities.ScenarioEntity.TestType.MANUAL));
                    List issues = new ArrayList();
                    if(scenario.getIssues() != null)
                    {
                        IssueEntity arr$[] = scenario.getIssues();
                        int len$ = arr$.length;
                        for(int i$ = 0; i$ < len$; i$++)
                        {
                            IssueEntity issue = arr$[i$];
                            issues.add(issue.getIssueKey());
                        }

                    }
                    model.setIssues(issues);
                    model.setStatus(status);
                    model.setTags(extractTags(scenario.getTags()));
                    return model;
                }

                Set extractTags(TagEntity tags[])
                {
                    Set tagsAsStrings = new HashSet();
                    if(tags != null && tags.length > 0)
                    {
                        tagsAsStrings = new HashSet();
                        TagEntity arr$[] = tags;
                        int len$ = arr$.length;
                        for(int i$ = 0; i$ < len$; i$++)
                        {
                            TagEntity tag = arr$[i$];
                            tagsAsStrings.add(tag.getName());
                        }

                    }
                    return tagsAsStrings;
                }

                final ZipOutputStream val$zipOutput;
                final boolean val$includeManual;
                final Template val$template;
                final ExportService this$0;

            
            {
                this$0 = ExportService.this;
                zipOutput = zipoutputstream;
                includeManual = flag;
                template = template1;
                super();
            }
            }
);
        }
    }

    private static final Logger LOG = Logger.getLogger(com/hindsighttesting/jira/behave/service/ExportService);
    private final FeatureDao featureDao;
    private final ScenarioManager scenarioManager;
    private final ProjectManager projectManager;
    private final ScenarioStateCalculator statusCalculator;



}
