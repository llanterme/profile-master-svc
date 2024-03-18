package za.co.digitalcowboy.profile.master.service.exception;


public enum ErrorCode {
  // 001 - 010 Common
  UNKNOWN_ERROR("001", "Unknown error"),
  MISSING_HEADER("002", "Missing header"),
  INVALID_INPUT_FIELD("003", "Invalid input field %s"),
  CONFLICT_REQUEST("004", "Conflict request"),
  INVALID_REQUEST_FORMAT("005", "Invalid request format"),

  // 011 - 020 Idempotency
  MISSING_IDEMPOTENCY("011", "Idempotency key is required"),
  MISSING_CALLER_ID("012", "Caller id is required"),
  INVALID_IDEMPOTENCY("013", "Invalid idempotency key"),
  PARAMETERS_MISMATCH("014", "Parameters mismatch"),

  // 021 - 030 Payment
  INVALID_ORDER("021", "Invalid order"),
  CONFIRM_PAYMENT_ORDER_MISMATCH("022", "Mismatch between payment order and confirm order"),
  ASYNC_MESSAGE_ID_NOT_EXIST("023", "AsyncMessageId  not existed"),
  CONFIRM_TAGS_MISMATCH("024", "Confirm tags must contains the same key set with reserved tags"),
  INVALID_ACCOUNT_NUMBER("025", "Invalid account number"),

  // 031 - 040 PUC
  DUPLICATE_PAYMENT_USE_CASE("031", "Payment use case already exists"),
  PAYMENT_USE_CASE_NOT_EXIST("032", "Payment use case does not exist"),
  INVALID_PAYMENT_DEFINITION("033", "Payment definition %s is invalid"),
  UNKNOWN_PAYMENT_USE_CASE("034", "Unknown payment use case"),
  PAYMENT_USE_CASE_ALREADY_ENABLED("035", "Payment use case is already enabled"),
  PAYMENT_USE_CASE_ALREADY_DISABLED("036", "Payment use case is already disabled"),

  PAYMENT_USE_CASE_VERSION_NOT_EXIST("037",
          "Payment use case [%s] with version [%s] does not exist"),

  // 041 - 050 Account, Profile Events
  EVENT_INPUT_FIELDS_INVALID("041", "Event input fields are invalid %s"),
  EVENT_ALREADY_MARKED_AS_READ("042", "Event is already marked as read"),
  EVENT_NOT_FOUND("043", "Event not found in the inbox"),
  PROFILE_NOT_EXIST("044", "Profile does not exist"),
  MESSAGE_TYPE_IS_NOT_SUPPORTED("045", "Message type is not supported"),

  // 050 - 060 Fee
  FEE_DOES_NOT_EXIST("050", "Fee does not exist"),

  // 061 - 070 Limit
  DUPLICATE_LIMIT_RULE("061", "Limit rule [%s] is already exists"),
  INVALID_LIMIT_RULE_FORMAT("062", "Invalid limit rule format"),
  LIMIT_NOT_EXIST("063", "Limit rule [%s] does not exist"),
  UNKNOWN_LIMIT("064", "Unknown limit"),
  LIMIT_ALREADY_ENABLED("065", "Limit rule with id [%s] is already enabled"),
  LIMIT_ALREADY_DISABLED("066", "Limit rule with id [%s] is already disabled"),
  INVALID_LIMIT_BETWEEN_SCOPE_AND_PERIOD("067", "Limit rule [%s] is invalid between scope and period"),
  INVALID_LIMIT_PROFILE_TYPE_VALUES("068", "Limit rule [%s] is invalid profile type values"),
  INVALID_LIMIT_VALUE("069", "Limit rule [%s] is invalid value"),

  UNABLE_SO_SAVE_PROFILE("070", "Unable to save profile");

  private final String value;

  private final String message;

  ErrorCode(String value, String message) {
    this.value = value;
    this.message = message;
  }

  public String toUniversalCode() {
    return String.format("%s%s%s", SystemIdentifier.CORE_SYSTEM.getCode(),
            ServiceIdentifier.PAYMENT_AUTH.getCode(), value);
  }

  public String getMessage() {
    return message;
  }
}