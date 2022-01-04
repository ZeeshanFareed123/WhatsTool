package com.mobi.whatstool.statussaver.directchat.unseen.hiddenchat.services;

public interface FileListener {
    public void onFileCreated(String name);
    public void onFileDeleted(String name);
    public void onFileModified(String name);
    public void onFileRenamed(String oldName, String newName);
}
