package IBM.Group2.TudoDeBom.Dtos;

import java.util.ArrayList;
import java.util.UUID;

import lombok.Data;

@Data
public class SaleDto {

  private String payment_type;
  private ArrayList<ObjProduct> products;
  private UUID customer;

}
