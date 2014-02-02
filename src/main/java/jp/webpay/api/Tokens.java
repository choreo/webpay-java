package jp.webpay.api;

import jp.webpay.model.Token;
import jp.webpay.request.CardRequest;
import lombok.NonNull;

import javax.ws.rs.core.Form;

public class Tokens extends Accessor {
    Tokens(WebPayClient client) {
        super(client);
    }

    public Token create(@NonNull CardRequest request) {
        return create(request, null);
    }

    public Token create(@NonNull CardRequest request, String uuid) {
        Form form = request.toForm();
        if (uuid != null && !uuid.isEmpty())
            form.param("uuid", uuid);
        return Token.fromJsonResponse(client, client.post("/tokens", form));
    }

    public Token retrieve(@NonNull String id) {
        assertId(id);
        return Token.fromJsonResponse(client, client.get("/tokens/" + id));
    }
}
