package com.talent.posdat.story;

public class Story
{
    private  String imageUrl,storyId,userId;
    private  long timestart;
    private  long timeend;

    public Story() {
    }

    public Story(String imageUrl, String storyId, String userId, long timestart, long timeend) {
        this.imageUrl = imageUrl;
        this.storyId = storyId;
        this.userId = userId;
        this.timestart = timestart;
        this.timeend = timeend;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getStoryId() {
        return storyId;
    }

    public void setStoryId(String storyId) {
        this.storyId = storyId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public long getTimestart() {
        return timestart;
    }

    public void setTimestart(long timestart) {
        this.timestart = timestart;
    }

    public long getTimeend() {
        return timeend;
    }

    public void setTimeend(long timeend) {
        this.timeend = timeend;
    }
}
