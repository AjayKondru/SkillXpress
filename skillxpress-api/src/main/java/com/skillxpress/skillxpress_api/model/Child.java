package com.skillxpress.skillxpress_api.model;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Child {
    private String id;
    private String name;
    private int grade; // 0=KG
    private String timeZone;
}
