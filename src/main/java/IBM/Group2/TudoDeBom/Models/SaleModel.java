package IBM.Group2.TudoDeBom.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
@Data
@Entity
@Table(name = "tb_sale")
public class SaleModel {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID sale_id;

  @Column(nullable = false)
  private String payment_type;

  @Column(nullable = false)
  private ArrayList<UUID> products;

  @ManyToOne(targetEntity = CustomerModel.class)
  @JsonIgnore
  @JoinColumn(name = "customer_id")
  private CustomerModel customer;

  @Column(nullable = false)
  private Double total_price;

  @Column(nullable = false)
  private LocalDateTime created_at;

}
