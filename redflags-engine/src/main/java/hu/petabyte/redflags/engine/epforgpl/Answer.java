package hu.petabyte.redflags.engine.epforgpl;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Created by marcin on 11.06.2016.
 */
public class Answer {

    private ResponseEntity<String> re;

    Answer(ResponseEntity<String> re) {
        this.re = re;
    }

    public HttpStatus getCode(){
        return re.getStatusCode();
    }

    public String getBody(){
        return re.getBody();
    }


    @Override
    public String toString() {
        return "Answer{" +
                "re=" + re +
                '}';
    }
}
