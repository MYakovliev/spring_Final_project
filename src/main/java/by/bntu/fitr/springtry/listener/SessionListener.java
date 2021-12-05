package by.bntu.fitr.springtry.listener;

import by.bntu.fitr.springtry.util.SessionAttribute;
import org.springframework.context.ApplicationListener;
import org.springframework.session.Session;
import org.springframework.session.events.SessionCreatedEvent;
import org.springframework.stereotype.Component;

@Component
public class SessionListener implements ApplicationListener<SessionCreatedEvent> {
    private static final String DEFAULT_LANG = "en";

    @Override
    public void onApplicationEvent(SessionCreatedEvent event) {
        final Session session = event.getSession();
        session.setAttribute(SessionAttribute.USER, DEFAULT_LANG);
    }
}
