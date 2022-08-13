package IBM.Group2.TudoDeBom.Services;

import IBM.Group2.TudoDeBom.Models.ProductModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SaleService {
  public Double getTotalPrice(List<ProductModel> products){
    Double totalPrice = 0.0;
    for(ProductModel product : products){
      totalPrice += product.getPrice();
    }
    return totalPrice;
  }
}
