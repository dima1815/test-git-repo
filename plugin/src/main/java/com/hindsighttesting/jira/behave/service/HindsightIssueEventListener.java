// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   HindsightIssueEventListener.java

package com.hindsighttesting.jira.behave.service;

import com.atlassian.event.api.EventPublisher;
import com.atlassian.jira.event.issue.AbstractIssueEventListener;
import com.atlassian.jira.event.issue.IssueEvent;
import com.atlassian.jira.issue.Issue;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

// Referenced classes of package com.hindsighttesting.jira.behave.service:
//            IssueRelationService

public class HindsightIssueEventListener extends AbstractIssueEventListener
    implements InitializingBean, DisposableBean
{

    public HindsightIssueEventListener(IssueRelationService relationManager, EventPublisher eventPublisher)
    {
        this.relationManager = relationManager;
        this.eventPublisher = eventPublisher;
    }

    public void issueDeleted(IssueEvent event)
    {
        Issue issue = event.getIssue();
        relationManager.deleteIssue(issue.getKey());
    }

    public void afterPropertiesSet()
        throws Exception
    {
        eventPublisher.register(this);
    }

    public void destroy()
        throws Exception
    {
        eventPublisher.unregister(this);
    }

    private final IssueRelationService relationManager;
    private final EventPublisher eventPublisher;
}
