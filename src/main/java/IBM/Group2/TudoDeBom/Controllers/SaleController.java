package IBM.Group2.TudoDeBom.Controllers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import IBM.Group2.TudoDeBom.Dtos.SaleDto;
import IBM.Group2.TudoDeBom.Models.SaleModel;
import IBM.Group2.TudoDeBom.Repositories.CustomerRepository;
import IBM.Group2.TudoDeBom.Repositories.ProductRepository;
import IBM.Group2.TudoDeBom.Repositories.SaleRepository;
import IBM.Group2.TudoDeBom.Services.SaleService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/sale")
public class SaleController {

  ObjResponse objResponse = new ObjResponse();

  @Autowired
  ProductRepository productRepository;

  @Autowired
  CustomerRepository customerRepository;

  @Autowired
  SaleRepository saleRepository;

  @Autowired
  SaleService saleService;

  @ApiOperation(value = "Create a new sell given it's customer, payment type and products")
  @ApiResponses(value = {
    @ApiResponse(code = 200, message = "Sell added into the database"),
    @ApiResponse(code = 403, message = "User not allowed to perform this action"),
    @ApiResponse(code = 500, message = "There was an internal server error"),
})
  @PostMapping("/sell")
  public ResponseEntity<Object> sell(@RequestBody SaleDto sale){
    objResponse.target = null;
    if(sale.getPayment_type() == null){
      objResponse.status  = HttpStatus.BAD_REQUEST;
      objResponse.message = "Sale without 'payment_type' attribute";
      return ResponseEntity.status(objResponse.status).body(objResponse);
    }
    if(sale.getCustomer() == null){
      objResponse.status  = HttpStatus.BAD_REQUEST;
      objResponse.message = "Sale without 'customer' attribute";
      return ResponseEntity.status(objResponse.status).body(objResponse);
    }
    if(sale.getProducts() == null){
      objResponse.status  = HttpStatus.BAD_REQUEST;
      objResponse.message = "Sale without 'products' attribute";
      return ResponseEntity.status(objResponse.status).body(objResponse);
    }


    var customerId = customerRepository.findById(sale.getCustomer());
    if(customerId.isEmpty()){
      objResponse.status = HttpStatus.BAD_REQUEST;
      objResponse.message = "Customer id '"+sale.getCustomer()+"' not found";
      return ResponseEntity.status(objResponse.status).body(objResponse);
    }

    for(var objProduct : sale.getProducts()){
      var product = productRepository.findById(objProduct.getId());
      if(product.isEmpty()){
        objResponse.status = HttpStatus.BAD_REQUEST;
        objResponse.message = "Product id '"+objProduct.getId()+"' not found";
        return ResponseEntity.status(objResponse.status).body(objResponse);
      }
    }

    var saleModel = new SaleModel();
    saleModel.setPayment_type(sale.getPayment_type());
    saleModel.setCustomer(customerId.get());

    ArrayList<UUID> productsIdList = new ArrayList<>();
    for(var objProduct : sale.getProducts()){
      for(int i=0; i<objProduct.getAmount(); i++){
        productsIdList.add(objProduct.getId());
      }
    }

    saleModel.setProducts(productsIdList);
    saleModel.setTotal_price(saleService.getTotalPrice(sale.getProducts()));
    saleModel.setCreated_at(LocalDateTime.now());

    for(var objProduct : sale.getProducts()){
      var productModel = productRepository.findById(objProduct.getId());
      var amount = productModel.get().getStock_amount();
      if(amount - objProduct.getAmount() < 0){
        objResponse.target = productModel.get();
        objResponse.status  = HttpStatus.FORBIDDEN;
        objResponse.message = "Amount not available in stock for product id -> '"+objProduct.getId()+"'";
        return ResponseEntity.status(objResponse.status).body(objResponse);
      }
      productModel.get().setStock_amount(amount - objProduct.getAmount());
    }
    objResponse.target = saleRepository.save(saleModel);
    objResponse.status  = HttpStatus.OK;
    objResponse.message = "Sale registered successfully";
    return ResponseEntity.status(objResponse.status).body(objResponse);
  }

  @ApiOperation(value = "List a specific sell based on it's ID")
  @ApiResponses(value = {
    @ApiResponse(code = 200, message = "Query OK"),
    @ApiResponse(code = 404, message = "Sale not found"),
    @ApiResponse(code = 500, message = "There was an internal server error"),
})
  @GetMapping("/{id}")
  public ResponseEntity<Object> getSaleById(@PathVariable UUID id){
    objResponse.target = null;
    var sale = saleRepository.findById(id);
    if(sale.isPresent()){
      return  ResponseEntity.ok().body(sale.get());
    }
    objResponse.status  = HttpStatus.NOT_FOUND;
    objResponse.message = "Sale not found";
    return ResponseEntity.status(objResponse.status).body(objResponse);
  }

  @ApiOperation(value = "List all sells in the database")
  @ApiResponses(value = {
    @ApiResponse(code = 200, message = "Query OK"),
    @ApiResponse(code = 403, message = "User not allowed to perform this action"),
    @ApiResponse(code = 500, message = "There was an internal server error"),
})
  @GetMapping("/list")
  public ResponseEntity<Object> getSales(){
    return ResponseEntity.ok().body(saleRepository.findAll());
  }
}
