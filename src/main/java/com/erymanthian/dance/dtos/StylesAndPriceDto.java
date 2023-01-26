package com.erymanthian.dance.dtos;

import com.erymanthian.dance.enums.DanceStyle;
import com.erymanthian.dance.enums.PayGrade;

import javax.swing.text.Style;
import java.util.List;

public record StylesAndPriceDto(List<DanceStyle> styles, PayGrade payGrade) {
}
