package api;


import org.apache.log4j.Logger;

import java.io.IOException;

public class EmailVerificationHelper {
    private static Logger LOGGER = Logger.getLogger(EmailVerificationHelper.class);


    public static String getCreatedEmail() {
        try {
            return CustomEmailApiClient.post("/inboxes/withDefaults").get("emailAddress").getAsString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        LOGGER.info("Unable to create user");
        return null;
    }

    public static void verifyEmail() {
        CustomEmailApiClient.getReceivingEmailsID();
        try {
            CustomEmailApiClient.getActivationLinkAndConfirm();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
