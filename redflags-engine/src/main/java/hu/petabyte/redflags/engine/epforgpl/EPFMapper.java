package hu.petabyte.redflags.engine.epforgpl;

import hu.petabyte.redflags.engine.model.*;
import hu.petabyte.redflags.engine.model.noticeparts.ContractingAuthority;
import hu.petabyte.redflags.engine.model.noticeparts.LEFTInfo;
import hu.petabyte.redflags.engine.model.noticeparts.ObjOfTheContract;
import hu.petabyte.redflags.engine.model.noticeparts.Procedure;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by marcin on 13.06.2016.
 */
public class EPFMapper {

    private static final Logger LOG = LoggerFactory.getLogger(EPFMapper.class);

    private JSONObject json;

    private JSONObject caJson;

    private JSONObject docsJson;

    public EPFMapper(String json) throws JSONException {
        this.json = new JSONObject(json);
    }

    public void feed(String contrAuthJson, String docsBody) throws JSONException {
        this.caJson = new JSONObject(contrAuthJson);
        this.docsJson = new JSONObject(docsBody);
    }

    public String getPurchaserId(){
        try {
            JSONObject jsonObject = json.getJSONObject("data");
            return jsonObject.getString("zamowienia_publiczne_zamawiajacy.id");
        } catch (JSONException e) {
            LOG.warn("exception while parsing purchuaser", e);
        }
        return null;
    }

    public void fillNoticeId(BZPNoticeId id){
        try {
            JSONObject jsonObject = json.getJSONObject("data");
            id.setUrlParameters(jsonObject.getString("zamowienia_publiczne.pozycja"),
                    jsonObject.getString("zamowienia_publiczne.data_publikacji"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public Notice getNotice(Notice n){

        JSONObject data = null;
        try {
            data = json.getJSONObject("data");
        } catch (JSONException e) {
            LOG.warn("exception while parsing procurement data", e);
        }

        fillNoticeId((BZPNoticeId) n.getId());

        n.setLeft(getLEFTInfo());

        n.setContr(getContractibnAuthority());

        n.getData().setCountry("PL");
        n.getData().setOriginalLanguage("PL");

        n.getData().setTitle(findValue(data, "zamowienia_publiczne.nazwa"));
        n.getData().setContractType(Type.findOrCreate(
                findValue(data, "zamowienia_publiczne_rodzaje.id"),
                findValue(data, "zamowienia_publiczne_rodzaje.nazwa")));
        n.getData().setPublicationDate(findDate(data, "zamowienia_publiczne.data_publikacji", "yyyy-MM-dd"));

        n.getObjs().add(getObjOfContract());

        n.setProc(getProcedure());

        return n;
    }


    private Procedure getProcedure() {
        Procedure p = new Procedure();
        JSONObject jsonObject = null;
        try {
            jsonObject = json.getJSONObject("data");
            p.setProcedureTypeInfo(jsonObject.getString("zamowienia_publiczne_tryby.nazwa"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return p;

    }

    private ObjOfTheContract getObjOfContract() {
        ObjOfTheContract obj = new ObjOfTheContract();
        try {
            JSONObject jsonObject = json.getJSONObject("data");

            Duration d = new Duration();
            d.setInMonths(jsonObject.getInt("zamowienia_publiczne.liczba_miesiecy"));
            obj.setDuration(d);

            obj.setLots(jsonObject.getString("zamowienia_publiczne.oferty_czesciowe"));
            obj.setContractTitle(jsonObject.getString("zamowienia_publiczne.nazwa"));
            obj.setShortDescription(jsonObject.getString("zamowienia_publiczne.przedmiot"));

            JSONArray jsonArray = docsJson.getJSONArray("Dataobject");
            for(int i=0; i<jsonArray.length(); i++){
                JSONObject o1 = (JSONObject) jsonArray.get(i);
                try {
                    String offersCount = o1.getJSONObject("details").getJSONObject("czesci-wykonawcy")
                            .getJSONObject("").getString("liczba_ofert");
                    if (offersCount!=null && !offersCount.trim().isEmpty()) {
                        obj.setOffersCount(Integer.parseInt(offersCount));
                    }
                } catch (JSONException e) {
                    LOG.trace("error while mapping czesci-wykonawcy in contracting authority", e);
                }
            }

        } catch (Exception e) {
            LOG.trace("error while mapping objofcontract", e);
        }

        return obj;
    }

    public LEFTInfo getLEFTInfo(){

        LEFTInfo left = new LEFTInfo();
        try {
            JSONArray jsonArray = docsJson.getJSONArray("Dataobject");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject o1 = (JSONObject) jsonArray.get(i);
                try {
                    String financialCondition = o1.getJSONObject("details").getString("sytuacja_ekonomiczna");
                    if (financialCondition != null && !financialCondition.trim().isEmpty()) {
                        left.setFinancialAbility(financialCondition);
                    }
                } catch (JSONException e) {
                    LOG.trace("error while mapping sytuacja_ekonomiczna", e);
                }

                try {
                    String experience = o1.getJSONObject("details").getString("wiedza");
                    if (experience != null && !experience.trim().isEmpty()) {
                        left.setTechnicalCapacity(experience);
                    }
                } catch (JSONException e) {
                    LOG.trace("error while mapping wiedza", e);
                }
            }
        }catch(JSONException e){
            LOG.trace("error wjile parsing LEFT", e);
        }
        return left;
    }

    public ContractingAuthority getContractibnAuthority(){

        ContractingAuthority ca = new ContractingAuthority();
        Organization o = new Organization();
        ca.setContractingOrg(o);
        Address a = new Address();
        o.setAddress(a);

        try {

            JSONObject jsonObject = caJson.getJSONObject("data");

            ca.setId(jsonObject.getString("zamowienia_publiczne_zamawiajacy.id"));


            o.setName(findValue(jsonObject, "zamowienia_publiczne_zamawiajacy.nazwa"));
            o.setType(findValue(jsonObject, "zamowienia_publiczne_zamawiajacy.rodzaj"));


            a.setFax(jsonObject.getString("zamowienia_publiczne_zamawiajacy.fax"));
            a.setZip(jsonObject.getString("zamowienia_publiczne_zamawiajacy.kod_pocztowy"));
            a.setCity(jsonObject.getString("zamowienia_publiczne_zamawiajacy.miejscowosc"));
            a.setEmail(jsonObject.getString("zamowienia_publiczne_zamawiajacy.email"));
            a.setStreet(jsonObject.getString("zamowienia_publiczne_zamawiajacy.ulica"));
            a.setPhone(jsonObject.getString("zamowienia_publiczne_zamawiajacy.telefon"));
            a.setUrl(jsonObject.getString("zamowienia_publiczne_zamawiajacy.www"));
            a.setCountry("POLSKA");
            a.setRaw(caJson.toString());

        } catch (Exception e) {
            LOG.trace("error while mapping contracting authority", e);
        }

        return ca;
    }

    public String findValue(JSONObject json, String key) {
        return findValue(json, key, null);
    }


    public String findValue(JSONObject json, String key, String defaultValue){
        try {
            if(!json.has(key)){
                return defaultValue;
            } else {
                return json.getString(key);
            }
        } catch (JSONException e) {
            LOG.trace("error while accessing key " + key);
        }
        return defaultValue;
    }

    public Date findDate(JSONObject json, String key, String format){
        try {
            return new SimpleDateFormat(format).parse(findValue(json, key));
        } catch (ParseException e) {
            LOG.warn("error while parsing date", e);
            return null;
        }
    }


}
