package hu.petabyte.redflags.engine.gear.parser.pl;


import hu.petabyte.redflags.engine.epforgpl.Answer;
import hu.petabyte.redflags.engine.epforgpl.EPFData;
import hu.petabyte.redflags.engine.epforgpl.EPFMapper;
import hu.petabyte.redflags.engine.gear.AbstractGear;
import hu.petabyte.redflags.engine.model.Notice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by marcin on 13.06.2016.
 */
@Component
public class ProcurementParser extends AbstractGear {

    private static final Logger LOG = LoggerFactory.getLogger(ProcurementParser.class);

    @Autowired
    private EPFData dataSource;

    @Override
    protected Notice processImpl(Notice notice) throws Exception {

        LOG.info("parsing procurement for notice {}", notice.getId());

        Answer a = dataSource.getProcurement(notice);
        String procurementBody = a.getBody();

        a = dataSource.getProcurementPurchaser(EPFMapper.getPurchaserId(a.getBody()));
        String contractingAuthBody = a.getBody();

        a = dataSource.getProcurementDocument(notice);
        String docsBody = a.getBody();

        EPFMapper epfMapper = new EPFMapper(procurementBody, contractingAuthBody, docsBody);

        notice = epfMapper.getNotice(notice);

        LOG.info("mapping performed. Notice title {}", notice.getData().getTitle());

        return notice;
    }
}