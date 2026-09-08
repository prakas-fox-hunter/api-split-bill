package com.example.library.config;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.library.entity.AppUser;
import com.example.library.entity.BillGroup;
import com.example.library.entity.BillParticipant;
import com.example.library.entity.Expense;
import com.example.library.repository.BillGroupRepository;
import com.example.library.repository.UserRepository;

@Configuration
public class DemoDataInitializer {

    @Bean
    CommandLineRunner seedDemoData(UserRepository users, BillGroupRepository groups,
            PasswordEncoder passwordEncoder) {
        return args -> {
            String username = "demo";
            if (users.findByUsername(username).isEmpty()) {
                AppUser user = new AppUser();
                user.setUsername(username);
                user.setPassword(passwordEncoder.encode("demo123"));
                users.save(user);
            }

            boolean hasDemoGroup = groups.findAll().stream()
                    .anyMatch(group -> username.equals(group.getOwnerUsername()));
            if (hasDemoGroup) {
                return;
            }

            BillGroup group = new BillGroup();
            group.setName("Demo Trip Bali");
            group.setOwnerUsername(username);

            List<String> names = List.of("demo", "alice", "bob");
            for (String name : names) {
                BillParticipant participant = new BillParticipant();
                participant.setName(name);
                participant.setGroup(group);
                group.getParticipants().add(participant);
            }

            addExpense(group, "Dinner", "90000.00", "demo", names);
            addExpense(group, "Transport", "60000.00", "alice", List.of("demo", "alice", "bob"));
            groups.save(group);
        };
    }

    private void addExpense(BillGroup group, String description, String amount,
            String paidBy, List<String> beneficiaries) {
        Expense expense = new Expense();
        expense.setDescription(description);
        expense.setAmount(new BigDecimal(amount));
        expense.setPaidBy(paidBy);
        expense.setBeneficiaries(beneficiaries);
        expense.setGroup(group);
        group.getExpenses().add(expense);
    }
}
