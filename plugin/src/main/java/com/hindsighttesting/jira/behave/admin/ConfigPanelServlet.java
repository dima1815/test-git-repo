// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ConfigPanelServlet.java

package com.hindsighttesting.jira.behave.admin;

import com.atlassian.jira.bc.project.ProjectAction;
import com.atlassian.jira.bc.project.ProjectService;
import com.atlassian.jira.config.properties.ApplicationProperties;
import com.atlassian.jira.issue.customfields.CustomFieldUtils;
import com.atlassian.jira.project.Project;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.user.UserProjectHistoryManager;
import com.atlassian.jira.util.I18nHelper;
import com.atlassian.jira.util.http.JiraHttpUtils;
import com.atlassian.jira.util.velocity.*;
import com.atlassian.plugin.web.WebInterfaceManager;
import com.atlassian.plugin.webresource.WebResourceManager;
import com.atlassian.seraph.util.RedirectUtils;
import com.atlassian.templaterenderer.TemplateRenderer;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import org.apache.commons.io.IOUtils;

public class ConfigPanelServlet extends HttpServlet
{

    public ConfigPanelServlet(JiraAuthenticationContext authenticationContext, ProjectService service, TemplateRenderer templateRenderer, WebResourceManager webResourceManager, ApplicationProperties properties, UserProjectHistoryManager userHistoryManager, VelocityRequestContextFactory velocityRequestContextFactory, 
            WebInterfaceManager webInterfaceManager)
    {
        this.authenticationContext = authenticationContext;
        this.service = service;
        this.templateRenderer = templateRenderer;
        this.webResourceManager = webResourceManager;
        this.properties = properties;
        userProjectHistoryManager = userHistoryManager;
        this.velocityRequestContextFactory = velocityRequestContextFactory;
        this.webInterfaceManager = webInterfaceManager;
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
        String projectKey = req.getParameter("projectKey");
        if(projectKey != null)
        {
            com.atlassian.jira.bc.project.ProjectService.GetProjectResult result = service.getProjectByKeyForAction(authenticationContext.getLoggedInUser(), projectKey, ProjectAction.EDIT_PROJECT_CONFIG);
            if(!result.isValid())
            {
                if(authenticationContext.getLoggedInUser() == null)
                    redirectToLogin(req, resp);
            } else
            {
                userProjectHistoryManager.addProjectToHistory(authenticationContext.getLoggedInUser(), result.getProject());
                VelocityRequestContext requestContext = velocityRequestContextFactory.getJiraVelocityRequestContext();
                VelocityRequestSession session = requestContext.getSession();
                session.setAttribute("atl.jira.admin.current.project", result.getProject().getKey());
                session.setAttribute("atl.jira.admin.current.project.tab", "view_project_jira_behave");
                outputTab(req, resp, result.getProject());
            }
        } else
        {
            I18nHelper i18nHelper = authenticationContext.getI18nHelper();
            outputError(resp, i18nHelper.getText("admin.project.servlet.no.project"), i18nHelper.getText("common.words.error"));
        }
    }

    void redirectToLogin(HttpServletRequest req, HttpServletResponse resp)
        throws IOException
    {
        resp.sendRedirect(RedirectUtils.getLoginUrl(req));
    }

    void outputTab(HttpServletRequest request, HttpServletResponse reponse, Project project)
        throws IOException
    {
        JiraHttpUtils.setNoCacheHeaders(reponse);
        requireResources();
        Map context = new HashMap();
        context.put("project", project);
        context.put("dateFormat", CustomFieldUtils.getDateFormat());
        context.put("timeFormat", CustomFieldUtils.getTimeFormat());
        writeTemplate(reponse, "templates/project-config-panel.vm", context);
    }

    private void outputError(HttpServletResponse reponse, String errorMessage, String title)
        throws IOException
    {
        outputError(reponse, ((Collection) (Collections.singleton(errorMessage))), title);
    }

    void outputError(HttpServletResponse reponse, Collection errorMessage, String title)
        throws IOException
    {
        JiraHttpUtils.setNoCacheHeaders(reponse);
        requireResources();
        Map context = new HashMap();
        context.put("errorMessages", errorMessage);
        context.put("title", title);
        writeTemplate(reponse, "global/taberror.vm", context);
    }

    private void writeTemplate(HttpServletResponse reponse, String template, Map context)
        throws IOException
    {
        PrintWriter writer;
        reponse.setContentType(getContentType());
        writer = reponse.getWriter();
        try
        {
            templateRenderer.render(template, context, writer);
        }
        catch(IOException e)
        {
            IOUtils.closeQuietly(writer);
            throw e;
        }
        writer.close();
        break MISSING_BLOCK_LABEL_59;
        Exception exception;
        exception;
        writer.close();
        throw exception;
    }

    private String getContentType()
    {
        try
        {
            return properties.getContentType();
        }
        catch(Exception e)
        {
            return "text/html; charset=UTF-8";
        }
    }

    private void requireResources()
    {
        webResourceManager.requireResourcesForContext("jira.admin.conf");
    }

    private static final String TEMPLATE_TAB = "templates/project-config-panel.vm";
    private static final String TEMPLATE_ERROR = "global/taberror.vm";
    private final JiraAuthenticationContext authenticationContext;
    private final ProjectService service;
    private final TemplateRenderer templateRenderer;
    private final WebResourceManager webResourceManager;
    private final ApplicationProperties properties;
    private final UserProjectHistoryManager userProjectHistoryManager;
    private final VelocityRequestContextFactory velocityRequestContextFactory;
    private final WebInterfaceManager webInterfaceManager;
}
