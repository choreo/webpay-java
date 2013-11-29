package jp.webpay.request;

import javax.ws.rs.core.Form;

public class CustomerRequest implements RequestEntity {
    private CardRequest card;
    private String cardToken;
    private String email;
    private String description;

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

    @Override
    public Form toForm() {
        Form form = new Form();
        if (card != null) {
            card.addParams(form);
        } else if (cardToken != null) {
            form.param("card", cardToken);
        }
        if (email != null && !email.isEmpty()) {
            form.param("email", email);
        }
        if (description != null && !description.isEmpty()) {
            form.param("description", description);
        }
        return form;
    }
}
