package za.co.digitalcowboy.profile.master.service.exception;

public enum ServiceIdentifier {
  PAYMENT_AUTH("02");

  private final String code;

  ServiceIdentifier(String code) {
    this.code = code;
  }

  public String getCode() {
    return this.code;
  }
}
