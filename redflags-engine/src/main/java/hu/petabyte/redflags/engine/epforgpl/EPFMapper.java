package hu.petabyte.redflags.engine.epforgpl;

import hu.petabyte.redflags.engine.model.*;
import hu.petabyte.redflags.engine.model.noticeparts.ContractingAuthority;
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

    private String json;

    private String caJson;

    private String docsJson;

    public EPFMapper(String json, String contrAuthJson, String docsBody) {
        System.out.println(json);
        this.json = json;
        this.caJson = contrAuthJson;
        this.docsJson = docsBody;
    }

    public static String getPurchaserId(String procurementJson){
        try {
            JSONObject jsonObject = new JSONObject(procurementJson).getJSONObject("data");
            return jsonObject.getString("zamowienia_publiczne_zamawiajacy.id");
        } catch (JSONException e) {
            LOG.warn("exception while parsing purchuaser", e);
        }
        return null;
    }

    public Notice getNotice(Notice n){

        JSONObject data = null;
        try {
            data = getProcurementJSON().getJSONObject("data");
        } catch (JSONException e) {
            LOG.warn("exception while parsing procurement data", e);
        }


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
        /*
        "zamowienia_publiczne_typy.id":"1",
      "zamowienia_publiczne_typy.symbol":"ZP-400",
      "zamowienia_publiczne_typy.nazwa":"Og\u0142oszenie o zam\u00f3wieniu",
    + "zamowienia_publiczne_rodzaje.id":"1",
    + "zamowienia_publiczne_rodzaje.nazwa":"Roboty budowlane",
      "zamowienia_publiczne_zamawiajacy.id":"5150",
      "zamowienia_publiczne_zamawiajacy.kod_pocztowy_id":"20206",
    ? "zamowienia_publiczne_zamawiajacy.nazwa":"Centralny O\u015brodek Sportu - O\u015brodek Przygotowa\u0144 Olimpijskich w Zakopanem",
      "zamowienia_publiczne_tryby.id":"1",
      "zamowienia_publiczne_tryby.nazwa":"Przetarg nieograniczony",
      "zamowienia_publiczne.liczba_miesiecy":0,
      "zamowienia_publiczne.pozycja":81437,
      "zamowienia_publiczne.wartosc_cena_min":0,
      "zamowienia_publiczne.akcept":"1",
    ? "zamowienia_publiczne.zamawiajacy_ulica":"ul. Bronis\u0142awa Czecha 1",
      "zamowienia_publiczne.rodzaj_id":"1",
      "zamowienia_publiczne.uniewaznienie":"0",
    ? "zamowienia_publiczne.zamawiajacy_email":"zamowienia.zakopane@cos.pl",
      "zamowienia_publiczne.liczba_dni":0,
      "zamowienia_publiczne.aukcja":"0",
      "zamowienia_publiczne.oferty_czesciowe":"0",
      "zamowienia_publiczne.dyn_www":"",
    ? "zamowienia_publiczne.zamawiajacy_www":"http:\/\/bip.cos.pl\/strony,0,72-zakopane",
      "zamowienia_publiczne.status_id":"0",
      "zamowienia_publiczne.dsz_www":"",
      "zamowienia_publiczne.wartosc_szacunkowa":0,
    ? "zamowienia_publiczne.zamawiajacy_nazwa":"Centralny O\u015brodek Sportu - O\u015brodek Przygotowa\u0144 Olimpijskich w Zakopanem",
      "zamowienia_publiczne.sprawozdanie_calosc":"0",
      "zamowienia_publiczne.projekt_ue":"",
      "zamowienia_publiczne.zmiana_ogloszenia":"0",
      "zamowienia_publiczne.typ_id":"1",
      "zamowienia_publiczne.zamawiajacy_miejscowosc":"Zakopane",
      "zamowienia_publiczne.zamawiajacy_rodzaj_inny":"Instytucja Gospodarki Bud\u017cetowej",
      "zamowienia_publiczne.wartosc_szacowana":0,
      "zamowienia_publiczne.publikacja_obowiazkowa":"1",
      "zamowienia_publiczne.wariant":"0",
      "zamowienia_publiczne.tryb_id":"1",
    ? "zamowienia_publiczne.zamawiajacy_fax":"18 2014906",
      "zamowienia_publiczne.zamowienie_ue":"0",
      "zamowienia_publiczne.oferty_godz":"10:00",
      "zamowienia_publiczne.data_publikacji":"2016-06-12",
      "zamowienia_publiczne.wartosc_cena_max":0,
      "zamowienia_publiczne.zamawiajacy_wojewodztwo":"ma\u0142opolskie",
      "zamowienia_publiczne.id":"2827004",
      "zamowienia_publiczne.wykonawca_str":"",
      "zamowienia_publiczne.ogloszenie_bzp":"0",
      "zamowienia_publiczne.zamawiajacy_regon":"14273335600042",
    + "zamowienia_publiczne.nazwa":"Rozbudowa skoczni narciarskiej Wielka Krokiew im. Stanis\u0142awa Marusarza wraz z infrastruktur\u0105 towarzysz\u0105c\u0105 wraz z rozbi\u00f3rk\u0105 istniej\u0105cego progu kamiennego, drewnianej konstrukcji najazdu i zeskoku w Centralnym O\u015brodku Sportu-O\u015brodku Przygotowa\u0144 Olimpijskich w Zakopanem",
      "zamowienia_publiczne.liczba_dni_oferty":30,
      "zamowienia_publiczne.instytucja_id":"0",
    ? "zamowienia_publiczne.zamawiajacy_kod_poczt":"34-500",
      "zamowienia_publiczne.sprawozdanie_lata_obrotowe":"0",
      "zamowienia_publiczne.wartosc_cena":0,
      "zamowienia_publiczne.dialog":"0",
      "zamowienia_publiczne.zamawiajacy_nr_domu":"1",
      "zamowienia_publiczne.gmina_id":"1575",
      "zamowienia_publiczne.numer_zamowienia":0,
      "zamowienia_publiczne.zamawiajacy_nr_miesz":"",
      "zamowienia_publiczne.liczba_czesci":0,
      "zamowienia_publiczne.data_stop":"2017-07-31",
      "zamowienia_publiczne.powiat_id":"322",
      "zamowienia_publiczne.zamawiajacy_tel":"18 2012274",
      "zamowienia_publiczne.czas":"D",
      "zamowienia_publiczne.kod_pocztowy_id":"20206",
      "zamowienia_publiczne.termin":"O",
      "zamowienia_publiczne.liczba_wykonawcow":0,
      "zamowienia_publiczne.wojewodztwo_id":"6",
      "zamowienia_publiczne.uzupelniajace":"0",
      "zamowienia_publiczne.zaliczka":"0",
      "zamowienia_publiczne.zamawiajacy_rodzaj":"Inny: Instytucja Gospodarki Bud\u017cetowej",
      "zamowienia_publiczne.numer_biuletynu":1,
      "zamowienia_publiczne.zmiana_umowy":"1",
      "zamowienia_publiczne.zamawiajacy_id":"5150",
      "zamowienia_publiczne.kryterium_kod":"B"

        */

    }

    private JSONObject getProcurementJSON() throws JSONException {
        return new JSONObject(json);
    }


    private JSONObject getContractingAuthorityJSON() throws JSONException {
        return new JSONObject(caJson);
    }

    private JSONObject getDocsJSON() throws JSONException {
        return new JSONObject(docsJson);
    }

    private Procedure getProcedure() {
        Procedure p = new Procedure();
        JSONObject jsonObject = null;
        try {
            jsonObject = getProcurementJSON().getJSONObject("data");
            p.setProcedureTypeInfo(jsonObject.getString("zamowienia_publiczne_tryby.nazwa"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return p;

    }

    private ObjOfTheContract getObjOfContract() {
        ObjOfTheContract obj = new ObjOfTheContract();
        try {
            JSONObject jsonObject = getProcurementJSON().getJSONObject("data");

            Duration d = new Duration();
            d.setInMonths(jsonObject.getInt("zamowienia_publiczne.liczba_miesiecy"));
            obj.setFrameworkDuration(d);

            obj.setLots(jsonObject.getString("zamowienia_publiczne.oferty_czesciowe"));

            obj.setContractTitle(jsonObject.getString("zamowienia_publiczne.nazwa"));

            obj.setShortDescription(jsonObject.getString("zamowienia_publiczne.przedmiot"));

            JSONArray jsonArray = getDocsJSON().getJSONArray("Dataobject");
            for(int i=0; i<jsonArray.length(); i++){
                JSONObject o1 = (JSONObject) jsonArray.get(i);
                try{
                    String financialCondition = o1.getJSONObject("details").getString("sytuacja_ekonomiczna");
                    if(obj.getFinancingConditions()==null && financialCondition!=null && !financialCondition.trim().isEmpty()){
                        obj.setFinancingConditions(financialCondition);
                    }
                } catch (JSONException e) {
                    LOG.trace("error while mapping sytuacja_ekonomiczna", e);
                }

                try{
                    String experience = o1.getJSONObject("details").getString("wiedza");
                    if(obj.getAdditionalInfo()==null && experience!=null && !experience.trim().isEmpty()){
                        obj.setAdditionalInfo(experience);
                    }
                } catch (JSONException e) {
                    LOG.trace("error while mapping wiedza", e);
                }
            }

        } catch (Exception e) {
            LOG.trace("error while mapping objofcontract", e);
        }

        return obj;
    }

    public ContractingAuthority getContractibnAuthority(){

        ContractingAuthority ca = new ContractingAuthority();
        Organization o = new Organization();
        ca.setContractingOrg(o);
        Address a = new Address();
        o.setAddress(a);

        try {
            JSONArray jsonArray = getDocsJSON().getJSONArray("Dataobject");
            for(int i=0; i<jsonArray.length(); i++) {
                JSONObject o1 = (JSONObject) jsonArray.get(i);
                try {
                    String offersCount = o1.getJSONObject("details").getJSONObject("czesci-wykonawcy")
                            .getJSONObject("").getString("liczba_ofert");
                    if (ca.getOffersCount()==0 && offersCount!=null && !offersCount.trim().isEmpty()) {
                        ca.setOffersCount(Integer.parseInt(offersCount));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            JSONObject jsonObject = getContractingAuthorityJSON().getJSONObject("data");

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
            a.setRaw(caJson);


/*
                    "gminy.id":"1575",
                    "gminy.typ_id":"2",
                    "gminy.nazwa":"Poronin",
                    "zamowienia_publiczne_zamawiajacy.regon":"14273335600042",
                  + "zamowienia_publiczne_zamawiajacy.fax":"18 2014906",
                    "zamowienia_publiczne_zamawiajacy.rodzaj_inny":"Instytucja Gospodarki Bud\u017cetowej",
                    "zamowienia_publiczne_zamawiajacy.mieszkanie_numer":"",
                    "zamowienia_publiczne_zamawiajacy.object_id":"",
                  + "zamowienia_publiczne_zamawiajacy.www":"http:\/\/bip.cos.pl\/bip\/strony,0,72-zakopane",
                  + "zamowienia_publiczne_zamawiajacy.telefon":"18 2012274",
                  + "zamowienia_publiczne_zamawiajacy.ulica":"ul. Bronis\u0142awa Czecha 1",
                    "zamowienia_publiczne_zamawiajacy.rodzaj_id":"1302",
                    "zamowienia_publiczne_zamawiajacy.wojewodztwo":"ma\u0142opolskie",
                  + "zamowienia_publiczne_zamawiajacy.id":"5150",
                  - "zamowienia_publiczne_zamawiajacy.kod_pocztowy_id":"20206",
                  + "zamowienia_publiczne_zamawiajacy.miejscowosc":"Zakopane",
                  + "zamowienia_publiczne_zamawiajacy.email":"zamowienia.zakopane@cos.pl",
                    "zamowienia_publiczne_zamawiajacy.dataset":"",
                  + "zamowienia_publiczne_zamawiajacy.kod_pocztowy":"34-500",
                  + "zamowienia_publiczne_zamawiajacy.nazwa":"Centralny O\u015brodek Sportu - O\u015brodek Przygotowa\u0144 Olimpijskich w Zakopanem",
                    "zamowienia_publiczne_zamawiajacy.instytucja_id":"0",
                    "zamowienia_publiczne_zamawiajacy.dom_numer":"1",
                  + "zamowienia_publiczne_zamawiajacy.rodzaj":"Inny: Instytucja Gospodarki Bud\u017cetowej",
                    "zamowienia_publiczne_zamawiajacy.gmina_id":"1575"
*/


        } catch (Exception e) {
            LOG.trace("error while mapping", e);
        }

        return ca;
    }

    public String findValue(JSONObject json, String key) {
        return findValue(json, key, null);
    }


    public String findValue(JSONObject json, String key, String defaultValue){
        try {
            if(!json.has(key)){
                return null;
            } else {
                return json.getString(key);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
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
