package jp.webpay.api;

import jp.webpay.model.Customer;
import jp.webpay.model.CustomerList;
import jp.webpay.model.RetrievedCustomer;
import jp.webpay.request.CardRequest;
import jp.webpay.request.CustomerRequest;
import jp.webpay.request.ListRequest;
import org.junit.Test;

import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class CustomersTest extends ApiTestFixture {
    @Test
    public void testCreateCustomer() throws Exception {
        stubFor(post("/v1/customers")
                .withRequestBody(containing("description=Test"))
                .withRequestBody(containing("email=customer"))
                .withRequestBody(containing("card%5Bname%5D=YUUKO+SHIONJI"))
                .willReturn(response("customers/create")));
        CardRequest card = new CardRequest()
                .number("4242-4242-4242-4242")
                .expMonth(12)
                .expYear(2015)
                .cvc(123)
                .name("YUUKO SHIONJI");
        CustomerRequest request = new CustomerRequest()
                .card(card)
                .description("Test Customer from Java")
                .email("customer@example.com");

        Customer customer = client.customers.create(request);

        assertThat(customer.getEmail(), is("customer@example.com"));
        assertThat(customer.getActiveCard().getName(), is("YUUKO SHIONJI"));
    }

    @Test
    public void testCreateCustomerWithUUID() throws Exception {
        String uuid = UUID.randomUUID().toString();
        stubFor(post("/v1/customers")
                .withRequestBody(containing("uuid=" + uuid))
                .willReturn(response("customers/create")));
        CustomerRequest request = new CustomerRequest()
                .email("customer@example.com")
                .uuid(uuid);

        Customer customer = client.customers.create(request);

        assertThat(customer.getEmail(), is("customer@example.com"));
    }

    @Test
    public void testCreateCustomerWithToken() throws Exception {
        stubFor(post("/v1/customers")
                .withRequestBody(containing("description=Test"))
                .withRequestBody(containing("email=customer"))
                .withRequestBody(containing("card=tok_3dw2T20rzekM1Kf"))
                .willReturn(response("customers/create")));
        CustomerRequest request = new CustomerRequest()
                .card("tok_3dw2T20rzekM1Kf")
                .description("Test Customer from Java")
                .email("customer@example.com");

        Customer customer = client.customers.create(request);

        assertThat(customer.getEmail(), is("customer@example.com"));
        assertThat(customer.getActiveCard().getName(), is("YUUKO SHIONJI"));
    }

    @Test
    public void testRetrieveCustomer() throws Exception {
        stubFor(get("/v1/customers/cus_39o4Fv82E1et5Xb")
                .willReturn(response("customers/retrieve")));

        RetrievedCustomer customer = client.customers.retrieve("cus_39o4Fv82E1et5Xb");
        assertThat(customer.getId(), is("cus_39o4Fv82E1et5Xb"));
        assertThat(customer.isDeleted(), is(false));
        assertThat(customer.getCustomer().getEmail(), is("customer@example.com"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRetrieveCustomerWithoutId() throws Exception {
        client.customers.retrieve("");
    }

    @Test
    public void testRetrieveDeletedCustomer() throws Exception {
        String id = "cus_7GafGMbML8R28Io";
        stubFor(get("/v1/customers/" + id)
                .willReturn(response("customers/retrieve_deleted")));

        RetrievedCustomer customer = client.customers.retrieve(id);
        assertThat(customer.getId(), is(id));
        assertThat(customer.isDeleted(), is(true));
        assertThat(customer.getCustomer(), is(nullValue()));
    }

    @Test
    public void testUpdateRetrievedCustomer() throws Exception {
        String mail = "newmail@example.com";
        String description = "New description";
        CardRequest card = new CardRequest()
                .number("4242-4242-4242-4242")
                .expMonth(12)
                .expYear(2016)
                .cvc(123)
                .name("YUUKO SHIONJI");

        stubFor(get("/v1/customers/cus_39o4Fv82E1et5Xb")
                .willReturn(response("customers/retrieve")));
        stubFor(post("/v1/customers/cus_39o4Fv82E1et5Xb")
                .withRequestBody(containing("card%5Bexp_year%5D=2016"))
                .withRequestBody(containing("email=newmail"))
                .withRequestBody(containing("description=New"))
                .willReturn(response("customers/update")));

        Customer customer = client.customers.retrieve("cus_39o4Fv82E1et5Xb").getCustomer();
        customer.setNewCard(card);
        customer.setEmail(mail);
        customer.setDescription(description);
        customer.save();
        assertThat(customer.getActiveCard().getExpYear(), is(2016));
        assertThat(customer.getEmail(), is(mail));
        assertThat(customer.getDescription(), is(description));
    }

    @Test
    public void testDeleteCustomer() throws Exception {
        stubFor(get("/v1/customers/cus_39o4Fv82E1et5Xb")
                .willReturn(response("customers/retrieve")));
        stubFor(delete("/v1/customers/cus_39o4Fv82E1et5Xb")
                .willReturn(response("customers/delete")));

        Customer customer = client.customers.retrieve("cus_39o4Fv82E1et5Xb").getCustomer();
        assertThat(customer.delete(), is(true));
    }

    @Test
    public void testAllCustomers() throws Exception {
        stubFor(get("/v1/customers?count=3&offset=0&created%5Bgt%5D=1378000000")
                .willReturn(response("customers/all")));

        CustomerList customers = client.customers.all(new ListRequest().count(3).createdGt(1378000000));

        assertThat(customers.getCount(), is(4));
        assertThat(customers.getUrl(), is("/v1/customers"));
        assertThat(customers.getData().size(), is(3));
        assertThat(customers.getData().get(0).getId(), is("cus_39o9oU1N1dRm4LZ"));
    }

    @Test
    public void testAllCustomersWithoutArgument() throws Exception {
        String path = "/v1/customers?count=10&offset=0";
        stubFor(get(path).willReturn(response("customers/all")));
        client.customers.all();
        verify(getRequestedFor(urlEqualTo(path)));
    }
}
