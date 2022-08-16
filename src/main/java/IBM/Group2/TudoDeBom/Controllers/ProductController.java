package IBM.Group2.TudoDeBom.Controllers;

import IBM.Group2.TudoDeBom.Dtos.ProductDto;
import IBM.Group2.TudoDeBom.Models.ProductModel;
import IBM.Group2.TudoDeBom.Repositories.ProductRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/product")
public class ProductController {

  @Autowired
  ProductRepository productRepository;

  ObjResponse objResponse = new ObjResponse();

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

  @GetMapping("/list")
  public ResponseEntity<Object> getAllProducts(){
    var products = productRepository.findAll();
    return ResponseEntity.ok().body(products);
  }

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
