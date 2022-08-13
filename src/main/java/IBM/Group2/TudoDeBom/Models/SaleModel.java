package IBM.Group2.TudoDeBom.Models;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
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

  @ManyToOne(targetEntity = ProductModel.class)
  @JoinColumn(name = "product_id")
  private List<ProductModel> products;

  @ManyToOne(targetEntity = CustomerModel.class)
  @JoinColumn(name = "customer_id")
  private UUID customer;

  @Column(nullable = false)
  private Double total_price;

  @Column(nullable = false)
  private LocalDateTime created_at;

}
