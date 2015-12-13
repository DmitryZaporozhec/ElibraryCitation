package org.citations.model;

import java.util.List;

/**
 * Created by zaporozhec on 12/12/15.
 */
public class Citation {


    String name;
    List<String> authors;
    String publishInfo;
    int year = -1;
    String number;
    int startPage = -1;
    int endPage = -1;

    public Citation() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPublishInfo() {
        return publishInfo;
    }

    public void setPublishInfo(String publishInfo) {
        this.publishInfo = publishInfo;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getStartPage() {
        return startPage;
    }

    public void setStartPage(int startPage) {
        this.startPage = startPage;
    }

    public int getEndPage() {
        return endPage;
    }

    public void setEndPage(int endPage) {
        this.endPage = endPage;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append(authors);
        sb.append(", ");
        sb.append(name);
        sb.append(" // ");
        sb.append(publishInfo);
        sb.append(", ");
        if (number!=null){
            sb.append(number);
            sb.append(", ");
        }
        if (year != -1)
            sb.append(year);
        if (startPage != -1) {
            sb.append("С. ");
            sb.append(startPage);
            if (endPage != -1) {
                sb.append("-");
                sb.append(endPage);
            }
        }
        sb.append("\n");
        return sb.toString();
    }
}
