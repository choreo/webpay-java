package jp.webpay.api;

import jp.webpay.exception.ApiConnectionException;
import jp.webpay.exception.WebPayException;
import lombok.NonNull;
import lombok.experimental.NonFinal;
import org.glassfish.jersey.SslConfigurator;
import org.glassfish.jersey.client.filter.HttpBasicAuthFilter;

import javax.net.ssl.SSLContext;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class WebPayClient {
    public static final String DEFAULT_BASE = "https://api.webpay.jp/v1";

    private final String apiKey;
    private final String apiBase;
    private final Client client;
    
    private Locale acceptLanguage = Locale.ENGLISH;

    public final Charges charges;
    public final Customers customers;
    public final Tokens tokens;
    public final Events events;
    public final Account account;

    public WebPayClient(@NonNull String apiKey) {
        this(apiKey, DEFAULT_BASE);
    }

    public WebPayClient(@NonNull String apiKey, @NonNull String apiBase) {
        this.apiKey = apiKey;
        this.apiBase = apiBase;

        SSLContext ssl = SslConfigurator.newInstance().createSSLContext();
        client = ClientBuilder.newBuilder().sslContext(ssl).build();
        client.register(new HttpBasicAuthFilter(apiKey, ""));

        charges = new Charges(this);
        customers = new Customers(this);
        tokens = new Tokens(this);
        events = new Events(this);
        account = new Account(this);
    }
    
    public WebPayClient acceptLanguage(Locale locale) {
        this.acceptLanguage = locale;
        return this;
    }

    String post(@NonNull String path, Form form) {
        WebTarget target = client.target(apiBase).path(path);
        Response response;
        try {
            response = target.request(MediaType.APPLICATION_JSON_TYPE).acceptLanguage(this.acceptLanguage).post(Entity.form(form));
        } catch (javax.ws.rs.ProcessingException e) {
            throw new ApiConnectionException(e);
        }
        return processErrorResponse(response);
    }

    String get(@NonNull String path) {
        return get(path, null);
    }

    String get(@NonNull String path, Form form) {
        WebTarget target = client.target(apiBase).path(path);
        if (form != null) {
            MultivaluedMap<String,String> params = form.asMap();
            for (Map.Entry<String, List<String>> param : params.entrySet()) {
                target = target.queryParam(param.getKey(), param.getValue().toArray());
            }
        }
        Response response;
        try {
            response = target.request(MediaType.APPLICATION_JSON_TYPE).acceptLanguage(this.acceptLanguage).get();
        } catch (javax.ws.rs.ProcessingException e) {
            throw new ApiConnectionException(e);
        }
        return processErrorResponse(response);
    }

    String delete(String path) {
        WebTarget target = client.target(apiBase).path(path);
        Response response;
        try {
            response = target.request(MediaType.APPLICATION_JSON_TYPE).delete();
        } catch (javax.ws.rs.ProcessingException e) {
            throw new ApiConnectionException(e);
        }
        return processErrorResponse(response);
    }

    private String processErrorResponse(Response response) {
        int status = response.getStatus();
        String data = response.readEntity(String.class);
        if (status >= 200 && status < 300)  {
            return data;
        } else {
            throw WebPayException.fromJsonResponse(status, data);
        }
    }
}
