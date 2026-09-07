package com.example.library;
import static org.junit.jupiter.api.Assertions.*; import java.math.BigDecimal; import java.util.List;
import org.junit.jupiter.api.Test; import com.example.library.entity.Expense; import com.example.library.service.SettlementCalculator;
class SettlementCalculatorTest { @Test void calculatesMinimalTransfer() { Expense e=new Expense(); e.setAmount(new BigDecimal("90.00")); e.setPaidBy("alice"); e.setBeneficiaries(List.of("alice","bob","carol")); var r=SettlementCalculator.calculate(List.of("alice","bob","carol"),List.of(e)); assertEquals(2,r.size()); assertEquals("bob",r.get(0).from()); assertEquals(new BigDecimal("30.00"),r.get(0).amount()); } }
