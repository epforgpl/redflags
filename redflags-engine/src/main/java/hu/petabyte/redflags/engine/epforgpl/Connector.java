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

    RestTemplate restTemplate;

    public Connector(String url) {
        this.url = url;
        this.restTemplate = new RestTemplate();
    }

    public Answer fetch(String uri){
        LOG.debug("fetching data from {}", uri);
        ResponseEntity<String> re = null;
        try {
            re = restTemplate.getForEntity(url + "/" + uri, String.class);
        }catch (HttpClientErrorException e){
            LOG.trace("error while fetching from " + uri, e);
        }
        return new Answer(re);
    }


}
