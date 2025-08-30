package com.anubhav.dev.store.payments;

import com.anubhav.dev.store.entities.Order;
import com.anubhav.dev.store.entities.OrderItem;
import com.anubhav.dev.store.entities.PaymentStatus;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.StripeObject;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import com.stripe.param.checkout.SessionCreateParams.Builder;
import com.stripe.param.checkout.SessionCreateParams.LineItem;
import com.stripe.param.checkout.SessionCreateParams.LineItem.PriceData;
import com.stripe.param.checkout.SessionCreateParams.LineItem.PriceData.ProductData;
import com.stripe.param.checkout.SessionCreateParams.Mode;
import java.math.BigDecimal;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class StripePaymentGateway implements PaymentGateway {

  @Value("${websiteUrl}")
  private String websiteUrl;
  @Value("${stripe.webhookSecretKey}")
  private String webhookSecretKey;

  @Override
  public CheckoutSession createCheckoutSession(Order order) {
    try {
      Builder builder = SessionCreateParams.builder().setMode(Mode.PAYMENT)
          .setSuccessUrl(websiteUrl + "/checkout-success?orderId=" + order.getId())
          .setCancelUrl(websiteUrl + "/checkout-cancel")
          .putMetadata("order_id", order.getId().toString());
      order.getItems().forEach(item -> {
        LineItem lineItem = createLineItem(item);
        builder.addLineItem(lineItem);
      });
      SessionCreateParams build = builder.build();
      Session session = Session.create(build);
      return new CheckoutSession(session.getUrl());
    } catch (StripeException ex) {
      System.out.println(ex.getMessage());
      throw new PaymentException();
    }
  }

  @Override
  public Optional<PaymentResult> parseWebhookRequest(WebhookRequest request) {
    try {
      String payload = request.getPayload();
      String signature = request.getHeaders().get("stripe-signature");
      Event event = Webhook.constructEvent(payload, signature, webhookSecretKey);

      return switch (event.getType()) {
        case "payment_intent.succeeded" -> Optional.of(new PaymentResult(extractOrderId(event), PaymentStatus.PAID));

        case "payment_intent.payment_failed" ->
            Optional.of(new PaymentResult(extractOrderId(event), PaymentStatus.FAILED));

        default -> Optional.empty();

      };
    } catch (SignatureVerificationException e) {
      throw new PaymentException("Invalid Signature");
    }
  }


  private Long extractOrderId(Event event) {
    StripeObject stripeObject = event.getDataObjectDeserializer().getObject().orElseThrow(
        () -> new PaymentException("Could not deserialize Stripe Event. Check the SDK and API Verion.")
    );
    PaymentIntent paymentIntent = (PaymentIntent) stripeObject;
    String orderId = paymentIntent.getMetadata().get("order_id");
    return Long.valueOf(orderId);

  }

  private static LineItem createLineItem(OrderItem item) {
    return LineItem.builder()
        .setQuantity(Long.valueOf(item.getQuantity()))
        .setPriceData(createPriceData(item))
        .build();
  }

  private static PriceData createPriceData(OrderItem item) {
    return PriceData.builder()
        .setCurrency("inr")
        .setUnitAmountDecimal(item.getUnitPrice().multiply(BigDecimal.valueOf(100)))
        .setProductData(createProductData(item))
        .build();
  }

  private static ProductData createProductData(OrderItem item) {
    return ProductData.builder()
        .setName(item.getProduct().getName())
        .build();
  }
}
