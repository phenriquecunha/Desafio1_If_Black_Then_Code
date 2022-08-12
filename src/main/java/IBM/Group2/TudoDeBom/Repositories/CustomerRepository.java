package IBM.Group2.TudoDeBom.Repositories;

import IBM.Group2.TudoDeBom.Models.CustomerModel;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository extends JpaRepository<CustomerModel, UUID> {
  Optional<CustomerModel> findByEmail(String email);
  Optional<CustomerModel> findByCpf(String cpf);
  void deleteByCpf(String cpf);
}
