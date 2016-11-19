package model;

/**
 * Created by Ashwani on 16-03-2016.
 */
public class ClassModel {
    String id;
    String ClassID;


    public ClassModel(){

    }

    public ClassModel(String id1, String classid, String cname, String cvalue)
    {
        super();
        this.id = id1;
        this.ClassID = classid;
        this.ClassName = cname;
        this.ClassValue = cvalue;

    }

    public String getPosts() {
        return posts;
    }

    public void setPosts(String posts) {
        this.posts = posts;
    }

    String posts;

    public String getClassName() {
        return ClassName;
    }

    public void setClassName(String className) {
        ClassName = className;
    }

    public String getClassID() {
        return ClassID;
    }

    public void setClassID(String classID) {
        ClassID = classID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClassValue() {
        return ClassValue;
    }

    public void setClassValue(String classValue) {
        ClassValue = classValue;
    }

    String ClassName;
    String ClassValue;


    @Override
    public String toString() {
        return ClassValue;
    }
}
