package com.syrovama.notestask;;

public class Note {
    private long mId;
    private String mTitle;
    private String mContent;

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String mContent) {
        this.mContent = mContent;
    }

    public void setId(long mId) {
        this.mId = mId;
    }

    public long getId() {
        return mId;
    }
}
