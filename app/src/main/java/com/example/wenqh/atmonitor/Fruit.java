package com.example.wenqh.atmonitor;

/**
 * Created by wenqh on 2017/3/9.
 */

public class Fruit
{
    public Fruit(String name, int pic_id)
    {
        this.name = name;
        this.imageId = pic_id;
    }

    private String name;
    private int imageId;

    public int getImageId()
    {
        return this.imageId;
    }

    public String getName()
    {
        return name;
    }
}
