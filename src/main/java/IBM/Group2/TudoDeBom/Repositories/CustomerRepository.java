package IBM.Group2.TudoDeBom.Repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import IBM.Group2.TudoDeBom.Models.CustomerModel;

public interface CustomerRepository extends JpaRepository<CustomerModel, UUID> {
  Optional<CustomerModel> findByEmail(String email);
  Optional<CustomerModel> findByCpf(String cpf);
  void deleteByCpf(String cpf);
}
