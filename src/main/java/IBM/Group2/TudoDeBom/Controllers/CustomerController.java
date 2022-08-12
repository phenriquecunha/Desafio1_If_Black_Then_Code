package IBM.Group2.TudoDeBom.Controllers;

import IBM.Group2.TudoDeBom.Dtos.CustomerDto;
import IBM.Group2.TudoDeBom.Models.CustomerModel;
import IBM.Group2.TudoDeBom.Repositories.CustomerRepository;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/customer")
public class CustomerController {

  @Autowired
  CustomerRepository customerRepository;

  @PostMapping("/create")
  public ResponseEntity<Object> createCustomer(@RequestBody @Valid CustomerDto customer){

    var objResponse = new Object(){
      public HttpStatus status;
      public String messageError;
    };

    // Verificando se o cliente já existe no banco de dados
    Optional<CustomerModel> customerExists = customerRepository.findByCpf(customer.getCpf());
    if(customerExists.isPresent()){
      objResponse.status  = HttpStatus.FORBIDDEN;
      objResponse.messageError = "Customer already exists";
      return ResponseEntity.status(objResponse.status).body(objResponse);
    }

    // Validando os atributos do cliente no corpo da requisição
    if(customer.getName() == null){
      objResponse.status  = HttpStatus.BAD_REQUEST;
      objResponse.messageError = "Customer without name attribute";
      return ResponseEntity.status(objResponse.status).body(objResponse);
    }
    if(customer.getEmail() == null){
      objResponse.status  = HttpStatus.BAD_REQUEST;
      objResponse.messageError = "Customer without email attribute";
      return ResponseEntity.status(objResponse.status).body(objResponse);
    }
    if(customer.getCpf() == null){
      objResponse.status  = HttpStatus.BAD_REQUEST;
      objResponse.messageError = "Customer without cpf attribute";
      return ResponseEntity.status(objResponse.status).body(objResponse);
    }
    if(customer.getTel() == null){
      objResponse.status  = HttpStatus.BAD_REQUEST;
      objResponse.messageError = "Customer without telephone attribute";
      return ResponseEntity.status(objResponse.status).body(objResponse);
    }

    // Transformando o DTO em um MODEL válido para o banco de dados
    var customerModel = new CustomerModel();
    BeanUtils.copyProperties(customer, customerModel);

    customerModel.setCreated_at(LocalDateTime.now());
    customerRepository.save(customerModel);

    objResponse.status  = HttpStatus.CREATED;
    objResponse.messageError = "Customer created successfully";
    return ResponseEntity.status(objResponse.status).body(objResponse);
  }

}
