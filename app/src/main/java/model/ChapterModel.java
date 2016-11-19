package model;

/**
 * Created by Ashwani on 17-03-2016.
 */
public class ChapterModel {

    public ChapterModel(){

    }

    String ID;

    public String getClassID() {
        return classID;
    }

    public void setClassID(String classID) {
        this.classID = classID;
    }

    String classID;

    public String getChapId() {
        return chapId;
    }

    public void setChapId(String chapId) {
        this.chapId = chapId;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getChapName() {
        return chapName;
    }

    public void setChapName(String chapName) {
        this.chapName = chapName;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    String chapId;
    String chapName;
    String subjectId;

    public ChapterModel(String id1, String chapid1, String chapname1, String subjectId1, String classid1){
        super();
        this.ID = id1;
        this.chapId = chapid1;
        this.chapName = chapname1;
        this.subjectId = subjectId1;
        this.classID = classid1;
    }

    @Override
    public String toString() {
        return chapName;
    }

}
