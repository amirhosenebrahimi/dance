package com.erymanthian.dance.dtos.auth;

import com.erymanthian.dance.enums.Affiliation;
import com.erymanthian.dance.enums.DanceStyle;
import com.erymanthian.dance.enums.Expertise;
import com.erymanthian.dance.enums.OpportunityType;

import java.util.List;

public record Talents(List<DanceStyle> danceStyles, List<OpportunityType> opportunities, String represented,
                      Affiliation affiliation, String ethnicity, Expertise expertise) {
}
