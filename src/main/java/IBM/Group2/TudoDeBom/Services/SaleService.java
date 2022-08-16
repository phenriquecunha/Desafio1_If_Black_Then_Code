package IBM.Group2.TudoDeBom.Services;

import IBM.Group2.TudoDeBom.Dtos.ObjProduct;
import IBM.Group2.TudoDeBom.Repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

@Service
public class SaleService {

  @Autowired
  ProductRepository productRepository;
  public Double getTotalPrice(ArrayList<ObjProduct> products){
    double totalPrice = 0.0;
    for(var objProduct : products){
      var productModel = productRepository.findById(objProduct.getId());
      if(productModel.get().isGeneric()){
        totalPrice += ((productModel.get().getPrice() * 0.8) * objProduct.getAmount());
      }else{
        totalPrice += (productModel.get().getPrice() * objProduct.getAmount());
      }
    }
    return totalPrice;
  }
}
