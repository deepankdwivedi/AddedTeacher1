package model;

/**
 * Created by Ashwani on 18-03-2016.
 */
public class Lecture_InsightModel {


    String RowID;
    String TopicId;
    String Obective;

    public String getIntro() {
        return Intro;
    }

    public void setIntro(String intro) {
        Intro = intro;
    }

    public String getRowID() {
        return RowID;
    }

    public void setRowID(String rowID) {
        RowID = rowID;
    }

    public String getTopicId() {
        return TopicId;
    }

    public void setTopicId(String topicId) {
        TopicId = topicId;
    }

    public String getObective() {
        return Obective;
    }

    public void setObective(String obective) {
        Obective = obective;
    }

    public String getQuestion() {
        return Question;
    }

    public void setQuestion(String question) {
        Question = question;
    }

    public String getActivity() {
        return Activity;
    }

    public void setActivity(String activity) {
        Activity = activity;
    }

    public String getAssignment() {
        return Assignment;
    }

    public void setAssignment(String assignment) {
        Assignment = assignment;
    }

    public String getDuration() {
        return Duration;
    }

    public void setDuration(String duration) {
        Duration = duration;
    }

    String Intro;
    String Question;
    String Activity;
    String Assignment;
    String Duration;


    public Lecture_InsightModel( String RowID1,String TopicId1,String  Obective1,String Intro1,String Question1,
                                 String Activity1,String Assignment1,String Duration1){


        super();
        this.RowID = RowID1;
        this.TopicId = TopicId1;
        this.Obective = Obective1;
        this.Intro = Intro1;
        this.Question = Question1;
        this.Activity = Activity1;
        this.Assignment = Assignment1;
        this.Duration = Duration1;

    }
}
