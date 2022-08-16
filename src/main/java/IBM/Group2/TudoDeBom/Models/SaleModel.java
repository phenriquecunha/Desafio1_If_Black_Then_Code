package IBM.Group2.TudoDeBom.Models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
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
