package IBM.Group2.TudoDeBom.Controllers;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import javax.validation.Valid;

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

import IBM.Group2.TudoDeBom.Dtos.CustomerDto;
import IBM.Group2.TudoDeBom.Models.CustomerModel;
import IBM.Group2.TudoDeBom.Repositories.CustomerRepository;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/customer")
public class CustomerController {
  @Autowired
  CustomerRepository customerRepository;

  ObjResponse objResponse = new ObjResponse();

  @ApiOperation(value = "Create a new customer given it's name, email, phone and document ID (CPF)")
  @ApiResponses(value = {
    @ApiResponse(code = 200, message = "Customer created successfully"),
    @ApiResponse(code = 403, message = "User not allowed to perform this action"),
    @ApiResponse(code = 500, message = "There was an internal server error"),
})
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

  @ApiOperation(value = "Change a customer information")
  @ApiResponses(value = {
    @ApiResponse(code = 200, message = "Customer edited successfully"),
    @ApiResponse(code = 403, message = "User not allowed to perform this action"),
    @ApiResponse(code = 500, message = "There was an internal server error"),
})
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

  @ApiOperation(value = "Delete a customer from the database based on it's ID")
  @ApiResponses(value = {
    @ApiResponse(code = 200, message = "Customer deleted successfully"),
    @ApiResponse(code = 403, message = "User not allowed to perform this action"),
    @ApiResponse(code = 500, message = "There was an internal server error"),
})
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

  @ApiOperation(value = "List all customers in the database")
  @ApiResponses(value = {
    @ApiResponse(code = 200, message = "Query OK"),
    @ApiResponse(code = 403, message = "User not allowed to perform this action"),
    @ApiResponse(code = 500, message = "There was an internal server error"),
})
  @GetMapping("/list")
  public ResponseEntity<Object> getAllCustomers(){
    var customers = customerRepository.findAll();
    return ResponseEntity.ok().body(customers);
  }
  @ApiOperation(value = "List a specific customer based on it's ID or CPF")
  @ApiResponses(value = {
    @ApiResponse(code = 200, message = "Query OK"),
    @ApiResponse(code = 403, message = "User not allowed to perform this action"),
    @ApiResponse(code = 500, message = "There was an internal server error"),
})
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
