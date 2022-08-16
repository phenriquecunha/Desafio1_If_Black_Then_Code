package IBM.Group2.TudoDeBom.Controllers;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ObjResponse {
  public HttpStatus status;
  public String message;
  public Object target;
}
