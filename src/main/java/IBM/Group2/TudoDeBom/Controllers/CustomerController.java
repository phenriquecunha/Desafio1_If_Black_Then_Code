package IBM.Group2.TudoDeBom.Controllers;

import IBM.Group2.TudoDeBom.Dtos.CustomerDto;
import IBM.Group2.TudoDeBom.Models.CustomerModel;
import IBM.Group2.TudoDeBom.Repositories.CustomerRepository;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/customer")
public class CustomerController {
  @Autowired
  CustomerRepository customerRepository;

  ObjResponse objResponse = new ObjResponse();

  @PostMapping("/create")
  public ResponseEntity<Object> createCustomer(@RequestBody @Valid CustomerDto customer){

    objResponse.target = null;

    // Verificando se o cliente já existe no banco de dados
    var customerExists = customerRepository.findByCpf(customer.getCpf());
    if(customerExists.isPresent()){
      objResponse.target = customerExists.get();
      objResponse.status  = HttpStatus.FORBIDDEN;
      objResponse.message = "Customer already exists";
      return ResponseEntity.status(objResponse.status).body(objResponse);
    }

    // Validando os atributos do cliente no corpo da requisição
    if(customer.getName() == null){
      objResponse.status  = HttpStatus.BAD_REQUEST;
      objResponse.message = "Customer without name attribute";
      return ResponseEntity.status(objResponse.status).body(objResponse);
    }
    if(customer.getEmail() == null){
      objResponse.status  = HttpStatus.BAD_REQUEST;
      objResponse.message = "Customer without email attribute";
      return ResponseEntity.status(objResponse.status).body(objResponse);
    }
    if(customer.getCpf() == null){
      objResponse.status  = HttpStatus.BAD_REQUEST;
      objResponse.message = "Customer without cpf attribute";
      return ResponseEntity.status(objResponse.status).body(objResponse);
    }
    if(customer.getTel() == null){
      objResponse.status  = HttpStatus.BAD_REQUEST;
      objResponse.message = "Customer without telephone attribute";
      return ResponseEntity.status(objResponse.status).body(objResponse);
    }

    // Transformando o DTO em um MODEL válido para o banco de dados
    var customerModel = new CustomerModel();
    BeanUtils.copyProperties(customer, customerModel);

    customerModel.setCreated_at(LocalDateTime.now());
    objResponse.target = customerRepository.save(customerModel);
    objResponse.status  = HttpStatus.CREATED;
    objResponse.message = "Customer created successfully";
    return ResponseEntity.status(objResponse.status).body(objResponse);
  }

  @PutMapping("/update")
  public ResponseEntity<Object> updateCustomer(@RequestBody @Valid CustomerDto customer) {
    objResponse.target = null;
    var customerExists = customerRepository.findByCpf(customer.getCpf());
    if (customerExists.isEmpty()) {
      objResponse.status = HttpStatus.NOT_FOUND;
      objResponse.message = "Customer not found";
      return ResponseEntity.status(objResponse.status).body(objResponse);
    }
    // Validando os atributos do cliente no corpo da requisição
    if(customer.getName() == null){
      objResponse.status  = HttpStatus.BAD_REQUEST;
      objResponse.message = "Customer without name attribute";
      return ResponseEntity.status(objResponse.status).body(objResponse);
    }
    if(customer.getEmail() == null){
      objResponse.status  = HttpStatus.BAD_REQUEST;
      objResponse.message = "Customer without email attribute";
      return ResponseEntity.status(objResponse.status).body(objResponse);
    }
    if(customer.getCpf() == null){
      objResponse.status  = HttpStatus.BAD_REQUEST;
      objResponse.message = "Customer without cpf attribute";
      return ResponseEntity.status(objResponse.status).body(objResponse);
    }
    if(customer.getTel() == null){
      objResponse.status  = HttpStatus.BAD_REQUEST;
      objResponse.message = "Customer without telephone attribute";
      return ResponseEntity.status(objResponse.status).body(objResponse);
    }

    BeanUtils.copyProperties(customer, customerExists.get());
    customerExists.get().setUpdated_at(LocalDateTime.now());
    objResponse.target = customerRepository.save(customerExists.get());
    objResponse.status  = HttpStatus.OK;
    objResponse.message = "Customer updated successfully";
    return ResponseEntity.status(objResponse.status).body(objResponse);
  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity<Object> deleteCustomer(@PathVariable UUID id){
    objResponse.target = null;
    var customerExists = customerRepository.findById(id);

    // Verificando se o cliente existe
    if(customerExists.isEmpty()){
      objResponse.status = HttpStatus.NOT_FOUND;
      objResponse.message = "Customer not found";
      return ResponseEntity.status(objResponse.status).body(objResponse);
    }

    //TODO mostrar dados do cliente excluido (mudar config do hibernate)

    // Efetivando a exclusão do cliente
    customerExists.get().setSales(null);
    objResponse.target = customerExists.get();
    customerRepository.deleteById(id);
    objResponse.status = HttpStatus.OK;
    objResponse.message = "Customer deleted successfully";
    return ResponseEntity.status(objResponse.status).body(objResponse);
  }

  @GetMapping("/list")
  public ResponseEntity<Object> getAllCustomers(){
    var customers = customerRepository.findAll();
    return ResponseEntity.ok().body(customers);
  }
  @GetMapping("/{param}")
  public ResponseEntity<Object> getCustomerByCpf(@PathVariable String param){
    objResponse.target = null;
    Optional<CustomerModel> customer;

    // Possibilitando a pesquisa de cliente peloID no banco de dados ou pelo cpf através da mesma rota
    if(param.length()==11){
      customer = customerRepository.findByCpf(param);
    }else{
      customer = customerRepository.findById(UUID.fromString(param));
    }

    // Verificando se o cliente existe no banco de dados
    if(customer.isEmpty()){
      objResponse.status = HttpStatus.NOT_FOUND;
      objResponse.message = "Customer not found";
      return ResponseEntity.status(objResponse.status).body(objResponse);
    }

    return ResponseEntity.ok().body(customer);
  }
}
