package IBM.Group2.TudoDeBom.Dtos;

import lombok.Data;

@Data
public class ProductDto {

  private String name;
  private Double price;
  private boolean generic;
  private int stockAmount;
}
