package hu.petabyte.redflags.engine.epforgpl;

import hu.petabyte.redflags.engine.model.NoticeID;
import hu.petabyte.redflags.engine.scope.AbstractScope;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * Created by marcin on 11.06.2016.
 */
@Component
public class EPFScope extends AbstractScope {

    private static final Logger LOG = LoggerFactory.getLogger(EPFScope.class);


    @Autowired
    private EPFData dataSource = new EPFData();

    private final static int PAGE_SIZE = 10000;

    private Iterator<String> itr;

    private boolean first = false;

    private int currentPage = 0;

    private long count;

    public EPFScope() {
    }

    private void parseAnswer(Answer a) throws JSONException {
        List<String> ids = new ArrayList<>();
        JSONArray json = new JSONObject(a.getBody()).getJSONArray("Dataobject");
        for (int i = 0; i < json.length(); i++) {
            JSONObject objectInArray = json.getJSONObject(i);
            String id = objectInArray.getString("id");
            ids.add(id);
        }
        itr = ids.iterator();
    }

    @Override
    public boolean hasNext() {
        /*if(first){
            return true;
        }*/
        boolean hasNext = itr != null ? hasNext = itr.hasNext() : false;

        if(!hasNext){
            LOG.info("loading procurements. Page number: " + (currentPage+1));
            try {
                if(itr==null){
                    parseAnswer(dataSource.getProcurementIdsAnswer(++currentPage, PAGE_SIZE));
                } else {
                    return false;
                }
                hasNext = itr.hasNext();
            } catch (JSONException e) {
                LOG.error("error while parsing scope page", e);
            }
        }
        count++;
        if(!hasNext){
            LOG.info("scope processing finished");
        }
        return hasNext;
    }

    @Override
    public NoticeID next() {
        int nId = Integer.parseInt(itr.next());
        LOG.info("processing notice " + nId + ", " + count + " procurement in row");
        return new BZPNoticeId(nId);
    }

}
