package com.aiyafocus.taotao.common.bo;

/**
 * 首页大广告需要的数据封装的对象
 *
 * @author AiyaFocus
 * createDate 2020/6/15 21:07
 */
public class BigContent {
    private String src;
    private String srcB;
    private Integer width = 670;
    private Integer widthB = 550;
    private Integer height = 240;
    private Integer heightB = 240;
    private String href;
    private String alt;

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getSrcB() {
        return srcB;
    }

    public void setSrcB(String srcB) {
        this.srcB = srcB;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getWidthB() {
        return widthB;
    }

    public void setWidthB(Integer widthB) {
        this.widthB = widthB;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getHeightB() {
        return heightB;
    }

    public void setHeightB(Integer heightB) {
        this.heightB = heightB;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public BigContent() {
    }

    public BigContent(String src, String srcB, String href, String alt) {
        this.src = src;
        this.srcB = srcB;
        this.href = href;
        this.alt = alt;
    }
}
