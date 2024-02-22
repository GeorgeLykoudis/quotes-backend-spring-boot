package boot.spring.backend.quotes.dto;

public class ChangePasswordRequest {

    private String currentPassword;
    private String newPassword;
    private String confirmationPassword;

    public ChangePasswordRequest() {}

    public ChangePasswordRequest(String currentPassword, String newPassword, String confirmationPassword) {
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
        this.confirmationPassword = confirmationPassword;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmationPassword() {
        return confirmationPassword;
    }

    public void setConfirmationPassword(String confirmationPassword) {
        this.confirmationPassword = confirmationPassword;
    }

    public static ChangePasswordRequestBuilder builder() {
        return new ChangePasswordRequestBuilder();
    }

    public static class ChangePasswordRequestBuilder {
        private final ChangePasswordRequest instance = new ChangePasswordRequest();

        public ChangePasswordRequestBuilder currentPassword(String currentPassword) {
            this.instance.currentPassword = currentPassword;
            return this;
        }

        public ChangePasswordRequestBuilder newPassword(String newPassword) {
            this.instance.newPassword = newPassword;
            return this;
        }

        public ChangePasswordRequestBuilder confirmationPassword(String confirmationPassword) {
            this.instance.confirmationPassword = confirmationPassword;
            return this;
        }

        public ChangePasswordRequest build() {
            return this.instance;
        }
    }

}
