package com.example.jegerima.SIDWeb;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jegerima on 25/01/2015.
 */
public class SWItemsGroup {
    public String string;
    public final List<String> children = new ArrayList<String>();

    public SWItemsGroup(String string)
    {
        this.string = string;
    }
}
