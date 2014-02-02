package jp.webpay.api;

import jp.webpay.model.Token;
import jp.webpay.request.CardRequest;
import org.junit.Test;

import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.containing;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TokensTest extends ApiTestFixture {
    @Test
    public void testCreateToken() throws Exception {
        stubFor(post("/v1/tokens")
                .withRequestBody(containing("card%5Bname%5D=YUUKO+SHIONJI"))
                .willReturn(response("tokens/create")));
        CardRequest card = new CardRequest()
                .number("4242-4242-4242-4242")
                .expMonth(12)
                .expYear(2015)
                .cvc(123)
                .name("YUUKO SHIONJI");

        Token token = client.tokens.create(card);

        assertThat(token.getUsed(), is(false));
        assertThat(token.getCard().getName(), is("YUUKO SHIONJI"));
    }

    @Test
    public void testCreateTokenWithUUIDArgument() throws Exception {
        String uuid = UUID.randomUUID().toString();
        stubFor(post("/v1/tokens")
                .withRequestBody(containing("card%5Bname%5D=YUUKO+SHIONJI"))
                .withRequestBody(containing("uuid=" + uuid))
                .willReturn(response("tokens/create")));
        CardRequest card = new CardRequest()
                .number("4242-4242-4242-4242")
                .expMonth(12)
                .expYear(2015)
                .cvc(123)
                .name("YUUKO SHIONJI");

        Token token = client.tokens.create(card, uuid);

        assertThat(token.getUsed(), is(false));
        assertThat(token.getCard().getName(), is("YUUKO SHIONJI"));
    }

    @Test
     public void testRetrieveToken() throws Exception {
        stubFor(get("/v1/tokens/tok_3dw2T20rzekM1Kf")
                .willReturn(response("tokens/retrieve")));

        Token token = client.tokens.retrieve("tok_3dw2T20rzekM1Kf");
        assertThat(token.getId(), is("tok_3dw2T20rzekM1Kf"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRetrieveTokenWithoutId() throws Exception {
        client.tokens.retrieve("");
    }
}
