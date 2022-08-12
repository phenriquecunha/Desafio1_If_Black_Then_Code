package IBM.Group2.TudoDeBom.Repositories;

import IBM.Group2.TudoDeBom.Models.SaleModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SaleRepository extends JpaRepository<SaleModel, UUID> {
}
