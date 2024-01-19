package za.co.digitalcowboy.profile.master.service.exception;

public enum SystemIdentifier {
  CORE_SYSTEM("01");

  private final String code;

  SystemIdentifier(String code) {
    this.code = code;
  }

  public String getCode() {
    return this.code;
  }
}
