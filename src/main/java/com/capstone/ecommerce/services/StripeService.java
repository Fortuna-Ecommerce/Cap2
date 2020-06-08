package com.capstone.ecommerce.services;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.Customer;
import com.stripe.param.CustomerCreateParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class StripeService {

//    @Value("${STRIPE_SECRET_KEY}")
//    private String API_SECRET_KEY;

    @Autowired
    public StripeService() {
        Stripe.apiKey = "sk_test_JrSfaWJUKNQCctwLdKfEg4i200tLauD7Fx";
    }

    public String createCustomer(String token, String email) throws StripeException {
        CustomerCreateParams customerParams =
                CustomerCreateParams.builder()
                        .setSource(token)
                        .setEmail(email)
                        .build();
        Customer customer = Customer.create(customerParams);
        return customer.getId();
    }

    public String chargeNewCard(String token, double amount) throws Exception {
        String id = null;
        Map<String, Object> chargeParams = new HashMap<>();
        chargeParams.put("amount", (int)(amount * 100));
        chargeParams.put("currency", "USD");
        chargeParams.put("source", token);
        Charge charge = Charge.create(chargeParams);
        id = charge.getId();
        return id;
    }

    public String chargeExistingCard(String customerId, double amount) throws StripeException {
        Map<String, Object> chargeParams = new HashMap<>();
        chargeParams.put("amount", (int)(amount * 100));
        chargeParams.put("currency", "USD");
        chargeParams.put("customer", customerId);
        Charge charge = Charge.create(chargeParams);
        return charge.getId();
    }

}
//
//    @Value("${STRIPE_SECRET_KEY}")
//    private String secretKey;
//
//    @PostConstruct
//    public void init() {
//        Stripe.apiKey = secretKey;
//    }
//    public Charge charge(ChargeRequest chargeRequest)
//            throws AuthenticationException, InvalidRequestException, CardException, StripeException {
//        Map<String, Object> chargeParams = new HashMap<>();
//        chargeParams.put("amount", chargeRequest.getAmount());
//        chargeParams.put("currency", chargeRequest.getCurrency());
//        chargeParams.put("description", chargeRequest.getDescription());
//        chargeParams.put("source", chargeRequest.getStripeToken());
//        return Charge.create(chargeParams);
//    }

//}