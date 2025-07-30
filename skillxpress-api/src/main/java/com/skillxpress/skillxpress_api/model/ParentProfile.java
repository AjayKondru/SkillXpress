package com.skillxpress.skillxpress_api.model;

import lombok.*;

import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ParentProfile {
    private List<Child> children;
}
