package hu.petabyte.redflags.engine.epforgpl;

import hu.petabyte.redflags.engine.epforgpl.Answer;
import hu.petabyte.redflags.engine.epforgpl.Connector;
import hu.petabyte.redflags.engine.model.Notice;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by marcin on 11.06.2016.
 */
@Component
public class EPFData {


    private static final Logger LOG = LoggerFactory.getLogger(EPFData.class);

    protected String dataHost = "https://api-v3.mojepanstwo.pl/dane";

    private Connector connector;

    public EPFData(){
        connector = new Connector(dataHost);
    }

    /*public List<String> getProcurementIds(){

        try {
            JSONArray json = new JSONObject(a.getBody()).getJSONArray("Dataobject");
            for (int i = 0; i < json.length(); i++) {
                JSONObject objectInArray = json.getJSONObject(i);
                String id = objectInArray.getString("id");
                ids.add(id);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ids;
    }*/

    public Answer getProcurementIdsAnswer(Integer pageNo, Integer pageSize) {
        LOG.info("fetching procurement list");
        Answer a = connector.fetch("zamowienia_publiczne/?limit=" + pageSize + "&page=" + pageNo);
        return a;
    }

    public Answer getProcurement(Notice n){
        return connector.fetch("zamowienia_publiczne/" + n.getId().get());
    }

    public Answer getProcurementDocument(Notice n){
        return connector.fetch("zamowienia_publiczne_dokumenty?conditions[zamowienia_publiczne_dokumenty.parent_id]=" + n.getId().get() + "&fields[]=details" );
    }

    public Answer getProcurementExecutive(Notice n){
        return connector.fetch("zamowienia_publiczne_wykonawcy/" + n.getId().get());
    }

    public Answer getProcurementPurchaser(String purchaserId){
        return connector.fetch("zamowienia_publiczne_zamawiajacy/" + purchaserId);
    }

}
