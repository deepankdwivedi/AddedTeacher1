package model;

import java.util.ArrayList;

/**
 * Created by Asus 1 on 6/13/2016.
 */
public class ResponseReceiveModel {
    public String columnName,columnValue;
    public static ArrayList<ResponseReceiveModel> responseReceiveArrayList;

    public ResponseReceiveModel(String columnName,String columnValue)
    {
        this.columnName=columnName;
        this.columnValue = columnValue;

    }
}
