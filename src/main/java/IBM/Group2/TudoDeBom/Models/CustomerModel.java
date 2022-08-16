package IBM.Group2.TudoDeBom.Models;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "tb_customer")
public class CustomerModel {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID customer_id;

  @Column(nullable = false)
  private String name;
  @Column(nullable = false)
  private String email;
  @Column(nullable = false, length = 11, unique = true)
  private String cpf;
  @Column(nullable = false)
  private String tel;

  @OneToMany(mappedBy = "customer")
  private List<SaleModel> sales;

  @Column(nullable = false)
  private LocalDateTime created_at;

  private LocalDateTime updated_at;

}
