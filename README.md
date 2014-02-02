# webpay-java [![Build Status](https://travis-ci.org/webpay/webpay-java.png)](https://travis-ci.org/webpay/webpay-java)

WebPay Java (1.6 and later) bindings https://webpay.jp

## Installation

- [Artifact Details on Maven Central](http://search.maven.org/#artifactdetails%7Cjp.webpay%7Cwebpay-java%7C1.1.2%7Cjar)

### Maven users

```
<dependency>
  <groupId>jp.webpay</groupId>
  <artifactId>webpay-java</artifactId>
  <version>1.1.2</version>
</dependency>
```

### Gradle users

```
dependencies {
  compile 'jp.webpay:webpay-java:1.1.2'
}
```

## Usage

```java
WebPayClient client = new WebPayClient("test_secret_YOUR_TEST_API_KEY");

CardRequest card = new CardRequest()
  .number("4242-4242-4242-4242")
  .expMonth(11)
  .expYear(2014)
  .cvc(123)
  .name("YOUR NAME");

ChargeRequest request = new ChargeRequest()
  .amount(400)
  .currency("jpy")
  .card(card)
  .description("アイテムの購入 with Java");

client.charges.create(request)
```

See [WebPay Java API Document](https://webpay.jp/docs/api/java) for more details.

## Contributing

1. Fork it
2. Create your feature branch (`git checkout -b my-new-feature`)
3. Commit your changes (`git commit -am 'Add some feature'`)
4. Push to the branch (`git push origin my-new-feature`)
5. Create new Pull Request
