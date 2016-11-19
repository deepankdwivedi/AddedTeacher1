
package model;

/**
 * Created by Ashwani on 18-03-2016.
 */
public class LectureTopicModell {

    String Lt_id;

    public String getLectureNo() {
        return LectureNo;
    }

    public void setLectureNo(String lectureNo) {
        LectureNo = lectureNo;
    }

    public String getTopicId() {
        return TopicId;
    }

    public void setTopicId(String topicId) {
        TopicId = topicId;
    }

    public String getLt_id() {
        return Lt_id;
    }

    public void setLt_id(String lt_id) {
        Lt_id = lt_id;
    }

    String LectureNo;
    String TopicId;

    public  LectureTopicModell(String ltid1, String lecno1, String topicid1){
        super();
        this.Lt_id  = ltid1;
        this.LectureNo = lecno1;
        this.TopicId = topicid1;

    }

}
