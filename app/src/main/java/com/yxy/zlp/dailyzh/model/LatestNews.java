package com.yxy.zlp.dailyzh.model;

import java.util.List;

public class LatestNews {
    private String date;
    private List<TopStory> top_stories;
    private List<Story> stories;


    public void setTopStories(List<TopStory> top_stories) {
        this.top_stories = top_stories;
    }

    public void setStories(List<Story> stories) {
        this.stories = stories;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<TopStory> getTop_stories() {
        return top_stories;
    }

    public List<Story> getStories() {
        return stories;
    }

    public String getDate() {
        return date;
    }

    public static class TopStory {
        private String image;
        private int type;
        private int id;
        private String title;

        public void setId(int id) {
            this.id = id;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getImage() {
            return image;
        }

        public int getType() {
            return type;
        }

    }

}
