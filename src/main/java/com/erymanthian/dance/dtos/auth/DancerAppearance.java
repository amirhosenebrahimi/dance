package com.erymanthian.dance.dtos.auth;

import com.erymanthian.dance.enums.EyeColor;
import com.erymanthian.dance.enums.HairColor;

public record DancerAppearance(HairColor hairColor, EyeColor eyeColor, int height) {
}
