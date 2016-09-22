package hu.petabyte.redflags.engine.gear.archiver.pl;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.petabyte.redflags.engine.epforgpl.Answer;
import hu.petabyte.redflags.engine.epforgpl.EPFData;
import hu.petabyte.redflags.engine.epforgpl.EPFMapper;

import hu.petabyte.redflags.engine.gear.AbstractGear;
import hu.petabyte.redflags.engine.model.DisplayLanguage;
import hu.petabyte.redflags.engine.model.Notice;
import hu.petabyte.redflags.engine.model.Tab;

import hu.petabyte.redflags.engine.tedintf.cached.NoticeCache;
import hu.petabyte.redflags.engine.tedintf.cached.SimpleNoticeCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;



/**
 * Created by marcin on 11.06.2016.
 */
@Component
public class EpfArchiver extends AbstractGear {

    private static final Logger LOG = LoggerFactory.getLogger(EpfArchiver.class);

  /*  @Autowired
    private EPFData dataSource;*/

    private NoticeCache cache;

    public EpfArchiver(){
        //dataSource = new EPFData(dataHost);
        LOG.info("data source connected ");
        cache = new SimpleNoticeCache("/cache");

    }


    @Override
    protected Notice processImpl(Notice notice) throws Exception {

      /*  Answer a = dataSource.getProcurement(notice);
        LOG.info("for notice {} got answer {}", notice, a);
        ObjectMapper om = new ObjectMapper();
        Procurement p = om.readValue(a.getBody(), Procurement.class);

        LOG.info("procurement object {}",p.toString());

        cache.store(notice.getId(), DisplayLanguage.PL, Tab.DATA, a.getBody());

        a = dataSource.getProcurementPurchaser(new EPFMapper(a.getBody(), null, null).getPurchaserId());

        cache.store(notice.getId(), DisplayLanguage.PL, Tab.SUMMARY, a.getBody());

        a = dataSource.getProcurementDocument(notice);

        cache.store(notice.getId(), DisplayLanguage.PL, Tab.DOCUMENT_FAMILY, a.getBody());
*/
        return notice;
    }
}
