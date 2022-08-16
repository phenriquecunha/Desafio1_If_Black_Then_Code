package IBM.Group2.TudoDeBom.Controllers;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import IBM.Group2.TudoDeBom.Dtos.ProductDto;
import IBM.Group2.TudoDeBom.Models.ProductModel;
import IBM.Group2.TudoDeBom.Repositories.ProductRepository;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/product")
public class ProductController {

  @Autowired
  ProductRepository productRepository;

  ObjResponse objResponse = new ObjResponse();

  @ApiOperation(value = "Insert a new product in the database given it's name, price, stock amount and whether it's generic (if it's a medication) or not")
  @ApiResponses(value = {
    @ApiResponse(code = 200, message = "Product added successfully"),
    @ApiResponse(code = 403, message = "User not allowed to perform this action"),
    @ApiResponse(code = 500, message = "There was an internal server error"),
})
  @PostMapping("/create")
  public ResponseEntity<Object> createProduct(@RequestBody ProductDto product){

    objResponse.target = null;
    var productExists = productRepository.findByName(product.getName());

    // Verificando se o produto já existe no banco de dados
    if(productExists.isPresent()){
      objResponse.target = productExists;
      objResponse.status  = HttpStatus.FORBIDDEN;
      objResponse.message = "Product already exists";
      return ResponseEntity.status(objResponse.status).body(objResponse);
    }
    // Validando os atributos do produto no corpo da requisição
    if(product.getName() == null){
      objResponse.status  = HttpStatus.BAD_REQUEST;
      objResponse.message = "Product without 'name' attribute";
      return ResponseEntity.status(objResponse.status).body(objResponse);
    }
    if(product.getPrice() == null){
      objResponse.status  = HttpStatus.BAD_REQUEST;
      objResponse.message = "Product without 'price' attribute";
      return ResponseEntity.status(objResponse.status).body(objResponse);
    }

    var productModel = new ProductModel();
    BeanUtils.copyProperties(product, productModel);
    productModel.setCreated_at(LocalDateTime.now());

    objResponse.target = productRepository.save(productModel);
    objResponse.status = HttpStatus.CREATED;
    objResponse.message = "Product created successfully";
    return ResponseEntity.status(objResponse.status).body(objResponse);

  }

  @ApiOperation(value = "Change a product information")
  @ApiResponses(value = {
    @ApiResponse(code = 200, message = "Product changed successfully"),
    @ApiResponse(code = 403, message = "User not allowed to perform this action"),
    @ApiResponse(code = 500, message = "There was an internal server error"),
})
  @PutMapping("/update")
  public ResponseEntity<Object> updateProduct(@RequestBody ProductDto product){

    objResponse.target = null;
    var productExists = productRepository.findByName(product.getName());

    // Verificando se o produto já existe no banco de dados
    if(productExists.isEmpty()){
      objResponse.status  = HttpStatus.NOT_FOUND;
      objResponse.message = "Product not found";
      return ResponseEntity.status(objResponse.status).body(objResponse);
    }

    // Validando os atributos do produto no corpo da requisição
    if(product.getName() == null){
      objResponse.status  = HttpStatus.BAD_REQUEST;
      objResponse.message = "Product without 'name' attribute";
      return ResponseEntity.status(objResponse.status).body(objResponse);
    }
    if(product.getPrice() == null){
      objResponse.status  = HttpStatus.BAD_REQUEST;
      objResponse.message = "Product without 'price' attribute";
      return ResponseEntity.status(objResponse.status).body(objResponse);
    }

    BeanUtils.copyProperties(product,productExists.get());
    productExists.get().setUpdated_at(LocalDateTime.now());
    objResponse.target = productRepository.save(productExists.get());
    objResponse.status = HttpStatus.OK;
    objResponse.message = "Product updated successfully";
    return ResponseEntity.status(objResponse.status).body(objResponse);
  }

  @ApiOperation(value = "Delete a product based on it's ID")
  @ApiResponses(value = {
    @ApiResponse(code = 200, message = "Product deleted successfully"),
    @ApiResponse(code = 403, message = "User not allowed to perform this action"),
    @ApiResponse(code = 500, message = "There was an internal server error"),
})
  @DeleteMapping("/delete/{id}")
  public ResponseEntity<Object> deleteProduct(@PathVariable UUID id){

    objResponse.target = null;

    if(productRepository.existsById(id)){
      objResponse.target = productRepository.findById(id).get();
      productRepository.deleteById(id);
      objResponse.status = HttpStatus.OK;
      objResponse.message = "Product deleted successfully";
      return ResponseEntity.status(objResponse.status).body(objResponse);
    }
    objResponse.status = HttpStatus.NOT_FOUND;
    objResponse.message = "Product not found";
    return ResponseEntity.status(objResponse.status).body(objResponse);
  }

  @ApiOperation(value = "List all products in the database")
  @ApiResponses(value = {
    @ApiResponse(code = 200, message = "Query OK"),
    @ApiResponse(code = 403, message = "User not allowed to perform this action"),
    @ApiResponse(code = 500, message = "There was an internal server error"),
})
  @GetMapping("/list")
  public ResponseEntity<Object> getAllProducts(){
    var products = productRepository.findAll();
    return ResponseEntity.ok().body(products);
  }
  @ApiOperation(value = "List a product based on it's ID")
  @ApiResponses(value = {
    @ApiResponse(code = 200, message = "Query OK"),
    @ApiResponse(code = 403, message = "User not allowed to perform this action"),
    @ApiResponse(code = 500, message = "There was an internal server error"),
})
  @GetMapping("/id/{id}")
  public ResponseEntity<Object> getProductById(@PathVariable UUID id){
    if(productRepository.existsById(id)){
      return ResponseEntity.ok().body(productRepository.findById(id));
    }
    objResponse.target = null;
    objResponse.status = HttpStatus.NOT_FOUND;
    objResponse.message = "Product not found";
    return ResponseEntity.status(objResponse.status).body(objResponse);
  }
  @ApiOperation(value = "List a product based on it's name")
  @ApiResponses(value = {
    @ApiResponse(code = 200, message = "Query OK"),
    @ApiResponse(code = 403, message = "User not allowed to perform this action"),
    @ApiResponse(code = 500, message = "There was an internal server error"),
})
  @GetMapping("/name/{name}")
  public ResponseEntity<Object> getProductByName(@PathVariable String name){
    objResponse.target = null;

    //TODO Listar todos os produtos que contem o nome na rota
    if(productRepository.existsByName(name)){
      return ResponseEntity.ok().body(productRepository.findByName(name));
    }

    objResponse.status = HttpStatus.NOT_FOUND;
    objResponse.message = "Product not found";
    return ResponseEntity.status(objResponse.status).body(objResponse);
  }

  @ApiOperation(value = "Add a prefered quantity of the product defined by it's id into the stock")
  @ApiResponses(value = {
    @ApiResponse(code = 200, message = "Product added into the stock"),
    @ApiResponse(code = 403, message = "User not allowed to perform this action"),
    @ApiResponse(code = 500, message = "There was an internal server error"),
})
  @PostMapping("/{id}/add/{quantity}")
  public ResponseEntity<Object> addQuantity(@PathVariable UUID id, @PathVariable int quantity){
    objResponse.target = null;
    var product = productRepository.findById(id);
    if(product.isPresent()){
      int stockAmount = product.get().getStock_amount();
      product.get().setStock_amount(stockAmount + quantity);
      objResponse.target = productRepository.save(product.get());
      objResponse.status = HttpStatus.OK;
      objResponse.message = "Product quantity in stock updated successfully";
      return ResponseEntity.status(objResponse.status).body(objResponse);
    }

    objResponse.status = HttpStatus.NOT_FOUND;
    objResponse.message = "Product not found";
    return ResponseEntity.status(objResponse.status).body(objResponse);
  }
}
