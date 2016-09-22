package hu.petabyte.redflags.engine.epforgpl;

import hu.petabyte.redflags.engine.model.NoticeID;

/**
 * Created by marcin on 18.09.2016.
 */
public class BZPNoticeId extends NoticeID {


    public BZPNoticeId(int id) {
        super(id, "PL");
    }
    public String calculateURL(){
        return String.format("https://api-v3.mojepanstwo.pl/dane/zamowienia_publiczne/%s", this.id);
    }

    @Override
    public String toString() {
        return "" + id;
    }
}
