package com.acledabank.vicheak.api.core.spec;

import lombok.Builder;

@Builder
public record StudentFilter(String studentName,
                            Integer studentAge) {
}
