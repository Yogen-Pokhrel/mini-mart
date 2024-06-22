package com.minimart.helpers;

import com.minimart.mail.MailService;
import com.minimart.order.entity.Order;
import com.minimart.order.entity.OrderLineItem;
import com.minimart.product.entity.Product;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RequiredArgsConstructor
@Service
public class EmailTemplates {

    public static final String ORDER_CREATED_EMAIL_TEMPLATE;
    public static final String ORDER_CREATED_EMAIL_TEMPLATE_2_IMAGE;
    public static final String ORDER_CREATED_EMAIL_TEMPLATE_3_OR_MORE_IMAGE;

    private static String cdnUrl="https://sandbox.resources.yallahproperty.com/";

    @Autowired
    MailService mailService;

    static {
        ORDER_CREATED_EMAIL_TEMPLATE = readFile("email_templates/order_created.html");
        ORDER_CREATED_EMAIL_TEMPLATE_2_IMAGE = readFile("email_templates/order_created_2.html");
        ORDER_CREATED_EMAIL_TEMPLATE_3_OR_MORE_IMAGE = readFile("email_templates/order_created_3.html");
    }

    private static String readFile(String path) {
        try {
            return new String(Files.readAllBytes(Paths.get(ClassLoader.getSystemResource(path).toURI())), StandardCharsets.UTF_8);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            return "";
        }
    }


    public static String formatTemplate(String template, Map<String, String> values) {
        String formattedTemplate = template;
        for (Map.Entry<String, String> entry : values.entrySet()) {
            String key = "{{" + entry.getKey() + "}}";
            String value = entry.getValue() != null ? entry.getValue() : ""; // Provide default empty string for null values
            formattedTemplate = formattedTemplate.replace(key, value);
        }
        return formattedTemplate;
    }

    public static Map<String, String> getEmailValues(Order order){
        String emailtemplate = EmailTemplates.ORDER_CREATED_EMAIL_TEMPLATE;
        if (order.getOrderLineItems().size() == 2){
            emailtemplate = EmailTemplates.ORDER_CREATED_EMAIL_TEMPLATE_2_IMAGE;
        } else if (order.getOrderLineItems().size() >= 3) {
            emailtemplate = EmailTemplates.ORDER_CREATED_EMAIL_TEMPLATE_3_OR_MORE_IMAGE;
        }
        Order finalOrder = order;
        List<OrderLineItem> items = order.getOrderLineItems();
        Map<String, String> emailValues = new HashMap<String, String>() {{
            put("customerName", finalOrder.getCustomer().getFirstName() + " " + finalOrder.getCustomer().getLastName());
            put("customerImage",  cdnUrl + finalOrder.getCustomer().getImage());

            String image1 = null;
            String image2 = null;
            String image3 = null;

            if (items.size() > 0) {
                Product product = items.get(0).getProduct();
                if (product != null && !product.getImages().isEmpty()) {
                    image1 = cdnUrl + product.getImages().get(0).getPath();
                }
            }

            if (items.size() > 1) {
                Product product = items.get(1).getProduct();
                if (product != null && !product.getImages().isEmpty()) {
                    image2 = cdnUrl + product.getImages().get(0).getPath();
                }
            }

            if (items.size() > 2) {
                Product product = items.get(2).getProduct();
                if (product != null && !product.getImages().isEmpty()) {
                    image3 = cdnUrl  + product.getImages().get(0).getPath();
                }
            }

            put("productImage1",  image1);
            put("productImage2",  image2);
            put("productImage3",  image3);

            // Create HTML for each OrderLineItem
            StringBuilder orderLineHtml = new StringBuilder();
            StringBuilder orderLinePriceHtml = new StringBuilder();
            for (OrderLineItem item : items) {
                orderLineHtml.append("<div style=\"padding:0px 0px 0px 0px\">")
                        .append("<div style=\"font-size:16px;font-weight:normal;text-align:left;padding:0px 0px 0px 0px\">")
                        .append("$").append(item.getUnitPrice()).append(" x ").append(item.getQuantity())
                        .append("</div></div>");

                orderLinePriceHtml.append("<div style=\"padding:0px 0px 0px 0px\">")
                        .append("<div style=\"font-size:16px;font-weight:normal;text-align:right;padding:0px 0px 0px 0px\">")
                        .append("$").append(item.getTotalPrice())
                        .append("</div></div>");
            }

            put("orderLine", orderLineHtml.toString());
            put("orderLinePrice", orderLinePriceHtml.toString());
            put("total", String.valueOf(finalOrder.getTotalAmount()));
        }};
    return emailValues;
    }

    public void sendOrderCreatedEmail(Order order)  {
        Map<String, String> emailValues = getEmailValues(order);
        try {
            mailService.sendEmailMime(order.getCustomer().getEmail(),
                    "Order Created",
                    formatTemplate(ORDER_CREATED_EMAIL_TEMPLATE, emailValues)
            );
        } catch (MessagingException e) {
            System.out.printf(e.getMessage());
        }

    }


}