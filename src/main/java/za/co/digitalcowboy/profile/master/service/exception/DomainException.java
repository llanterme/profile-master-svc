package za.co.digitalcowboy.profile.master.service.exception;

import lombok.Getter;

@Getter
public class DomainException extends RuntimeException {

  private final ErrorCode errorCode;

  private final transient Object[] args;

  public DomainException(ErrorCode errorCode, Object... args) {
    super(String.format(errorCode.getMessage(), args));
    this.errorCode = errorCode;
    this.args = args;
  }

  public DomainException(ErrorCode errorCode, Throwable throwable, Object... args) {
    super(String.format(errorCode.getMessage(), args), throwable);
    this.errorCode = errorCode;
    this.args = args;
  }

  public ErrorCode getDomainCode() {
    return errorCode;
  }

  public Object[] getArgs() {
    return args;
  }
}
