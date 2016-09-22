package hu.petabyte.redflags.engine.epforgpl;

import hu.petabyte.redflags.engine.model.NoticeID;
import hu.petabyte.redflags.engine.scope.AbstractScope;
import org.springframework.stereotype.Component;

/**
 * Created by marcin on 18.09.2016.
 */
@Component
public class SingleNoticeScope extends AbstractScope {

    private boolean first = true;

    @Override
    public boolean hasNext() {
        if(first){
            first = false;
            return true;
        } else {
            return false;
        }

    }

    @Override
    public NoticeID next() {
        return new BZPNoticeId(2883195);
    }
}
