package jp.webpay.request;

import javax.ws.rs.core.MultivaluedMap;

import com.sun.jersey.core.util.MultivaluedMapImpl;

import lombok.val;


@SuppressWarnings("javadoc")
public class ListRequest implements RequestEntity {
    private int count = 10, offset = 0;
    private Integer createdGt, createdGte, createdLt, createdLte;

    public ListRequest count(int i) {
        count = i;
        return this;
    }

    public ListRequest offset(int i) {
        offset = i;
        return this;
    }

    public ListRequest createdGt(Integer i) {
        createdGt = i;
        return this;
    }

    public ListRequest createdGte(Integer i) {
        createdGte = i;
        return this;
    }

    public ListRequest createdLt(Integer i) {
        createdLt = i;
        return this;
    }

    public ListRequest createdLte(Integer i) {
        createdLte = i;
        return this;
    }

    @Override
    public MultivaluedMap<String, String> toForm() {
        val form = new MultivaluedMapImpl();
        form.add("count", String.valueOf(count));
        form.add("offset", String.valueOf(offset));
        if (createdGt != null) {
            form.add("created[gt]", createdGt.toString());
        }
        if (createdGte != null) {
            form.add("created[gte]", createdGte.toString());
        }
        if (createdLt != null) {
            form.add("created[lt]", createdLt.toString());
        }
        if (createdLte != null) {
            form.add("created[lte]", createdLte.toString());
        }
        return form;
    }
}
