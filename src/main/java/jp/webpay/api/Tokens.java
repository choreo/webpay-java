package jp.webpay.api;

import com.sun.jersey.core.util.MultivaluedMapImpl;

import jp.webpay.model.Token;
import jp.webpay.request.CardRequest;
import lombok.NonNull;
import lombok.val;


@SuppressWarnings("javadoc")
public class Tokens extends Accessor {
    Tokens(WebPayClient client) {
        super(client);
    }

    public Token create(@NonNull CardRequest request) {
        return create(request, null);
    }

    public Token create(@NonNull CardRequest request, String uuid) {
        val form = new MultivaluedMapImpl();
        if (uuid != null && !uuid.isEmpty())
            form.add("uuid", uuid);
        return Token.fromJsonResponse(client, client.post("/tokens", form));
    }

    public Token retrieve(@NonNull String id) {
        assertId(id);
        return Token.fromJsonResponse(client, client.get("/tokens/" + id));
    }
}
