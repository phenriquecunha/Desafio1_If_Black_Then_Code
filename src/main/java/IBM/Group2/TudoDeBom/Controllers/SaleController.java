package IBM.Group2.TudoDeBom.Controllers;

import IBM.Group2.TudoDeBom.Dtos.SaleDto;
import IBM.Group2.TudoDeBom.Models.SaleModel;
import IBM.Group2.TudoDeBom.Repositories.SaleRepository;
import IBM.Group2.TudoDeBom.Services.SaleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/sale")
public class SaleController {

  ObjResponse objResponse = new ObjResponse();

  @Autowired
  SaleRepository saleRepository;

  @Autowired
  SaleService saleService;

  @PostMapping("/sell")
  public ResponseEntity<Object> sell(@RequestBody SaleDto sale){

    if(sale.getPayment_type() == null){
      objResponse.status  = HttpStatus.BAD_REQUEST;
      objResponse.messageError = "Sale without 'payment_type' attribute";
      return ResponseEntity.status(objResponse.status).body(objResponse);
    }
    if(sale.getCustomer() == null){
      objResponse.status  = HttpStatus.BAD_REQUEST;
      objResponse.messageError = "Sale without 'customer' attribute";
      return ResponseEntity.status(objResponse.status).body(objResponse);
    }
    if(sale.getProducts() == null){
      objResponse.status  = HttpStatus.BAD_REQUEST;
      objResponse.messageError = "Sale without 'products' attribute";
      return ResponseEntity.status(objResponse.status).body(objResponse);
    }

    var saleModel = new SaleModel();
    BeanUtils.copyProperties(sale, saleModel);
    saleModel.setTotal_price(saleService.getTotalPrice(saleModel.getProducts()));
    saleModel.setCreated_at(LocalDateTime.now());
    objResponse.status  = HttpStatus.OK;
    objResponse.messageError = "Sale registered successfully";
    return ResponseEntity.status(objResponse.status).body(objResponse);
  }

}
