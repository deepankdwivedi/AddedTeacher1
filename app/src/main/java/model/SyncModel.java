package model;

import java.util.ArrayList;

/**
 * Created by ASUS on 09-04-2016.
 */
public class SyncModel {
    public String table_name;
    public int lastupdate;
    public static  ArrayList<SyncModel> added_sync_array_list;

    public SyncModel(int lastupdate, String table_name )
    {
        this.lastupdate=lastupdate;
        this.table_name=table_name;
    }
}
