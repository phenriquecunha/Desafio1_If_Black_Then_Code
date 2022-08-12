package IBM.Group2.TudoDeBom.Models;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
  @Column(nullable = false, length = 11)
  private String cpf;
  @Column(nullable = false)
  private String tel;

  @OneToMany(mappedBy = "customer")
  private List<SaleModel> sales;

  @Column(nullable = false)
  private LocalDateTime created_at;

  private LocalDateTime updated_at;

}
