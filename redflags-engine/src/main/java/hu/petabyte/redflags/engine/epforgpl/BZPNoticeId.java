package hu.petabyte.redflags.engine.epforgpl;

import hu.petabyte.redflags.engine.model.NoticeID;

/**
 * Created by marcin on 18.09.2016.
 */
public class BZPNoticeId extends NoticeID {


    private String position;

    private String year;


    public BZPNoticeId(int id) {
        super(id, "PL");
    }

    public void setUrlParameters(String position, String year){
        this.position = position;
        this.year = year;
    }

    public String calculateURL(){
        return String.format("http://bzp0.portal.uzp.gov.pl/index.php?ogloszenie=show&pozycja=%s&rok=%s", this.position, this.year);
    }

    @Override
    public String toString() {
        return "" + id;
    }
}
