// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   QUnitStoryBoardServlet.java

package com.hindsighttesting.jira.behave.servlet;

import com.atlassian.templaterenderer.TemplateRenderer;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.*;

public class QUnitStoryBoardServlet extends HttpServlet
{

    public QUnitStoryBoardServlet(TemplateRenderer renderer)
    {
        this.renderer = renderer;
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException
    {
        response.setContentType("text/html;charset=utf-8");
        renderer.render("templates/qunit/storyboard.vm", response.getWriter());
    }

    private final TemplateRenderer renderer;
}
