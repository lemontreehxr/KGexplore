/*
 * Copyright (c) 2018. by Chen Jun
 */

package cn.edu.ruc.domain;

public class Description {
    private String content;
    private String image;

    public Description(String content, String image) {
        setContent(content);
        setImage(image);
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Description{" +
                "content='" + content + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
