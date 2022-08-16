package IBM.Group2.TudoDeBom.Dtos;

import lombok.Data;

import java.util.ArrayList;
import java.util.UUID;

@Data
public class SaleDto {

  private String payment_type;
  private ArrayList<UUID> products;
  private UUID customer;

}
