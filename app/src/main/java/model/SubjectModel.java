package model;

/**
 * Created by Ashwani on 16-03-2016.
 */
public class SubjectModel {


    public SubjectModel(){

    }

    public SubjectModel(String id1, String subjectid1, String subname, String CId){
        super();
        this.id = id1;
        this.subjectid = subjectid1;
        this.subjectname = subname;
        this.cId = CId;
    }

    String id;

    public String getSubjectid() {
        return subjectid;
    }

    public void setSubjectid(String subjectid) {
        this.subjectid = subjectid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubjectname() {
        return subjectname;
    }

    public void setSubjectname(String subjectname) {
        this.subjectname = subjectname;
    }

    public String getcId() {
        return cId;
    }

    public void setcId(String cId) {
        this.cId = cId;
    }

    String subjectid;
    String subjectname;
    String cId;

    @Override
    public String toString() {
        return subjectname;
    }
}
