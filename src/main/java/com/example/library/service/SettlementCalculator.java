package com.example.library.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import com.example.library.entity.Expense;

public final class SettlementCalculator {
    private SettlementCalculator() {}
    public record Transfer(String from, String to, BigDecimal amount) {}
    public static List<Transfer> calculate(Collection<String> participants, Collection<Expense> expenses) {
        Map<String, BigDecimal> net = new HashMap<>();
        participants.forEach(p -> net.put(p, BigDecimal.ZERO));
        for (Expense e : expenses) {
            net.computeIfAbsent(e.getPaidBy(), k -> BigDecimal.ZERO).add(BigDecimal.ZERO);
            net.put(e.getPaidBy(), net.get(e.getPaidBy()).add(e.getAmount()));
            BigDecimal share = e.getAmount().divide(BigDecimal.valueOf(e.getBeneficiaries().size()), 2, RoundingMode.HALF_UP);
            for (String beneficiary : e.getBeneficiaries()) net.put(beneficiary, net.getOrDefault(beneficiary, BigDecimal.ZERO).subtract(share));
        }
        List<Map.Entry<String, BigDecimal>> creditors = net.entrySet().stream().filter(e -> e.getValue().signum() > 0).sorted(Map.Entry.comparingByKey()).collect(java.util.stream.Collectors.toCollection(ArrayList::new));
        List<Map.Entry<String, BigDecimal>> debtors = net.entrySet().stream().filter(e -> e.getValue().signum() < 0).sorted(Map.Entry.comparingByKey()).collect(java.util.stream.Collectors.toCollection(ArrayList::new));
        List<Transfer> result = new ArrayList<>(); int d = 0, c = 0;
        while (d < debtors.size() && c < creditors.size()) {
            BigDecimal amount = debtors.get(d).getValue().negate().min(creditors.get(c).getValue()).setScale(2, RoundingMode.HALF_UP);
            result.add(new Transfer(debtors.get(d).getKey(), creditors.get(c).getKey(), amount));
            debtors.get(d).setValue(debtors.get(d).getValue().add(amount)); creditors.get(c).setValue(creditors.get(c).getValue().subtract(amount));
            if (debtors.get(d).getValue().compareTo(BigDecimal.ZERO) == 0) d++;
            if (creditors.get(c).getValue().compareTo(BigDecimal.ZERO) == 0) c++;
        }
        return result;
    }
}
