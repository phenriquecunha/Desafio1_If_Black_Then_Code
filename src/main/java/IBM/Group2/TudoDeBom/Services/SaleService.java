package IBM.Group2.TudoDeBom.Services;

import IBM.Group2.TudoDeBom.Repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

@Service
public class SaleService {

  @Autowired
  ProductRepository productRepository;
  public Double getTotalPrice(ArrayList<UUID> products){
    Double totalPrice = 0.0;
    for(UUID product : products){
      var productModel = productRepository.findById(product);
      if(productModel.get().isGeneric()){
        totalPrice += (productModel.get().getPrice() * 0.8);
      }else{
        totalPrice += productModel.get().getPrice();
      }
    }
    return totalPrice;
  }

}
