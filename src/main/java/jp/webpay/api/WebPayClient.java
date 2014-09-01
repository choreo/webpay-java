package jp.webpay.api;

import java.util.Locale;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import jp.webpay.exception.ApiConnectionException;
import jp.webpay.exception.WebPayException;
import lombok.NonNull;
import lombok.val;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;

@SuppressWarnings("javadoc")
public class WebPayClient {
    public static final String DEFAULT_BASE = "https://api.webpay.jp/v1";

    //private final String apiKey;
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
        this(Client.create(), apiKey, apiBase);
    }

    public WebPayClient(Client client, @NonNull String apiKey) {
        this(client, apiKey, DEFAULT_BASE);
    }

    public WebPayClient(Client client, @NonNull String apiKey, @NonNull String apiBase) {
        //this.apiKey = apiKey;
        this.apiBase = apiBase;
        this.client = client;
//        SSLContext ssl = SslConfigurator.newInstance().createSSLContext();
//        client = ClientBuilder.newBuilder().sslContext(ssl).build();
        client.addFilter(new HTTPBasicAuthFilter(apiKey, ""));

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

    String post(@NonNull String path, MultivaluedMap<String, String> formData) {

        try {
            WebResource resource = client.resource(apiBase)
                                 .path(path);
            if (formData != null) {
                resource = resource.queryParams(formData);
            }

            val response = resource.acceptLanguage(this.acceptLanguage)
                                   .accept(MediaType.APPLICATION_JSON_TYPE)
                                   .post(ClientResponse.class);
            
            return processErrorResponse(response);
            
        } catch (ClientHandlerException e) {
            throw new ApiConnectionException(e);
        }
    }

    String get(@NonNull String path) {
        return get(path, null);
    }

    String get(@NonNull String path, MultivaluedMap<String, String> formData) {
        try {
            val resource = client.resource(apiBase)
                                 .path(path);
            if (formData != null) {
                resource.queryParams(formData);
            }
            val response = resource.acceptLanguage(this.acceptLanguage)
                                   .accept(MediaType.APPLICATION_JSON_TYPE)
                                   .get(ClientResponse.class);

            return processErrorResponse(response);

        } catch (ClientHandlerException e) {
            throw new ApiConnectionException(e);
        }
    }

    String delete(String path) {
        try {
            val response = client.resource(apiBase)
                                 .path(path)
                                 .accept(MediaType.APPLICATION_JSON_TYPE)
                                 .delete(ClientResponse.class);

            return processErrorResponse(response);
            
        } catch (ClientHandlerException e) {
            throw new ApiConnectionException(e);
        }
    }

    private String processErrorResponse(ClientResponse response) {
        int status = response.getStatus();
        String data = response.getEntity(String.class);
        if (status >= 200 && status < 300)  {
            return data;
        } else {
            throw WebPayException.fromJsonResponse(status, data);
        }
    }
}
