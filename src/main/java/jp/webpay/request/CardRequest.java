package jp.webpay.request;

import javax.ws.rs.core.MultivaluedMap;

import com.sun.jersey.core.util.MultivaluedMapImpl;

import lombok.NonNull;
import lombok.val;

@SuppressWarnings("javadoc")
public class CardRequest implements RequestEntity {
    private String number, name;
    private Integer expMonth, expYear, cvc;

    public CardRequest number(@NonNull String number) {
        this.number = number;
        return this;
    }

    public CardRequest name(@NonNull String name) {
        this.name = name;
        return this;
    }

    public CardRequest expMonth(int expMonth) {
        this.expMonth = expMonth;
        return this;
    }

    public CardRequest expYear(int expYear) {
        this.expYear = expYear;
        return this;
    }

    public CardRequest cvc(int cvc) {
        this.cvc = cvc;
        return this;
    }

    public void addParams(MultivaluedMap<String, String> form) {
        form.add("card[number]", number);
        form.add("card[exp_month]", expMonth.toString());
        form.add("card[exp_year]", expYear.toString());
        form.add("card[cvc]", cvc.toString());
        form.add("card[name]", name);
    }

    @Override
    public MultivaluedMap<String, String> toForm() {
        val form = new MultivaluedMapImpl();
        addParams(form);
        return form;
    }
}
