package IBM.Group2.TudoDeBom.Dtos;

import IBM.Group2.TudoDeBom.Models.ProductModel;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class SaleDto {

  private String payment_type;
  private List<ProductModel> products;
  private UUID customer;

}
