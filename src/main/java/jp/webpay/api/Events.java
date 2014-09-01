package jp.webpay.api;

import com.sun.jersey.core.util.MultivaluedMapImpl;

import jp.webpay.model.Event;
import jp.webpay.model.EventList;
import jp.webpay.request.ListRequest;
import lombok.NonNull;
import lombok.val;


@SuppressWarnings("javadoc")
public class Events extends Accessor {
    protected Events(WebPayClient client) {
        super(client);
    }

    public Event retrieve(@NonNull String id) {
        assertId(id);
        return Event.fromJsonResponse(client, client.get("/events/" + id));
    }

    public EventList all() {
        return all(new ListRequest(), null);
    }

    public EventList all(@NonNull ListRequest request) {
        return all(request, null);
    }

    public EventList all(@NonNull String type) {
        return all(new ListRequest(), type);
    }

    public EventList all(@NonNull ListRequest request, String type) {
        val form = new MultivaluedMapImpl();
        if (type != null && !type.isEmpty()) {
            form.add("type", type);
        }
        return EventList.fromJsonResponse(client, client.get("/events", form));
    }
}
