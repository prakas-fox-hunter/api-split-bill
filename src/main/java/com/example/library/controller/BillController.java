package com.example.library.controller;
import java.security.Principal; import java.util.Map;
import org.springframework.http.HttpStatus; import org.springframework.web.bind.annotation.*;
import com.example.library.dto.*; import com.example.library.service.BillService; import lombok.RequiredArgsConstructor;
@RestController @RequestMapping("/api/groups") @RequiredArgsConstructor
public class BillController {
 private final BillService service;
 @PostMapping public Object create(@RequestBody CreateGroupRequest r, Principal p) { return service.create(r,p.getName()); }
 @PostMapping("/{id}/expenses") @ResponseStatus(HttpStatus.CREATED) public Object expense(@PathVariable Long id,@RequestBody CreateExpenseRequest r,Principal p){return service.addExpense(id,r,p.getName());}
 @GetMapping("/{id}/settlement") public Map<String,Object> settlement(@PathVariable Long id,Principal p){return service.settlement(id,p.getName());}
}
