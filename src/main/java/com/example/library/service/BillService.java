package com.example.library.service;

import java.math.*; import java.util.*;
import org.springframework.stereotype.Service;
import com.example.library.dto.*; import com.example.library.entity.*; import com.example.library.repository.BillGroupRepository;
import jakarta.persistence.EntityNotFoundException; import lombok.RequiredArgsConstructor;

@Service @RequiredArgsConstructor
public class BillService {
    private final BillGroupRepository repository;
    public BillGroup create(CreateGroupRequest request, String owner) {
        if (request.getName() == null || request.getName().isBlank() || request.getParticipants() == null || request.getParticipants().isEmpty()) throw new IllegalArgumentException("name and participants are required");
        Set<String> names = new LinkedHashSet<>(request.getParticipants()); if (names.stream().anyMatch(n -> n == null || n.isBlank())) throw new IllegalArgumentException("participant names must not be blank");
        BillGroup group = new BillGroup(); group.setName(request.getName()); group.setOwnerUsername(owner); names.forEach(n -> { BillParticipant p = new BillParticipant(); p.setName(n); p.setGroup(group); group.getParticipants().add(p); }); return repository.save(group);
    }
    public Expense addExpense(Long id, CreateExpenseRequest request, String owner) {
        BillGroup group = get(id, owner); if (request.getAmount() == null || request.getAmount().compareTo(BigDecimal.ZERO) <= 0 || request.getBeneficiaries() == null || request.getBeneficiaries().isEmpty()) throw new IllegalArgumentException("amount and beneficiaries are required");
        Set<String> participants = group.getParticipants().stream().map(BillParticipant::getName).collect(java.util.stream.Collectors.toSet());
        if (!participants.contains(request.getPaidBy()) || !participants.containsAll(request.getBeneficiaries())) throw new IllegalArgumentException("all people must be group participants");
        Expense e = new Expense(); e.setDescription(request.getDescription()); e.setAmount(request.getAmount().setScale(2, RoundingMode.HALF_UP)); e.setPaidBy(request.getPaidBy()); e.setBeneficiaries(new ArrayList<>(request.getBeneficiaries())); e.setGroup(group); group.getExpenses().add(e); repository.save(group); return e;
    }
    public Map<String,Object> settlement(Long id, String owner) {
        BillGroup group = get(id, owner); BigDecimal total = group.getExpenses().stream().map(Expense::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add); int pct = "prakosadwiprasetya".chars().sum() % 10;
        Map<String,Object> result = new LinkedHashMap<>(); result.put("group_id", id); result.put("total_expenses", total.setScale(2)); result.put("service_charge_pct", BigDecimal.valueOf(pct)); result.put("service_charge_amount", total.multiply(BigDecimal.valueOf(pct)).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP)); result.put("settlements", SettlementCalculator.calculate(group.getParticipants().stream().map(BillParticipant::getName).toList(), group.getExpenses())); return result;
    }
    private BillGroup get(Long id, String owner) { return repository.findByIdAndOwnerUsername(id, owner).orElseThrow(() -> new EntityNotFoundException("Group not found")); }
}
