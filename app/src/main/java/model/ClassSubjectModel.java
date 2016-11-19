package model;

/**
 * Created by Ashwani on 17-03-2016.
 */
public class ClassSubjectModel {

    String classId;

    public String getSubjectId() {
        return SubjectId;
    }

    public void setSubjectId(String subjectId) {
        SubjectId = subjectId;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    String SubjectId;

    public ClassSubjectModel(String ClassId, String Sub_ID){
        super();
        this.classId = ClassId;
        this.SubjectId = Sub_ID;
    }
}
