package model;

/**
 * Created by Ashwani on 18-03-2016.
 */
public class LectureModel {

    String L_id;

    public String getChapterID() {
        return ChapterID;
    }

    public void setChapterID(String chapterID) {
        ChapterID = chapterID;
    }

    public String getL_id() {
        return L_id;
    }

    public void setL_id(String l_id) {
        L_id = l_id;
    }

    public String getTotalLec() {
        return TotalLec;
    }

    public void setTotalLec(String totalLec) {
        TotalLec = totalLec;
    }

    String ChapterID;
    String TotalLec;

    public LectureModel(String lid1, String chapid1, String tole){

        super();
        this.L_id = lid1;
        this.ChapterID = chapid1;
        this.TotalLec = tole;

    }

}
