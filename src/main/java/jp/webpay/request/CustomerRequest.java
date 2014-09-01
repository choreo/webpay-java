package jp.webpay.request;

import javax.ws.rs.core.MultivaluedMap;

import lombok.val;

import com.sun.jersey.core.util.MultivaluedMapImpl;


@SuppressWarnings("javadoc")
public class CustomerRequest implements RequestEntity {
    private CardRequest card;
    private String cardToken;
    private String email;
    private String description;
    private String uuid;

    public CustomerRequest card(CardRequest card) {
        this.card = card;
        this.cardToken = null;
        return this;
    }

    public CustomerRequest card(String card) {
        this.card = null;
        this.cardToken = card;
        return this;
    }

    public CustomerRequest email(String email) {
        this.email = email;
        return this;
    }

    public CustomerRequest description(String description) {
        this.description = description;
        return this;
    }

    public CustomerRequest uuid(String uuid) {
        this.uuid = uuid;
        return this;
    }

    @Override
    public MultivaluedMap<String, String> toForm() {
        val form = new MultivaluedMapImpl();
        if (card != null) {
            card.addParams(form);
        } else if (cardToken != null) {
            form.add("card", cardToken);
        }
        if (email != null && !email.isEmpty()) {
            form.add("email", email);
        }
        if (description != null && !description.isEmpty()) {
            form.add("description", description);
        }
        if (uuid != null && !uuid.isEmpty()) {
            form.add("uuid", uuid);
        }
        return form;
    }
}
