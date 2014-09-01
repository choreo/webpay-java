package jp.webpay.request;

import javax.ws.rs.core.MultivaluedMap;

import com.sun.jersey.core.util.MultivaluedMapImpl;

import lombok.NonNull;
import lombok.val;


@SuppressWarnings("javadoc")
public class ChargeRequest implements RequestEntity {
    private Long amount;
    private String customer;
    private String cardToken;
    private CardRequest cardObject;
    private String currency = "jpy";
    private String description = "";
    private boolean capture = true;
    private String uuid;

    public ChargeRequest amount(long amount) {
        this.amount = amount;
        return this;
    }

    public ChargeRequest customer(String customer) {
        this.customer = customer;
        this.cardToken = null;
        this.cardObject = null;
        return this;
    }

    public ChargeRequest card(String card) {
        this.customer = null;
        this.cardToken = card;
        this.cardObject = null;
        return this;
    }

    public ChargeRequest card(CardRequest card) {
        this.customer = null;
        this.cardToken = null;
        this.cardObject = card;
        return this;
    }

    public ChargeRequest currency(@NonNull String currency) {
        this.currency = currency;
        return this;
    }

    public ChargeRequest description(String description) {
        this.description = description;
        return this;
    }

    public ChargeRequest capture(boolean capture) {
        this.capture = capture;
        return this;
    }

    public ChargeRequest uuid(String uuid) {
        this.uuid = uuid;
        return this;
    }

    @Override
    public MultivaluedMap<String, String> toForm() {
        val form = new MultivaluedMapImpl();

        if (amount == null) {
            throw new RequiredParamNotSetException("amount");
        } else {
            form.add("amount", amount.toString());
        }
        if (currency == null) {
            throw new RequiredParamNotSetException("currency");
        } else {
            form.add("currency", currency);
        }
        if (customer != null) {
            form.add("customer", customer);
        } else if (cardToken != null) {
            form.add("card", cardToken);
        } else if (cardObject != null) {
            cardObject.addParams(form);
            form.add("card[number]", cardToken);
        } else {
            throw new RequiredParamNotSetException("card");
        }
        if (description != null) {
            form.add("description", description);
        }
        form.add("capture", capture ? "true" : "false");
        if (uuid != null) {
            form.add("uuid", uuid);
        }

        return form;
    }
}
