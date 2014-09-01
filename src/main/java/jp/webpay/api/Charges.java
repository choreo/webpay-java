package jp.webpay.api;

import com.sun.jersey.core.util.MultivaluedMapImpl;

import jp.webpay.model.Charge;
import jp.webpay.model.ChargeList;
import jp.webpay.request.ChargeRequest;
import jp.webpay.request.ListRequest;
import lombok.NonNull;
import lombok.val;


@SuppressWarnings("javadoc")
public class Charges extends Accessor {
    Charges(@NonNull WebPayClient client) {
        super(client);
    }

    public Charge create(@NonNull ChargeRequest request) {
        return Charge.fromJsonResponse(client, client.post("/charges", request.toForm()));
    }

    public Charge retrieve(@NonNull String id) {
        assertId(id);
        return Charge.fromJsonResponse(client, client.get("/charges/" + id));
    }

    public Charge refund(@NonNull String id, long amount) {
        assertId(id);
        val form = new MultivaluedMapImpl();
        form.add("amount", String.valueOf(amount));
        return Charge.fromJsonResponse(client, client.post("/charges/" + id + "/refund", form));
    }

    public Charge capture(@NonNull String id) {
        assertId(id);
        return Charge.fromJsonResponse(client, client.post("/charges/" + id + "/capture", new MultivaluedMapImpl()));
    }

    public Charge capture(@NonNull String id, long amount) {
        assertId(id);
        val form = new MultivaluedMapImpl();
        form.add("amount", String.valueOf(amount));
        return Charge.fromJsonResponse(client, client.post("/charges/" + id + "/capture", form));
    }

    public ChargeList all() {
        return all(new ListRequest(), null);
    }

    public ChargeList all(@NonNull ListRequest request) {
        return all(request, null);
    }

    public ChargeList all(@NonNull String customerId) {
        assertId(customerId);
        return all(new ListRequest(), customerId);
    }

    public ChargeList all(@NonNull ListRequest request, String customerId) {
        val form = new MultivaluedMapImpl();
        if (customerId != null && !customerId.isEmpty()) {
            form.add("customer", customerId);
        }
        return ChargeList.fromJsonResponse(client, client.get("/charges", form));
    }
}
