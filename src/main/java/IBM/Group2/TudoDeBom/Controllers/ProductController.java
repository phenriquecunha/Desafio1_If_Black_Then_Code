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
import java.util.Optional;

@RestController
@RequestMapping("/product")
public class ProductController {

  @Autowired
  ProductRepository productRepository;

  ObjResponse objResponse = new ObjResponse();

  @PostMapping("/create")
  public ResponseEntity<Object> createProduct(@RequestBody ProductDto product){

    var productExists = productRepository.findByName(product.getName());

    // Verificando se o produto já existe no banco de dados
    if(productExists.isPresent()){
      objResponse.status  = HttpStatus.FORBIDDEN;
      objResponse.messageError = "Product already exists";
      return ResponseEntity.status(objResponse.status).body(objResponse);
    }
    // Validando os atributos do produto no corpo da requisição
    if(product.getName() == null){
      objResponse.status  = HttpStatus.BAD_REQUEST;
      objResponse.messageError = "Product without 'name' attribute";
      return ResponseEntity.status(objResponse.status).body(objResponse);
    }
    if(product.getPrice() == null){
      objResponse.status  = HttpStatus.BAD_REQUEST;
      objResponse.messageError = "Product without 'price' attribute";
      return ResponseEntity.status(objResponse.status).body(objResponse);
    }

    var productModel = new ProductModel();
    BeanUtils.copyProperties(product, productModel);
    productModel.setCreated_at(LocalDateTime.now());

    productRepository.save(productModel);

    objResponse.status = HttpStatus.CREATED;
    objResponse.messageError = "Product created successfully";
    return ResponseEntity.status(objResponse.status).body(objResponse);

  }

  @PutMapping("/update")
  public ResponseEntity<Object> updateProduct(@RequestBody ProductDto product){

    var productExists = productRepository.findByName(product.getName());

    // Verificando se o produto já existe no banco de dados
    if(productExists.isEmpty()){
      objResponse.status  = HttpStatus.NOT_FOUND;
      objResponse.messageError = "Product not found";
      return ResponseEntity.status(objResponse.status).body(objResponse);
    }

    // Validando os atributos do produto no corpo da requisição
    if(product.getName() == null){
      objResponse.status  = HttpStatus.BAD_REQUEST;
      objResponse.messageError = "Product without 'name' attribute";
      return ResponseEntity.status(objResponse.status).body(objResponse);
    }
    if(product.getPrice() == null){
      objResponse.status  = HttpStatus.BAD_REQUEST;
      objResponse.messageError = "Product without 'price' attribute";
      return ResponseEntity.status(objResponse.status).body(objResponse);
    }

    BeanUtils.copyProperties(product,productExists.get());
    productExists.get().setUpdated_at(LocalDateTime.now());
    objResponse.status = HttpStatus.OK;
    objResponse.messageError = "Product updated successfully";
    return ResponseEntity.status(objResponse.status).body(objResponse);
  }

}
