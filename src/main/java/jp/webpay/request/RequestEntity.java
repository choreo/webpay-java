package jp.webpay.request;

import javax.ws.rs.core.MultivaluedMap;

@SuppressWarnings("javadoc")
public interface RequestEntity {
    public MultivaluedMap<String, String> toForm();
}
