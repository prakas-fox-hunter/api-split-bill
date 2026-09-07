package com.example.library.dto;
import java.math.BigDecimal; import java.util.List;
import lombok.Getter; import lombok.Setter;
@Getter @Setter public class CreateExpenseRequest { private String description; private BigDecimal amount; private String paidBy; private List<String> beneficiaries; }
