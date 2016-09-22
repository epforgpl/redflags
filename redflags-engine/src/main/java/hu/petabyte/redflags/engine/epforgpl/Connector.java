package hu.petabyte.redflags.engine.epforgpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.sql.Connection;

/**
 * Created by marcin on 08.06.2016.
 */
public class Connector {

    private static final Logger LOG = LoggerFactory.getLogger(Connector.class);

    private String url;

    private ResponseEntity<String> re;

    public Connector(String url) {
        this.url = url;
    }

    public Answer fetch(String uri){
        LOG.info("fetching data from {}", uri);
        RestTemplate restTemplate = new RestTemplate();
        try {
            re = restTemplate.getForEntity(url + "/" + uri, String.class);
        }catch (HttpClientErrorException e){
            LOG.trace("error while fetching from " + uri, e);
        }
        LOG.trace(re.toString());
        return new Answer(re);
    }


}
