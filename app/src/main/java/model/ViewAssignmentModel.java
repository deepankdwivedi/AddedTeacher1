package model;

/**
 * Created by SWASTIKA on 18-05-2016.
 */
public class ViewAssignmentModel {

    public String subject;
    public String className;
    public int assign_row_id;
    public  String sub_line;
   // public static ArrayList<ViewAssignmentModel>ar;

    public ViewAssignmentModel(String className, String sub,String subject){
       this.className=className;
        this.sub_line=sub;
        this.subject=subject;
    }

    public int getAssign_Row_id()
    {
        return assign_row_id;
    }

    public void setAssign_Row_id(int assign_row_id)
    {
        this.assign_row_id = assign_row_id;
    }

    public String getSub_line()
    {
        return sub_line;
    }

    public void setSub_line(String sub_line)
    {
        this.sub_line = sub_line;
    }
}
