package model;

/**
 * Created by Ashwani on 18-03-2016.
 */
public class TopicModel {
    String Topic_id;

    public String getTopicName() {
        return TopicName;
    }

    public void setTopicName(String topicName) {
        TopicName = topicName;
    }

    public String getTopic_id() {
        return Topic_id;
    }

    public void setTopic_id(String topic_id) {
        Topic_id = topic_id;
    }

    public String getChap_Id() {
        return Chap_Id;
    }

    public void setChap_Id(String chap_Id) {
        Chap_Id = chap_Id;
    }

    String TopicName;
    String Chap_Id;

    public  TopicModel(String tpicid, String topicname1, String Chapid){
        super();
        this.Topic_id = tpicid;
        this.TopicName = topicname1;
        this.Chap_Id = Chapid;

    }

}
