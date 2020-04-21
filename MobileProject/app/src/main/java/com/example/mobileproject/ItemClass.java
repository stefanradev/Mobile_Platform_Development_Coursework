/************************************
 * Name: Stefan Radev
 * Matric number: S1631258
 *
 */
package com.example.mobileproject;

public class ItemClass {

    private String title;
    private String description;
    private String link;
    private String georss;
    private String pubDate;


    public ItemClass()
    {
        title = "";
        description = "";
        link = "";
        georss = "";
        pubDate = "";
    }

    public ItemClass(String atitle,String adescription,String alink,String ageorss,String apubDate)
    {
        title = atitle;
        description = adescription;
        link = alink;
        georss = ageorss;
        pubDate = apubDate;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String atitle)
    {
        title = atitle;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String adescription)
    {
        description = adescription;
    }

    public String getLink()
    {
        return link;
    }

    public void setLink(String alink)
    {
        link = alink;
    }

    /*GeoRSS will be usefull when a map representation is implemented
    public String getGeorss()
    {
        return georss;
    }

    public void setGeorss(String ageorss)
    {
        georss = ageorss;
    }
    */
    public String getPubDate()
    {
        return pubDate;
    }

    public void setPubDate(String apubDate)
    {
        pubDate = apubDate;
    }


    public String toString()
    {
        String temp;

        temp = title + description + link /*+ georss*/ + pubDate;

        return temp;
    }

}// End of class
