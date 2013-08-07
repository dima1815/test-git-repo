// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   FeatureImport.java

package com.hindsighttesting.jira.behave.admin.cukeimport;

import com.hindsighttesting.jira.behave.service.*;
import com.hindsighttesting.jira.behave.service.exceptions.InvalidRequestException;
import gherkin.formatter.Formatter;
import gherkin.formatter.model.*;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FeatureImport
    implements Formatter
{

    public FeatureImport(String projectKey, FeatureManager featureManager, ScenarioManager scenarioManager)
    {
        scenarioSteps = new HashMap();
        scenarioTypes = new HashMap();
        scenarioTags = new HashMap();
        fail = false;
        this.projectKey = projectKey;
        this.featureManager = featureManager;
        this.scenarioManager = scenarioManager;
    }

    public void uri(String s)
    {
    }

    public void feature(Feature feature)
    {
        this.feature = feature;
    }

    public void background(Background background)
    {
        currentStepBlock = new StringWriter();
        this.background = currentStepBlock;
    }

    public void scenario(Scenario scenario)
    {
        scenarioTypes.put(scenario.getName(), Boolean.valueOf(false));
        currentStepBlock = new StringWriter();
        scenarioSteps.put(scenario.getName(), currentStepBlock);
        scenarioTags.put(scenario.getName(), scenario.getTags());
    }

    public void scenarioOutline(ScenarioOutline scenarioOutline)
    {
        scenarioTypes.put(scenarioOutline.getName(), Boolean.valueOf(true));
        currentStepBlock = new StringWriter();
        scenarioSteps.put(scenarioOutline.getName(), currentStepBlock);
        scenarioTags.put(scenarioOutline.getName(), scenarioOutline.getTags());
    }

    public void examples(Examples examples)
    {
        currentStepBlock.append("\n  ");
        printComments(examples.getComments(), "    ", currentStepBlock);
        currentStepBlock.append((new StringBuilder()).append(examples.getKeyword()).append(":").toString());
        currentStepBlock.append("\n");
        printTable(examples.getRows(), "    ", currentStepBlock);
    }

    public void step(Step step)
    {
        printComments(step.getComments(), "  ", currentStepBlock);
        if(step.getKeyword() != null && step.getName() != null)
        {
            currentStepBlock.append((new StringBuilder()).append(step.getKeyword()).append(step.getName()).toString());
            currentStepBlock.append("\n");
        } else
        {
            log.debug((new StringBuilder()).append("Step keyword: ").append(step.getKeyword() != null).toString());
            log.debug((new StringBuilder()).append("Step name: ").append(step.getName() != null).toString());
        }
        if(step.getDocString() != null)
        {
            currentStepBlock.append("  \"\"\"\n");
            currentStepBlock.append((new StringBuilder()).append("  ").append(step.getDocString().getValue()).toString());
            currentStepBlock.append("\n  \"\"\"\n");
        }
        if(step.getRows() != null)
            printTable(step.getRows(), "  ", currentStepBlock);
    }

    private void printTable(List rows, String indenting, StringWriter writer)
    {
        int columnCount = ((Row)rows.get(0)).getCells().size();
        Integer cellWidth[] = new Integer[columnCount];
        for(Iterator i$ = rows.iterator(); i$.hasNext();)
        {
            Row row = (Row)i$.next();
            List cells = row.getCells();
            int i = 0;
            while(i < columnCount) 
            {
                int width = ((String)cells.get(i)).length();
                cellWidth[i] = Integer.valueOf(Math.max(cellWidth[i] != null ? cellWidth[i].intValue() : 0, width));
                i++;
            }
        }

        for(Iterator i$ = rows.iterator(); i$.hasNext(); writer.append("\n"))
        {
            Row row = (Row)i$.next();
            writer.append((new StringBuilder()).append(indenting).append("| ").toString());
            List cells = row.getCells();
            for(int i = 0; i < columnCount; i++)
            {
                int padding = cellWidth[i].intValue() - ((String)cells.get(i)).length();
                writer.append((CharSequence)cells.get(i));
                for(int j = 0; j < padding; j++)
                    writer.append(" ");

                writer.append(" | ");
            }

        }

    }

    public void eof()
    {
        if(!fail)
            try
            {
                boolean allManual = false;
                FeatureModel featureModel = new FeatureModel(feature.getName());
                featureModel.setDescription(feature.getDescription());
                if(feature.getTags().size() > 0)
                {
                    Set newTags = new HashSet();
                    allManual = convertTags(feature.getTags(), newTags);
                    featureModel.setTags(newTags);
                }
                if(background != null)
                    featureModel.setBackground(background.toString());
                FeatureModel parent = featureManager.addFeature(projectKey, featureModel);
                ScenarioModel scenario;
                for(Iterator i$ = scenarioTypes.keySet().iterator(); i$.hasNext(); scenarioManager.addScenario(parent.getId(), scenario))
                {
                    String name = (String)i$.next();
                    scenario = new ScenarioModel();
                    scenario.setName(name);
                    scenario.setOutline((Boolean)scenarioTypes.get(name));
                    scenario.setProjectKey(projectKey);
                    String steps = new String(((StringWriter)scenarioSteps.get(name)).toString().getBytes("UTF-8"));
                    scenario.setSteps(steps);
                    boolean manual = false;
                    if(((List)scenarioTags.get(name)).size() > 0)
                    {
                        Set newTags = new HashSet();
                        manual = convertTags((List)scenarioTags.get(name), newTags);
                        scenario.setTags(newTags);
                    }
                    scenarioTags.get(name);
                    scenario.setManual(Boolean.valueOf(manual || allManual));
                    log.debug((new StringBuilder()).append("Importing Scenario: ").append(name).toString());
                    log.debug(steps);
                }

            }
            catch(InvalidRequestException e)
            {
                fail = true;
            }
            catch(UnsupportedEncodingException e)
            {
                fail = true;
                log.warn("Could not change string encoding type", e);
            }
    }

    private boolean convertTags(List tags, Set results)
    {
        boolean manual = false;
        for(Iterator i$ = tags.iterator(); i$.hasNext();)
        {
            Tag tag = (Tag)i$.next();
            String tagName = tag.getName().substring(1);
            if(tagName.equalsIgnoreCase("manual"))
                manual = true;
            else
                results.add(tagName);
        }

        return manual;
    }

    public void syntaxError(String state, String event, List legalEvents, String uri, Integer line)
    {
        fail = true;
    }

    public void done()
    {
    }

    public void close()
    {
    }

    private void printComments(List comments, String indenting, StringWriter writer)
    {
        Comment comment;
        for(Iterator i$ = comments.iterator(); i$.hasNext(); writer.append((new StringBuilder()).append(indenting).append(comment.getValue()).toString()).append("\n"))
            comment = (Comment)i$.next();

    }

    public boolean isSucessful()
    {
        return !fail;
    }

    private static final Logger log = LoggerFactory.getLogger(com/hindsighttesting/jira/behave/admin/cukeimport/FeatureImport);
    private final FeatureManager featureManager;
    private final ScenarioManager scenarioManager;
    private final String projectKey;
    private Feature feature;
    private Map scenarioSteps;
    private Map scenarioTypes;
    private Map scenarioTags;
    private StringWriter currentStepBlock;
    private StringWriter background;
    private boolean fail;

}
