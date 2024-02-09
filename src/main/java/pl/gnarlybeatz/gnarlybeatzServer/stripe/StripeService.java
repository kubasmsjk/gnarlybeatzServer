package pl.gnarlybeatz.gnarlybeatzServer.stripe;

import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.gnarlybeatz.gnarlybeatzServer.audioFilesManagement.fileData.FileDataRepository;
import pl.gnarlybeatz.gnarlybeatzServer.emial.EmailService;
import pl.gnarlybeatz.gnarlybeatzServer.licenses.DeluxeLicenseAgreement;
import pl.gnarlybeatz.gnarlybeatzServer.licenses.StandardLicenseAgreement;
import pl.gnarlybeatz.gnarlybeatzServer.stripe.transaction.TransactionHistory;
import pl.gnarlybeatz.gnarlybeatzServer.stripe.transaction.TransactionHistoryRepository;
import pl.gnarlybeatz.gnarlybeatzServer.user.UserRepository;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StripeService {

    @Value("${stripe.apikey}")
    String stripeKey;
    @Value("${stripe.endpointSecret}")
    String endpointSecret;

    private final UserRepository userRepository;
    private final FileDataRepository fileDataRepository;
    private final TransactionHistoryRepository transactionHistoryRepository;
    private final EmailService emailService;

    public String checkout(List<StripeProductRequest> request) {
        Stripe.apiKey = stripeKey;
        List<SessionCreateParams.LineItem> collect = request.parallelStream().map(this::createLineItem).collect(Collectors.toList());
        String email = userRepository.findUserEmailByUserId(request.get(0).getUserId());

        SessionCreateParams params =
                SessionCreateParams.builder()
                        .setMode(SessionCreateParams.Mode.PAYMENT)
                        .setSuccessUrl("http://localhost:3000/purchasedBeats")
                        .setCancelUrl("http://localhost:3000")
                        .addAllLineItem(collect)
                        .setCustomerEmail(email)
                        .build();

        try {
            Session session = Session.create(params);
            return session.getUrl();
        } catch (StripeException e) {
            throw new RuntimeException(e);
        }
    }

    private SessionCreateParams.LineItem createLineItem(StripeProductRequest stripeProductRequest) {
        String productStripePriceId = (stripeProductRequest.getLicenseType().equals("Standard Lease") ? stripeProductRequest.getStandardProductStripePriceId() : stripeProductRequest.getDeluxeProductStripePriceId());
        return SessionCreateParams.LineItem.builder()
                .setPrice(productStripePriceId)
                .setQuantity(1L)
                .build();
    }

    public String handleStripeEvent(String payload, String sigHeader) {
        Event event = null;
        try {
            event = Webhook.constructEvent(
                    payload, sigHeader, endpointSecret
            );
        } catch (SignatureVerificationException e) {
            return "";
        }

        EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
        StripeObject stripeObject = null;
        if (dataObjectDeserializer.getObject().isPresent()) {
            stripeObject = dataObjectDeserializer.getObject().get();
        } else {
            return "";
        }

        switch (event.getType()) {
            case "checkout.session.completed" -> {
                if (stripeObject instanceof Session session) {
                    Session resource = null;
                    try {
                        resource = Session.retrieve(session.getId());
                        resource.listLineItems().getData().forEach(lineItem -> saveToTransactionHistory(session.getId(), lineItem, session.getCustomerEmail()));
                        resource.listLineItems().getData().forEach(lineItem -> sendEmailWithLicensesAttachment(session.getCustomerDetails().getName(), lineItem, session.getCustomerEmail()));
                    } catch (StripeException e) {
                        throw new RuntimeException(e);
                    }

                } else {
                    return "Unexpected stripeObject type for checkout.session.completed event";
                }
            }
        }
        return "";
    }

    private void sendEmailWithLicensesAttachment(String customer, LineItem lineItem, String customerEmail) {
        List<File> listOfAttachments = new ArrayList<>();
        if (lineItem.getPrice().getUnitAmount() == 1000) {
            listOfAttachments.add(StandardLicenseAgreement.generatePDF(customer, customer, customerEmail, lineItem.getDescription()));
        }
        if (lineItem.getPrice().getUnitAmount() == 2000) {
            listOfAttachments.add(DeluxeLicenseAgreement.generatePDF(customer, customer, customerEmail, lineItem.getDescription()));
        }
        try {
            emailService.createCheckoutEmailDetails(customerEmail, listOfAttachments);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    private void saveToTransactionHistory(String sessionId, LineItem lineItem, String email) {
        var user = userRepository.findByEmail(email);
        var beat = fileDataRepository.findByProductStripePriceId(lineItem.getPrice().getId());
        if (user.isPresent() && beat.isPresent()) {
            var transactionHistoryItem = TransactionHistory.builder()
                    .sessionId(sessionId)
                    .user(user.get())
                    .beat(beat.get())
                    .build();
            transactionHistoryRepository.save(transactionHistoryItem);
        } else {
            throw new UsernameNotFoundException("Couldn't find your account.");
        }
    }
}
