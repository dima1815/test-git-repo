// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ImportJob.java

package com.hindsighttesting.jira.behave.admin.cukeimport;

import java.io.File;

public class ImportJob
{
    public static final class Status extends Enum
    {

        public static Status[] values()
        {
            return (Status[])$VALUES.clone();
        }

        public static Status valueOf(String name)
        {
            return (Status)Enum.valueOf(com/hindsighttesting/jira/behave/admin/cukeimport/ImportJob$Status, name);
        }

        public static final Status INPROGRESS;
        public static final Status FAILED;
        public static final Status DONE;
        public static final Status READY;
        private static final Status $VALUES[];

        static 
        {
            INPROGRESS = new Status("INPROGRESS", 0);
            FAILED = new Status("FAILED", 1);
            DONE = new Status("DONE", 2);
            READY = new Status("READY", 3);
            $VALUES = (new Status[] {
                INPROGRESS, FAILED, DONE, READY
            });
        }

        private Status(String s, int i)
        {
            super(s, i);
        }
    }


    public ImportJob(int id, String projectKey)
    {
        this.id = id;
        this.projectKey = projectKey;
    }

    public int getId()
    {
        return id;
    }

    public synchronized Status getStatus()
    {
        return status;
    }

    public synchronized String getMessage()
    {
        return message;
    }

    public File getFile()
    {
        return file;
    }

    public void setFile(File file)
    {
        this.file = file;
    }

    public synchronized void update(String message, Status status)
    {
        this.message = message;
        this.status = status;
    }

    public synchronized void update(Status status)
    {
        this.status = status;
    }

    public String getProjectKey()
    {
        return projectKey;
    }

    private final int id;
    private Status status;
    private String message;
    private File file;
    private final String projectKey;
}
