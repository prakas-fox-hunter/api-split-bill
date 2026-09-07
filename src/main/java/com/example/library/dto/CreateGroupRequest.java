package com.example.library.dto;
import java.util.List;
import lombok.Getter; import lombok.Setter;
@Getter @Setter public class CreateGroupRequest { private String name; private List<String> participants; }
